package com.api.domain.base.Login.service;

import com.api.domain.base.Login.dto.KakaoLoginRequest;
import com.api.domain.base.Member.entity.MemberEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {
    void saveLog(MemberEntity member, HttpServletRequest request, String loginYn, String failReason);
    MemberEntity kakaoJoin(KakaoLoginRequest request,HttpServletRequest httpRequest);
}
