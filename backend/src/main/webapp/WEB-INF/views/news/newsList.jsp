<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="root" value="${pageContext.servletContext.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>자유게시판</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- index.css (사용 중인 CSS) -->
  <link rel="stylesheet" href="${root}/static/css/index.css">
  <link rel="stylesheet" href="${root}/static/css/notice-board.css">
</head>
<body>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
	<jsp:include page="/WEB-INF/views/fragments/nav.jsp" />
  </nav>	

  <main>
	  <!-- 메인 컨테이너 -->
	  <div class="board-container">
	    <div class="board-title">뉴스 정보방</div>
	    
	    <!-- 게시글 테이블 -->
	    <table>
	      <thead>
	        <tr>
	          <th style="width: 60px;">No</th>
	          <th style="width: 100px;">제목</th>
	          <th>요약</th>
	          <th style="width: 140px;">작성날짜</th>
	          <!--   <th style="width: 60px;"></th>
	          <th style="width: 80px;">관리</th> -->
	        </tr>
	      </thead>
	      <tbody id="postList">
	        <!-- 게시글 목록이 동적으로 표시됩니다. -->
	        <c:set var="count" value="1"/>
			  <c:forEach var="news" items="${list}">
			    <tr>
			      <td>${count}</td>
			      <td>
			        <a href="${news.url}" target="_blank" rel="noopener noreferrer">${news.title}</a>
			      </td>
			      <td>${news.summary}</td>
			      <td>${news.publishAt}</td>
			        
			    </tr>
			    <c:set var="count" value="${count + 1}" />
			  </c:forEach>
	        
	        
	      </tbody>
	    </table>
	
	    <!-- 페이지네이션 -->
	    <div class="pagination" id="pagination"></div>
	
	   
	  </div>
  </main>
  
  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3">
  	<%@ include file="../fragments/footer.jsp" %>
  </footer>

  <script src="${root}/static/js/notice-board.js"></script>
</body>
<script>
	const loggedIn = "${loggedIn.name}";
</script>
</html>
