package com.aibracadabra.model.service;

import java.util.List;

import com.aibracadabra.model.dto.domain.BucketList;

public interface BucketListService {
	
    BucketList add(BucketList bucketList);
    
    BucketList get(int mno, int ano);
    
    List<BucketList> getByMember(int mno);
    
    int remove(int mno, int ano);
}
