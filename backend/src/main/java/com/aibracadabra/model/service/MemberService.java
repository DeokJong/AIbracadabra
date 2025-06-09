package com.aibracadabra.model.service;

import com.aibracadabra.model.dto.domain.Member;
import com.aibracadabra.model.dto.request.UpdateMemberRequest;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface MemberService {

    Member login(String email, String password);

    int add(Member member);

    Member get(int mno);

    Member getByEmail(String email);

    int set(UpdateMemberRequest updateMemberRequest);

    int remove(int mno);

    @Deprecated
    int signUp(Member member);

    int signUp(Member member, String passwordCheck);

    List<GrantedAuthority> getGrantedAuthorityByMno(Integer mno);

}
