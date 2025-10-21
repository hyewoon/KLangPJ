# 🪅KLang- 외국인을 위한 한국어 단어 학습 앱

### 🔥 COMPOSE 적용 프로젝트는 <a href="https://github.com/hyewoon/KLangComposePJ">다음에서</a> 확인할 수 있습니다. 

## 1️⃣ 프로젝트 개요

* **KLang** 은 한국어능력시험인 TOPIK '초급' 단어를 학습 할 수 있는 앱입니다.

## 2️⃣ KLang 이란


* 학습 길잡이인 캐릭터 'K(케이)'😸와 함께 언어(Lang:랑)을 학습한다는 의미를 담은 **KLang(케이랑)** 을 앱의 이름으로 선정하였습니다.
* 사용자가 **학습목표를 설정**하고, 학습한 만큼 **포인트를 획득** 할 수 있는 앱입니다.
* 신뢰할 수 있는 단어데이터베이스인 **'한국어기초사전'API**에서 제공하는 단어 정보와 예문을 학습 할 수 있습니다.

## 3️⃣ 기술 스택
| skills | 설명 |
|------------------------|------|
| **kotlin** |kotlin2.0 적용  |
| **MVVM 아키텍처 패턴** | 멀티모듈 적용 |
| **ViewModel & StateFlow** | 상태 관리 |
| **DataStore** | 설정 및 전역 변수 관리 |
| **RoomDB** | SSOT(Single Source Of Truth)와 Offline First 구현 |
| **MLKit DigitalInk** | 손글씨 인식 기능을 onDevice로 구현 |
| **TTS/STT** | 인식 기능 전반 개발 |
| **Serverless Firebase Firestore** | 데이터 저장 및 동기화 구현 |
| **Navigation Graph** | 앱 화면 전환 |
| **Jetpack Material3 UI/UX** | CorBind 적용한 이벤트 처리 |


## 4️⃣ 아키텍처
### 시스템 아키텍처
<img width="1100" height="687" alt="image" src="https://github.com/user-attachments/assets/e30f1553-dfdf-4a2f-8cd7-194925e2000a" />

### 아키텍처 다이어그램
<img width="478" height="984" alt="image" src="https://github.com/user-attachments/assets/245fd182-513d-419a-9efc-ca193b6af17c" />



## 5️⃣ KLang 핵심기능
<img width="800" height="470" alt="image" src="https://github.com/user-attachments/assets/cd87d486-f4c1-4c22-a163-36dbda6bf326" />






 ✅ **오늘의 단어 학습** 
  - 예문, 듣기, 말하기, 쓰기 기능을 통해 사용자가 직접 참여하는 학습
  - 북마크 기능을 통해 나만의 단어장 만들기

 <img src="https://github.com/user-attachments/assets/2dfc4851-6a7b-4667-9241-ac11e414d79e" width="250" hspace="5" vspace="5">
 <img width="733" height="478" alt="image" src="https://github.com/user-attachments/assets/de2f3796-90ac-47d6-8556-b6d5fcda801e" />




✅ **단어와 놀기 : MLKit 손글씨 인식기능**
* 사용자가 손글씨를 입력하면 그에 해당하는 한글 단어를 비교해서 보여줌

<table width="90%">
  <tr>
    <td width="45%" align="center">
      <img src="https://github.com/user-attachments/assets/f4a753c4-a239-47aa-bff0-b5f4b0f475d5"  width="250" hspace="5" vspace="5">
    </td>
    <td width="45%" align="center">
      <img src="https://github.com/user-attachments/assets/7275c65a-471d-4db1-96c5-bc2c5efae2d9"  width="250" hspace="5" vspace="5">
    </td>
  </tr>
</table>

✅ **단어와 놀기 : 단어검색,TTS/STT** 
<img width="865" height="583" alt="image" src="https://github.com/user-attachments/assets/2ca4ecf6-94a2-4ca6-ae16-34ea0ace956c" />


 

   
  
 

