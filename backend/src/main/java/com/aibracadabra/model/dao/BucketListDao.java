package com.aibracadabra.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aibracadabra.model.dto.domain.BucketList;

import java.util.List;

@Mapper
public interface BucketListDao {

	int add(BucketList bucketList);

    BucketList get(@Param("mno") int mno, @Param("ano") int ano);

    List<BucketList> getByMember(@Param("mno") int mno);

    int remove(@Param("mno") int mno, @Param("ano") int ano);

	int remove(int mno);
}
