package com.ssafy.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.model.dto.domain.Attraction;
import com.ssafy.model.dto.domain.Filter;

import java.util.List;

@Mapper
public interface AttractionDao {

	public List<Attraction> getByFilter(Filter filter);

}
