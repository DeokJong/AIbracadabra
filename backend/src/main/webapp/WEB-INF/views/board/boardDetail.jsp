<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="root" value="${pageContext.servletContext.contextPath}" />
    
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시글</title>
  
  <link rel="stylesheet" href="${root }/static/css/notice-board-view.css">
</head>
<body>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
	<jsp:include page="/WEB-INF/views/fragments/nav.jsp" />
  </nav>
  
  <main>
	  <div class="main-container container">
	    <div id="postDetail">
	      <div class="post-title">${board.title}</div>
	      <div class="post-meta">
	        작성자: ${board.author} | 작성시간: ${board.created_date} | 수정시간: ${board.updated_date} | 좋아요: ${board.like}
	      </div>
	      <div class="post-content">
	        ${board.content}
	      </div>
	    </div>
	    <a href="${root }/board/boardList" class="btn btn-primary back-btn" >목록으로</a>
	  </div>
  </main>
  
  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3">
  	<%@ include file="../fragments/footer.jsp" %>
  </footer>
  
  <script src="${root}/static/js/notice-board-view.js"></script>
  
</body>
</html>
