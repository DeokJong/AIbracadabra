package com.ssafy.model.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssafy.exception.RecordInsertException;
import com.ssafy.exception.RecordNotFoundException;
import com.ssafy.model.dao.BoardDao;
import com.ssafy.model.dto.domain.Board;
import com.ssafy.model.dto.domain.Comment;
import com.ssafy.model.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	static final int d = 256;
	static final int q = 101;
	private static final Integer DEFAULT_PAGE_SIZE = 20;
	private final BoardDao boardDao;

	@Override
	public int add(Board board) {

		int result = boardDao.add(board);
		if (result == 0) {
			throw new RecordInsertException("게시판 생성 실패");
		}
		return result;
	}

	@Override
	public PageInfo<Board> getByTitle(String title, Integer currentPage) {
		try (Page<Board> page = PageHelper.startPage(currentPage, DEFAULT_PAGE_SIZE)) {
			boardDao.getBytitle(title);
			return new PageInfo<>(page);
		}
	}

	@Override
	public PageInfo<Board> getAllBoards(Integer currentPage) {
		try (Page<Board> page = PageHelper.startPage(currentPage, DEFAULT_PAGE_SIZE)) {
			boardDao.getAllBoards();
			return new PageInfo<>(page);
		}
	}

	@Override
	public Board set(Board board) {
		boardDao.set(board);
		return boardDao.getBoardBno(board.getBno());
	}

	@Override
	public int remove(int bno) {
		int result = boardDao.remove(bno);
		if (result == 0) {
			throw new RecordNotFoundException("게시판이 존재하지 않음");
		}
		return result;
	}

	@Override
	public void addComment(Comment comment) {
		int result = boardDao.addComment(comment);
		if (result == 0) {
			throw new RecordInsertException("해당 게시판이 존재하지 않음");
		}
	}

	@Override
	public Board getBoardBno(int bno) {
		Board board = boardDao.getBoardBno(bno);
		if (board == null) {
			throw new RecordNotFoundException("게시판이 존재하지 않음");
		}
		return board;
	}

	@Override
	public Board getBoardDetail(int bno) {
		Board board = getBoardBno(bno);
		board.setViews(board.getViews() + 1);
		boardDao.set(board);
		Board newBoard = boardDao.getBoardDetail(bno);
		if (newBoard == null) {
			throw new RecordNotFoundException("게시판이 존재하지 않음");
		}
		return newBoard;
	}

	@Override
	public void removeComment(int cno) {
		int result = boardDao.removeComment(cno);
		if (result == 0) {
			throw new RecordInsertException("댓글이 없음");
		}
	}

	@Override
	public void setComment(Comment comment) {

		if (boardDao.getCommentCno(comment.getCno()) == null) {
			throw new RecordInsertException("수정할 댓글이 없음");
		}

		boardDao.setComment(comment);
	}

	@Override
	public Comment getCommentCno(int cno) {
		Comment comment = boardDao.getCommentCno(cno);
		if (comment == null) {
			throw new RecordInsertException("댓글이 없음");
		}
		return comment;
	}

	@Override
	public List<Board> boardFindWord(String text) {

		List<Board> board = boardDao.getAllBoards();
		List<Board> newBoards = new ArrayList<>();
		String[] texts = text.split(" ");
		PriorityQueue<Node> pq = new PriorityQueue<>();

		for (int i = 0; i < board.size(); i++) {
			int num = 0;
			for (String s : texts) {
				int k = findWord(board.get(i).getTitle(), s);
				int k2 = findWord(board.get(i).getContent(), s);

				if (k == 1 || k2 == 1) {
					num += 1;
				}

			}
			if (num > 0) {
				pq.add(new Node(board.get(i), num));

			}

		}
		while (!pq.isEmpty()) {
			Node now = pq.poll();
			newBoards.add(now.board);
		}
		return newBoards;

	}

	public int findWord(String text, String pattern) {
		if (text == null || pattern == null)
			return 0;

		int n = text.length();
		int m = pattern.length();

		if (m > n || m == 0)
			return 0;

		int i, j;
		int p = 0;
		int t = 0;
		int h = 1;

		for (i = 0; i < m - 1; i++)
			h = (h * d) % q;

		for (i = 0; i < m; i++) {
			p = (d * p + pattern.charAt(i)) % q;
			t = (d * t + text.charAt(i)) % q;
		}

		for (i = 0; i <= n - m; i++) {
			if (p == t) {
				for (j = 0; j < m; j++) {
					if (text.charAt(i + j) != pattern.charAt(j))
						break;
				}
				if (j == m) {
					return 1;
				}
			}

			if (i < n - m) {
				t = (d * (t - text.charAt(i) * h) + text.charAt(i + m)) % q;
				if (t < 0)
					t += q;
			}
		}

		return 0;
	}

	// TODO 8. SQL 문으로 정렬 변환
	private static class Node implements Comparable<Node> {
		Board board = Board.builder().build();
		int cost;

		public Node(Board board, int cost) {
			super();
			this.board = board;
			this.cost = cost;
		}

		@Override
		public int compareTo(Node o) {
			if (this.cost == o.cost) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime thisDate = LocalDateTime.parse(this.board.getCreatedDate(), formatter);
				LocalDateTime otherDate = LocalDateTime.parse(o.board.getCreatedDate(), formatter);
				return otherDate.compareTo(thisDate);
			}
			return Integer.compare(o.cost, this.cost);
		}
	}


}
