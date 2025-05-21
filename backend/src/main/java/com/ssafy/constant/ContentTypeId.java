package com.ssafy.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ContentTypeId {
  TOURIST_SPOT("12", "관광지"),
  CULTURE_FACILITY("14", "문화시설"),
  FESTIVAL_EVENT("15", "축제공연행사"),
  TRAVEL_COURSE("25", "여행코스"),
  LEISURE_SPORTS("28", "레포츠"),
  ACCOMMODATION("32", "숙박"),
  SHOPPING("38", "쇼핑"),
  RESTAURANT("39", "음식점");


  private final String code;
  private final String description;

  ContentTypeId(String code, String description) {
    this.code = code;
    this.description = description;
  }


  /**
   * ID에 해당하는 Enum을 Optional로 반환
   */
  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  public static ContentTypeId from(String code) {
    for (ContentTypeId ct : values()) {
      if (ct.code.equals(code)) {
        return ct;
      }
    }
    throw new IllegalArgumentException("Invalid ContentTypeId: " + code);
  }
}