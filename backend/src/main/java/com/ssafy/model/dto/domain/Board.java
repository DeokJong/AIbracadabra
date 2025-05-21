package com.ssafy.model.dto.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
  
  /** 게시판 고유번호: AUTO_INCREMENT */
  @JsonProperty(access = Access.READ_ONLY)
  private int bno;
  
  /** 작성자 회원번호: 내부 로직에서만 사용 */
  @JsonIgnore
  private int mno;

  /** 게시판 타입 (board, notice, qna) */
  private String boardType;

  /** 작성자 이름: READ_ONLY */
  @JsonProperty(access = Access.READ_ONLY)
  private String author;

  /** 조회수: READ_ONLY (default 0) */
  @JsonProperty(access = Access.READ_ONLY)
  private int views;

  /** 질문 제목 */
  private String title;

  /** 질문 내용 */
  private String content;

  /** 작성일시: DEFAULT CURRENT_TIMESTAMP */
  @JsonProperty(access = Access.READ_ONLY)
  private String createdDate;

  /** 수정 완료일시 */
  @JsonProperty(access = Access.READ_ONLY)
  private String updatedDate;

  /** 공개 여부 (PUBLIC, PRIVATE) */
  private String visibility;
  
  @JsonProperty(access = Access.READ_ONLY)
  private List<Comment> comments;

  

}

