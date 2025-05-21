
// 관광 정보 조회
const searchAttractions = async () => {
	const queryObj = {
		serviceKey: key_data,
		numOfRows: 20,
		pageNo: 1,
		MobileOS: "ETC",
		MobileApp: "Test",
		_type: "json",
	};
	// 추가로 설정할 조건이 있다면 추가하기
	if (area.value) {
		queryObj.areaCode = area.value;
	}
	if (sigungu.value) {
		queryObj.sigunguCode = sigungu.value;
	}
	if (contentType.value) {
		queryObj.contentTypeId = contentType.value;
		storage.setItem("contentType", contentType.value);
	}

	try {
		const json = await getFetch("https://apis.data.go.kr/B551011/KorService1/areaBasedList1", queryObj);

		const spots = json.response.body.items.item;

		if (!spots) {
			resetMarker();
			return;
		}
		spots.forEach((element) => {
			// 기본적으로 통계청의 SGIS map은 utmk 기반이므로 WG384(lat, lng)기반을 utmk 로 변경
			element.utmk = new sop.LatLng(element.mapy, element.mapx);
			element.address = element.addr1;
			element.label = element.title;
		});

		storage.setItem("spots", JSON.stringify(spots));
		updateMap(spots);

	} catch (e) {
		console.log(e);
	}
}


// 관광지 조회 버튼 클릭시
document.querySelector("#btn_trip_search").addEventListener("click", async () => {
	updateParams();
	searchAttractions();
});


// 상세 내용 검색
const searchDetailContent = async (contentid) => {
	// 추가로 설정할 조건이 있다면 추가하기
	const queryObj = {
		serviceKey: key_data,
		MobileOS: "ETC",
		MobileApp: "Test",
		_type: "json",
		contentId: contentid,
		defaultYN: "Y",
		firstImageYN: "Y",
		addrinfoYN: "Y",
		overviewYN: "Y",
		mapinfoYN: "Y",
	};

	try {
		document.querySelector("#info-contatiner").innerHTML = `<div class="my-5 spinner-border text-primary" role="status" style="width: 3rem; height: 3rem;">
                                                                    <span class="visually-hidden">Loading...</span>
                                                                </div>`;

		const json = await getFetch("https://apis.data.go.kr/B551011/KorService1/detailCommon1", queryObj);

		const detail = json.response.body.items.item[0];
		const detailHomePage = detail.homepage || "홈페이지가 없습니다.";
		let buttons = `<button id="addRouteButton" class="btn btn-sm btn-primary my-2" onClick="addRoute(${contentid}, ${detail.contenttypeid},'${detail.title}', '${detail.addr1}', ${detail.mapx}, ${detail.mapy})">경로 추가</button>
					     <button id="deleteRouteButton" class="btn btn-sm btn-danger my-2" onClick="deleteRoute(${contentid})">경로 제거</button>`;
		for (const r of route) {
			if (r.contentid == contentid) {
				buttons = `<button id="addRouteButton" class="btn btn-sm btn-primary my-2" style="display:none;" onClick="addRoute(${contentid}, '${detail.title}', '${detail.addr1}', ${detail.mapx}, ${detail.mapy})">경로 추가</button>
						   <button id="deleteRouteButton" class="btn btn-sm btn-danger my-2" style="display:block" onClick="deleteRoute(${contentid})">경로 제거</button>`;
				break;
			}
		}
		storage.setItem("detail", JSON.stringify(detail));

		console.log(detail);
		const innerDetail = `
							<h4 id='landmarkName' class="list-group-item overflow-auto text-center"><strong>${detail.title}</strong></h4>
							<div class="d-flex row justify-content-flex-end gap-2">
                            	<img id='landmarkImg' src="${detail.firstimage || "/static/img/noimage.jpeg"}" alt="관광지 이미지" class="img-fluid rounded object-fit-cover">
								<div id="favorite" class="position-absolute end-0 rounded bg-gradient" style="width: 60px; aspect-ratio: 1;">
								    <svg xmlns="http://www.w3.org/2000/svg" width="80%" height="80%" fill="white" class="bi bi-star" viewBox="0 0 16 16">
								        <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/>
								    </svg>
								    <svg xmlns="http://www.w3.org/2000/svg" width="80%" height="80%" fill="yellow" class="bi bi-star-fill visually-hidden" viewBox="0 0 16 16">
								        <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/>
								    </svg>
								</div>
							</div>
							${buttons}
                            <div id="landmarkInfo" class="col-md-12">
                                <ul class="list-group w-100">
                                    <li id='landmarkURL' class="list-group-item overflow-auto">${detailHomePage}</li>
                                    <li id='landmarkAddress' class="list-group-item overflow-auto"><strong>주소: </strong>${detail.addr1}</li>
                                    <li class="list-group-item overflow-auto">${detail.overview}</li>
                                </ul>
                            </div>`;

		document.querySelector("#info-contatiner").innerHTML = innerDetail;
		// document.querySelector("#favorite").querySelector("svg.bi-star").classList.add("visually-hidden");
		// document.querySelector("#favorite").querySelector("svg.bi-star-fill").classList.remove("visually-hidden");

		document.getElementById("tour-info").style.display = "block";
		document.getElementById("toggle-info-btn").innerHTML = "<"; // 버튼 모양 변경

	} catch (e) {
		console.log("관광지 상세 정보 조회 도중 오류 발생:", e);
	}

	document.getElementById("favorite").addEventListener("click", function() {
		// 첫 번째 svg: outline 별 (보이는 상태)
		const starOutline = this.querySelector("svg.bi-star");
		// 두 번째 svg: 채워진 별 (처음엔 숨겨진 상태; "visually-hidden" 클래스가 있음)
		const starFill = this.querySelector("svg.bi-star-fill");

		starOutline.classList.toggle("visually-hidden");
		starFill.classList.toggle("visually-hidden");



		if (starOutline.classList.contains("visually-hidden")) {
			// 버킷리스트 추가
			const toastEl = document.getElementById('liveToast');
			const toast = new bootstrap.Toast(toastEl); // 옵션({ delay: 5000, autohide: true })를 설정해도 됨
			toast.show();

		} else {
			// 버킷리스트 삭제
		}
	});
}