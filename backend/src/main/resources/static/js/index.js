// 기존 코드 주석 처리
/*
// 사용할 배경 이미지 목록
const images = [
  "static/img/메인사진1.jpg",
  "static/img/메인사진2.jpg",
  "static/img/메인사진3.jpg",
];

// 현재 이미지 인덱스
let currentIndex = 0;

// 슬라이드 요소 가져오기
const banner = document.querySelector(".banner");

// 처음에는 애니메이션 없이 배경 적용
banner.style.backgroundImage = `url('${images[currentIndex]}')`;
// banner.style.opacity = "0";

// 일정 시간 후 애니메이션 시작
setTimeout(() => {
  setInterval(changeBackground, 3000);
}, 3000); // 3초 후 애니메이션 시작

// 배경 변경 함수 (페이드인/페이드아웃 효과 적용)
function changeBackground() {
  // 페이드아웃 효과 적용
  banner.style.opacity = "0";

  // 애니메이션 후 배경 변경
  setTimeout(() => {
    // 다음 이미지 인덱스 계산
    currentIndex = (currentIndex + 1) % images.length;

    // 새로운 배경 이미지 설정
    banner.style.backgroundImage = `url('${images[currentIndex]}')`;

    // 페이드인 효과 적용
    setTimeout(() => {
      banner.style.opacity = "1";
    }, 50);
  }, 1000); // 1초 후 배경 변경
}
*/

// 새로운 JavaScript 코드 - CSS 애니메이션과 함께 작동
document.addEventListener('DOMContentLoaded', function() {
  // 배경 이미지 목록
  const backgroundImages = [
    "static/img/메인사진1.jpg",
    "static/img/메인사진2.jpg",
    "static/img/메인사진3.jpg",
    "static/img/메인사진4.jpg"
  ];
  
  // 배너 요소와 가상 요소 스타일 가져오기
  const banner = document.querySelector('.banner');
  
  // CSS에서 이미 애니메이션이 적용되어 있으므로 추가 작업은 필요 없음
  // 하지만 필요한 경우 여기에 추가 기능 구현 가능
  
  // 예: 배경 이미지 동적 변경이 필요한 경우
  function updateBackgroundImages() {
    // CSS 변수를 통해 배경 이미지 업데이트 (필요한 경우)
    document.documentElement.style.setProperty('--bg-image-1', `url('${backgroundImages[0]}')`);
    document.documentElement.style.setProperty('--bg-image-2', `url('${backgroundImages[1]}')`);
    document.documentElement.style.setProperty('--bg-image-3', `url('${backgroundImages[2]}')`);
    document.documentElement.style.setProperty('--bg-image-4', `url('${backgroundImages[3]}')`);
  }
  
  // 필요한 경우 이미지 업데이트 함수 호출
  // updateBackgroundImages();
  
  // 캐러셀 초기화 - 이 부분을 index.html의 스크립트로 이동
  try {
    // 투어 캐러셀 설정
    const tourCarouselEl = document.getElementById('tourCarousel');
    if (tourCarouselEl) {
      const tourCarousel = new bootstrap.Carousel(tourCarouselEl, {
        interval: 5000,
        wrap: true,
        touch: true
      });
    }
    
    // 지역 캐러셀 설정
    const localCarouselEl = document.getElementById('localCarousel');
    if (localCarouselEl) {
      const localCarousel = new bootstrap.Carousel(localCarouselEl, {
        interval: 4000,
        wrap: true,
        touch: true
      });
    }
  } catch (error) {
    console.error('캐러셀 초기화 중 오류 발생:', error);
  }
});

