// 페이지 로드 시 실행되는 함수
document.addEventListener('DOMContentLoaded', function() {
  // 로컬 스토리지에서 현재 로그인한 사용자 정보 가져오기
  const currentUser = JSON.parse(localStorage.getItem('user'));

  // 사용자가 로그인되어 있지 않으면 처리 중단
  if (!currentUser) {
    console.log('로그인된 사용자가 없습니다...');
    document.getElementById('card-container-container').innerHTML = '<div class="col-12 text-center"><h3>로그인 후 이용해주세요.</h3></div>';
    return;
  }
  
  // 로컬 스토리지에서 사용자 이름을 키로 가진 즐겨찾기 데이터 가져오기
  const favoritesData = localStorage.getItem(`${currentUser.userid}`);

  // 즐겨찾기 데이터가 없으면 처리 중단
  if (!favoritesData || JSON.parse(favoritesData).length === 0) {
    console.log('즐겨찾기 데이터가 없습니다.');
    document.getElementById('card-container').innerHTML = '<div class="col-12 text-center fade-in"><h3>즐겨찾기한 관광지가 없습니다.</h3></div>';
    return;
  }
  
  // JSON 문자열을 객체로 변환
  const favorites = JSON.parse(favoritesData);
  
  
  // 카드 컨테이너 요소 가져오기
  const cardContainer = document.getElementById('card-container');
  
  // 기존 카드 제거
  cardContainer.innerHTML = '';
  
  // 즐겨찾기 데이터를 기반으로 카드 생성
  favorites.forEach(item => {
    // overview 텍스트가 너무 길 경우 처리
    let overview = item.overview || '관광지 설명이 없습니다.';
    
    const cardHTML = `
      <div class="col">
        <div class="card h-100 shadow-sm">
          <img class="card-img" src="${item.firstimage || '/static/img/메인사진1.jpg'}" alt="${item.title || '관광지 이미지'}">
          <ul class="list-group list-group-flush">
            <li class="list-group-item">${item.title || '관광지 이름'}</li>
            <li class="list-group-item">${item.addr1 || '관광지 주소'}</li>
          </ul>
          <div class="card-body d-flex flex-column justify-content-between">
            <p class="card-text">${overview}</p>
            <div class="d-flex justify-content-between">
              <div class="btn-group">
                <button type="button" id="${item.contentid}" class="star starclick btn btn-sm btn-outline-secondary" data-id="${item.id}">★ | 즐겨찾기 해제</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    `;
    
    cardContainer.innerHTML += cardHTML;
  });
  
  // 즐겨찾기 버튼에 이벤트 리스너 추가
  const stars = document.querySelectorAll(".star");
  stars.forEach((star) => {
    star.addEventListener("click", (e) => {
      e.target.classList.toggle("starclick");
      e.target.classList.toggle("starunclick");
      
      if (e.target.classList.contains("starclick")) {
        e.target.innerHTML = "★ | 즐겨찾기 해제";
      } else {
        
        // 즐겨찾기 해제 시 해당 항목 제거
        const itemId = e.target.getAttribute('id');
        
        // 카드 요소 찾기 (버튼의 상위 요소들을 탐색)
        const cardElement = e.target.closest('.col');
        
        // 애니메이션 효과와 함께 카드 제거
        if (cardElement) {
          // 페이드 아웃 효과 적용
          cardElement.style.transition = 'opacity 0.5s ease';
          cardElement.style.opacity = '0';
          
          // 애니메이션 완료 후 요소 제거 및 데이터 업데이트
          setTimeout(() => {
            cardElement.remove();
            removeFavorite(itemId, false); // 두 번째 매개변수로 false 전달하여 새로고침 방지
            
            // 카드가 모두 제거되었는지 확인
            checkEmptyContainer();
          }, 500);
        } else {
          // 카드 요소를 찾지 못한 경우 기존 방식으로 처리
          removeFavorite(itemId, false);
        }
      }
    });
  });
});

// 카드 컨테이너가 비어있는지 확인하고 메시지 표시
function checkEmptyContainer() {
  const cardContainer = document.getElementById('card-container');
  if (cardContainer.children.length === 0) {
    cardContainer.innerHTML = '<div class="col-12 text-center fade-in"><h3>즐겨찾기한 관광지가 없습니다.</h3></div>';
  }
}

// 즐겨찾기 항목 제거 함수
function removeFavorite(itemId, shouldReload = true) {
  // 로컬 스토리지에서 즐겨찾기 데이터 가져오기
  const currentUser = JSON.parse(localStorage.getItem('user'));
  const favoritesData = localStorage.getItem(`${currentUser.userid}`);
  
  if (favoritesData) {
    const favorites = JSON.parse(favoritesData);
    
    // 해당 ID를 가진 항목 찾기 및 제거
    const updatedFavorites = favorites.filter(item => item.contentid !== itemId);
    
    // 업데이트된 즐겨찾기 목록 저장
    localStorage.setItem(`${currentUser.userid}`, JSON.stringify(updatedFavorites));

    // shouldReload가 true인 경우에만 페이지 새로고침
    if (shouldReload) {
      location.reload();
    }
  }
}