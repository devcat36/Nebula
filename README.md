# Nebula: 음반 장터 프로그램

---

## 1. 설계 배경
### 프로젝트의 필요성
- 최근 LP 판매량이 CD 판매량을 넘어서는 등 아날로그 음반 매체에 대한 관심도가 증가함
- 이에 따라 중고 음반을 구매하려는 수요가 늘고 있음
- 기존 중고 거래 플랫폼에서는 음반 목록을 체계적으로 탐색하기 어려움
- 따라서 음반을 체계적으로 탐색할 수 있는 플랫폼 필요
### 문제정의
- 체계화된 음반 데이터베이스와 음반 장터를 결합
- 사용자가 자유롭게 음반 데이터베이스에 기여
### 기대 효과
- 손쉬운 중고 음반 재고 관리로 판매업체 증가
- 손수운 음반 탐색으로 구매자 증가
- 중고 음반 시장 활성화 기대

## 2. 설계 목표
### 요구사항 분석
- 음반 관리 기능
    - 음반 생성/수정/삭제 요청 생성
    - 나의 음반 생성/수정/삭제 요청 목록 조회
    - 음반 검색
        - 앨범명, 아티스트명으로 text search
        - relevacne 순, 최대 100개 결과
    - 음반 조회
- 장터 기능
    - 판매 기능
        - 상품 등록/삭제
        - 나의 상품 목록 조회
        - 받은 구매 요청 조회
        - 구매 요청 승인/거절/완료
            - 구매 요청 승인 시 상품 상태 예약중으로 변경
            - 다수의 구매요청 동시 승인 불가
            - 승인된 구매요청 거절 시 상품 상태 판매중으로 변경
            - 구매요청 완료 시 거래 상태 판매완료로 변경, 나머지 구매요청 거절처리
    - 구매 기능
        - 상품 검색
            - 앨범명, 아티스트명, 판매자명으로 text search
            - relevance 순, 최대 100개 결과
        - 상품 조회
        - 구매 요청 생성/취소
        - 생성한 구매 요청 목록 조회

- 계정 기능
    - 회원 가입
    - 회원 정보 수정
    - 회원 탈퇴
- 관리자 기능
    - 음반 생성/수정/삭제 요청 승인/거절
    - 상품 삭제
    - 회원 삭제
    - 회원 권한 변경
    - 회원 목록 조회

### 고려 사항
- 회원이 삭제되면 그 회원이 생성한 상품, 활성화된 구매요청이 함께 삭제됨
- 각 회원의 역할과 구분에 제한이 필요함
    - 각 회원은 각자가 권한을 가진 요소 (상품, 요청 등) 에 정해진 권한을 가짐
    - 각 회원의 권한을 제한할 수 있는 보안 장치 필요

### 사용 기술 스택
- DB: H2
- WAS: Spring Boot + JPA
- Client: Node 기반 CLI 어플리케이션

### 시스템 목표
- 관리자를 제외한 각 endpoint 에서 100tps 이상의 처리 속도
- ngrinder 를 이용한 부하 성능 측정

## 3. 설계 내용
### ERD
![ERD](https://drive.google.com/uc?id=1izq8oqxw1oM3l6viIxihPKnkPYxSrBol)

### API
- 음반 관리
    - 음반 조회  
      ```GET /disco/album/{album_id}```
    - 음반 검색  
      ```GET /disco/search?type={search_type}&term={search_term}```
    - 음반 기여 요청  
      ```POST /disco/contribution```
        - Header: ```{session_id}```
        - Request Body: ```{type, album, title, artist, description, notes}```
    - 음반 기여 목록 조회  
      ```GET /disco/contributions```
        - Header: ```{session_id}```
    - 음반 기여 조회  
      ```GET /disco/contribution/{request_id}```
- 음반 장터
    - 상품 등록  
      ```POST /marketplace/item```
        - Header: ```{session_id}```
        - Request Body: ```{album, media, description, condition, price, status}```
    - 상품 조회  
      ```GET /marketplace/item/{item_id}```
    - 상품 삭제  
      ```DELETE /marketplace/item/{item_id}```
        - Header: ```{session_id}```
    - 상품 목록 조회 (판매자용)  
      ```GET /marketplace/items```
        - Header: ```{auth}```
    - 상품 검색  
      ```GET /marketplace/search?type={search_type}&term={search_term}```
    - 구매 요청 생성  
      ```POST /marketplace/order_request```
        - Header: ```{session_id}```
        - Request Body: ```{item, message}```
    - 구매 요청 승인/거절/취소/완료  
      ```PUT /markeplace/order_request/{request_id}?action={action_type}```
        - Header: ```{session_id}```
    - 구매 요청 목록 조회 (판매자)  
      ```GET /marketplace/order_requests/seller?item={item_id}```
        - Header: ```{session_id}```
    - 구매 요청 목록 조회 (구매자)  
      ```GET /marketplace/order_requests/buyer?item={item_id}```
        - Header: ```{session_id}```
- 계정
    - 로그인  
      ```POST /account/login```
        - Request Body: ```username=ME&password=pass```
    - 회원 가입
      ```POST /account```
        - Request Body: ```username=ME&password=pass&email=john@example.com```
    - 회원 정보 수정  
      ```PUT /account/{username}```
        - Header: ```{session_id}```
        - Request Body: ```{email}```
- 관리자
    - 음반 기여 승인  
      ```PUT /admin/contribute/approve/{request_id}```
        - Header: ```{session_id}```
    - 음반 기여 거절  
      ```PUT /admin/contribute/deny/{request_id}```
        - Header: ```{session_id}```
    - 상품 삭제  
      ```DELETE /admin/marketplace/item/{item_id}```
        - Header: ```{session_id}```
    - 회원 삭제  
      ```DELETE /admin/account/{username}```
        - Header: ```{session_id}```
    - 회원 권한 변경  
      ```PUT /admin/account/{username}```
        - Header: ```{session_id}```
        - Request Body: ```{role}```
    - 회원 목록 조회
      ```GET /admin/accounts```
        - Header: ```{session_id}```

### Client
- Node 기반 CLI 애플리케이션

## 4. 설계 결과 및 분석
- 소스코드(깃헙)
    - 서버
    - 클라이언트
- 기능적 요구사항 미충족 사항
    - Text search 대신 대소문자 무시 스트링 매쳐 사용
    - 클라이언트 매뉴얼 미작성
    - MySQL 대신 H2 사용
- 성능적 요구사항 미충족 사항
    - 객관적 성능 테스트 미수행

## 5. 결론 및 활용 분야
- 교훈
    - 요구사항에서 자신을 과다평가하여 무리하게 넣으면 결과가 좋지 않다.
- 새로운 목표
    - 공수가 너무 많이 들 것 같아 브라우저 웹 앱 대신 node 기반 cli 클라이언트로 작성하였는데, HTML/CSS 로 구현하면 좋을 것 같다.
    - 테스트 커버리지를 높혀 프로그램 안정성을 개선한다.
- 소요 비용 분석
    - BE: 16h
    - FE: 4h
    - 통합 테스트: 2h

## 6. 참고 문헌
1. [33년만에 CD 넘었다… 디지털 시대에 부는 LP열풍](https://www.donga.com/news/Culture/article/all/20200115/99227623/1)