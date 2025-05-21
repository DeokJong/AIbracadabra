package com.ssafy.model.service.impl;

import com.ssafy.model.dao.ScheduleDao;
import com.ssafy.model.dto.domain.Schedule;
import com.ssafy.model.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
	private final ScheduleDao scheduleDao;

	@Override
	public int add(Schedule schedule) {
		return scheduleDao.add(schedule);
	}

	@Override
	public Schedule get(int sno) {
		return scheduleDao.get(sno);
	}

	@Override
	public List<Schedule> getByPlan(int pno) {
		return scheduleDao.getByPlan(pno);
	}

	@Override
	public int set(Schedule schedule) {
		return scheduleDao.set(schedule);
	}

	@Override
	public int remove(int sno) {
		return scheduleDao.remove(sno);
	}
}
