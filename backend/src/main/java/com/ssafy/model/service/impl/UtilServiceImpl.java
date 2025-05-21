package com.ssafy.model.service.impl;

import com.ssafy.model.dao.UtilDao;
import com.ssafy.model.dto.domain.Pair;
import com.ssafy.model.service.UtilService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UtilServiceImpl implements UtilService {

	private final UtilDao utilDao;

	@Override
	public String getSidoName(int sidoCode) {
		return null;
	}

	@Override
	public String getGugunName(int gunguCode) {
		return null;
	}

	@Override
	public String getContentTypeName(int contentTypeId) {
		return null;
	}

	@Override
	public List<Pair> getSidos() {
		return utilDao.getSidos();
	}

	@Override
	public List<Pair> getGuguns(int sidoCode) {
		return utilDao.getGuguns(sidoCode);
	}

	@Override
	public List<Pair> getContentTypes() {
		return utilDao.getContentTypes();
	}

}
