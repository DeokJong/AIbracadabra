const area = document.querySelector("#areaCode");
const sigungu = document.querySelector("#sigunguCode");
const contentType = document.querySelector("#contentType");

// 지역 및 시군구 필터 얻어오기
const getFilters = async (areaCode) => {
	const queryObj = {
		serviceKey: key_data,
		numOfRows: 120,
		pageNo: 1,
		MobileOS: "ETC",
		MobileApp: "Test",
		_type: "json",
	};
	if (areaCode) {
		queryObj.areaCode = areaCode;
		console.log("시군구 필터 얻어오기");
	} else {
		console.log("지역 필터 얻어오기");
	}
	try {
		const json = await getFetch("https://apis.data.go.kr/B551011/KorService1/areaCode1", queryObj);
		console.log("fetched JSON: " + json);
		let info = json.response.body.items.item;
		info.forEach((item) => {
			item.key = item.code;
			item.label = item.name;
		});
		if (areaCode) {
			updateSelect(sigungu, "시군구", info);
		} else {
			updateSelect(area, "시도", info);
		}
	} catch (e) {
		console.log("검색 필터 로드 중 오류 발생:", e);
	}
};

const updateFilter = async () => {
	try {
		// URL 파라미터에서 검색 조건 읽기
		const urlParams = new URLSearchParams(window.location.search);
		const sidoCode = urlParams.get("sidoCode");
		const gugunCode = urlParams.get("gugunCode");
		const contentTypeId = urlParams.get("contentType");
		console.log("선택된 필터 갱신:", sidoCode, gugunCode, contentTypeId);

		if (sidoCode) {
			area.value = sidoCode;
			await getFilters(sidoCode);
		}
		if (gugunCode) sigungu.value = gugunCode;
		if (contentTypeId) contentType.value = contentTypeId;

		if (sidoCode || gugunCode || contentTypeId) {
			return true;
		} else {
			return false;
		}

	} catch (e) {
		console.log("선택된 필터 갱신 중 오류 발생:", e)
	}
}


// URL 파라미터 정보 갱신
const updateParams = async () => {
	const newParams = new URLSearchParams({
		action: "get",
		sidoCode: area.value,
		gugunCode: sigungu.value,
		contentType: contentType.value,
	});

	window.history.replaceState({}, "", `${window.location.pathname}?${newParams.toString()}`);
}

// 시군구 코드 얻어오기
const getGuguns = async () => {
	window.location.href = window.location.href;
};




/********** 이벤트리스너 등록 **********/
area.addEventListener("change", async function() {
	await getFilters(area.value);  // 시군구 필터 가져오기
});

sigungu.addEventListener("change", async function() {
});

contentType.addEventListener("change", async function() {
});