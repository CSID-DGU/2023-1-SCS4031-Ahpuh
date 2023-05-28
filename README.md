# 2023-1-SCS4031-Ahpuh
### 🏊🏻‍♂️ 실내 수영장 안전사고 감지 및 신고 시스템

     낙상/익사/다이빙 사고를 감지하여 단계별 대응을 진행하는 종합적인 실내 수영장 안전사고 감지 및 신고 시스템

<br>

### 👥 어푸(Ah-puh) 팀원 소개
>- 2019110462 양지우 팀장 딥러닝
>
>- 2018112461 정재혁 팀원 프론트엔드
>
>- 2020112487 백채연 팀원 백엔드
>
>- 2019110500 안도현 팀원 딥러닝


### 모델 데모 실행 방법
0. `cd 2023-1-SCS4031-Ahpuh`
1. deep sort의 pretrained 모델 준비 : [여기](https://drive.google.com/drive/folders/1xhG0kRH1EX5B9_Iz8gQJb7UNnn_riXi6)서 받거나, dcloud-global-dir에서 ckpt.t7 파일 가져와서 현재 디렉토리에 넣기
2. slowfast의 pretrained 모델 준비 : dcloud-global-dir에서 net.params 파일 가져와서 현재 디렉토리에 넣기
```
3. 가상환경 생성 & 패키지 설치
```
conda create -n {name} python=3.8
conda activate {name}
pip install -r requirements.txt
```
4. 모델 실행
```
python BackEnd/model/yolo_slowfast.py --input /path/to/test/video
```
