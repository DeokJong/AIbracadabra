package com.ssafy.model.service;

import java.util.List;

import com.ssafy.model.dto.domain.Pair;

public interface UtilService {

	public String getSidoName(int sidoCode);

	public String getGugunName(int gunguCode);

	public String getContentTypeName(int contentTypeId);

	public List<Pair> getSidos();

	public List<Pair> getGuguns(int sidoCode);

	public List<Pair> getContentTypes();
}
