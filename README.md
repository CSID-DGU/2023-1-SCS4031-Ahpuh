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
1. deep sort의 pretrained 모델 다운로드 
```
wget -O BackEnd/model/deep_sort/deep_sort/deep/checkpoint/ckpt.t7 https://drive.google.com/file/d/1fa8QonVhFZG332Y22I5IuA43JgVp-cm-/view?usp=sharing
```
2. slowfast의 pretrained 모델 다운로드
```
wget -O net.params https://dongguk0-my.sharepoint.com/:u:/g/personal/andh1014_dongguk_edu/EV7bKpaqYO1GsPtC9n7PaN8Bi7MqNSTLAB1vtYaDdq33EA?e=fPoiag
```
3. 가상환경 생성 & 패키지 설치
```
conda create -n {name} python=3.8
conda activate {name}
pip install -r requirements.txt
```
4. 모델 실행
python BackEnd/model/yolo_slowfast.py --input /path/to/test/video
