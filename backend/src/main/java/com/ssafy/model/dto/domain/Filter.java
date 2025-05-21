package com.ssafy.model.dto.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filter {
	int sidoCode;
	int gunguCode;
	int contentTypeId;

}
