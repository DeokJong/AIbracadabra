package com.ssafy.model.service;

import java.util.List;

import com.ssafy.model.dto.domain.Schedule;

public interface ScheduleService {
	
    int add(Schedule schedule);
    
    Schedule get(int sno);
    
    List<Schedule> getByPlan(int pno);
    
    int set(Schedule schedule);
    
    int remove(int sno);
}
