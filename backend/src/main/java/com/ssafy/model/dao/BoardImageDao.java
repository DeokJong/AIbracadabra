package com.ssafy.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.model.dto.domain.BoardImage;

@Mapper
public interface BoardImageDao {

    void insertImage(BoardImage image);

    List<Integer> selectImageIdsByBno(int bno);

    BoardImage selectImageMetaById(long imgNo);
}
