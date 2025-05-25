package com.ssafy.restcontroller;

import com.ssafy.model.dto.domain.Board;
import com.ssafy.model.dto.domain.Comment;
import com.ssafy.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Board", description = "게시판 API")
public interface RestBoardController {
	@Operation(summary = "게시판 목록 조회", description = "모든 게시판의 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공", content =
		@Content(
			mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = Board.class))
		)),
		@ApiResponse(responseCode = "404", description = "게시판을 찾을 수 없음")
	})
	@GetMapping
	ResponseEntity<?> boardList(@RequestParam(defaultValue = "1") Integer currentPage);

	
	
	@Operation(summary = "게시판 상세 조회", description = "게시판 번호(bno)에 해당하는 게시판의 상세 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공", content =
		@Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Board.class)
		)),
		@ApiResponse(responseCode = "404", description = "게시판을 찾을 수 없음")
	})
	@GetMapping("/{bno}")
	ResponseEntity<?> detail(@PathVariable("bno") Integer bno);

	
    @Operation(summary = "게시글 boardType으로 목록 조회", description = "전체 게시글 또는 boardType으로 필터링된 목록을 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Board.class))))
    })
    @GetMapping(params = "boardType")
    ResponseEntity<?> boardList(
        @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam("boardType") String boardType
    );
    
    @Operation(
            summary = "게시판 조회수 순 조회",
            description = "주어진 boardType에 해당하는 게시글을 조회수 순으로 반환합니다."
        )
        @ApiResponses({
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Board.class))
                )
            ),
            @ApiResponse(responseCode = "404", description = "해당 유형의 게시글이 존재하지 않습니다.")
        })
        @GetMapping("/views/{boardType}")
        ResponseEntity<List<Board>> boardViews(
            @PathVariable("boardType") String boardType
        );
	
	

	@Operation(summary = "게시판 등록", description = "새로운 게시판을 등록합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "등록 성공", content =
		@Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Board.class)
		)),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 바디")
	})
	@PostMapping
	ResponseEntity<?> register(@RequestBody Board board, @AuthenticationPrincipal CustomUserDetails userDetails);


	@Operation(summary = "게시판 수정", description = "게시판 번호(bno)에 해당하는 게시판을 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "수정 성공", content =
		@Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Board.class)
		)),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 바디"),
		@ApiResponse(responseCode = "404", description = "게시판을 찾을 수 없음")
	})
	@PutMapping("/{bno}")
	ResponseEntity<?> update(@PathVariable("bno") Integer bno, @RequestBody Board board);


	@Operation(summary = "게시판 삭제", description = "게시판 번호(bno)에 해당하는 게시판을 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "삭제 성공", content =
		@Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Board.class)
		)),
		@ApiResponse(responseCode = "404", description = "게시판을 찾을 수 없음")
	})
	@DeleteMapping("/{bno}")
	ResponseEntity<?> delete(@PathVariable("bno") int bno);


	@Operation(summary = "게시판 검색", description = "게시판 제목 또는 내용을 검색합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "검색 성공", content =
		@Content(
			mediaType = "application/json",
			array = @ArraySchema(schema = @Schema(implementation = Board.class))
		)),
		@ApiResponse(responseCode = "404", description = "게시판을 찾을 수 없음")
	})
	@GetMapping("/boardFindWord")
	ResponseEntity<?> find(@RequestParam("text") String text);

	@Operation(summary = "댓글 등록", description = "게시글 번호(bno)에 해당하는 게시글에 댓글을 등록합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "등록 성공", content =
		@Content(
			mediaType = "application/json",
			schema = @Schema(implementation = Comment.class)
		)),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 바디")
	})
	@PostMapping("/{bno}/comment")
	ResponseEntity<?> addComment(
		@PathVariable("bno") Integer bno,
		@RequestBody Comment comment,
		@AuthenticationPrincipal CustomUserDetails userDetails
	);

	@Operation(summary = "댓글 수정", description = "게시글 번호(bno)와 댓글 번호(cno)에 해당하는 댓글을 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "수정 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 바디"),
		@ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
		@ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
	})
	@PutMapping("/{bno}/comment/{cno}")
	ResponseEntity<?> updateComment(
		@PathVariable("bno") Integer bno,
		@PathVariable("cno") Integer cno,
		@RequestBody Comment comment,
		@AuthenticationPrincipal CustomUserDetails userDetails
	);
	
	
	@Operation(summary = "댓글 삭제", description = "게시글 번호(bno)와 댓글 번호(cno)에 해당하는 댓글을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
        @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
    })
    @DeleteMapping("/{bno}/comment/{cno}")
    ResponseEntity<?> deleteComment(
        @PathVariable("bno") Integer bno,
        @PathVariable("cno") Integer cno,
        @AuthenticationPrincipal CustomUserDetails userDetails
    );
	
	
    @Operation(summary = "이미지 업로드", description = "특정 게시글에 이미지 첨부")
    @ApiResponse(responseCode = "200", description = "업로드 성공")
    @PostMapping("/{bno}/images")
    ResponseEntity<Map<String, Integer>> uploadImage(
        @PathVariable int bno,
        @RequestParam("file") MultipartFile file,
        @AuthenticationPrincipal CustomUserDetails user);

    @Operation(summary = "이미지 조회", description = "권한 검사 후 이미지 반환")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/images/{imgNo}")
    ResponseEntity<Resource> serveImage(
        @PathVariable Long imgNo,
        @AuthenticationPrincipal CustomUserDetails user);
    
    
    @Operation(
            summary = "회원별 게시글 조회",
            description = "쿼리 파라미터 mno에 해당하는 회원의 게시글을 현재 페이지 단위로 조회합니다."
        )
        @ApiResponses({
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Board.class))
                )
            ),
            @ApiResponse(responseCode = "404", description = "해당 회원의 게시글이 존재하지 않습니다.")
        })
        @GetMapping("member")
        ResponseEntity<?> boardList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @AuthenticationPrincipal CustomUserDetails userDetails
        );
    
    @Operation(
    		summary = "회원별 댓글 조회",
    		description =  "쿼리 파라미터 mno에 해당하는 회원의 댓글을 현재 페이지 단위로 조회합니다."
    		
		)
	    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Board.class))
                    )
                ),
                @ApiResponse(responseCode = "404", description = "해당 회원의 댓글이 존재하지 않습니다.")
            })
    		@GetMapping("comment")
		    ResponseEntity<?> commentList(
		            @RequestParam(defaultValue = "1") Integer currentPage,
		            @AuthenticationPrincipal CustomUserDetails userDetails
		        );
}
