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

## 프로젝트 구조

```
src/main/java/com/api/
├── ApiApplication.java
├── domain/
│   ├── admin/                  # 관리자
│   ├── auth/                   # 인증 (토큰 재발급)
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── service/
│   └── base/
│       ├── Accession/          # 회원가입
│       ├── Contact/            # 문의
│       ├── Login/              # 로그인/로그아웃
│       └── Member/             # 회원 관리
└── global/
├── common/                 # 공통 응답 등
├── config/                 # 설정 (Security, Redis 등)
├── constants/              # 메시지 상수
├── exception/              # 예외 처리
├── health/                 # 헬스 체크
├── redis/                  # Redis 서비스
├── security/               # JWT 필터, 인증
└── util/                   # 유틸리티
```

---

## 🏗 Architecture

```
Client → Cloudflare Tunnel → Nginx → Spring Boot (WAS)
                                   ↘ Redis (Token 관리)
                                   ↘ PostgreSQL (DB)
```

---

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