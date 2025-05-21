package com.ssafy.model.service.impl;

import java.util.Collections;
import java.util.List;

import com.ssafy.model.dto.request.UpdateMemberRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.exception.DuplicatedException;
import com.ssafy.exception.MismatchException;
import com.ssafy.exception.RecordInsertException;
import com.ssafy.exception.RecordNotFoundException;
import com.ssafy.model.dao.MemberDao;
import com.ssafy.model.dto.domain.Member;
import com.ssafy.model.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member login(String email, String password) {
		Member member = memberDao.getByEmail(email);

		if (member == null) {
			throw new RecordNotFoundException("존재하지 않는 이메일 입니다.");
		} else if (passwordEncoder.matches(password, member.getPassword())) {
			member.setPassword("");
			return member;
		}

		return null;
	}

	@Override
	public int signUp(Member member, String passwordCheck) {
		Member m = memberDao.getByEmail(member.getEmail());
		if (m != null) {
			throw new DuplicatedException("이미 존재하는 계정입니다.");
		}

		if (!member.getPassword().equals(passwordCheck)) {
			throw new MismatchException("입력한 비밀번호와 비밀번호 확인이 맞지 않습니다.");
		}
		log.debug("encoding password: {}", passwordEncoder.encode(passwordCheck));
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		memberDao.add(member);
		return member.getMno();
	}

	@Override
	public int signUp(Member member) {
		Member m = memberDao.getByEmail(member.getEmail());
		if (m != null) {
			throw new DuplicatedException("이미 존재하는 계정입니다.");
		}
		log.debug("encoding password: {}", passwordEncoder.encode(member.getPassword()));
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		memberDao.add(member);
		return member.getMno();
	}

	@Override
	public int add(Member member) {
		if (memberDao.getByEmail(member.getEmail()) != null) {
			throw new DuplicatedException("이미 존재하는 계정");
		}
		int result = memberDao.add(member);

		if (result == 0) {
			throw new RecordInsertException("계정 생성 중 에러가 발생하여 삽입 불가");
		}

		return result;
	}

	@Override
	public Member get(int mno) {
		Member member = memberDao.get(mno);
		if(member == null) {
			throw new RecordNotFoundException("해당 멤버를 찾을 수가 없습니다.");
		}
		return member;
	}

	@Override
	public Member getByEmail(String email) {
		Member member = memberDao.getByEmail(email);
		if(member == null) {
			throw new RecordNotFoundException("해당 멤버를 찾을 수가 없습니다.");
		}
		return member;
	}

	@Override
	public int set(UpdateMemberRequest updateMemberRequest) {
		Member member = memberDao.getByEmail(updateMemberRequest.getEmail());
		if (member == null) {
			throw new RecordNotFoundException("해당 멤버의 데이터를 업데이트 하기 위한 email이 존재하지 않습니다.");
		}
		member.setName(updateMemberRequest.getName());

		if (!passwordEncoder.matches(updateMemberRequest.getCurrentPassword(), member.getPassword())) {
			throw new MismatchException("입력 하신 비밀번호가 현재 비밀번호와 다릅니다.");
		}

		if(updateMemberRequest.getNewPassword() != null && !updateMemberRequest.getNewPassword().equals(updateMemberRequest.getConfirmPassword())) {
			throw new MismatchException("새 비밀번호와 확인 비밀번호가 다릅니다.");
		}

		if(updateMemberRequest.getNewPassword() != null && !updateMemberRequest.getNewPassword().isBlank()) {
			member.setPassword(passwordEncoder.encode(updateMemberRequest.getNewPassword()));
		}

		return memberDao.set(member);
	}

	@Override
	public int remove(int mno) {

		if (memberDao.get(mno) == null) {
			throw new RecordNotFoundException("해당 멤버의 데이터를 업데이트 하기 위한 email이 존재하지 않습니다.");
		}

		return memberDao.remove(mno);
	}

	@Override
	public List<GrantedAuthority> getGrantedAuthorityByMno(Integer mno) {
		Member member  = memberDao.get(mno);

		if(member == null) {
			throw new RecordNotFoundException("해당 멤버를 찾을 수가 없습니다.");
		}
		
		if (member.getRole() == null) {
			return Collections.emptyList();
		}

		
		return List.of(new SimpleGrantedAuthority(member.getRole()));
	}

}