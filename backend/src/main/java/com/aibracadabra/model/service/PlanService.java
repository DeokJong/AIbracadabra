package com.aibracadabra.model.service;

import com.aibracadabra.model.dto.domain.Plan;

import java.util.List;

public interface PlanService {


	void insert(Plan plan);

	List<Plan> findByMno(int mno);

	void update(Plan plan);

	Plan getByPno(Integer pno);

	void delete(Integer pno);
}
