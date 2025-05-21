<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>관광지도</title>
    <link rel="stylesheet" href="${root}/static/css/map.css">
    <!-- Bootstrap CSS 추가 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css" rel="stylesheet">
    <style>
    	body {
    		width: 100%;
    		height: 100%;
    	}
    </style>
</head>
<body>

    <!-- 네비게이션 바 -->
    <nav class="navbar navbar-expand-lg navbar-light bg-primary">
        <%@ include file="/WEB-INF/views/fragments/nav.jsp" %>
    </nav>

    <!-- 메인 컨텐츠 -->
    <main>
        <div class='d-flex position-relative'>
            <div class="w-100 position-relative center">
            
                <!-- 지역 선택 필터 -->
				<div class="filters d-flex flex-wrap justify-content-center gap-3 my-3 me-5">
				    <select id="areaCode" class="form-select" style="width: 200px;">
				        <option value="" selected disabled>시도 선택</option>
				    </select>
				
				    <select id="sigunguCode" class="form-select" style="width: 200px;">
				        <option value="" selected disabled>시군구 선택</option>
				    </select>
				
				    <select id="contentType" class="form-select" style="width: 200px;">
				        <option value="" selected disabled>관광타입 선택</option>
				        <c:forEach items="${contentTypeList}" var="content">
				            <option value="${content.code}">${content.name}</option>
				        </c:forEach>
				    </select>
				
				    <button id="btn_trip_search" class="btn btn-primary px-4">조회</button>
				</div>
                
                <!-- 지도 영역 -->
                <div>
                    <div id="map"></div>
                    <div id="popup" class='h-100 d-flex align-items-center position-absolute z-3 bottom-0 pe-none'>
                        
                        <!-- 관광지 상세 정보 -->
                        <section id="tour-info" class="h-100 p-3 bg-light border rounded overflow-auto pe-auto" style="display: none;">
                            <div id="info-contatiner" class="position-relative align-items-center d-flex flex-column">
                                <div id="landmarkInfo" class="col-md-12">
                                    <ul class="list-group w-100">
                                    	<!-- 
                                        <li id='landmarkName' class="list-group-item"><strong>관광지 명:</strong> <span id="tour-name">-</span></li>
                                        <li id='landmarkAddress' class="list-group-item"><strong>주소:</strong> <span id="tour-address">-</span></li>
                                        -->
                                        <li id='landmarkAddress' class="list-group-item"><strong></strong> <span id="tour-address">선택된 관광지가 없습니다.</span></li>
                                    </ul>
                                </div>
                            </div>
                        </section>
                        <!-- ✅ 토글 버튼 추가 -->
                        <button id="toggle-info-btn" class="btn btn-primary h-25 pe-auto">
                            >
                        </button>
                    </div>
                    <!-- 경로 추천 사이드 -->
                    <div id="route-info" class="d-flex flex-column bg-light border rounded me-5">
                    	<div class="d-flex flex-row justify-content-between">
	                    	<button id="saveRouteButton" class="btn btn-secondary btn-sm m-2 me-1">경로 저장</button>
	                    	<button id="getRouteButton" onClick="getRoute()" class="btn btn-outline-primary btn-sm m-2 ms-1">경로 추천받기</button>
                    	</div>
                    	<div id="route-container" class="overflow-auto d-flex flex-column p-2 align-items-center z-3">
                    	</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 토스트 컨테이너 -->
        <div class="toast-container position-fixed bottom-0 end-0 p-3">
            <!-- 토스트 요소에 fade 클래스를 추가 -->
            <div id="liveToast" class="toast fade" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="3000">
                <div class="toast-header">
                  <strong class="me-auto">거 기 어 때</strong>
                  <small>방금 전</small>
                  <button type="button" class="btn-close ms-2 mb-1" data-bs-dismiss="toast" aria-label="Close">
                    <span aria-hidden="true"></span>
                  </button>
                </div>
                <div class="toast-body">
                  즐겨찾기에 추가되었습니다!
                </div>
            </div>
        </div>

    </main>

    <!--서비스 ID 설정-->
    <script type = "text/javascript" src = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/javascriptAuth?consumer_key=97c19896de3843c9bf5e" ></script>
    <script src="${root}/static/js/common.js"></script>
    <script src="${root}/static/js/filter.js"></script>
    <script src="${root}/static/js/kostat.js"></script>
    <script src="${root}/static/js/enjoytrip.js"></script>
    <script src="${root}/static/js/map.js"></script>
    
    <script>
		//alert 메세지 출력
		const alertMsg = `${alertMsg}`;
		if (alertMsg) {
			alert(alertMsg);
		}
	</script>
	
	<script>
		 const key_vworld = `${key_vworld}`;
		 const key_sgis_service_id = `${key_sgis_service_id}`;  // 서비스 id
		 const key_sgis_security = `${key_sgis_security}`; // 보안 key
		 const key_data = `${key_data}`;
	</script>
    
    <script>
        const init = async () => {
        	await getFilters();
        	
        	if (await updateFilter()) {
        		searchAttractions();
        	} else {
	        	updateMap([
	                {
	                    address: "서울특별시 강남구 테헤란로 212",
	                    utmk: await getCoords("서울특별시 강남구 테헤란로 212"),
	                    label: "멀티캠퍼스",
	                },
	            ]);
        	}
            //{ address: address, utmk: getCoords(address), label: aptNm }
        };
        init();
    </script>
    
    <script>
        document.getElementById("toggle-info-btn").addEventListener("click", function () {
            let tourInfo = document.getElementById("tour-info");
    
            if (tourInfo.style.display === "none" || tourInfo.style.display === "") {
                tourInfo.style.display = "block"; // 보이기
                document.getElementById("toggle-info-btn").innerHTML = "<"; // 버튼 모양 변경
            } else {
                tourInfo.style.display = "none"; // 숨기기
                document.getElementById("toggle-info-btn").innerHTML = ">"; // 버튼 모양 변경
            }
        });
    </script>
</body>

</html>
