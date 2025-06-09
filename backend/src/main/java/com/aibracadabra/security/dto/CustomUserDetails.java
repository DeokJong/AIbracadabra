package com.aibracadabra.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aibracadabra.model.dto.domain.Member;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@Data
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	private @NonNull Member member;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> roles = new ArrayList<>();
		if (member.getRole() != null) {
			roles.add(new SimpleGrantedAuthority("ROLE_" + member.getRole()));
		}
		return roles;
	}

	
	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getEmail();
	}

}
