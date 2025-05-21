/*
package com.ssafy.model.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.exception.RecordNotFoundException;
import com.ssafy.model.dao.BoardDao;
import com.ssafy.model.dto.domain.Board;
import com.ssafy.model.service.BoardService;

@SpringBootTest
@Transactional
public class TestBoardService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BoardService boardService;

	private int testMno; // 삽입된 회원의 PK
	private Board baseBoard;
	private Board savedBoard;

	@BeforeEach
	void setUp() {
		// 1) boards가 mno를 외래키로 쓰기 때문에 같이 삭제하고 삽입해주기
	    jdbcTemplate.update("DELETE FROM bucketlists");
		jdbcTemplate.update("DELETE FROM boards");
		jdbcTemplate.update("DELETE FROM members");

		// 2) members먼저 객체하나 만들어서 추가해주기
		jdbcTemplate.update("""
				INSERT INTO members(name, email, password, role)
				VALUES (?, ?, ?, ?)
				""", "testUser", // name
				"test@ssafy.com", // email
				"password", // password
				"USER" // role (NOT NULL)
		);

		// 3) 추가한 값에서 mno가져오기
		testMno = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

		// 4) Board 객체 준비 (mno 설정)
		baseBoard = Board.builder().title("테스트 제목").content("테스트 내용").like(0).mno(testMno)
				.created_date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).build();

		// 5) 저장 후 실제 bno가 세팅된 객체 조회
		boardService.add(baseBoard);
		savedBoard = boardService.getBytitle(baseBoard.getTitle()).get(0);
	}

	@Test
	@DisplayName("add(): 게시글 추가 테스트")
	void add_test() {
		// 새로운 객체 만들기
		Board newBoard = Board.builder().title("새 글 제목").content("새 글 내용").like(0).mno(testMno)
				.created_date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).build();

		// add 함수 호출
		int result = boardService.add(newBoard);

		// 반환값 검증하기
		// 반환값이 양수이면 성공
		assertEquals(result, 1);

	}

	@Test
	@DisplayName("getBytitle(): 게시글 제목을 통해 게시물을 반환")
	void getBytitleTest() {
		List<Board> boardList = boardService.getBytitle("테스트 제목");
		assertEquals(boardList.size(), 1);
	
	}
	@Test
	@DisplayName("getAllBoard(): 게시글 전체를 반환함")
	void getAllBoardsTest() {
		List<Board> boardList = boardService.getAllBoards();
		assertEquals(boardList.size(), 1);
		
	
	}

	@Test
	@DisplayName("set(): 수정한 게시물을 반환함")
	void setTest() {
		baseBoard.setContent("변경된 내용");
		baseBoard.setLike(1);
		baseBoard.setTitle("변경된 제목");
		Board setBoard = boardService.set(baseBoard);
		assertEquals(setBoard.getTitle(), baseBoard.getTitle());
	
	
	}

	@Test
	@DisplayName("remove(): 게시글을 제거함")
	void remove() {

		int bno = boardService.getBytitle("테스트 제목").get(0).getBno();

		int result = boardService.remove(bno);

		assertEquals(result, 1);
		// 에러를 던지는 것도 체크하기
		assertThrows(RecordNotFoundException.class, () -> boardService.remove(bno));

	}
	@Test
	@DisplayName("getBoardBno(): 게시글번호 조회해서 게시물을 반환")
	void getBoardBno() {
		Board result = boardService.getBoardBno(savedBoard.getBno());
		assertEquals(result.getBno(), savedBoard.getBno());
	}

	@Test
	@DisplayName("boardFindWord(): 게시글 키워드 검색 cost 내림차순 정렬 테스트")
	void boardFindWordTest() {
	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    LocalDateTime now = LocalDateTime.now();

	    String olderDate = now.minusSeconds(2).format(fmt);
	    Board lowBoard = Board.builder()
	        .title("검색테스트")
	        .content("")                
	        .like(0)
	        .mno(testMno)
	        .created_date(olderDate)
	        .build();
	    boardService.add(lowBoard);
	    Board savedLow = boardService.getBytitle("검색테스트").get(0);

	    String newerDate = now.format(fmt);
	    Board highBoard = Board.builder()
	        .title("검색테스트 키워드")
	        .content("검색테스트")       
	        .like(0)
	        .mno(testMno)
	        .created_date(newerDate)
	        .build();
	    boardService.add(highBoard);
	    Board savedHigh = boardService.getBytitle("검색테스트 키워드").get(0);

	    List<Board> results = boardService.boardFindWord("검색테스트 키워드");

	    assertEquals(2, results.size(), "매칭된 게시글은 2건이어야 합니다");
	    assertEquals(savedHigh.getBno(), results.get(0).getBno(),
	        "cost=2인 게시글이 첫 번째여야 합니다");
	    assertEquals(savedLow.getBno(),  results.get(1).getBno(),
	        "cost=1인 게시글이 두 번째여야 합니다");

	    assertEquals(boardService.boardFindWord("없는키워드").size(),0);
	}


}
*/
