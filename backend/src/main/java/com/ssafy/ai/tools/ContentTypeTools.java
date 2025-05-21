package com.ssafy.ai.tools;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.ssafy.model.dto.domain.Pair;
import com.ssafy.model.service.UtilService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContentTypeTools {
	private final UtilService utilService;

	@Tool(description = "여행지 카테고리(코드,이름) 리스트를 반환합니다.")
	public List<Pair> getContentTypes() {
		return utilService.getContentTypes();
	}

}
