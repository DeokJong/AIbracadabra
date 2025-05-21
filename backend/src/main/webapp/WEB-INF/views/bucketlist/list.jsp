<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>거기어때</title>
    <!-- 스타일 파일 연결 -->
    <link rel="stylesheet" href="./static/css/tour-map.css"> 
    <link rel="stylesheet" href="./static/css/my-page.css">
    <!-- Bootstrap CSS 추가 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
	<%@ include file="/fragments/nav.jsp" %>
  </nav>

    <!-- 메인 컨텐츠 -->
    <main class="container my-5">
        <h1 class="my-5 text-center">즐겨찾기</h1>
        <div class='container-container my-5'>
            <div id="card-container" class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-5">
                <div class="col">
                  <div class="card h-100 shadow-sm">
                    <img class="card-img" src="/static/img/메인사진1.jpg" alt="">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">관광지 이름</li>
                        <li class="list-group-item">관광지 주소</li>
                    </ul>
                    <div class="card-body d-flex flex-column justify-content-between">
                      <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                      <div class="d-flex justify-content-between">
                        <div class="btn-group">
                          <button type="button" class="star starclick btn btn-sm btn-outline-secondary">★ | 즐겨찾기 해제</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
            </div>
        </div>
    </main>

    <!-- 푸터 -->
    <footer class="bg-dark text-white py-3">
		<%@ include file="/fragments/footer.jsp" %>
    </footer>
    <!-- Bootstrap JS 추가 -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!--서비스 ID 설정-->
    <script type = "text/javascript" src = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/javascriptAuth?consumer_key=79f99dac024e4d3484a2" ></script>
    <script src="/static/js/navbar.js"></script>
    <script src="/static/js/my-page.js"></script>

</body>

</html>
