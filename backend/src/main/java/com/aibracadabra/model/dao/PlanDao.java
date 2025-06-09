package com.aibracadabra.model.dao;

import com.aibracadabra.model.dto.domain.Plan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanDao {
	int insert(Plan plan);

	int update(Plan plan);

	int delete(int pno);

	List<Plan> findByMno(int mno);

	Plan findByPno(int pno);
}
