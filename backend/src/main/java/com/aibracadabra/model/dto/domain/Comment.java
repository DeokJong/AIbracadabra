package com.aibracadabra.model.dto.domain;

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
public class Comment {
  
  /** 댓글 고유번호: AUTO_INCREMENT */
  @JsonProperty(access = Access.READ_ONLY)
  private int cno;

  /** 게시글 번호 */
  @JsonProperty(access = Access.READ_ONLY)
  private int bno;

  /** 댓글 작성자 회원번호(내부 로직용) */
  @JsonIgnore
  private int mno;

  /** 댓글 작성자 이름: READ_ONLY */
  @JsonProperty(access = Access.READ_ONLY)
  private String author;

  /** 댓글 내용 */
  private String content;

  /** 댓글 작성일시: DEFAULT CURRENT_TIMESTAMP */
  @JsonProperty(access = Access.READ_ONLY)
  private String createdDate;

  /** 수정 완료일시: ON UPDATE CURRENT_TIMESTAMP */
  @JsonProperty(access = Access.READ_ONLY)
  private String updatedDate;
}
