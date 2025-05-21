package com.ssafy.model.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ssafy.exception.RecordInsertException;
import com.ssafy.exception.RecordNotFoundException;
import com.ssafy.model.dao.BucketListDao;
import com.ssafy.model.dto.domain.BucketList;
import com.ssafy.model.service.impl.BucketListServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootTest
class TestBucketListService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private BucketListServiceImpl bucketListService;

    private BucketList sample;
    private int testMno;

    
    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM bucketlists");
        jdbcTemplate.update("DELETE FROM boards");
        jdbcTemplate.update("DELETE FROM attractions");
        // 2) 부모 테이블 삭제
        jdbcTemplate.update("DELETE FROM members");

        jdbcTemplate.update("""
                INSERT INTO members(name, email, password, role)
                VALUES (?, ?, ?, ?)
                """,
                "testUser", "test@ssafy.com", "password", "admin"
        );
        testMno = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        jdbcTemplate.update("""
                INSERT INTO attractions(
                  no, content_id, title, content_type_id,
                  area_code, si_gun_gu_code,
                  first_image1, first_image2,
                  map_level, latitude, longitude, tel,
                  addr1, addr2, homepage, overview
                ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """,
                100, 1000, "테스트관광지", 12,
                1, 1,
                "", "", 1, 0, 0, 0,
                "서울특별시", "테스트구", "http://test", "테스트 개요"
        );

        jdbcTemplate.update("""
                INSERT INTO bucketlists(mno, ano)
                VALUES (?, ?)
                """,
                testMno, 100
        );

        sample = BucketList.builder()
                           .mno(testMno)
                           .ano(100)
                           .build();
    }


    // 성공
    @Test
    @DisplayName("add(): buketlist에 추가")
    void add_ReturnsBucketList() {
    	

        BucketList result = bucketListService.add(sample);
        assertNotNull(result.getAno());
        assertEquals(sample, result);
    }


    // 성공함
    @Test
    @DisplayName("get(): 존재하는 BucketList를 반환함")
    void get_ReturnsBucketList() {

        BucketList result = bucketListService.get(testMno, 100);

        assertEquals(sample.getAno(), result.getAno());
    }

    // 성공함
    @Test
    @DisplayName("get(): DAO가 null 반환 시 RecordNotFoundException 발생")
    void get_WhenNotFound_ThrowsException() {

        assertThrows(RecordNotFoundException.class,
            () -> bucketListService.get(1, 100));
    }
    // 성공함
    @Test
    @DisplayName("getByMember(): 멤버의 전체 BucketList를 반환함")
    void getByMember_ReturnsList() {
        List<BucketList> list = List.of(sample);

        List<BucketList> result = bucketListService.getByMember(testMno);

        assertEquals(1, result.size());
        assertEquals(sample, result.get(0));
    }

    // 성공함
    @Test
    @DisplayName("remove(): 정상 삭제 시 삭제 건수를 반환함")
    void remove_ReturnsDeletedCount() {

        int result = bucketListService.remove(testMno, 100);

        assertEquals(1, result);
    }

    // 성공함
    @Test
    @DisplayName("remove(): 삭제 대상이 없으면 RecordNotFoundException 발생함")
    void remove_WhenNotFound_ThrowsException() {

        assertThrows(RecordNotFoundException.class,
            () -> bucketListService.remove(1, 100));
    }


}
