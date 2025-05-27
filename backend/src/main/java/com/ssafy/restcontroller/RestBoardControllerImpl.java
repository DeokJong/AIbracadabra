package com.ssafy.restcontroller;

import com.github.pagehelper.PageInfo;
import com.ssafy.model.dto.domain.Board;
import com.ssafy.model.dto.domain.Comment;
import com.ssafy.model.dto.domain.Member;
import com.ssafy.model.service.BoardService;
import com.ssafy.security.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;


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
    public ResponseEntity<?> register(Board board,
                                      CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        board.setMno(member.getMno());
        board.setAuthor(member.getName());

        // bService.add 를 Long 리턴하도록 바꿔주세요.
        int newBno = bService.add(board);

        // { status:"SUCCESS", data: { bno:123 } } 형태로 반환
        return handleSuccess(Map.of("bno", newBno), HttpStatus.CREATED);
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

	@Override
    public ResponseEntity<Map<String, Integer>> uploadImage(
            @PathVariable("bno") int bno,
            @RequestParam("file") MultipartFile file,
            CustomUserDetails user) {

        Member member = user.getMember();
        Board board = bService.getBoardBno(bno);
        if (board.getMno()!=member.getMno()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long imgNo = bService.storeImage(file, bno);
        // 인터페이스 리턴 타입에 맞춰 Map<String,Integer> 사용
        return ResponseEntity.ok(Map.of("imgNo", imgNo.intValue()));
    }
	@GetMapping("/images/{imgNo}")
	@Override
    public ResponseEntity<Resource> serveImage(
            @PathVariable Long imgNo,
            CustomUserDetails user) {
        try {
            // 1) 리소스(파일) 로드 & 권한 체크
            Resource img = bService.loadImageAsResource(imgNo);

            // 2) 파일명으로부터 MIME 타입 추측
            String filename = img.getFilename(); // UrlResource 의 경우 실제 저장된 UUID+확장자
            String contentType = URLConnection.guessContentTypeFromName(filename);
            if (contentType == null) {
                // 못찾으면 바이너리 스트림으로
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            // 3) 헤더 설정하고 리턴
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(img);

        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IOException ex) {
            return ResponseEntity.notFound().build();
        }
    }
	
    @Override
    public ResponseEntity<?> boardList(Integer currentPage,
    		CustomUserDetails user) {
    	Member member  = user.getMember();
    	
        PageInfo<Board> pageInfo = bService.getBoardMno(member.getMno(), currentPage);
        return new ResponseEntity<>(pageInfo, HttpStatus.OK);
    }

	@Override
	public ResponseEntity<?> commentList(Integer currentPage, CustomUserDetails userDetails) {
		Member member = userDetails.getMember();
		
		PageInfo<Comment> pageInfo = bService.getCommentAll(member.getMno(), currentPage);
        return new ResponseEntity<>(pageInfo, HttpStatus.OK);
	}


}
