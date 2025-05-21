package com.ssafy.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.model.dto.domain.Schedule;

import java.util.List;

@Mapper
public interface ScheduleDao {

	int add(Schedule schedule);

	Schedule get(int sno);

	List<Schedule> getByPlan(int pno);

	int set(Schedule schedule);

	int remove(int sno);
}
