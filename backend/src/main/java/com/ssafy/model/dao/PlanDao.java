package com.ssafy.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.model.dto.domain.Plan;

import java.util.List;

@Mapper
public interface PlanDao {

	int add(Plan plan);

	Plan get(int pno);

	List<Plan> getByMember(int mno);

	int set(Plan plan);

	int remove(int pno);

	int removeAll(int mno);
}