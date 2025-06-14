package com.aibracadabra.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.aibracadabra.model.dto.domain.Member;

@Mapper
public interface MemberDao {

	int add(Member member);

	Member get(int mno);

	int set(Member member);

	int remove(int mno);

	Member getByEmail(String email);

}
