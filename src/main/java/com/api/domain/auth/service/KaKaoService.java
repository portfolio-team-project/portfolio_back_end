package com.api.domain.auth.service;

import com.api.domain.base.Member.entity.MemberEntity;

public interface KaKaoService {
    
    MemberEntity login(String code);
}
