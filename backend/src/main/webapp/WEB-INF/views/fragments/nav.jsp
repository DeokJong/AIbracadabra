<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="root" value="${pageContext.servletContext.contextPath}" />

<!-- Bootstrap CSS 추가 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css" rel="stylesheet">

<style>
	/* 네비게이션 바 그라데이션 */
	.navbar.bg-primary {
	  background: linear-gradient(90deg, #4a6bff, #77e1fb) !important;
	  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	}
	
	
	@font-face {
	  font-family: 'GongGothicMedium';
	  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-10@1.0/GongGothicMedium.woff') format('woff');
	  font-weight: normal;
	  font-style: normal;
	}
	
	* {
	  font-family: 'GongGothicMedium';
	}
	
	:root {
	  /* COLORS */
	  --white: #e9e9e9;
	  --gray: #333;
	  --blue: #0367a6;
	  --lightblue: #008997;
	  --bg-image-1: url('../img/메인사진1.jpg');
	  --bg-image-2: url('../img/메인사진2.jpg');
	  --bg-image-3: url('../img/메인사진3.jpg');
	  --bg-image-4: url('../img/메인사진4.jpg');
	}
	
    body {
	   background-color: #f5f5f5;
	   position: absolute;
	   width: 100%;
	   height: 100%;
	   display: flex;
	   flex-direction: column;
    }
    
    main {
    	flex-grow: 1;
    }
	
</style>

<div class="container">
    <a class="navbar-brand text-white fw-bold" href="${root}/index.jsp">거기어때</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul id="authLinks" class="navbar-nav ms-auto">
            <li class="nav-item"><a class="nav-link text-white" href="${root}/map/get">관광지도</a></li>
            <li class="nav-item"><a class="nav-link text-white" href="${root }/board/boardList">게시판</a></li>
            <li class="nav-item"><a class="nav-link text-white" href="${root }/notice/noticeList">공지사항</a></li>
            <li class="nav-item"><a class="nav-link text-white" href="${root }/news/newsList">뉴스정보보기</a></li>
            
            <c:if test="${SPRING_SECURITY_CONTEXT == null}">
	            <li class="nav-item"><a class="nav-link text-white" href="${root}/member/authForm?login=true">로그인</a></li>
	            <li class="nav-item"><a class="nav-link text-white" href="${root}/member/authForm">회원가입</a></li>
            </c:if>
            <c:if test="${SPRING_SECURITY_CONTEXT != null }">
	            <li class="nav-item"><a class="nav-link text-white" href="${root}/member/logout">로그아웃</a></li>
	            <li class="nav-item"><a class="nav-link text-white" href="${root}/member/mypage">마이페이지</a></li>
            </c:if>
        </ul>
    </div>
</div>

<!-- Bootstrap Bundle JS (Popper 포함) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>