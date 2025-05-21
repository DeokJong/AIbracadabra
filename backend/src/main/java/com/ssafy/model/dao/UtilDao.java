package com.ssafy.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.model.dto.domain.Pair;

import java.util.List;

//TODO 3. 짬통 정리
//TODO 3-1. sidos table
//TODO 3-2. gundo table
//TODO 3-3. contenttype
//TODO 3-4. (optional) contenttype table 이름 content_type 으로 변경
@Mapper
public interface UtilDao {

	public String getSidoName(int sidoCode);

	public String getGugunName(int gunguCode);

	public String getContentTypeName(int contentTypeId);

	public List<Pair> getSidos() ;

	public List<Pair> getGuguns(int sidoCode);

	public List<Pair> getContentTypes();
}
