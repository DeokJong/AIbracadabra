package com.ssafy.model.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
	@Schema(description = "여행 일정에 대한 여행",
		example = "좌충우돌 파워 P들의 즉흥 여행 계획"
	)
	private String title;

	@Schema(description = "여행 계획 식별 ID")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer pno;

	@Schema(description = "이 여행 계획을 세운 멤버의 ID")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer mno;

	@Schema(description = "여행 일정들. 순서는 idx 기준")
	private List<Document.Item> schedules;
}
