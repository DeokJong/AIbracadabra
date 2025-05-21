# SSAFY_TRIP 프로젝트

## 📌 프로젝트 개요

**SSAFY_TRIP**은 카카오 API, 뉴스 크롤링, AI 채팅 기능, Redis 메모리, JWT 인증을 활용하여 사용자에게 여행 정보(관광지, 축제, 경로 추천), 커뮤니티 기능(게시판, 공지사항), 뉴스 정보 기능을 제공하는 웹 애플리케이션입니다.

주요 기술 스택: Java 17, Spring Boot, MyBatis, HikariCP, Redis, Spring Security, JWT, Spring AI, Jsoup, Swagger UI, MySQL

## 🗂 파일 구조

```yaml
trip/
├── src/
│   ├── main/
│   │   ├── java/com/ssafy/
│   │   │   ├── ai/            # Spring AI 설정, ChatClient, ContentTypeTools
│   │   │   ├── config/        # Redis, Security, ChatMemory 설정
│   │   │   ├── exception/     # 커스텀 예외 및 핸들러
│   │   │   ├── model/dao/     # MyBatis 매퍼 인터페이스
│   │   │   ├── model/dto/     # 도메인 DTO
│   │   │   ├── model/service/impl/  # 서비스 구현체
│   │   │   ├── restcontroller/ # REST API 컨트롤러 (회원, 게시판, 공지, 뉴스, 지도)
│   │   │   ├── security/      # Spring Security 필터, JWT, 구성
│   │   │   ├── util/          # 공통 유틸리티 (CookieUtil, RuntimeTypeAdapterFactory)
│   │   └── resources/         # 설정 파일, 프로퍼티, mapper XML
│   └── test/                  # 단위 및 통합 테스트
├── pom.xml                    # 프로젝트 의존성 관리
└── README.md                  # 프로젝트 설명 문서
```

## 🌟 구현 기능

### 1. 관광지 정보 & 최적 경로 추천  

- **데이터 수집**: Kakao Map API과 생성형 AI를 활용해서 최적의 경로를 제공  
- **거리 계산**: 맨해튼 거리로 인접행렬 생성  
- **알고리즘**: Held–Karp DP 기반 TSP구현 (최대 32개)

### 2. 커뮤니티  

- **게시판**:  
  - 글쓰기/수정/삭제/조회  
  - Rabin–Karp + 우선순위 큐 활용 키워드 검색  
- **공지사항**:  
  - CRUD  
  - 키워드 검색 시 작성일 최신순 정렬

### 3. 인증·인가  

- **로그인**: JSON 요청 처리(JsonLoginAuthenticationConverter) → JWT 발급
- **토큰 갱신**: Access 토큰 만료 시 Refresh 토큰으로 재발급  
- **권한 계층**: ADMIN→USER→GUEST 상속 구조

### 4. 뉴스 크롤링  

- **주기**: 스케줄러(1시간)  
- **크롤러**: Jsoup 기반, 중복 URL 체크 → DB 저장
- **API**: `/news` MVC 뷰 & REST API 제공  

### 5. AI 채팅 및 메모리 관리  

- **ChatClient 구성**: Spring AI 기반 OpenAI ChatClient 설정 (ChatClientConfig)  
- **함수 호출 도구**: ContentTypeTools 활용  
- **Kakao Map API 도구**: `KakaoApiTools` 사용, 선택된 장소의 x,y 좌표와 사용자 입력을 기반으로 카테고리별 장소 검색 결과를 리스트 형태로 반환  
- **챗 메모리**: RedisChatMemory, InMemoryChatMemory 구현  
- **엔드포인트**:  
  - `/api/v1/chat/recommend` (추천 대화)  
  - `/api/v1/chat/chatHistory` (대화 기록 조회)  
  - `/api/v1/chat/cleanChatMemory` (메모리 초기화)  

### 6. 기타 기능

- **Swagger UI**: OpenAPI 문서 제공 (`springdoc-openapi`)
- **파일 업로드**: Multipart 설정 (최대 50MB) (`application.properties`)
- **Redis Caching**, **AOP**, **Lombok**, 지원

## 🚀 실행 방법

1. **환경 설정**

   - MySQL: `jdbc:mysql://localhost:3306/ssafytrip` (프로필: local)
   - Redis: `localhost:6379`

2. **프로젝트 빌드 및 실행**

```bash
mvn clean package
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

3. **Swagger UI 접속**

   - `http://localhost:8080/swagger-ui/index.html#/`

---

## 🧪 테스트

- **단위 테스트(Unit Test)**: `mvn test` 또는 `mvn verify` 명령어로 서비스 로직 및 DAO 레이어 테스트를 실행합니다.
- **통합 테스트(Integration Test)**: Postman이나 Insomnia를 이용해 주요 REST API 엔드포인트를 검증합니다. `test/` 디렉토리 내에 샘플 요청 스크립트가 포함되어 있습니다.

## 📖 API 문서

- Swagger UI: `http://localhost:8080/swagger-ui/index.html#/`에서 실시간 API 스펙을 확인할 수 있습니다.

---

## 📦 배포

- **Docker**: `docker-compose up --build`
- **Health Check**: `/actuator/health`

---

## 🔗 Git Flow 및 PR 프로세스

### 브랜치 전략

- **main**
  - 항상 안정적인 배포 가능한 코드가 존재하는 브랜치
- **develop**
  - 기능 개발이 완료된 코드를 통합하는 브랜치
- **feature/기능명**
  - 새로운 기능 개발 후, develop 브랜치로 병합
- **bugfix/버그명**
  - 버그 수정 전용 브랜치 (수정 후 develop 또는 main 브랜치로 병합)

### 커밋 메세지 컨벤션

- ✨ `feat`: 새로운 기능 추가
- 🔨 `refactor`: 코드 리팩토링 (기능 변화 없음)
- 📝 `docs`: 문서 추가 또는 수정 (README, 주석 등)
- 🐞 `fix`: 버그 수정
- 🚀 `perf`: 성능 개선
- 🎨 `style`: 코드 스타일 및 포맷팅 수정
- 🔧 `chore`: 기타 잡다한 작업
- ⚙️ `conf` : 프로젝트 설정

### PR 프로세스

1. **기능 개발**
    - 각 기능별로 feature 브랜치에서 작업 후 Pull Request(PR)를 생성합니다.
2. **코드 리뷰 및 테스트**
    - 팀원들과 코드 리뷰를 진행하고, 테스트가 완료되면 develop 브랜치에 병합합니다.
3. **배포**
    - 안정화된 코드는 main 브랜치로 병합 후 배포합니다.

---

## 🖼️ SWAGGER

### Auth

![Auth API](/img/auth.png)

### Board

![Board API](/img/board.png)

### Chat

![Chat API](/img/chat.png)

### Map

![Map API](/img/map.png)

### Member

![Member API](/img/member.png)

### Notice

![Notice API](/img/notice.png)
