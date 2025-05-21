<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시글</title>
  <link rel="stylesheet" href="${root}/static/css/notice-board-view.css">
</head>
<body>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
    <jsp:include page="/WEB-INF/views/fragments/nav.jsp" />
  </nav>

  <main>
    <div class="main-container container">
      <div id="postDetail">

        <!-- 수정 폼 -->
        <form action="${root}/notice/noticeUpdate" method="post">
          <input type="hidden" name="nno" value="${notice.nno}" />
          
          <div class="post-title">
            <input type="text" id="title" name="title" value="${notice.title}" required />
          </div>
          
          <div class="post-meta">
            작성자: ${notice.author} | 작성시간: ${notice.created_date} | 수정시간: ${notice.updated_date}
          </div>
          
          <div class="post-content">
            <textarea id="content" rows="5" name="content" required>${notice.content}</textarea>
          </div>
          
          <button type="submit" class="btn btn-primary back-btn">수정</button>
        </form>

        <!-- 삭제 폼 (수정폼과 분리) -->
        <form action="${root}/notice/noticeDelete" method="post" style="display:inline;">
          <input type="hidden" name="nno" value="${notice.nno}" />
          <button type="submit" class="btn btn-danger back-btn">삭제하기</button>
        </form>

        <!-- 목록으로 가기 -->
        <a href="${root}/notice/noticeList" class="btn btn-secondary back-btn">목록으로</a>

      </div>
    </div>
  </main>

  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3">
    <%@ include file="../fragments/footer.jsp" %>
  </footer>

  <script src="${root}/static/js/notice-board-view.js"></script>
</body>
</html>
