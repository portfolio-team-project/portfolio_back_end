# Spring Boot + Docker + Jenkins CI/CD (Home Server)

## 📌 프로젝트 개요
이 프로젝트는 Spring Boot 애플리케이션을 Docker 기반으로 컨테이너화하고,
Jenkins를 이용해 CI/CD를 구성하여 홈 서버에 자동 배포하는 것을 목표로 합니다.

---

## 🧱 인프라 구성

- Database: PostgreSQL
- Application: Spring Boot 3.5
- Container: Docker
- CI/CD: Jenkins
- Server: Home Server (Ubuntu 22.04.5 LTS)

---

## 🧰 Environment

- JDK: 17.0.19 (LTS)
- Spring Boot: 3.5.0
- Gradle
- Redis
- Nginx
- Cloudflare Tunnel

---

## 🏗 Architecture

```
Client → Cloudflare Tunnel → Nginx → Spring Boot (WAS)
                                   ↘ Redis (Token / 로그인 실패 횟수 관리)
                                   ↘ PostgreSQL (DB)
```

---

## 프로젝트 구조

```
src/main/java/com/api/
├── ApiApplication.java
├── domain/
│   ├── admin/                          # 관리자
│   │   ├── controller/
│   │   │   └── adminController.java
│   │   ├── service/
│   │   │   ├── AdminService.java
│   │   │   └── AdminServiceImpl.java
│   │   └── dto/
│   │       ├── adminRequest.java
│   │       └── adminResponse.java
│   ├── auth/                           # 인증 (토큰 재발급, 카카오)
│   │   ├── controller/
│   │   │   └── AuthController.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   ├── AuthServiceImpl.java
│   │   │   ├── KaKaoService.java
│   │   │   └── KaKaoServiceImpl.java
│   │   ├── entity/
│   │   │   ├── AuthEntity.java
│   │   │   └── UserAuthEntity.java
│   │   ├── repository/
│   │   │   ├── AuthRepository.java
│   │   │   └── UserAuthRepository.java
│   │   └── dto/
│   │       ├── AuthRequest.java
│   │       ├── AuthResponse.java
│   │       ├── RefreshRequest.java
│   │       └── SocialLoginRequest.java
│   └── base/
│       ├── Accession/                  # 회원가입
│       │   ├── controller/
│       │   │   └── AccessionController.java
│       │   ├── service/
│       │   │   ├── AccessionService.java
│       │   │   └── AccessionServiceImpl.java
│       │   └── dto/
│       │       ├── AccessionRequest.java
│       │       ├── EmailAuthRequest.java
│       │       └── VerifyNumRequest.java
│       ├── Contact/                    # 문의
│       │   ├── controller/
│       │   │   └── ContactController.java
│       │   └── dto/
│       │       └── ContactRequest.java
│       ├── Login/                      # 로그인/로그아웃
│       │   ├── controller/
│       │   │   └── LoginController.java
│       │   ├── service/
│       │   │   ├── LoginService.java
│       │   │   └── LoginServiceImpl.java
│       │   ├── entity/
│       │   │   └── LoginEntity.java
│       │   ├── repository/
│       │   │   └── LoginRepository.java
│       │   └── dto/
│       │       ├── LoginRequest.java
│       │       ├── LoginResponse.java
│       │       └── KakaoLoginRequest.java
│       └── Member/                     # 회원 관리
│           ├── controller/
│           │   └── MemberController.java
│           ├── service/
│           │   ├── MemberService.java
│           │   └── MemberServiceImpl.java
│           ├── entity/
│           │   └── MemberEntity.java
│           ├── repository/
│           │   └── MemberRepository.java
│           └── dto/
│               ├── MemberRequest.java
│               ├── MemberResponse.java
│               ├── MemberDetailResponse.java
│               ├── ChangePasswordRequest.java
│               └── WithdrawRequest.java
├── board/                              # 게시판
│   ├── controller/
│   │   ├── BoardController.java
│   │   └── BoardCommentController.java
│   ├── service/
│   │   ├── BoardService.java
│   │   ├── BoardServiceImpl.java
│   │   ├── BoardCommentService.java
│   │   └── BoardCommentServiceImpl.java
│   ├── entity/
│   │   ├── BoardEntity.java
│   │   └── BoardCommentEntity.java
│   ├── repository/
│   │   ├── BoardRepository.java
│   │   └── BoardCommentRepository.java
│   └── dto/
│       ├── BoardRequest.java
│       ├── BoardDetailResponse.java
│       ├── BoardListResponse.java
│       ├── BoardPageResponse.java
│       ├── BoardDeleteRequest.java
│       ├── CommentRequest.java
│       └── CommentResponse.java
├── qna/                                # Q&A
│   ├── controller/
│   │   └── QnaController.java
│   ├── service/
│   │   ├── QnaService.java
│   │   └── QnaServiceImpl.java
│   ├── entity/
│   │   └── QnaEntity.java
│   ├── repository/
│   │   └── QnaRepository.java
│   └── dto/
│       ├── QnaRequest.java
│       ├── QnaDetailResponse.java
│       ├── QnaListResponse.java
│       └── QnaMemberRequest.java
└── global/
    ├── common/
    │   └── ApiResponse.java            # 공통 응답 래퍼
    ├── config/
    │   ├── AppConfig.java
    │   ├── CorsProperties.java
    │   └── KaKaoProperties.java
    ├── constants/
    │   ├── MessageConstants.java
    │   └── MailConstant.java
    ├── exception/
    │   ├── GlobalExceptionHandler.java
    │   └── BusinessException.java
    ├── health/
    │   └── HealthController.java
    ├── redis/
    │   ├── RedisService.java
    │   └── LoginFailService.java       # 로그인 실패 횟수 추적
    ├── security/
    │   ├── SecurityConfig.java
    │   ├── jwt/
    │   │   ├── JwtFilter.java
    │   │   └── JwtProvider.java
    │   └── handler/
    │       ├── CustomAccessDeniedHandler.java
    │       └── CustomAuthenticationEntryPoint.java
    └── util/
        ├── MailUtil.java               # 이메일 발송
        ├── MailConstant.java
        ├── TempPwdUtil.java            # 임시 비밀번호 생성
        ├── UuidUtil.java
        ├── HashUtil.java
        ├── HtmlSanitizer.java          # XSS 방어
        └── HttpUtil.java
```

---

## 주요 기능

- JWT 인증 (Access Token + HttpOnly Cookie Refresh Token)
- 카카오 소셜 로그인 (OAuth 2.0)
- 로그인 실패 횟수 제한 (Redis, 5회 초과 시 잠금)
- 비밀번호 만료 주기 체크 (3개월)
- 관리자 임시 비밀번호 발급 및 이메일 자동 발송
- 게시판 CRUD + 댓글
- Q&A (비회원 작성 지원)
- XSS 방어 (HTML Sanitizer)
- 글로벌 예외 처리

---

## 환경 변수

`.env` 파일을 루트에 생성하고 아래 값을 설정하세요.

```env
DB_URL=jdbc:postgresql://host:port/dbname
DB_USERNAME=username
DB_PASSWORD=password

REDIS_HOST=localhost
REDIS_PORT=6379

JWT_SECRET=your-secret-key
JWT_ACCESS_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=604800000

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email
MAIL_PASSWORD=your-password
```

---

## 📄 API 명세

[back_api.md](./dist/docs/back_api.md)

## 🚀 실행 방법

### 1. 로컬 실행
```bash
./gradlew bootRun
```
