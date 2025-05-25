package com.ssafy.model.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssafy.exception.RecordInsertException;
import com.ssafy.exception.RecordNotFoundException;
import com.ssafy.model.dao.BoardDao;
import com.ssafy.model.dao.BoardImageDao;
import com.ssafy.model.dto.domain.Board;
import com.ssafy.model.dto.domain.BoardImage;
import com.ssafy.model.dto.domain.Comment;
import com.ssafy.model.service.BoardService;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

	static final int d = 256;
	static final int q = 101;
	private static final Integer DEFAULT_PAGE_SIZE = 20;
	private final BoardDao boardDao;
    private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

	@Value("${upload.path}")
    private String uploadDir;

    private static final List<String> ALLOWED = 
        List.of(".jpg",".jpeg",".png",".gif",".webp",".bmp");
    
    private final BoardImageDao boardImageDao;    // 위 XML 매퍼


	@Override
	public int add(Board board) {

		int result = boardDao.add(board);
		if (result == 0) {
			throw new RecordInsertException("게시판 생성 실패");
		}
		return board.getBno();
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
	public PageInfo<Board> getByBoardType(String boardType, Integer currentPage) {
		try (Page<Board> page = PageHelper.startPage(currentPage, DEFAULT_PAGE_SIZE, "created_date DESC" )) {
			boardDao.getBoardType(boardType);
			return new PageInfo<>(page);
		}	
	}

	@Override
	public List<Board> getBoardViews(String boardType) {
		List<Board> list = boardDao.getBoardViews(boardType);
		if(list == null) {
			throw new RecordNotFoundException("게시글을 찾을 수 없습니다.");
		}
		return list;
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
	@Override
    @Transactional
    public Long storeImage(MultipartFile file, int bno) {
        // 1) MIME & 확장자 검사
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
        String orig = file.getOriginalFilename();
        String ext  = orig.substring(orig.lastIndexOf('.')).toLowerCase();
        if (!ALLOWED.contains(ext)) {
            throw new IllegalArgumentException("허용되지 않는 확장자입니다: " + ext);
        }
        // 2) 파일 저장
        String uuid = UUID.randomUUID().toString() + ext;
        Path target = Paths.get(uploadDir, uuid);
        try {
            Files.createDirectories(target.getParent());
            file.transferTo(target.toFile());
        } catch (IOException e) {
            logger.error("파일 저장 경로: {}", target, e);

            throw new RuntimeException("파일 저장 실패", e);
        }
        // 3) 메타 DB 저장
        BoardImage meta = new BoardImage();
        meta.setBno(bno);
        meta.setFilename(orig);
        meta.setContentType(file.getContentType());
        meta.setSize(file.getSize());
        meta.setStoragePath(target.toString());
        boardImageDao.insertImage(meta);
        return meta.getImgNo();
    }

    @Override
    public Resource loadImageAsResource(Long imgNo) throws AccessDeniedException, FileNotFoundException {
        BoardImage meta = boardImageDao.selectImageMetaById(imgNo);
        Board board = boardDao.getBoardBno(meta.getBno());
        boolean canView = "PUBLIC".equals(board.getVisibility());
        if (!canView) {
            throw new AccessDeniedException("열람 권한이 없습니다.");
        }
        Path file = Paths.get(meta.getStoragePath());
        Resource res = null;
		try {
			res = new UrlResource(file.toUri());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (!res.exists() || !res.isReadable()) {
            throw new FileNotFoundException("이미지를 찾을 수 없습니다.");
        }
        return res;
    }

    @Override
    public List<Integer> getImageIdsByBno(int bno) {
        return boardImageDao.selectImageIdsByBno(bno);
    }




}
