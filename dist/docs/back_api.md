# API 명세서

## 공통
- **Base URL:** `https://[도메인]`
- **Content-Type:** `application/json`

---

## Health

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/health` | 서버 상태 확인 |

---

## Auth

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/api/auth/refresh` | Access Token 재발급 |

### POST /api/auth/refresh
- **Request:** `refreshToken` (HttpOnly Cookie)
- **Response:**
```json
{
  "accessToken": "string"
}
```

---

## Login

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/api/login` | 로그인 |
| POST | `/api/logout` | 로그아웃 |

### POST /api/login
> 반복 실패 시 일시적으로 로그인이 제한될 수 있습니다

- **Request:**
```json
{
  "userId": "string",
  "password": "string"
}
```
- **Response:**
```json
{
  "accessToken": "string",
  "userId": "string",
  "userName": "string"
}
```

### POST /api/logout
- **Response:** `204 No Content`

---

## Member

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/api/member/join` | 회원가입 |
| POST | `/api/member/withdraw` | 회원탈퇴 |
| GET | `/api/member/idCheck` | 아이디 중복확인 |
| POST | `/api/member/findPassword` | 인증 메일 발송 |
| POST | `/api/member/verifyNum` | 인증번호 확인 |
| POST | `/api/member/resetPassword` | 비밀번호 변경 |

### GET /api/member/idCheck
- **Query:** `?userId=string`
- **Response:** `true` (중복) / `false` (사용 가능)

### POST /api/member/findPassword
- **Query:** `?userId=string&email=string`
- **Response:** `true`

### POST /api/member/verifyNum
> findPassword 호출 후 발급된 인증번호로만 확인 가능
- **Query:** `?userId=string&certificateNum=string`
- **Response:** `true`

### POST /api/member/resetPassword
> verifyNum 완료 후에만 호출 가능
- **Request:**
```json
{
  "userId": "string",
  "newPassword": "string"
}
```
- **Response:** `true`
