package com.aibracadabra.model.service.impl;

import com.aibracadabra.exception.RecordInsertException;
import com.aibracadabra.exception.RecordNotFoundException;
import com.aibracadabra.model.dao.BucketListDao;
import com.aibracadabra.model.dto.domain.BucketList;
import com.aibracadabra.model.service.BucketListService;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BucketListServiceImpl implements BucketListService {
	private final BucketListDao bucketListDao;

	@Override
	public BucketList add(BucketList bucketList) {
		int result = bucketListDao.add(bucketList);

		if (result == 0) {
			throw new RecordInsertException("버킷 리스트 추가 중 에러");
		}

		return bucketList;
	}

	@Override
	public BucketList get(int mno, int ano) {
		BucketList bucketList = bucketListDao.get(mno, ano);

		if (bucketList == null) {
			throw new RecordNotFoundException("존재 하지 않는 버킷리스트 참고");
		}

		return bucketList;
	}

	@Override
	public List<BucketList> getByMember(int mno) {
		return bucketListDao.getByMember(mno);
	}

	@Override
	public int remove(int mno, int ano) {
		if (bucketListDao.get(mno, ano) == null) {
			throw new RecordNotFoundException("존재 하지 않는 버킷 리스트 삭제");
		}

		int result = bucketListDao.remove(mno, ano);
		
		if(result == 0) {
			throw new DataAccessResourceFailureException("데이터가 존재하나, 삭제 실패");
		}

		return result;
	}
}
