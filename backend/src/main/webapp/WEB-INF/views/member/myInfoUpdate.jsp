<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>회원 정보 수정</title>
  
  <link rel="stylesheet" href="../static/css/myInfoUpdate.css">
</head>
<body>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
	<%@ include file="../fragments/nav.jsp" %>
  </nav>
  
    
  <main>
  <c:set var="loggedIn" value="${SPRING_SECURITY_CONTEXT.authentication.principal.member }" ></c:set>
  	<div class="mypage-container">
	    <h2>회원 정보 수정</h2>
	    <!-- 회원정보 수정 폼 -->
	    <form id="updateForm" action="${root}/member/update" method="post">
	   	  <input type="hidden" name="mno" value="${loggedIn.mno}"/>
	      <div class="form-group">
	        <label for="email">이름</label>
	        <input type="text" id="name" name="name" value="${loggedIn.name}" required />
	      </div>
	      <div class="form-group">
	        <label for="email">이메일</label>
	        <input type="email" id="email" name="email" value="${loggedIn.email}" readonly />
	      </div>
	      <div class="form-group">
	        <label for="email">비밀번호</label>
	        <input type="password" id="password" name="password" value="" required />
	      </div>
	      <div class="form-group">
	          <input type="radio" id="ADMIN" name="role" value="ADMIN" ${loggedIn.role.equals('ADMIN')? 'checked':''}>
	          <label for="ADMIN">관리자</label>
	          <input type="radio" id="USER" name="role" value="USER" ${loggedIn.role == null || loggedIn.role.equals('USER')? 'checked':''}>
	          <label for="USER">일반회원</label>
	      </div>
	      
	      <div class="buttons">
			  <button id="submit" class="btn-confirm">수정</button>
		      <a href="${root}/member?action=mypage&mno=${loggedIn.mno}"><button type="button" class="btn-cancel" >취소</button></a>
	      </div>
	    </form>
  	</div>
  </main>
  
  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3">
  	<%@ include file="../fragments/footer.jsp" %>
  </footer>

</body>
</html>
