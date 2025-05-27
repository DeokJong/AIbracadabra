package com.ssafy.model.service.impl;

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
	public void insert(Plan plan) {
		if (planDao.insert(plan) == 0 ) {
			throw new RuntimeException("예상치 못한 흐름: 여행 계획 생성 실패");
		}
	}

	@Override
	public List<Plan> findByMno(int mno) {
		return planDao.findByMno(mno);
	}

	@Override
	public void update(Plan plan) {
		if (planDao.update(plan) == 0 ) {
			throw new RuntimeException("예상치 못한 흐름: 여행 계획 업데이트 실패");
		}
	}

	@Override
	public Plan getByPno(Integer pno) {
		return planDao.findByPno(pno);
	}

	@Override
	public void delete(Integer pno) {
		if (planDao.delete(pno) == 0 ) {
			throw new RuntimeException("예상치 못한 흐름: 여행 계획 제거 실패");
		}
	}
}
