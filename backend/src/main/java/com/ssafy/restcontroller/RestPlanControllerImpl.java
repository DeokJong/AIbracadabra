package com.ssafy.restcontroller;

import com.ssafy.model.dao.PlanDao;
import com.ssafy.model.dto.domain.Plan;
import com.ssafy.model.service.PlanService;
import com.ssafy.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestPlanControllerImpl implements ResponseEntityHelper, RestPlanController {

	private final PlanService planService;

	@Override
	public ResponseEntity<?> createPlan(CustomUserDetails userDetails, Plan plan) {
		plan.setMno(userDetails.getMember().getMno());
		planService.insert(plan);
		return handleResponse("CREATE PLAN", HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> updatePlan(Integer pno, Plan plan) {
		plan.setPno(pno);
		planService.update(plan);
		return handleResponse("UPDATE PLAN", HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<?> deletePlan(Integer pno) {
		planService.delete(pno);
		return handleResponse("DELETE PLAN", HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<?> getPlansByMember(CustomUserDetails userDetails) {
		return handleResponse(planService.findByMno(userDetails.getMember().getMno()), "OK", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getPlanByPno(Integer pno) {
		return handleResponse(planService.getByPno(pno),"OK",HttpStatus.OK);
	}
}
