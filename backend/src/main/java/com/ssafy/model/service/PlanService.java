package com.ssafy.model.service;

import java.util.List;

import com.ssafy.model.dto.domain.Plan;

public interface PlanService {
	
    int add(Plan plan);
    
    Plan get(int pno);
    
    List<Plan> getByMember(int mno);
    
    int set(Plan plan);
    
    int remove(int pno);
}
