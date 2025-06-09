package com.aibracadabra.model.service;

import com.github.pagehelper.PageInfo;
import com.aibracadabra.model.dto.domain.Board;
import com.aibracadabra.model.dto.domain.Comment;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    int add(Board board);

    PageInfo<Board> getByTitle(String title, Integer currentPage);

    PageInfo<Board> getAllBoards(Integer currentPage);
    
    PageInfo<Board> getBoardMno(Integer mno, Integer currentPage);
    
    PageInfo<Comment> getCommentAll(Integer mno, Integer currentPage);

    Board set(Board board);

    int remove(int bno);

    Board getBoardBno(int bno);

    Board getBoardDetail(int bno);

    List<Board> boardFindWord(String text);
    
    PageInfo<Board> getByBoardType(String boardType, Integer currentPage);
    
    List<Board> getBoardViews(String boardType);

    void addComment(Comment comment);

    void removeComment(int cno);

    void setComment(Comment comment);

    Comment getCommentCno(int cno);
    
    /** 이미지 저장 후 imgNo 반환 */
    Long storeImage(MultipartFile file, int bno);

    /** 권한 검사 후 Resource 반환 
     * @throws AccessDeniedException 
     * @throws FileNotFoundException */
    Resource loadImageAsResource(Long imgNo) throws AccessDeniedException, FileNotFoundException;

    /** 게시글 상세에 뿌릴 이미지 ID 리스트 */
    List<Integer> getImageIdsByBno(int bno);

}
