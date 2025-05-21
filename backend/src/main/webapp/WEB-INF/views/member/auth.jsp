<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.servletContext.contextPath}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>로그인</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <!-- dd.css: 로그인/회원가입 폼 스타일 -->
  <link rel="stylesheet" href="${root}/static/css/auth.css">
  <!-- 로그인 창 크기 조정을 위한 인라인 오버라이드 -->
  <style>
    :root {
      --max-height: 600px; /* 기존 420px에서 증가 */
      --max-width: 900px
      
    }
  </style>
</head>
<body>
  <!-- 로그인/회원가입 폼 영역 -->
  <div class="container ${login == null ? 'right-panel-active' : ''}">
    <!-- 회원가입 폼 (Sign Up) -->
    <div class="container__form container--signup">
      <form id="signupForm" class="form" action="${root}/member/signup" method="post">
        <h2 class="form__title">회원가입</h2>
        <div class="form-group">
          <label for="signupUserId">이름</label>
          <input type="text" id="loginUserName" name="name" placeholder="Name" value="${name}" class="input" required />
        </div>
        <div class="form-group">
          <label for="signupEmail">이메일</label>
          <input type="email" id="signupEmail" name="email" placeholder="Email" value="${email}" class="input" required />
        </div>
        <div class="form-group">
          <label for="signupPassword">비밀번호</label>
          <input type="password" id="signupPassword" name="password" placeholder="Password" value="${password}" class="input" required />
        </div>
        <div class="form-group">
          <label for="signupPasswordCheck">비밀번호 확인</label>
          <input type="password" id="signupPasswordCheck" name="passwordCheck" placeholder="Password 확인" value="${passwordCheck}" class="input" required />
        </div>
        <div class="form-group">
          <input type="radio" id="ADMIN" name="role" value="ADMIN" ${role.equals('ADMIN')? 'checked':''}>
          <label for="ADMIN">관리자</label>
          <input type="radio" id="USER" name="role" value="USER" ${role == null || role.equals('USER')? 'checked':''}>
          <label for="USER">일반회원</label>
        </div>
        <hr>
        <div class="checkbox-group">
          <input type="checkbox" id="agree" name="agree" required ${agree != null? 'checked':''}/>
          <label for="agree">개인정보정책 동의</label>
        </div>
        <button type="submit" class="btn btn-submit">회원가입</button>
        <hr>
        <div class="login-link">
          이미 계정이 있으신가요? <a href="#" id="toLoginLink">로그인</a>
        </div>
      </form>
    </div>

    <!-- 로그인 폼 (Sign In) -->
    <div class="container__form container--signin">
      <form id="loginForm" class="form" action="${root}/member/login" method="post">
        <h2 class="form__title">로그인</h2>
        <div class="form-group">
          <label for="loginUserId">이메일</label>
          <input type="text" id="loginUserEmail" name="email" placeholder="Email" class="input" required />
        </div>
        <div class="form-group">
          <label for="loginPassword">비밀번호</label>
          <input type="password" id="loginPassword" name="password" placeholder="Password" class="input" required />
        </div>
        <button type="submit" class="btn btn-submit">로그인</button>
        <hr>
        <div class="login-link">
          계정이 없으신가요? <a href="#" id="toSignupLink">회원가입</a> | <a href="find_password.html">비밀번호 찾기</a>
        </div>
      </form>
    </div>

    <!-- 오버레이 영역 -->
    <div class="container__overlay">
      <div class="overlay">
        <div class="overlay__panel overlay--left">
          <button class="btn" id="signIn">로그인</button>
        </div>
        <div class="overlay__panel overlay--right">
          <button class="btn" id="signUp">회원가입</button>
        </div>
      </div>
    </div>
  </div>

  <!-- 기존 dd.js: 로그인/회원가입 토글 스크립트 (기능이 이미 구현되어 있음) -->
  <script src="${root}/static/js/auth.js"></script>
  <script>
  	//alert 메세지 출력
  	const alertMsg = `${alertMsg}`;
  	if (alertMsg) {
  		alert(alertMsg);
  	}
  </script>
  
  <script>
  	// 회원가입 이후 화면 전환
  	if (`${signedUp}`) {
  		console.log("회원가입 성공!");
  		document.querySelector(".container").classList.remove("right-panel-active");
  	}
  </script>
  <c:remove var="signedUp"/>
  <c:remove var="alertMsg"/>
    
</body>
</html>
