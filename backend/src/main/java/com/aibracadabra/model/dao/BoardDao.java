package com.aibracadabra.model.dao;

import com.github.pagehelper.Page;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aibracadabra.model.dto.domain.Board;
import com.aibracadabra.model.dto.domain.BoardImage;
import com.aibracadabra.model.dto.domain.Comment;

@Mapper
public interface BoardDao {

	// 게시판에 글추가
	int add(Board board);

	// 게시판 원하는 제목만 찾기
	Page<Board> getBytitle(String title);

	// 게시판 전체 조회
	Page<Board> getAllBoards();
	
	// 게시판 글 수정
	int set(Board board);

	// 게시글을 bno로 가져오기(이거는 수정떄 쓰임 정말 해당 board 테이블 정보만 가져옴)
	Board getBoardBno(int bno);
	
	// 게시글 상세페이지 전용으로 가져오는데 댓글까지 동시에 가져올거임
	Board getBoardDetail(int bno);
	
	// 게시글을 boardType으로 선택해서 가져오기
	Page<Board> getBoardType(String boardType);
	
	// view로 가져오기
	List<Board> getBoardViews(String boardType);
	
	// mno로 가져오기
	Page<Board> getBoardMno(int mno);
	
	// mno로 댓글 전체 가져오기
	Page<Board> getCommentAll(int mno);

	
	// 게시판 글 삭제
	int remove(int bno);

	int removeAll(int mno);
	
	// 댓글 추가하기
	int addComment(Comment comment);
	
	// 댓글 삭제하기
	int removeComment(int cno);
	
	// 댓글 수정하기
	int setComment(Comment comment);
	
	// 댓글 하나 선택하기
	Comment getCommentCno(int cno);
	
	
	
	

	
    /** 이미지 메타 저장 */
    int insertImage(BoardImage image);

    /** 게시글별 이미지 ID 조회 */
    List<Integer> selectImageIdsByBno(int bno);

    /** 단일 이미지 메타 조회 */
    BoardImage selectImageMetaById(long imgNo);
}
