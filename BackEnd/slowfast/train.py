from __future__ import division

import argparse, time, logging, os, sys, math

import numpy as np
import mxnet as mx
import gluoncv as gcv
from mxnet import gluon, nd, init, context
from mxnet import autograd as ag
from mxnet.gluon import nn
from mxnet.gluon.data.vision import transforms

from gluoncv.data.transforms import video

from gluoncv.data import Kinetics400
from gluoncv.model_zoo import get_model
from gluoncv.utils import makedirs, LRSequential, LRScheduler, split_and_load, TrainingHistory

# number of GPUs to usenum_gpus = 1
num_gpus = 1
ctx = [mx.gpu(i) for i in range(num_gpus)]

# Get the model slowfast_4x16_resnet50_kinetics400 with 400 output classes, without pre-trained weights
net = get_model(name='slowfast_4x16_resnet50_kinetics400', nclass=4)
net.collect_params().reset_ctx(ctx)

transform_train = transforms.Compose([
    # Fix the input video frames size as 256×340 and randomly sample the cropping width and height from
    # {256,224,192,168}. After that, resize the cropped regions to 224 × 224.
    video.VideoCenterCrop(size=(224, 224)),
    # Randomly flip the video frames horizontally
    video.VideoRandomHorizontalFlip(),
    # Transpose the video frames from height*width*num_channels to num_channels*height*width
    # and map values from [0, 255] to [0,1]
    video.VideoToTensor(),
    # Normalize the video frames with mean and standard deviation calculated across all images
    video.VideoNormalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
])

# Batch Size for Each GP
per_device_batch_size = 10
# Number of data loader workers
num_workers = 0
# Calculate effective total batch size
batch_size = per_device_batch_size * num_gpus


# Set train=True for training the model.
# ``new_length`` indicates the number of frames we will cover.
# For SlowFast network, we evenly sample 32 frames for the fast branch and 4 frames for the slow branch.
# This leads to the actual input length of 36 video frames.
train_dataset = Kinetics400(train=True, new_length=64, slowfast=True, transform=transform_train, root='/home/irteam/dcloud-global-dir/Ahpuh/swim_dataset/rawframes_train', setting='/home/irteam/dcloud-global-dir/Ahpuh/swim_dataset/swim_train_list_rawframes.txt')
print('Load %d training samples.' % len(train_dataset), flush=True)
train_data = gluon.data.DataLoader(train_dataset, batch_size=batch_size,
                                   shuffle=True, num_workers=num_workers)

# Optimizer, Loss and Metric
lr_decay = 0.1
warmup_epoch = 34
total_epoch = 196
num_batches = len(train_data)
lr_scheduler = LRSequential([
    LRScheduler('linear', base_lr=0.01, target_lr=0.1,
                nepochs=warmup_epoch, iters_per_epoch=num_batches),
    LRScheduler('cosine', base_lr=0.1, target_lr=0,
                nepochs=total_epoch - warmup_epoch,
                iters_per_epoch=num_batches,
                step_factor=lr_decay, power=2)
])

# Stochastic gradient descent
optimizer = 'sgd'

# Set parameters
optimizer_params = {'learning_rate': 0.01, 'wd': 0.0001, 'momentum': 0.9}
optimizer_params['lr_scheduler'] = lr_scheduler

# Define our trainer for net
trainer = gluon.Trainer(net.collect_params(), optimizer, optimizer_params)
loss_fn = gluon.loss.SoftmaxCrossEntropyLoss()
train_metric = mx.metric.Accuracy()
train_history = TrainingHistory(['training-acc'])

# Training
print("Start Training", flush=True)
epochs = 100

for epoch in range(epochs):
    tic = time.time()
    train_metric.reset()
    train_loss = 0

    # Loop through each batch of training data
    for i, batch in enumerate(train_data):
        # Extract data and label
        data = split_and_load(batch[0], ctx_list=ctx, batch_axis=0, even_split=False)
        label = split_and_load(batch[1], ctx_list=ctx, batch_axis=0, even_split=False)

        # AutoGrad
        with ag.record():
            output = []
            for _, X in enumerate(data):
                X = X.reshape((-1,) + X.shape[2:])
                pred = net(X)
                output.append(pred)
            loss = [loss_fn(yhat, y) for yhat, y in zip(output, label)]

        # Backpropagation
        for l in loss:
            l.backward()

        # Optimize
        trainer.step(batch_size)

        # Update metrics
        train_loss += sum([l.mean().asscalar() for l in loss])
        train_metric.update(label, output)

        if i == 100:
            break

    name, acc = train_metric.get()

    # Update history and print metrics
    train_history.update([acc])
    print('[Epoch %d] train=%f loss=%f time: %f' %
        (epoch, acc, train_loss / (i+1), time.time()-tic), flush=True)

# save model
file_name = "/home/irteam/dcloud-global-dir/Ahpuh/net.params"
net.save_parameters(file_name)