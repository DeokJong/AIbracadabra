<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.servletContext.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>거기어때</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- Bootstrap CSS 추가 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="static/css/index.css">
  <style>
    /* 캐러셀 컨트롤 버튼 스타일 */
    .carousel-control-prev-icon,
    .carousel-control-next-icon {
      background-color: rgba(0, 0, 0, 0.5);
      border-radius: 50%;
      padding: 10px;
    }
    
    /* 캐러셀 인디케이터 스타일 */
    .carousel-indicators button {
      background-color: rgba(0, 0, 0, 0.5);
      height: 10px;
      width: 10px;
      border-radius: 50%;
      margin: 0 5px;
    }
    
    /* 카드 호버 효과 */
    .card {
      transition: transform 0.3s ease;
    }
    
    .card:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
    }
  </style>
</head>
<body>
    <!-- 네비게이션 바 -->
    <nav class="navbar navbar-expand-lg navbar-light bg-primary">
		<%@ include file="/WEB-INF/views/fragments/nav.jsp" %>
    </nav>


  <!-- 메인 컨텐츠 -->
  <main class="mv-100 m-0 p-0">
	  <!-- 배너 섹션 -->
	  <section class="banner d-flex align-items-center justify-content-center text-white">
	    <div class="banner-bg-1"></div>
	    <div class="banner-bg-2"></div>
	    <div class="banner-bg-3"></div>
	    <div class="banner-bg-4"></div>
	    <div class="text-center banner-content" style="position: relative; z-index: 10;">
	      <h1 class="display-4 fw-bold text-shadow">여행을 즐겨라</h1>
	      <p class="lead text-shadow">당신의 완벽한 여행을 위한 최고의 가이드, 거기어때와 함께하세요</p>
	      <a href="${root}/map/get" class="btn btn-light btn-lg mt-3 shadow-sm">관광지 둘러보기 <i class="bi bi-arrow-right"></i></a>
	    </div>
	  </section>
	  
    <!-- 이번달 관광지 정보 -->
    <div class="container my-5 mb-5">
	    <section class="tour-info m-5">
	      <h2 class="mb-4">이번달 축제 정보</h2>
	      <div id="tourCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="5000">
	        <div class="carousel-indicators">
	          <button type="button" data-bs-target="#tourCarousel" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
	          <button type="button" data-bs-target="#tourCarousel" data-bs-slide-to="1" aria-label="Slide 2"></button>
	        </div>
	        <div class="carousel-inner">
	          <div class="carousel-item active">
	            <div class="row">
	              <div class="col-md-6 mb-4">
	                <div class="card">
	                  <img src="static/img/축제카드사진1.jpg" class="card-img-top main-card" alt="대만축제">
	                  <div class="card-body">
	                    <h5 class="card-title">서울 세계 불꽃 축제</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-6">
	                <div class="row">
	                  <div class="col-6 mb-4">
	                    <div class="card">
	                      <img src="static/img/축제카드사진2.jpg" class="card-img-top" alt="부산 - 여름 축제">
	                      <div class="card-body">
	                        <h5 class="card-title">무주 반딧불이 축제</h5>
	                      </div>
	                    </div>
	                  </div>
	                  <div class="col-6 mb-4">
	                    <div class="card">
	                      <img src="static/img/축제카드사진3.jpg" class="card-img-top" alt="서울 - 겨울 축제">
	                      <div class="card-body">
	                        <h5 class="card-title">진도 신비의 바닷길 축제</h5>
	                      </div>
	                    </div>
	                  </div>
	                  <div class="col-6">
	                    <div class="card">
	                      <img src="static/img/축제카드사진4.jpg" class="card-img-top" alt="광주 - 장구 축제">
	                      <div class="card-body">
	                        <h5 class="card-title">논살 딸기 축제</h5>
	                      </div>
	                    </div>
	                  </div>
	                  <div class="col-6">
	                    <div class="card">
	                      <img src="static/img/축제카드사진5.jpg" class="card-img-top" alt="대구 - 축제">
	                      <div class="card-body">
	                        <h5 class="card-title">영암 왕인 문화 축제</h5>
	                      </div>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	         </div>
	      </div>
	    </section>
	
	    <!-- 지역 정보 섹션 -->
	    <section class="local-info m-5">
	      <h2 class="mb-4">지역 정보</h2>
	      <div id="localCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="4000">
	        <div class="carousel-indicators">
	          <button type="button" data-bs-target="#localCarousel" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
	          <button type="button" data-bs-target="#localCarousel" data-bs-slide-to="1" aria-label="Slide 2"></button>
	          <button type="button" data-bs-target="#localCarousel" data-bs-slide-to="2" aria-label="Slide 3"></button>
	          <button type="button" data-bs-target="#localCarousel" data-bs-slide-to="3" aria-label="Slide 4"></button>
	          <button type="button" data-bs-target="#localCarousel" data-bs-slide-to="4" aria-label="Slide 5"></button>
	        </div>
	        <div class="carousel-inner">
	          <div class="carousel-item active">
	            <div class="row">
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/서울.jpeg" class="card-img-top" alt="서울">
	                  <div class="card-body">
	                    <h5 class="card-title">서울</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/부산.jpeg" class="card-img-top" alt="부산">
	                  <div class="card-body">
	                    <h5 class="card-title">부산</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/인천.jpeg" class="card-img-top" alt="인천">
	                  <div class="card-body">
	                    <h5 class="card-title">인천</h5>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	          <div class="carousel-item">
	            <div class="row">
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/대전.jpg" class="card-img-top" alt="대전">
	                  <div class="card-body">
	                    <h5 class="card-title">대전</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/대구.jpg" class="card-img-top" alt="대구">
	                  <div class="card-body">
	                    <h5 class="card-title">대구</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/광주.jpg" class="card-img-top" alt="광주">
	                  <div class="card-body">
	                    <h5 class="card-title">광주</h5>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	          <div class="carousel-item">
	            <div class="row">
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/울산.jpeg" class="card-img-top" alt="울산">
	                  <div class="card-body">
	                    <h5 class="card-title">울산</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/세종.jpg" class="card-img-top" alt="세종">
	                  <div class="card-body">
	                    <h5 class="card-title">세종특별자치시</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/경기.jpeg" class="card-img-top" alt="경기">
	                  <div class="card-body">
	                    <h5 class="card-title">경기도</h5>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	          <div class="carousel-item">
	            <div class="row">
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/강원.jpeg" class="card-img-top" alt="강원">
	                  <div class="card-body">
	                    <h5 class="card-title">강원도</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/충북.jpeg" class="card-img-top" alt="충북">
	                  <div class="card-body">
	                    <h5 class="card-title">충청북도</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/충남.jpeg" class="card-img-top" alt="충남">
	                  <div class="card-body">
	                    <h5 class="card-title">충청남도</h5>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	          <div class="carousel-item">
	            <div class="row">
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/경북.jpeg" class="card-img-top" alt="경북">
	                  <div class="card-body">
	                    <h5 class="card-title">경상북도</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/경남.jpeg" class="card-img-top" alt="경남">
	                  <div class="card-body">
	                    <h5 class="card-title">경싱남도</h5>
	                  </div>
	                </div>
	              </div>
	              <div class="col-md-4 mb-4">
	                <div class="card">
	                  <img src="static/img/local/제주.jpeg" class="card-img-top" alt="제주">
	                  <div class="card-body">
	                    <h5 class="card-title">제주특별자치도</h5>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	        </div>
	        <button class="carousel-control-prev" type="button" data-bs-target="#localCarousel" data-bs-slide="prev">
	          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
	          <span class="visually-hidden">이전</span>
	        </button>
	        <button class="carousel-control-next" type="button" data-bs-target="#localCarousel" data-bs-slide="next">
	          <span class="carousel-control-next-icon" aria-hidden="true"></span>
	          <span class="visually-hidden">다음</span>
	        </button>
	      </div>
	    </section>
    </div>
  </main>

  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3 position-relative">
  	<%@ include file="/WEB-INF/views/fragments/footer.jsp" %>
  </footer>
  
  <!-- 로그인 상태에 따른 네비게이션 동적 구성 및 로그아웃 처리 -->
  <script src="static/js/index.js"></script>
</body>
</html>
