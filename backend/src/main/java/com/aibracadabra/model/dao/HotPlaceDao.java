package com.aibracadabra.model.dao;

import com.aibracadabra.model.dto.domain.HotPlace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * HotPlace DAO
 */
@Mapper
public interface HotPlaceDao {

  /**
   * 위도 경도 기준 주변 핫 플레이스 불러옴.
   * 이미지는 담아있지 않고, ino만 담아서 옴
   *
   * @param mapX  위도
   * @param mapY  경도
   * @param meter 주변 반경
   * @return 간략한 핫 플레이스 리스트
   */
  List<HotPlace> findByCoordinates(double mapX, double mapY, int meter);

  /**
   * hno 기준 핫 플레이스의 상세를 조회해옴.
   * 이미지가 제대로 담겨있음
   *
   * @param hno 핫 플레이스 id
   * @return 이미지의 경로가 담겨있는 핫 플레이스
   */
  HotPlace findById(int hno);

  /**
   * 핫 플레이스를 등록.
   * 넣을때 ino도 함께 지정해 줘야함
   *
   * @param hotPlace 새로 등록할 핫 플레이스 객체
   */
  void save(HotPlace hotPlace);

  /**
   * hno 기준 핫 플레이스를 업데이트 하는 함수
   * ino 수정시 함께 ino 등록을 해줘야함
   *
   * @param hotPlace 업데이트 할 핫 플레이스 객체
   */
  void update(HotPlace hotPlace);

  /**
   * hno 기준 핫 플레이스를 제거하는 함수
   * 같이 등록된 ino도 cascade로 제거됨.
   *
   * @param mno 핫 플레이스 작성자 id
   * @param hno 제거 할 핫 플레이스 id
   */
  void delete(int hno, Integer mno);

}
