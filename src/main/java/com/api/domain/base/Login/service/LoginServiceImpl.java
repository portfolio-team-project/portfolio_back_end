package com.api.domain.base.Login.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.base.Login.entity.LoginEntity;
import com.api.domain.base.Login.repository.LoginRepository;
import com.api.domain.base.Member.entity.MemberEntity;
import com.api.global.util.HttpUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService{
    
    private final LoginRepository loginRepository;
    
    @Override
    public void saveLog(MemberEntity member, HttpServletRequest request, String loginYn, String failReason) {
        
        String clientIp = HttpUtil.getClientIp(request);
        
        LoginEntity loginLog = LoginEntity.builder()
                                       .member(member)
                                       .userIp(clientIp)
                                       .requestUrl(request.getRequestURI())
                                       .userAgent(request.getHeader("User-Agent"))
                                       .loginYn(loginYn)
                                       .failReason(failReason)
                                       .build();
        
        loginRepository.save(loginLog);
    }
    
}
