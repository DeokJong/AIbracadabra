package com.ssafy.model.service;

import java.util.List;

import com.ssafy.model.dto.domain.Attraction;
import com.ssafy.model.dto.domain.Filter;
import com.ssafy.model.dto.domain.Route;

public interface AttractionService {

	public List<Attraction> getByFilter(Filter filter);

	public List<Route> getRoute(List<Route> route, int[][] adjMat, int startId, int endId);
}
