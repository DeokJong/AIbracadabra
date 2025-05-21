package com.ssafy.model.service.impl;

import com.ssafy.exception.RecordNotFoundException;
import com.ssafy.model.dao.PlanDao;
import com.ssafy.model.dto.domain.Plan;
import com.ssafy.model.service.PlanService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
	private final PlanDao planDao;

	@Override
	public int add(Plan plan) {
		return planDao.add(plan);
	}

	@Override
	public Plan get(int pno) {
		Plan plan = planDao.get(pno);

		if (plan == null) {
			throw new RecordNotFoundException("존재하지 않는 pno로 조회");
		}

		return plan;
	}

	@Override
	public List<Plan> getByMember(int mno) {
		return planDao.getByMember(mno);
	}

	@Override
	public int set(Plan plan) {
		return planDao.set(plan);
	}

	@Override
	public int remove(int pno) {
		return planDao.remove(pno);
	}
}
