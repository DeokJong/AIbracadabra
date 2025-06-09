package com.aibracadabra.model.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class News {

	private Long id;              // AUTO_INCREMENT PK
    private String title;         // news.title
    private String publishAt;   // news.publishAt (DATETIME)
    private Integer sidoCode;     // news.sido_code
    private String summary;       // news.summary (빈 문자열 가능)
    private String url;           // news.url

}
