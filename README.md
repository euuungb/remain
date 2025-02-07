# remain
![created](https://img.shields.io/github/created-at/easternkite/remain)
![cb](https://img.shields.io/github/contributors/easternkite/remain)  
![checks](https://img.shields.io/github/check-runs/easternkite/remain/main)
![kotlin](https://img.shields.io/badge/dynamic/toml?url=https%3A%2F%2Fraw.githubusercontent.com%2Feasternkite%2Fremain%2Frefs%2Fheads%2Fmain%2Fgradle%2Flibs.versions.toml&query=%24.versions.kotlin&logo=Kotlin&label=Kotlin&color=purple)
![ktor](https://img.shields.io/badge/dynamic/toml?url=https%3A%2F%2Fraw.githubusercontent.com%2Feasternkite%2Fremain%2Frefs%2Fheads%2Fmain%2Fgradle%2Flibs.versions.toml&query=%24.versions.ktor&logo=Ktor&label=Ktor)  
남은 시간 알려주는 Dooray 슬래시 커맨드

## Features
### /time 
> 남은 시간 계산 커맨드
<img width="477" alt="image" src="https://github.com/user-attachments/assets/f8dc0979-6815-47f5-84ce-57f750d20b07" />

### /fortune
> MZ력 풀충전 오늘의 운세
<img src="https://github.com/user-attachments/assets/cb4167a1-a46f-4d7e-9160-456ea521c43d" width="400">


## 간단한 원리
![image](https://github.com/user-attachments/assets/db13804a-6766-465f-b2c1-f14c73360aa2)
사용자가 특정 커맨드를 전송하면, 두레이 자체에서 이 서버로 post 요청을 대신해주는 원리

이때, 서버에서는 전송 정보(커맨드, 사용자)가 Body에 담겨서 들어옴.  
<img width="466" alt="image" src="https://github.com/user-attachments/assets/2a8731f4-9d83-49e3-96d2-133c2e0f7bee" />

해당 정보를 이용하여, 특정 커맨드에 따른 분기처리를 해주면 된다.  
이때, 응답으로 내려줘야 하는 리스폰스에 대해서는 [다음](https://helpdesk.dooray.com/share/pages/9wWo-xwiR66BO5LGshgVTg/2900080846994699495)을 참고하면 된다.

## 테스트는 어케해여
1. 개인 로컬 환경에서 서버 run
2. 앞서 설명한 Request Body를 담아서 POST 요청
   ``` bash
   curl --location --request POST 'http://localhost:2424/time' \
    --header 'Content-Type: application/json' \
    --data '{
        "text": "22:00",
        "command": "/time"
    }'
   ```
   ``` bash
   curl --location --request POST 'http://localhost:2424/time' \
    --header 'Content-Type: application/json' \
    --data '{
        "text": "22:00 점심",
        "command": "/time"
    }'
   ```
3. 리스폰스 확인

## Contribution
1. 해당 프로젝트를 fork하여 개인 레포지토리에서 작업 후, 본 레포지토리에 pr 요청
2. 검토 후 머지 여부 결정
3. 로직은 기능단위로, 별도의 파일로서 분리되었음 하는 바람이 있음
4. 재밌는 아이디어 있으면 바로바로 pr 요청 마구 찔러도됨!
5. [선택 사항] 테스트 코드 작성해주면 더더욱 좋음!

## Licence
```
Copyright 2024-2025 easternkite

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
