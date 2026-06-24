# API 명세서

## 공통

- **Base URL:** `https://[도메인]`
- **Content-Type:** `application/json`

---

## Login

| Method | URL | 인증 | 설명 |
|--------|-----|------|------|
| POST | `/api/login` | - | 로그인 |
| POST | `/api/logout` | O | 로그아웃 |

### POST /api/login
> 반복 실패 시 로그인이 일시 제한됩니다.
> 임시 비밀번호 또는 비밀번호 만료 시 강제 변경 응답이 반환됩니다.

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
  "userName": "string",
  "role": "string",
  "isSocial": true
}
```

### POST /api/logout
- **Response:** `204 No Content`

---

## Member

| Method | URL | 인증 | 설명 |
|--------|-----|------|------|
| POST | `/api/member/join` | - | 회원가입 |
| GET | `/api/member/idCheck` | - | 아이디 중복확인 |
| POST | `/api/member/findPassword` | - | 비밀번호 찾기 인증 메일 발송 |
| POST | `/api/member/verifyNum` | - | 인증번호 확인 |
| POST | `/api/member/resetPassword` | - | 비밀번호 재설정 (인증 후) |
| POST | `/api/member/changePassword` | △ | 비밀번호 변경 |
| POST | `/api/member/withdraw` | O | 일반 회원 탈퇴 |
| POST | `/api/member/socialWithdraw` | O | 소셜 회원 탈퇴 |

> △ 로그인 상태(계정설정)에서는 인증 필요, 만료/임시 비밀번호 변경 시에는 불필요

### POST /api/member/join
```json
{
  "userId": "string",
  "password": "string",
  "userName": "string",
  "email": "string"
}
```

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
```json
{
  "userId": "string",
  "newPassword": "string"
}
```
- **Response:** `true`

### POST /api/member/changePassword
- **로그인 상태 (계정설정):** 인증 헤더 필요
```json
{ "currentPassword": "string", "newPassword": "string" }
```
- **미인증 (만료 / 임시 비밀번호):**
```json
{ "userId": "string", "currentPassword": "string", "newPassword": "string" }
```
- **Response:** `true`

### POST /api/member/withdraw
```json
{ "password": "string" }
```

### POST /api/member/socialWithdraw
- **Request:** 없음

---

## Board (게시판)

> 모든 엔드포인트 인증 필요

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/api/board/list` | 게시글 목록 |
| GET | `/api/board/{localId}` | 게시글 상세 |
| POST | `/api/board/write` | 게시글 작성 |
| PUT | `/api/board/{localId}` | 게시글 수정 |
| DELETE | `/api/board/{localId}` | 게시글 삭제 |
| GET | `/api/board/{localId}/comments` | 댓글 목록 |
| POST | `/api/board/{localId}/comments` | 댓글 작성 |
| PUT | `/api/board/{localId}/comments/{commentId}` | 댓글 수정 |
| DELETE | `/api/board/{localId}/comments/{commentId}` | 댓글 삭제 |

### GET /api/board/list
- **Query:** `?page=0&title=string`
- **Response:**
```json
{
  "content": [
    {
      "localId": 1,
      "title": "string",
      "userId": "string",
      "createdDate": "string",
      "viewCnt": 0,
      "noticeYn": "N"
    }
  ],
  "page": { "totalPages": 1, "totalElements": 10, "number": 0 }
}
```

### POST /api/board/write
```json
{ "title": "string", "content": "string" }
```

### PUT /api/board/{localId}
```json
{ "title": "string", "content": "string" }
```

### DELETE /api/board/{localId}
```json
{ "password": "string" }
```

### POST /api/board/{localId}/comments
```json
{ "content": "string" }
```

### PUT /api/board/{localId}/comments/{commentId}
```json
{ "content": "string" }
```

### DELETE /api/board/{localId}/comments/{commentId}
```json
{ "password": "string" }
```

---

## QNA

| Method | URL | 인증 | 설명 |
|--------|-----|------|------|
| GET | `/api/qna` | - | QNA 목록 (간략) |
| GET | `/api/qna/searchQna` | - | QNA 목록 (검색/페이지) |
| GET | `/api/qna/detail/{qnaSeq}` | - | QNA 상세 |
| POST | `/api/qna/guest` | - | 비회원 Q&A 작성 |

### GET /api/qna/searchQna
- **Query:** `?page=0&title=string`

### POST /api/qna/guest
```json
{
  "nickname": "string",
  "title": "string",
  "content": "string"
}
```

---

## Contact (문의)

| Method | URL | 인증 | 설명 |
|--------|-----|------|------|
| POST | `/api/contact` | - | 문의 메일 발송 |

### POST /api/contact
```json
{
  "name": "string",
  "email": "string",
  "message": "string"
}
```
- **Response:** `true`
