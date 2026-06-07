package com.api.domain.base.Accession.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.api.domain.base.Accession.dto.AccessionRequest;
import com.api.domain.base.Accession.dto.EmailAuthRequest;
import com.api.domain.base.Accession.dto.VerifyNumRequest;
import com.api.domain.base.Accession.service.AccessionService;
import com.api.global.common.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 회원가입(Accession) 컨트롤러
 *
 * 회원가입 3단계 플로우를 처리합니다.
 *
 * [플로우]
 *   1단계: POST /api/accession/sendEmailAuth  → 이메일로 6자리 인증번호 발송
 *   2단계: POST /api/accession/verifyNum      → 인증번호 일치 여부 확인
 *   3단계: POST /api/accession/join           → 실제 회원 정보 저장
 *
 * - 세 엔드포인트 모두 인증 없이 접근 가능 (SecurityConfig.permitAll 설정됨)
 * - @Valid 로 DTO 필드 유효성을 검사하며, 실패 시 400 Bad Request 반환
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accession")
@Validated
public class AccessionController {

    private final AccessionService accessionService;

    /**
     * [1단계] 회원가입 이메일 인증번호 발송
     *
     * - 6자리 난수 인증번호를 생성해 Redis에 3분간 저장합니다.
     * - 입력한 이메일 주소로 인증번호를 발송합니다.
     * - 3분 내에 2단계(verifyNum)를 완료해야 합니다.
     *
     * @param request 이메일 주소 (NotBlank, Email 형식 검증)
     * @return 성공 시 ApiResponse(success=true)
     */
    @PostMapping("/sendEmailAuth")
    public ResponseEntity<ApiResponse<Void>> sendEmailAuth(@RequestBody @Valid EmailAuthRequest request) {
        accessionService.sendEmailAuth(request.getEmail());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * [2단계] 이메일 인증번호 확인
     *
     * - 1단계에서 발송한 인증번호와 입력값을 비교합니다.
     * - 일치하면 Redis에 인증 완료 상태(SIGNUP_VERIFIED)를 5분간 저장합니다.
     * - 5분 내에 3단계(join)를 완료해야 합니다.
     *
     * @param request 이메일 주소 + 6자리 인증번호
     * @return 성공 시 ApiResponse(success=true)
     */
    @PostMapping("/verifyNum")
    public ResponseEntity<ApiResponse<Void>> verifyNum(@RequestBody @Valid VerifyNumRequest request) {
        accessionService.verifyNum(request.getEmail(), request.getCertNum());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * [3단계] 회원가입
     *
     * - 이메일 인증 완료 여부(2단계 통과)를 Redis에서 확인합니다.
     * - 아이디 중복 여부를 검사합니다.
     * - 비밀번호를 BCrypt로 해싱 후 Member 테이블에 저장합니다.
     * - USER_AUTH 테이블에 기본 권한('USER')을 부여합니다.
     * - 가입 시점의 IP를 last_login_address에 기록합니다.
     * - 성공 시 200 OK + redirectUrl 반환 (프론트에서 메인페이지로 이동 처리)
     *
     * @param request     회원가입 정보 (아이디, 이름, 비밀번호, 이메일 등)
     * @param httpRequest 클라이언트 IP 추출용
     * @return 200 OK, data = redirectUrl "/"
     */
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> join(@RequestBody @Valid AccessionRequest request,
                                                    HttpServletRequest httpRequest) {
        String clientIp = resolveClientIp(httpRequest);
        accessionService.join(request, clientIp);
        return ResponseEntity.ok(ApiResponse.ok("/"));
    }

    /** X-Forwarded-For 헤더를 우선 확인해 실제 클라이언트 IP를 반환합니다. IPv6 루프백은 127.0.0.1로 정규화합니다. */
    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        String ip = (forwarded != null && !forwarded.isBlank())
                ? forwarded.split(",")[0].trim()
                : request.getRemoteAddr();
        return "0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip) ? "127.0.0.1" : ip;
    }
}
