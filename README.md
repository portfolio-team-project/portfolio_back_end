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
                                   ↘ Redis (Token 관리)
                                   ↘ PostgreSQL (DB)
```

---

## 📄 API 명세

[back_api.md](./dist/docs/back_api.md)

## 🚀 실행 방법

### 1. 로컬 실행
```bash
./gradlew bootRun
```