package com.api.domain.auth.service;

import org.springframework.stereotype.Service;

import com.api.domain.base.Member.entity.MemberEntity;
import com.api.domain.base.Member.repository.MemberRepository;
import com.api.global.config.KaKaoProperties;
import com.api.global.constants.MessageConstants;
import com.api.global.exception.BusinessException;
import com.api.global.util.HashUtil;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KaKaoServiceImpl implements KaKaoService{
    
    private final KaKaoProperties kakaoProperties;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final HashUtil hashUtil;

    @Override
    public MemberEntity login(String code) {
        
        // 엑세스 토큰 불러오기
        String accessToken = getKakaoAccessToken(code);
        
        // 카카오아이디값 불러오기
        String kakaoId = getKakaoUserInfo(accessToken);
        
        String kakaoIdEnc = hashUtil.hash(kakaoId);
        
        //db 내 카카오 아이디 조회 ( 없을 시 에러 반환 )
        MemberEntity member = memberRepository.findByKakaoId(kakaoIdEnc)
                .orElseThrow(() -> new BusinessException( MessageConstants.MEMBER_NOT_FOUND,
                                                          Map.of("kakaoId", kakaoId)
                                                         ));
        
        return member;
    }
    
    private String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.clientId());
        params.add("redirect_uri", kakaoProperties.redirectUri());
        params.add("code", code);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
            kakaoProperties.tokenUrl(), request, Map.class
        );
        
        return (String) response.getBody().get("access_token");
    }
    
    private String getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            kakaoProperties.userInfoUrl(),
            HttpMethod.GET,
            request,
            Map.class
        );
        
        return String.valueOf(response.getBody().get("id"));
    }
}
