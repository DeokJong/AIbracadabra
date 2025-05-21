<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>마이 페이지</title>
  
  <link rel="stylesheet" href="../static/css/mypage.css">
</head>
<body>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
	<%@ include file="../fragments/nav.jsp" %>
  </nav>
  
  <main>
  <c:set var="loggedIn" value="${SPRING_SECURITY_CONTEXT.authentication.principal.member }" ></c:set>
	  <div class="mypage-container">
	    <h2>마이 페이지</h2>
	    <!-- 회원정보 수정 폼 -->
	    <form id="memberForm" action="${root}/member/delete" method="post">
	    	<input type="hidden" name="mno" value="${loggedIn.mno}"/>
	      <div class="form-group">
	        <label for="email">이름</label>
	        <input type="text" id="name" name="name" value="${loggedIn.name}" disabled />
	      </div>
	      <div class="form-group">
	        <label for="email">이메일</label>
	        <input type="email" id="email" name="email" value="${loggedIn.email}" disabled />
	      </div>
	      <div class="form-group">
	        <label for="email">권한</label>
	        <input type="text" id="rold" name="rold" value="${loggedIn.role}" disabled />
	      </div>
	      <div class="buttons">
		      <a href="${root}/member/updateForm"><button type="button" class="btn-update" >회원정보 수정</button></a>
		      <!-- 회원 탈퇴 버튼 -->
			  <button id="submit" class="btn-delete">회원 탈퇴</button>
	      </div>
	    </form>
	  </div>
  </main>
  
  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3">
  	<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
  </footer>

</body>
</html>
