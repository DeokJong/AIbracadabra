package com.ssafy.model.service;

import com.github.pagehelper.PageInfo;
import com.ssafy.model.dto.domain.Board;
import com.ssafy.model.dto.domain.Comment;

import java.util.List;

public interface BoardService {

    int add(Board board);

    PageInfo<Board> getByTitle(String title, Integer currentPage);

    PageInfo<Board> getAllBoards(Integer currentPage);

    Board set(Board board);

    int remove(int bno);

    Board getBoardBno(int bno);

    Board getBoardDetail(int bno);

    List<Board> boardFindWord(String text);

    void addComment(Comment comment);

    void removeComment(int cno);

    void setComment(Comment comment);

    Comment getCommentCno(int cno);

}
