# 실내 수영장 안전 관리 시스템, 어푸
![image](https://github.com/CSID-DGU/2023-1-SCS4031-Ahpuh/assets/77263479/c7180b68-7960-4a64-ad34-3b02d67a35d9)

<br>

## 🏊🏻‍♂️ 프로젝트 소개

최근 발생한 물놀이장 내 안전사고를 분석한 결과 물리적 충격은 약 80%였고 그 중 낙상, 부딪힘, 추락이 가장 많습니다.<br>
또한 아파트 커뮤니티 시설과 같은 소규모 수영장의 경우 수상 안전요원의 배치가 필수가 아니기에 *예기치 못한 익수사고가 많이 발생*하며 안전요원의 부재시 즉각적인 대응이 어렵습니다.

이에 따라 **AI 모델을 활용해 실시간 cctv 영상에서 안전 사고의 발생을 감지 후 단계적으로 위험 상황에 대응하는 서비스, 어푸**를 제작하였습니다. 


1. CCTV 화면 속 익수, 낙상, 다이빙 위치를 감지할 수 있습니다.
2. 사고 여부 단계를 판단하여 사고 상황을 감지하고 신고 프로세스를 진행할 수 있습니다.
3. TTS를 통한 위기 알림 프로세스를 진행할 수 있습니다.

<br>

## 💫 주요 기능
### 1️⃣ CCTV 화면 내 사고 감지
- 자체 준비/기존 데이터를 학습한 AI 모델을 활용해 CCTV 화면 내 사고 상황 파악

### 2️⃣ CCTV 기본 설정
- CCTV 위치 파악을 위해 초기 설정 진행
- 수영장 내 CCTV 개수 및 화면 사진 입력
- 레인 개수와 좌표값 바탕으로 수영장 구역 지정

### 3️⃣ 신고 프로세스
- 사고 대응 총 3단계 과정을 거쳐 신고 프로세스 진행
- 회원 관리 페이지에서 사고 발생 문자 발송

<br>

## 🖥️ 화면 구성
| 실시간 CCTV 모니터링 페이지  |  CCTV 기본 설정 페이지   |
| :-------------------------------------------: | :------------: |
|  <img width="329" src="https://github.com/CSID-DGU/2023-1-SCS4031-Ahpuh/assets/77263479/7d7678a0-b013-497f-948b-93ea329c5f2c"/> |  <img width="329" src="https://github.com/CSID-DGU/2023-1-SCS4031-Ahpuh/assets/77263479/b0e9f8ce-bb46-47fc-b091-3bdbbfff043a"/>|  
| 사고 감지 및 신고 페이지   |  회원 관리 페이지   |  
| <img width="329" src="https://github.com/CSID-DGU/2023-1-SCS4031-Ahpuh/assets/77263479/533abf09-5ee5-4217-8550-b076731d1f89"/>   |  <img width="329" src="https://github.com/CSID-DGU/2023-1-SCS4031-Ahpuh/assets/77263479/0bfe4cba-5af4-4985-8757-5bda7edbefd3"/>     |

<br>


## 🔧 Tech Stack

### DL
<img src="https://img.shields.io/badge/python-3776AB?style=for-the-badge&logo=python&logoColor=white"> <img src="https://img.shields.io/badge/pytorch-EE4C2C?style=for-the-badge&logo=pytorch&logoColor=white"> <img src="https://img.shields.io/badge/flask-000000?style=for-the-badge&logo=flask&logoColor=white">

### Frontend
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">

### Backend
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<br>
<img src="https://img.shields.io/badge/amazon rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/amazon ec2-FF9900?style=for-the-badge&logo=amazon ec2&logoColor=white"><br>
<img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white">

<br>

### 👥 어푸(Ah-puh) Team 
<table align="center" >
   <tr>
        <td align="center"><a href="https://github.com/didwldn3032"><img src="https://github.com/didwldn3032.png" width="100px;" alt=""/><br/><sub>Deep Learning<b><br/>팀장 양지우</b></sub></a></td>
        <td align="center"><a href="https://github.com/an-dhyun"><img src="https://github.com/an-dhyun.png" width="100px;" alt=""/><br/><sub>Deep Learning<b><br/>팀원 안도현</b></sub></a></td>
        <td align="center"><a href="https://github.com/jaehyukjung"><img src="https://github.com/jaehyukjung.png" width="100px;" alt=""/><br/><sub>Frontend<b><br/>팀원 정재혁</b></sub></a></td>
        <td align="center"><a href="https://github.com/codusl100"><img src="https://github.com/codusl100.png" width="100px;" alt=""/><br/><sub>Backend<b><br/>팀원 백채연</b></sub></a></td>
   </tr>
</table>
