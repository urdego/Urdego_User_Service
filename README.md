<img width="339" height="238" alt="image" src="https://github.com/user-attachments/assets/2660a1a2-086f-44e9-ad4a-3fdeb159ac13" /># Urdego_User_Service

## 📅 프로젝트 진행 과정

- **기획기간** : 2024.10.21 ~ 2024.11.18
- **개발기간** : 2024.11.19 ~ 2025.03.25

![제목 없는 디자인](https://github.com/user-attachments/assets/b3b3f56c-6323-4f6e-aea8-30d5fde65813)

## 주요기능 
- 카카오 소셜 로그인
- 애플 소셜 로그인
- 유저 조회 및 저장
- 부적절한 닉네임 필터
- 유저 데이터 관리

## ERD
<img width="888" height="633" alt="image" src="https://github.com/user-attachments/assets/d1809f5c-dfd7-4388-9e61-35c6f0975e8c" />

## 설계
- 도메인의 중심 역할을 하는 요소들만 모아 관리하며 `단일 책임 원칙`과 `관심사 분리 원칙`에 따라 하위 패키지(컴포넌트)를 분리하였습니다.
- `Application Layer`와 `Infrastructure Layer`의 추상화를 통해 의존성의 경계를 구분하여 유지보수성을 높이고, 요구사항 변화에 유연하게 대응할 수 있도록 설계하였습니다. <br>
<img width="413" height="393" alt="image" src="https://github.com/user-attachments/assets/e92df156-f736-4c41-840b-e81e1f1f8567" />

- 결과적으로 SonarQube 기준 `유지보수성 A등급`과 `중복 코드율 0.4%`를 달성하였습니다.<br>
<img width="339" height="238" alt="image" src="https://github.com/user-attachments/assets/536ea421-c007-4baf-93db-58a0c00c90bc" />


## 성능 최적화
- 서버 부하를 줄이기 위해 Redis 캐시 도입
- 캐시 시 순서 보장을 위한 JAVA의 BlockingQueue를 통해 해결
- JPA의 LAZY 전략으로 인한 N+1 쿼리 문제 해결

## 테스트 
- 서비스로직을 역할과 책임으로 분류해 하위 컴포넌트 (`userCommander`, `userValidator` 등)으로 구분하여 단위 테스트 진행
- Mockito Framework를 활용하여 고립된 테스트 코드 작성
- 구문 커버리지 68%, 분기 커버리지 65% 달성

## 성능 테스트 
- JMeter를 사용하여 테스트를 진행하였습니다.
- Redis캐싱 10,000의 테스트 결과<br>
  <img width="743" height="170" alt="image" src="https://github.com/user-attachments/assets/b3c99839-6a07-47d6-902d-ae1105a85c8b" />
  - 평균 응답속도 380ms -> 7ms로 약 54배 개선하였습니다.
  - 1498ms까지 지연되던 응답이 97ms까지 감소하였습니다.
  - 전체 처리량 2662건 -> 6600건으로 약 2.4배 향상되었습니다.
 
- N+1 문제 해결 1,000의 테스트 결과 <br>
  <img width="744" height="174" alt="image" src="https://github.com/user-attachments/assets/29fdb9f3-1164-4cec-ad26-91f2cae6ee04" />
  - 응답시간 10ms -> 5ms로 50% 감소하였습니다.


# 🗺️ 어데고?! - 위치 추적 게임 서비스

<div align=center>

  <img src="https://github.com/user-attachments/assets/38306637-e5db-4335-93a5-82469e8d236e" width="300"><br/>
  <br/><strong> 소중한 사람들과 떠나는 추억 여행 🧳</strong><Br/>
  <strong>'어데고?!’</strong>는 사용자가 추천하거나 소중한 추억이 담긴 장소를 공유하고, 가족, 친구들과 함께 그 장소를 찾는 재미를 제공하는 서비스입니다. <br>지인들과 의미 있는 장소를 소재로 이야기를 나누며 추억을 공유하고 새로운 경험을 쌓을 수 있는  특별한 즐거움을 경험할 수 있습니다. <br> 이 서비스를 통해 바쁜 일상 속에서도 함께했던 소소한 순간들을 떠올리며 소중한 사람들과 따뜻한 추억을 새롭게 만들어 가세요!<br><br>
  <a href="https://urdego.vercel.app/">어데고?! 서비스 바로가기</a><Br/><Br/>
</div>




## 🎯 기능 소개

| 컨텐츠 업로드 | 컨텐츠 조회 |
| :-----------: | :---------: |
| ![ppt](https://github.com/user-attachments/assets/1c21460c-fef2-4355-9166-f23026880a69)| ![image](https://github.com/user-attachments/assets/92f6db29-93bd-4878-a731-87998c47bb67) |


| 친구 초대 | 대기방 |
| :-------: | :----: |
| ![image](https://github.com/user-attachments/assets/e3747f88-687f-4275-b8e5-384f49beebc7) | ![image](https://github.com/user-attachments/assets/caacfdec-9429-4686-b6d3-fb9f24a8c7b0) |

| 게임 시작 | 문제 출제 |
| :-------: | :-------: |
| ![image](https://github.com/user-attachments/assets/0ef508c5-fdfb-4c1f-b2da-ccf3b1f345c0) | ![image](https://github.com/user-attachments/assets/448292d3-4124-4002-822a-12ceb08ec2fc) |


| 답안 제출 | 결과 반환 |
| :-------: | :-------: |
| ![a7364026b0640d79](https://github.com/user-attachments/assets/345804da-a8bd-44e6-9cf8-9f8cfa71265f) | ![7209d730e9e4e594](https://github.com/user-attachments/assets/3b51a2de-272e-4561-b12f-07fc6bdeba0b) |

