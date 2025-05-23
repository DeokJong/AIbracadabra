package com.ssafy.restcontroller;

import com.github.pagehelper.PageInfo;
import com.ssafy.model.dto.domain.Board;
import com.ssafy.model.dto.domain.Comment;
import com.ssafy.model.dto.domain.Member;
import com.ssafy.model.service.BoardService;
import com.ssafy.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class RestBoardControllerImpl implements ResponseEntityHelper, RestBoardController {

	private final BoardService bService;

	@Override
	public ResponseEntity<?> boardList(Integer currentPage) {
		PageInfo<Board> board = bService.getAllBoards(currentPage);
		return handleResponse(board, "OK", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> boardList(Integer currentPage, String boardType) {
		PageInfo<Board> pageInfo;
        if (boardType != null && !boardType.isEmpty()) {
            // boardType으로 필터링
            pageInfo = bService.getByBoardType(boardType, currentPage);
        } else {
            // 전체 게시글
            pageInfo = bService.getAllBoards(currentPage);
        }
        return new ResponseEntity<>(pageInfo, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> detail(Integer bno) {
		Board board = bService.getBoardDetail(bno);
		return handleResponse(board, "OK", HttpStatus.OK);
	}
	
    @Override
    public ResponseEntity<List<Board>> boardViews(String boardType) {
        List<Board> list = bService.getBoardViews(boardType);
        return ResponseEntity.ok(list);
    }

	@Override
	public ResponseEntity<?> register(Board board, CustomUserDetails userDetails) {
		Member member = userDetails.getMember();
		board.setMno(member.getMno());
		board.setAuthor(member.getName());
		bService.add(board);
		return handleResponse("CREATED", HttpStatus.CREATED);
	}


	@Override
	public ResponseEntity<?> update(Integer bno, Board board) {
		board.setBno(bno);
		bService.set(board);
		return handleResponse("OK", HttpStatus.NO_CONTENT);
	}


	@Override
	public ResponseEntity<?> delete(int bno) {
		bService.remove(bno);
		return handleResponse("OK", HttpStatus.NO_CONTENT);
	}


	@Override
	public ResponseEntity<?> find(String text) {
		List<Board> list = bService.boardFindWord(text);
		return handleResponse(list, "OK", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addComment(Integer bno, Comment comment, CustomUserDetails userDetails) {
		Member member = userDetails.getMember();
		// PathVariable로 받은 bno 세팅
		comment.setBno(bno);
		// 인증된 사용자 정보 세팅
		comment.setMno(member.getMno());
		comment.setAuthor(member.getName());
		// 서비스 호출
		bService.addComment(comment);  // 댓글 등록 메서드 호출
		// 응답 처리
		return handleResponse("CREATED", HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> updateComment(Integer bno, Integer cno, Comment comment, CustomUserDetails userDetails) {
		Member member = userDetails.getMember();
		Comment existing = bService.getCommentCno(cno);
		if (existing.getMno() != member.getMno()) {
			return handleResponse("FORBIDDEN", HttpStatus.FORBIDDEN);
		}

		// PathVariable 세팅
		comment.setBno(bno);
		comment.setCno(cno);
		// 권한 확인을 위해 MNO/Author도 세팅
		comment.setMno(member.getMno());
		comment.setAuthor(member.getName());
		// 서비스 호출
		bService.setComment(comment);
		// NO_CONTENT로 응답
		return handleResponse("OK", HttpStatus.NO_CONTENT);
	}

	@Override
    public ResponseEntity<?> deleteComment(Integer bno, Integer cno, CustomUserDetails userDetails) {
		Member member = userDetails.getMember();
		Comment existing = bService.getCommentCno(cno);
		if (existing.getMno() != member.getMno()) {
			return handleResponse("FORBIDDEN", HttpStatus.FORBIDDEN);
		}
        bService.removeComment(cno);
        return handleResponse("OK", HttpStatus.NO_CONTENT);
    }



}
