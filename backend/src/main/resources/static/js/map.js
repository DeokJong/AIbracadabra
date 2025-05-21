
/***** 경로 관리 *****/

// 경로에 관광지 추가
const addRoute = (contentid, contenttypeid, title, address, x, y) => {
	document.getElementById("addRouteButton").style.display = "none";
	document.getElementById("deleteRouteButton").style.display = "block";

	if (routeMarkers[contentid]) return;

	const routeIcon = sop.icon({
		iconUrl: "./static/img/markers/current.gif",
		iconSize: [32, 32],
		iconAnchor: [22, 0],
		popupAnchor: [-3, -76],
		shadowSize: [32, 32],
		shadowAnchor: [32, 32],
	});
	let utmk = new sop.LatLng(y, x);

	const marker = sop.marker([utmk.x, utmk.y], { icon: routeIcon });
	marker.contentid = contentid;
	marker.contenttypeid = contenttypeid;
	marker.utmk = utmk;
	marker.label = title;
	marker.addTo(map).bindInfoWindow(title);
	routeMarkers[contentid] = marker;

	// 마커 이벤트 등록 (contenttypeid 기준으로 묶였으므로 2중 순회 필요)
	marker.on("click", function(e) {
		if (e.target.contentid) {
			document.getElementById("tour-info").style.display = "block";
			document.getElementById("toggle-info-btn").innerHTML = "<";
			searchDetailContent(e.target.contentid);
		}
	});

	let tmpAdj = [];
	for (let i = 0; i < route.length; i++) {
		let dist = Math.abs(route[i].x - x) + Math.abs(route[i].y - y);
		dist = Math.floor(dist * 10000);
		adjMat[i].push(dist);
		tmpAdj.push(dist);
	}
	tmpAdj.push(0);
	route.push({ contentid, title, address, x, y });
	adjMat.push(tmpAdj);

	showRoute();
	resetLine();
};

// 경로에서 관광지 삭제
const deleteRoute = (contentid) => {
	console.log("경로에서 삭제: " + contentid);

	// checkbox 원상복구
	if (contentid == selectedStartId) selectedStartId = null;
	else if (contentid == selectedEndId) selectedEndId = null;

	// route에서 제거
	for (let i = 0; i < route.length; i++) {
		if (route[i].contentid == contentid) {
			for (let j = 0; j < adjMat.length; j++) {
				adjMat[j] = adjMat[j].slice(0, i).concat(adjMat[j].slice(i + 1));
			}
			adjMat = adjMat.slice(0, i).concat(adjMat.slice(i + 1));
			route = route.slice(0, i).concat(route.slice(i + 1));
			break;
		}
	}

	// routeMarkers에서 제거
	if (routeMarkers[contentid]) {
		const r = routeMarkers[contentid];
		const contenttypeid = r.contenttypeid;

		// 현재 컨텐츠타입 이라면 기존 마커 생성
		if (routeMarkers[contentid].contenttypeid + "" == contentType.value
			|| contentType.value == '') {
			const customIcon = sop.icon({
				iconUrl: getMarkerIconByContentType(contenttypeid + ""),
				iconSize: [32, 32],
				iconAnchor: [22, 0],
				popupAnchor: [-3, -76],
				shadowSize: [32, 32],
				shadowAnchor: [32, 32],
			});

			const marker = sop.marker([r.utmk.x, r.utmk.y], { icon: customIcon });
			marker.addTo(map).bindInfoWindow(r.label);
			marker.contentid = parseInt(contentid);
			marker.contenttypeid = parseInt(contenttypeid);

			marker.on("click", function(e) {
				if (e.target.contentid) {
					document.getElementById("tour-info").style.display = "block";
					document.getElementById("toggle-info-btn").innerHTML = "<";
					searchDetailContent(e.target.contentid);
				}
			});

			// contenttypeid를 기준으로 그룹화
			if (!markers[contenttypeid]) markers[contenttypeid] = [];
			markers[contenttypeid].push(marker);
		}

		// 경로 마커 제거
		routeMarkers[contentid].remove(); // 지도에서 제거
		delete routeMarkers[contentid];   // 객체에서도 제거
	}

	document.getElementById("deleteRouteButton").style.display = "none";
	document.getElementById("addRouteButton").style.display = "block";

	showRoute();
	resetLine();
};


let selectedStartId = null;
let selectedEndId = null;

const showRoute = () => {

	const container = document.getElementById("route-container");
	container.innerHTML = "";
	if (!route) route = [];

	route.forEach((r) => {
		const routeBlock = document.createElement("div");
		routeBlock.className =
			"route-block bg-white border rounded shadow-sm p-2 mb-2 ps-2 pe-2 d-flex flex-column align-items-center";
		routeBlock.style.width = "100%";
		routeBlock.style.cursor = "pointer";

		routeBlock.addEventListener("click", () => {
			Object.values(routeMarkers).forEach((marker) => {
				if (marker.closeInfoWindow) marker.closeInfoWindow();
			});

			const marker = routeMarkers[r.contentid];
			if (marker && marker.openInfoWindow) {
				marker.openInfoWindow();
				if (marker.utmk) map.setView(marker.utmk, map.getZoom());
			}

			searchDetailContent(r.contentid);
		});

		// 아이콘
		const iconEl = document.createElement("img");
		iconEl.className = "me-2"; // 오른쪽 여백
		iconEl.src = getMarkerIconByContentType(routeMarkers[r.contentid].contenttypeid + "");
		iconEl.style.height = "20px"; // 적당한 사이즈

		// 제목
		const titleEl = document.createElement("h6");
		titleEl.className = "fw-bold mb-1 m-0"; // 여백 제거
		titleEl.textContent = `${r.title}`;
		titleEl.style.color = "#0d6efd";

		// 아이콘, 제목 묶는 div
		const titleRow = document.createElement("div");
		titleRow.className = "d-flex align-items-center";
		titleRow.appendChild(iconEl);
		titleRow.appendChild(titleEl);

		const addrEl = document.createElement("p");
		addrEl.innerHTML = `${r.address}`;
		addrEl.style.fontSize = "15px";
		addrEl.style.marginBottom = "0";

		const deleteBtn = document.createElement("button");
		deleteBtn.className = "btn btn-sm btn-outline-danger";
		deleteBtn.textContent = "삭제";
		deleteBtn.style.alignSelf = "flex-end";
		deleteBtn.addEventListener("click", (e) => {
			e.stopPropagation();
			deleteRoute(r.contentid);
		});

		const startCheckbox = document.createElement("input");
		startCheckbox.type = "checkbox";
		startCheckbox.className = "form-check-input me-2";
		startCheckbox.checked = selectedStartId === r.contentid;
		startCheckbox.addEventListener("click", (e) => {
			e.stopPropagation();
			if (selectedStartId != r.contentid) resetLine();
			selectedStartId = selectedStartId === r.contentid ? null : r.contentid;
			showRoute();
		});

		const startLabel = document.createElement("label");
		startLabel.className = "form-check-label";
		startLabel.appendChild(startCheckbox);
		startLabel.append("출발지");

		const endCheckbox = document.createElement("input");
		endCheckbox.type = "checkbox";
		endCheckbox.className = "form-check-input me-2";
		endCheckbox.checked = selectedEndId === r.contentid;
		endCheckbox.addEventListener("click", (e) => {
			e.stopPropagation();
			if (selectedEndId != r.contentid) resetLine();
			selectedEndId = selectedEndId === r.contentid ? null : r.contentid;
			showRoute();
		});

		const endLabel = document.createElement("label");
		endLabel.className = "form-check-label";
		endLabel.appendChild(endCheckbox);
		endLabel.append("도착지");

		const wrapper = document.createElement("div");
		wrapper.className = "d-flex flex-row justify-content-between gap-2 mt-1";
		wrapper.appendChild(startLabel);
		wrapper.appendChild(endLabel);
		wrapper.appendChild(deleteBtn);

		routeBlock.appendChild(titleRow);
		routeBlock.appendChild(addrEl);
		routeBlock.appendChild(wrapper);

		container.appendChild(routeBlock);
	});
};

const getRoute = async () => {
	if (route.length < 4) {
		alert("관광지를 추가해 주세요."); return;
	} else if (!selectedStartId) {
		alert("출발지를 선택해 주세요."); return;
	} else if (!selectedEndId) {
		alert("도착지를 선택해 주세요."); return;
	}

	const param = {
		route: route,
		adjMat: adjMat,
		startId: selectedStartId,
		endId: selectedEndId
	};

	try {
		const result = await postFetch("/trip/map/getRoute", param);
		route = result;
	} catch (e) {
		alert("최적 경로 요청 실패");
		return;
	}

	showRoute();

	// 지도에 선 그리기
	drawGradientRoute();

};

