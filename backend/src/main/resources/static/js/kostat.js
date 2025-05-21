const map = sop.map("map");
// marker 목록
const markers = {};  // key: contenttypeid, value: 일반 마커
const routeMarkers = {};  // key: contentid, value: 경로 마커
let routeLine = null;  // 이전 경로 라인 저장용

// 경계 목록
const bounds = [];

// 경로 거리 인접행렬
var route = [];
var adjMat = [];

const landmarkImg = document.querySelector("#landmarkImg");
const landmarkName = document.querySelector("#landmarkName");
const landmarkAddress = document.querySelector("#landmarkAddress");

var accessToken = storage.getItem("accessToken");  // 로컬 스토리지 이용 바람

// access token 가져오기
const getAccessToken = async () => {
	try {
		const json = await getFetch("https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json", {
			consumer_key: key_sgis_service_id, // 서비스 id
			consumer_secret: key_sgis_security, // 보안 key
		});
		accessToken = json.result.accessToken;
		storage.setItem("accessToken", accessToken);
	} catch (e) {
		console.log(e);
	}
};

// 주소를 UTM-K좌표로 변환해서 반환: - json의 errCd ==-401에서 access token 확보!!
const getCoords = async (address) => {
	try {
		const json = await getFetch("https://sgisapi.kostat.go.kr/OpenAPI3/addr/geocode.json", {
			accessToken: accessToken,
			address: address,
			resultcount: 1,
		});
		if (json.errCd === -401) {
			await getAccessToken();
			return await getCoords(address);
		}
		return json.result.resultdata[0];
	} catch (e) {
		console.log(e);
	}
};

const getMarkerIconByContentType = (contenttypeid) => {
	switch (contenttypeid) {
		case "12": return "/static/img/markers/travel.png"; // 관광지
		case "14": return "/static/img/markers/culture.png"; // 문화시설
		case "15": return "/static/img/markers/festival.png"; // 행사/공연/축제
		case "25": return "/static/img/markers/course.png"; // 여행코스
		case "28": return "/static/img/markers/activity.png"; // 레포츠
		case "32": return "/static/img/markers/house.png"; // 숙박
		case "38": return "/static/img/markers/shopping.png"; // 쇼핑
		case "39": return "/static/img/markers/food.png"; // 음식점
		default: return "/static/img/markers/current.gif";
	}
};

const updateMap = (infos) => {
	resetMarker();
	try {
		for (let i = 0; i < infos.length; i++) {
			const info = infos[i];
			const contenttypeid = info.contenttypeid;
			const customIcon = sop.icon({
				iconUrl: getMarkerIconByContentType(contenttypeid),
				iconSize: [32, 32],
				iconAnchor: [22, 0],
				popupAnchor: [-3, -76],
				shadowSize: [32, 32],
				shadowAnchor: [32, 32],
			});

			// 이미 routeMarker에 존재한다면 생략
			if (routeMarkers[info.contentid]) continue;
			const marker = sop.marker([info.utmk.x, info.utmk.y], { icon: customIcon });
			marker.addTo(map).bindInfoWindow(info.label);
			marker.contentid = parseInt(info.contentid);
			marker.contenttypeid = parseInt(contenttypeid);

			// contenttypeid를 기준으로 그룹화
			if (!markers[contenttypeid]) markers[contenttypeid] = [];
			markers[contenttypeid].push(marker);

			bounds.push([info.utmk.x, info.utmk.y]);
		}

		// 중심 이동
		if (bounds.length > 1) {
			map.setView(map._getBoundsCenterZoom(bounds).center, map._getBoundsCenterZoom(bounds).zoom);
		} else {
			map.setView(map._getBoundsCenterZoom(bounds).center, 9);
		}

	} catch (e) {
		console.log(e);
	}

	// 마커 이벤트 등록 (contenttypeid 기준으로 묶였으므로 2중 순회 필요)
	Object.values(markers).flat().forEach((marker) => {
		marker.on("click", function(e) {
			if (e.target.contentid) {
				searchDetailContent(e.target.contentid);
			}
		});
	});
};

// 마커와 경계 초기화
const resetMarker = () => {
	// 모든 마커 제거 (중첩 배열 처리)
	Object.values(markers).flat().forEach((marker) => {
		if (map.hasLayer(marker)) map.removeLayer(marker);
	});

	// 객체 비우기
	for (const key in markers) delete markers[key];

	// 경계 초기화
	bounds.length = 0;
};


/****** 지도 위에 라인 그리기 ******/

// 라인 초기화
const resetLine = () => {
	try {
		if (routeLine?.segments?.forEach) {
			routeLine.segments.forEach(line => {
				if (map.hasLayer(line)) map.removeLayer(line);
			});
		}
	} catch (e) {
		console.warn("resetLine 실패:", e);
	} finally {
		routeLine = null;
	}
};

// 색상 보간 함수
const interpolateColor = (startColor, endColor, factor) => {
	const hexToRgb = hex => hex.match(/\w\w/g).map(x => parseInt(x, 16));
	const rgbToHex = rgb => "#" + rgb.map(x => x.toString(16).padStart(2, '0')).join('');

	const startRGB = hexToRgb(startColor);
	const endRGB = hexToRgb(endColor);

	const resultRGB = startRGB.map((startVal, i) => {
		return Math.round(startVal + factor * (endRGB[i] - startVal));
	});

	return rgbToHex(resultRGB);
};

// 라인 그리기
const drawGradientRoute = () => {
	// 기존 라인 제거
	if (routeLine && routeLine.segments) {
		routeLine.segments.forEach(line => {
			if (map.hasLayer(line)) map.removeLayer(line);
		});
		routeLine = null;
	}

	const segments = [];
	const startColor = "#0d6efd"; // 파랑
	const endColor = "#dc3545";   // 빨강

	// LatLng 배열 구성
	const latlngs = route.map(r => new sop.LatLng(r.y, r.x));

	// 루프 처리 (출발지 = 도착지일 경우)
	if (selectedStartId === selectedEndId && latlngs.length > 1) {
		latlngs.push(latlngs[0]); // 처음 지점 다시 연결
	}

	// 선 그리기
	for (let i = 0; i < latlngs.length - 1; i++) {
		const from = latlngs[i];
		const to = latlngs[i + 1];
		const factor = i / (latlngs.length - 2); // 0 ~ 1
		const color = interpolateColor(startColor, endColor, factor);

		const segmentLine = sop.polyline([from, to], {
			color: color,
			weight: 5,
			opacity: 0.5,
			lineCap: "round",
			lineJoin: "round"
		}).addTo(map);

		segments.push(segmentLine);
	}

	// 경로 전체 보기
	const bounds = sop.utmkBounds(latlngs);
	map.fitBounds(bounds);

	// 관리용 저장
	routeLine = { segments };
};

