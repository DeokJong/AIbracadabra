package com.ssafy.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HotPlaceLikeDao {

	int insert(@Param("mno") int mno, @Param("hno") int hno);

	int delete(@Param("mno") int mno, @Param("hno") int hno);

	boolean exists(@Param("mno") int mno, @Param("hno") int hno);

	List<Integer> findLikedHotPlaceIdsByMno(@Param("mno") int mno);
}
