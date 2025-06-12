# AIbracadabra

**Spring Boot & Vue.js 기반 채팅 여행 계획 플랫폼**

> 삼성 청년 AI·SW 아카데미(SSAFY) 13기 1학기 관통프로젝트

---

## 목차 (Table of Contents)

1. [소개 (Introduction)](#introduction)
2. [기능 (Features)](#features)
3. [기술 스택 (Tech Stack)](#tech-stack)
4. [설치 및 실행 방법 (Installation & Usage)](#installation-and-usage)

   * [사전 요구사항 (Prerequisites)](#prerequisites)
   * [설치 (Installation)](#installation)
   * [실행 (Running)](#running)
5. [폴더 구조 (Project Structure)](#project-structure)
6. [환경 설정 (Configuration)](#configuration)
7. [다이어그램 (Diagrams)](#diagrams)
8. [화면 설계 (UI)](#ui)
9. [커밋 컨벤션 (Commit Convention)](#commit-convention)

---

<h2 id="introduction">소개 (Introduction)</h2>

AIbracadabra은 **Spring Boot** 기반 Backend와 **Vue.js** 기반 Frontend로 개발된 여행 정보 제공 및 OpenAI 기반 챗봇을 통한 여행 계획 추천 서비스를 제공하는 플랫폼입니다.

주요 기능은 다음과 같습니다:

* 기본 커뮤니티: 게시판, Q\&A, 공지사항 관리
* 지역 축제 뉴스: Jsoup를 활용한 실시간 웹 크롤링
* 지도 서비스: Kakao Map 연동
* 공공 데이터: 공공 데이터 포털 API를 통한 관광지·숙소 정보 수집
* 날씨 정보: OpenWeatherMap API 연동
* 채팅 여행 플래너: **Spring AI (OpenAI)** 기반 LLM을 활용한 대화형 일정 및 상권 추천

<h2 id="features">기능 (Features)</h2>

| 구분        | 설명                                           |
| --------- | -------------------------------------------- |
| 커뮤니티      | 게시판, Q\&A, 공지사항 관리                           |
| 축제 뉴스     | Jsoup를 사용한 HTML 파싱 기반 웹 크롤링                  |
| 지도 서비스    | vue3-kakao-maps를 이용한 위치 검색 및 팝업              |
| 공공 데이터    | 공공 데이터 포털 API를 통해 관광지·숙소 목록 제공               |
| 날씨 정보     | OpenWeatherMap API를 통한 실시간 날씨 조회             |
| 채팅 여행 플래너 | Spring AI(OpenAI)와 Redis를 활용한 대화형 일정 및 상권 계획 |

<h2 id="tech-stack">기술 스택 (Tech Stack)</h2>

### Backend

* **Spring Boot**: REST API 및 애플리케이션 프레임워크
* **MyBatis**: SQL 매핑 및 데이터베이스 접근 계층
* **Spring AI (OpenAI)**: LLM 모델 연동
* **Spring Security & JWT**: 인증·인가 및 토큰 처리
* **Redis**: Chat 메시지 및 세션 관리
* **Jsoup**: 웹 크롤링
* **springdoc-openapi (Swagger UI)**: API 문서화 및 Swagger UI 제공
* **Spring WebFlux**: 비동기·논블로킹 HTTP 클라이언트
* **Docker & Docker Compose**: 컨테이너화 및 배포

### Frontend

* **Vue.js 3**: UI 프레임워크
* **Vite**: 번들러 및 개발 서버
* **Pinia**: 상태 관리
* **Vuetify**: UI 컴포넌트
* **Axios**: HTTP 통신
* **vue-toastification**: 알림 UI
* **vuedraggable (SortableJS)**: 드래그 앤 드롭
* **vue3-kakao-maps**: Kakao Map 연동
* **ESLint & Prettier**: 코드 스타일 유지

<h2 id="installation-and-usage">설치 및 실행 방법 (Installation & Usage)</h2>

<h3 id="prerequisites">사전 요구사항 (Prerequisites)</h3>

* **Java 17** 이상
* **Maven Wrapper**(`./mvnw`)
* **Docker & Docker Compose** (선택)
* **Node.js** ≥ 16
* **npm** ≥ 8 또는 **yarn**

<h3 id="installation">설치 (Installation)</h3>

```bash
# 저장소 클론
git clone https://github.com/DeokJong/AIbracadabra.git
cd EnjoyTrip_FINAL_PJT

# Backend 빌드
cd backend
./mvnw clean package

# Frontend 의존성 설치
cd ../frontend
npm install  # 또는 yarn install
```

#### (선택) Docker Compose 실행

```bash
# 루트 디렉터리에서
docker-compose up --build
```

<h3 id="running">실행 (Running)</h3>

```bash
# Backend (포트 8080)
cd backend
./mvnw spring-boot:run

# Frontend (포트 5173)
cd ../frontend
npm run dev
```

<h2 id="project-structure">폴더 구조 (Project Structure)</h2>

```
.
├── docker-compose.yml           # Docker Compose 설정
├── README.md                    # 이 문서
├── backend/                     # Spring Boot 프로젝트
│   ├── Dockerfile               # Docker 이미지 설정
│   ├── pom.xml                  # Maven 설정
│   └── src/                     # 소스 코드
│       ├── main/                # 메인 애플리케이션
│       └── test/                # 테스트 코드
├── frontend/                    # Vue.js 프로젝트
│   ├── package.json             # npm 스크립트 및 의존성
│   ├── public/                  # 정적 자원
│   └── src/                     # Vue 컴포넌트 등
├── database/                    # DB 초기화 스크립트
│   └── init/                    # 스키마 및 데이터 로드 SQL
└── docs/                        # 추가 문서 및 다이어그램
    └── diagrams/                # 설계 다이어그램 이미지
        ├── Controller_Diagram.png
        ├── DAO_Class_Diagram.png
        ├── Service_Class_Diagram.png
        ├── Spring_AI_Class_Diagram.png
        └── ERD.png
```

<h2 id="configuration">환경 설정 (Configuration)</h2>

Spring Boot 설정 파일:

* `backend/src/main/resources/application.properties`
* `backend/src/main/resources/application-local.properties`
* `backend/src/main/resources/application-docker.properties`

```properties
# 데이터베이스
spring.datasource.url=jdbc:mysql://localhost:3306/enjoy_trip
spring.datasource.username=root
spring.datasource.password=pass

# 공공 데이터 포털
spring.web-client.gw-api-key=YOUR_API_KEY

# OpenAI
spring.ai.openai.api-key=YOUR_API_KEY

# OpenWeatherMap
weather.openweathermap.api-key=YOUR_API_KEY

# Kakao Map
spring.web-client.kakao-api-key=YOUR_KAKAO_API_KEY
```

<h2 id="diagrams">다이어그램 (Diagrams)</h2>

### Controller

![Controller Diagram](/docs/diagrams/Controller_Diagram.png)

### DAO

![DAO Class Diagram](/docs/diagrams/DAO_Class_Diagram.png)

### Service

![Service Class Diagram](/docs/diagrams/Service_Class_Diagram.png)

### Spring AI

![Spring AI Class Diagram](/docs/diagrams/Spring_AI_Class_Diagram.png)

### ERD

![ERD](/docs/diagrams/ERD.png)


<h2 id="ui">화면 설계 (UI)</h2>

### 메인

![Main Page](/docs/ui/index.png)

### 로그인

![Login Page](/docs/ui/login.png)

### 게시판

![Board Page](/docs/ui/board.png)

### 여행 지도

![Map Page](/docs/ui/map.png)

<h2 id="commit-convention">커밋 컨벤션 (Commit Convention)</h2>

본 프로젝트는 **Git Flow** 전략을 기반으로 하며, 다음과 같은 커밋 메시지 타입을 사용합니다:

* ✨ `feat`: 새로운 기능 추가
* 🔨 `refactor`: 코드 리팩토링 (기능 변경 없음)
* 📝 `docs`: 문서 추가 또는 수정 (README, 주석 등)
* 🐞 `fix`: 버그 수정
* 🚀 `perf`: 성능 개선
* 🎨 `style`: 코드 스타일 및 포맷팅 변경
* 🔧 `chore`: 기타 잡다한 작업
* ⚙️ `conf`: 프로젝트 설정 변경
