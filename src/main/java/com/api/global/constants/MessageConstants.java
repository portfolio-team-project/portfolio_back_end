package com.api.global.constants;

public class MessageConstants {
    
    // 로그인
    public static final String LOGIN_LOCKED = "로그인이 일시적으로 제한되었습니다. 10분 후 다시 시도해주세요.";
    public static final String LOGIN_FAILED = "아이디 또는 비밀번호가 올바르지 않습니다.";
    
    // 회원
    public static final String MEMBER_NOT_FOUND = "존재하지 않는 아이디입니다.";
    public static final String PASSWORD_NOT_MATCH = "현재 비밀번호가 올바르지 않습니다.";
    public static final String AUTH_NOT_FOUND = "권한 정보 없음";
    public static final String UUID_NOT_FOUND = "존재하지 않는 유저 식별번호입니다.";
    public static final String MEMBER_INFO_NOT_FOUND = "정보가 존재하지 않습니다.";
    public static final String USER_NOT_FOUND = "존재하지 않는 유저입니다.";
    public static final String PWD_EXPIRED = "비밀번호를 변경해주세요.";
    public static final String SAME_PASSWORD = "새 비밀번호가 현재 비밀번호와 동일합니다.";
    public static final String ALREADY_WITHDRAW = "이미 탈퇴한 회원입니다.";
    public static final String PWD_CHG_FAILED = "비밀번호 변경에 실패했습니다.";

    // 인증
    public static final String CERT_NUM_EXPIRED = "인증번호가 만료되었습니다.";
    public static final String CERT_NUM_NOT_MATCH = "인증번호가 일치하지 않습니다.";
    public static final String EMAIL_SEND_FAILED = "이메일 발송에 실패했습니다.";
    public static final String CERT_NOT_VERIFIED = "인증이 완료되지 않았습니다.";
    public static final String CHECK_EMAIL_ID = "아이디 또는 이메일을 다시 한번 확인해주세요.";
    public static final String LOCKED_ID = "계정이 제한되었습니다. 관리자한테 문의해주세요.";
    public static final String PWD_CHG_LOCKED = "비밀번호를 5회 이상 잘못 입력하였습니다. 보안을 위해 로그아웃합니다.";
    public static final String PWD_CHG_LOCKED_EXPIRED = "비밀번호를 5회 이상 잘못 입력하였습니다. 비밀번호 찾기를 이용해주세요.";
    public static final String CHG_PWD_TEMP_PWD = "임시비밀번호입니다. 보안을 위해 비밀번호 변경 페이지로 이동합니다.";

    
    // 회원가입
    public static final String USER_ID_DUPLICATED = "이미 사용 중인 아이디입니다.";

    // 회원 탈퇴
    public static final String MEMBER_WITHDRAWN = "탈퇴한 계정입니다.";

    // 인증 토큰
    public static final String COOKIE_NOT_FOUND = "쿠키 정보가 없습니다.";
    public static final String REFRESH_TOKEN_NOT_FOUND = "refresh token not found";
    public static final String REFRESH_TOKEN_INVALID = "invalid refresh token";
    public static final String REFRESH_TOKEN_MISMATCH = "refresh token mismatch";
    
    //qna
    public static final String SEQ_NOT_FOUND = "QNA 정보를 찾을 수 없습니다.";
    public static final String ALREADY_ANSWER_EXIST = "이미 답변이 등록된 QNA입니다.";
    public static final String ALREADY_ANSWER_DELETE = "이미 삭제된 QNA입니다.";
}
