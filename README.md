# remain
남은 시간 알려주는 Dooray 슬래시 커맨드

## 간단한 원리
![image](https://github.com/user-attachments/assets/db13804a-6766-465f-b2c1-f14c73360aa2)
사용자가 특정 커맨드를 전송하면, 두레이 자체에서 이 서버로 post 요청을 대신해주는 원리

이때 전송 정보(커맨드, 사용자)가 Request Body로 들어옴.  
<img width="466" alt="image" src="https://github.com/user-attachments/assets/2a8731f4-9d83-49e3-96d2-133c2e0f7bee" />

해당 정보를 이용하여, 특정 커맨드에 따른 분기처리를 해주면 된다.  
이때, 응답으로 내려줘야 하는 리스폰스에 대해서는 [다음](https://helpdesk.dooray.com/share/pages/9wWo-xwiR66BO5LGshgVTg/2900080846994699495)을 참고하면 된다.

## 테스트는 어케해여
1. 개인 로컬 환경에서 서버 run
2. 앞서 설명한 Request Body를 담아서 POST 요청
3. 리스폰스 확인

## Contribution
1. 해당 프로젝트를 fork하여 개인 레포지토리에서 작업 후, 본 레포지토리에 pr 요청
2. 검토 후 머지 여부 결정
3. 로직은 기능단위로, 별도의 파일로서 분리되었음 하는 바람이 있음
4. 재밌는 아이디어 있으면 바로바로 pr 요청 마구 찔러도됨!
5. [선택 사항] 테스트 코드 작성해주면 더더욱 좋음!
