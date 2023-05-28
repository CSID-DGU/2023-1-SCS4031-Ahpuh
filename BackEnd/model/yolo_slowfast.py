import torch
import numpy as np
import os,cv2,time,torch,random,pytorchvideo,warnings,argparse,math
warnings.filterwarnings("ignore",category=UserWarning)

from pytorchvideo.transforms.functional import (
    uniform_temporal_subsample,
    short_side_scale_with_boxes,
    clip_boxes_to_image,)
from torchvision.transforms._functional_video import normalize
from pytorchvideo.data.ava import AvaLabeledVideoFramePaths
from pytorchvideo.models.hub import slowfast_r50_detection
from deep_sort.deep_sort import DeepSort
import datetime
from collections import deque

from gluoncv.model_zoo import get_model
from gluoncv.utils.filesystem import try_import_decord
from gluoncv.data.transforms import video
from mxnet import gluon, nd, init, context
decord = try_import_decord()

class MyVideoCapture:
    
    def __init__(self, source):
        self.cap = cv2.VideoCapture(source)
        self.idx = -1
        self.end = False
        self.stack = []
        
    def read(self):
        self.idx += 1
        ret, img = self.cap.read()
        if ret:
            self.stack.append(img)
        else:
            self.end = True
        return ret, img
    
    def to_tensor(self, img):
        img = torch.from_numpy(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
        return img.unsqueeze(0)
        
    def get_video_clip(self):
        assert len(self.stack) > 0, "clip length must large than 0 !"
        self.stack = [self.to_tensor(img) for img in self.stack]
        clip = torch.cat(self.stack).permute(-1, 0, 1, 2)
        del self.stack
        self.stack = []
        return clip
    
    def release(self):
        self.cap.release()
        
def tensor_to_numpy(tensor):
    img = tensor.cpu().numpy().transpose((1, 2, 0))
    return img

def ava_inference_transform(
    clip, 
    boxes,
    num_frames = 32, #if using slowfast_r50_detection, change this to 32, 4 for slow 
    crop_size = 640, 
    data_mean = [0.45, 0.45, 0.45], 
    data_std = [0.225, 0.225, 0.225],
    slow_fast_alpha = 4, #if using slowfast_r50_detection, change this to 4, None for slow
):
    boxes = np.array(boxes)
    roi_boxes = boxes.copy()
    clip = uniform_temporal_subsample(clip, num_frames)
    clip = clip.float()
    clip = clip / 255.0
    height, width = clip.shape[2], clip.shape[3]
    boxes = clip_boxes_to_image(boxes, height, width)
    clip, boxes = short_side_scale_with_boxes(clip,size=crop_size,boxes=boxes,)
    clip = normalize(clip,
        np.array(data_mean, dtype=np.float32),
        np.array(data_std, dtype=np.float32),) 
    boxes = clip_boxes_to_image(boxes, clip.shape[2],  clip.shape[3])
    if slow_fast_alpha is not None:
        fast_pathway = clip
        slow_pathway = torch.index_select(clip,1,
            torch.linspace(0, clip.shape[1] - 1, clip.shape[1] // slow_fast_alpha).long())
        clip = [slow_pathway, fast_pathway]
    
    return clip, torch.from_numpy(boxes), roi_boxes

def swim_inference_transform(
    clip, 
    boxes,
    num_frames = 64, #if using slowfast_r50_detection, change this to 32, 4 for slow 
    crop_size = 1080, 
    data_mean = [0.45, 0.45, 0.45], 
    data_std = [0.225, 0.225, 0.225],
    slow_fast_alpha = 4, #if using slowfast_r50_detection, change this to 4, None for slow
):
    boxes = np.array(boxes)
    roi_boxes = boxes.copy()
    clip = torch.tensor(clip)
    clip = uniform_temporal_subsample(clip, num_frames)
    clip = clip.float()
    height, width = clip.shape[2], clip.shape[3]
    boxes = clip_boxes_to_image(boxes, height, width)
    clip, boxes = short_side_scale_with_boxes(clip,size=crop_size,boxes=boxes,)
    boxes = clip_boxes_to_image(boxes, clip.shape[2],  clip.shape[3])
    clip = normalize(clip,
        np.array(data_mean, dtype=np.float32),
        np.array(data_std, dtype=np.float32),) 
    boxes = clip_boxes_to_image(boxes, clip.shape[2],  clip.shape[3])
    clip = np.transpose(clip, (1, 2 , 3, 0))
    
    fast_frame_id_list = range(0, 64, 2)
    slow_frame_id_list = range(0, 64, 16)
    frame_id_list = list(fast_frame_id_list) + list(slow_frame_id_list)
    clip_input = [clip[vid, :, :, :] for vid, _ in enumerate(frame_id_list)]

    transform_fn = video.VideoGroupValTransform(size=224, mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    clip_input = transform_fn(clip_input)
    clip_input = np.stack(clip_input, axis=0)
    clip_input = clip_input.reshape((-1,) + (36, 3, 224, 224))
    clip_input = np.transpose(clip_input, (0, 2, 1, 3, 4))
    return clip_input

def plot_one_box(x, img, color=[100,100,100], text_info="None",
                 velocity=None, thickness=1, fontsize=0.5, fontthickness=1):
    # Plots one bounding box on image img
    c1, c2 = (int(x[0]), int(x[1])), (int(x[2]), int(x[3]))
    c3=int((int(x[0])+int(x[2]))/2),int((int(x[1])+int(x[3]))/2)
    c4, c5 = (max(0,int(int(x[0])-(int(x[2])-int(x[0]))/2)),max(0,int(int(x[1])-(int(x[3])-int(x[1]))/2))),(int(int(x[2])+(int(x[2])-int(x[0]))/2),int(int(x[3])+(int(x[3])-int(x[1]))/2))
    cv2.line(img,c3,c3,color,5)
    cv2.rectangle(img, c1, c2, color, thickness, lineType=cv2.LINE_AA)
    t_size = cv2.getTextSize(text_info, cv2.FONT_HERSHEY_TRIPLEX, fontsize , fontthickness+2)[0]
    cv2.rectangle(img, c1, (c1[0] + int(t_size[0]), c1[1] + int(t_size[1]*1.45)), color, -1)
    cv2.rectangle(img, c4, c5, color, thickness, lineType=cv2.LINE_AA)
    cv2.putText(img, text_info, (c1[0], c1[1]+t_size[1]+2), 
                cv2.FONT_HERSHEY_TRIPLEX, fontsize, [255,255,255], fontthickness)
    return img

def deepsort_update(Tracker, pred, xywh, np_img):
    outputs = Tracker.update(xywh, pred[:,4:5],pred[:,5].tolist(),cv2.cvtColor(np_img,cv2.COLOR_BGR2RGB))
    return outputs





def save_yolopreds_tovideo(yolo_preds, id_to_ava_labels, color_map, output_video, vis=False):
    for i, (im, pred) in enumerate(zip(yolo_preds.ims, yolo_preds.pred)):
        im=cv2.cvtColor(im,cv2.COLOR_BGR2RGB)
        if pred.shape[0]:
            #print(pred[:,:4])
            for j, (*box, cls, trackid, vx, vy) in enumerate(pred):
                if int(cls) != 0:
                    ava_label = ''
                elif trackid in id_to_ava_labels.keys():
                    ava_label = id_to_ava_labels[trackid].split(' ')[0]
                else:
                    ava_label = 'Unknow'
                text = '{} {} {}'.format(int(trackid),yolo_preds.names[int(cls)],ava_label)
                color = color_map[int(cls)]
                if yolo_preds.names[int(cls)]=='person':
                    im = plot_one_box(box,im,color,text)
                    #print((box[0]+box[2])/2,(box[1]+box[3])/2)
                    
        im = im.astype(np.uint8)
        im = cv2.cvtColor(im, cv2.COLOR_BGR2RGB)
        output_video.write(im)
        if vis:
            im=cv2.cvtColor(im,cv2.COLOR_RGB2BGR)
            cv2.imshow("demo", im)

def main(config):
    device = config.device
    imsize = config.imsize
    # swim_list=deque()
    drown_list = deque()
    
    model = torch.hub.load('ultralytics/yolov5', 'yolov5l6').to(device)
    model.conf = config.conf
    model.iou = config.iou
    model.max_det = 100
    if config.classes:
        model.classes = config.classes
    
    # video_model = slowfast_r50_detection(True).eval().to(device)
    video_model = get_model(name='slowfast_4x16_resnet50_kinetics400', nclass=3)
    video_model.load_parameters("net.params")
    
    deepsort_tracker = DeepSort("deep_sort/deep_sort/deep/checkpoint/ckpt.t7")    
    # ava_labelnames,_ = AvaLabeledVideoFramePaths.read_label_map("selfutils/temp.pbtxt")
    swim_labelnames,_ = AvaLabeledVideoFramePaths.read_label_map("selfutils/swim.pbtxt")
    coco_color_map = [[random.randint(0, 255) for _ in range(3)] for _ in range(80)]

    vide_save_path = config.output
    video=cv2.VideoCapture(config.input)
    width,height = int(video.get(3)),int(video.get(4))
    video.release()
    outputvideo = cv2.VideoWriter(vide_save_path,cv2.VideoWriter_fourcc(*'mp4v'), 25, (width,height))
    print("processing...")
    
    cap = MyVideoCapture(config.input)
    vr = decord.VideoReader(config.input)
    # id_to_ava_labels = {}
    id_to_swim_labels = {}
    a=time.time()
    while not cap.end:
        ret, img = cap.read()
        if not ret:
            continue
        yolo_preds=model([img], size=imsize)
        yolo_preds.files=["img.jpg"]
        
        deepsort_outputs=[]
        for j in range(len(yolo_preds.pred)):
            temp=deepsort_update(deepsort_tracker,yolo_preds.pred[j].cpu(),yolo_preds.xywh[j][:,0:4].cpu(),yolo_preds.ims[j])
            if len(temp)==0:
                temp=np.ones((0,8))
            deepsort_outputs.append(temp.astype(np.float32))
            
        yolo_preds.pred=deepsort_outputs
        if len(cap.stack) == 25:
            print(f"processing {cap.idx // 25}th second clips")
            clip = cap.get_video_clip()
            if yolo_preds.pred[0].shape[0]:
                inputs = swim_inference_transform(clip, yolo_preds.pred[0][:,0:4], crop_size=imsize)
                # inputs, inp_boxes, _=ava_inference_transform(clip, yolo_preds.pred[0][:,0:4], crop_size=imsize)
                # inp_boxes = torch.cat([torch.zeros(inp_boxes.shape[0],1), inp_boxes], dim=1)
                # if isinstance(inputs, list):
                #     inputs = [inp.unsqueeze(0).to(device) for inp in inputs]
                # else:
                #     inputs = inputs.unsqueeze(0).to(device)
                with torch.no_grad():
                    # slowfaster_preds = video_model(inputs, inp_boxes.to(device))
                    slowfaster_preds = video_model(nd.array(inputs))
                    # slowfaster_preds = slowfaster_preds.cpu()
                # for tid,avalabel,location, pred in zip(yolo_preds.pred[0][:,5].tolist(), np.argmax(slowfaster_preds, axis=1).tolist(), yolo_preds.pred[0][:,0:4], slowfaster_preds):
                for tid,location, pred in zip(yolo_preds.pred[0][:,5], yolo_preds.pred[0][:,0:4], slowfaster_preds):
                    # id_to_ava_labels[tid] = ava_labelnames[avalabel+1]
                    
                    id_to_swim_labels[tid] = swim_labelnames[np.argmax(pred).asscalar()] # 객체 id와 행동라벨 매핑
                    d_code=False
                    # if ava_labelnames[avalabel+1]=='swim':
                    if swim_labelnames[np.argmax(pred).asscalar()+1]=='drown':
                        #print(ava_labelnames[avalabel+1],location,datetime.datetime.now())
                        # if swim_list:
                        if drown_list:
                            center_x=int((int(location[0])+int(location[2]))/2)
                            center_y=int((int(location[1])+int(location[3]))/2)
                            # if (datetime.datetime.now()-swim_list[0][0]).seconds>60: #시간 지나면 삭제
                            if (datetime.datetime.now()-drown_list[0][0]).seconds>60: #시간 지나면 삭제
                                # swim_list.popleft()
                                drown_list.popleft()
                            # for index,i in enumerate(swim_list):
                            for index,i in enumerate(drown_list):    
                                if center_x>=i[1][0] and center_x<=i[1][2] and center_y >=i[1][1] and center_y <=i[1][3]:
                                    # swim_list[index][2]+=1
                                    drown_list[index][2]+=1
                                    # if swim_list[index][2]==1:
                                    if drown_list[index][2]==1:
                                        print('익사 1단계')
                                    # elif swim_list[index][2]==2:
                                    elif drown_list[index][2]==2:
                                        print('익사 2단계')
                                    # elif swim_list[index][2]>=3:
                                    elif drown_list[index][2]>=3:
                                        print('익사 3단계')
                                    else:
                                        pass
                                    d_code=True
                            if d_code==False:
                                print('등록')
                                find_x1, find_y1 = max(0,int(int(location[0])-(int(location[2])-int(location[0]))/2)), max(0,int(int(location[1])-(int(location[3])-int(location[1]))/2))
                                find_x2, find_y2 = int(int(location[2])+(int(location[2])-int(location[0]))/2),int(int(location[3])+(int(location[3])-int(location[1]))/2)
                                # swim_list.append([datetime.datetime.now(),[find_x1,find_y1,find_x2, find_y2],0])
                                drown_list.append([datetime.datetime.now(),[find_x1,find_y1,find_x2, find_y2],0])
                        else:
                            print('등록')
                            find_x1, find_y1 = max(0,int(int(location[0])-(int(location[2])-int(location[0]))/2)), max(0,int(int(location[1])-(int(location[3])-int(location[1]))/2))
                            find_x2, find_y2 = int(int(location[2])+(int(location[2])-int(location[0]))/2),int(int(location[3])+(int(location[3])-int(location[1]))/2)
                            # swim_list.append([datetime.datetime.now(),[find_x1,find_y1,find_x2, find_y2],0])
                            drown_list.append([datetime.datetime.now(),[find_x1,find_y1,find_x2, find_y2],0])

        # save_yolopreds_tovideo(yolo_preds, id_to_ava_labels, coco_color_map, outputvideo, config.show)
        save_yolopreds_tovideo(yolo_preds, id_to_swim_labels, coco_color_map, outputvideo, config.show)
    print(drown_list)
    print("total cost: {:.3f} s, video length: {} s".format(time.time()-a, cap.idx / 25))
    
    cap.release()
    outputvideo.release()
    print('saved video to:', vide_save_path)
    
    
if __name__=="__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('--input', type=str, default="./home/wufan/images/video/ahpuh.mp4", help='test imgs folder or video or camera')
    parser.add_argument('--output', type=str, default="output.mp4", help='folder to save result imgs, can not use input folder')
    # object detect config
    parser.add_argument('--imsize', type=int, default=1080, help='inference size (pixels)')
    parser.add_argument('--conf', type=float, default=0.4, help='object confidence threshold')
    parser.add_argument('--iou', type=float, default=0.4, help='IOU threshold for NMS')
    parser.add_argument('--device', default='cuda', help='cuda device, i.e. 0 or 0,1,2,3 or cpu')
    parser.add_argument('--classes', nargs='+', type=int, help='filter by class: --class 0, or --class 0 2 3')
    parser.add_argument('--show', action='store_true', help='show img')
    config = parser.parse_args()
    
    if config.input.isdigit():
        print("using local camera.")
        config.input = int(config.input)
        
    print(config)
    main(config)
