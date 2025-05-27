<template>
  <footer class="modern-footer">
    <!-- 메인 그라디언트 배경 -->
    <div class="footer-gradient">
      <!-- 상단 메인 콘텐츠 -->
      <div class="footer-container">
        <div class="footer-grid">
          <!-- 브랜드 섹션 -->
          <div class="footer-brand">
            <div class="brand-content">
              <div class="logo-section">
                <div class="logo-icon">🌍</div>
                <h3 class="brand-name">TourGuide</h3>
              </div>
              <p class="brand-tagline">
                여행의 모든 순간을 함께하며<br>
                새로운 세상을 발견하세요
              </p>
              <div class="brand-stats">
                <div class="stat-item">
                  <span class="stat-number">50K+</span>
                  <span class="stat-label">여행객</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">200+</span>
                  <span class="stat-label">관광지</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 서비스 링크 -->
          <div class="footer-links">
            <h4 class="section-title">서비스</h4>
            <ul class="link-list">
              <li><a href="/map" class="footer-link">
                <span class="link-icon">🗺️</span>관광지도
              </a></li>
              <li><a href="/board" class="footer-link">
                <span class="link-icon">📝</span>자유게시판
              </a></li>
              <li><a href="/notice" class="footer-link">
                <span class="link-icon">📢</span>공지사항
              </a></li>
              <li><a href="/qna" class="footer-link">
                <span class="link-icon">💬</span>문의하기
              </a></li>
            </ul>
          </div>

          <!-- 고객지원 -->
          <div class="footer-support">
            <h4 class="section-title">고객지원</h4>
            <div class="contact-info">
              <div class="contact-item">
                <span class="contact-icon">📧</span>
                <div class="contact-details">
                  <span class="contact-label">이메일</span>
                  <a href="mailto:support@yoursite.com" class="contact-value">
                    support@yoursite.com
                  </a>
                </div>
              </div>
              <div class="contact-item">
                <span class="contact-icon">📞</span>
                <div class="contact-details">
                  <span class="contact-label">전화</span>
                  <a href="tel:02-123-4567" class="contact-value">
                    02-123-4567
                  </a>
                </div>
              </div>
              <div class="contact-item">
                <span class="contact-icon">🕒</span>
                <div class="contact-details">
                  <span class="contact-label">운영시간</span>
                  <span class="contact-value">평일 09:00-18:00</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 소셜미디어 & 뉴스레터 -->
          <div class="footer-social">
            <h4 class="section-title">소셜미디어</h4>
            <div class="social-buttons">
              <a href="#" class="social-btn instagram">
                <i class="mdi mdi-instagram"></i>
                <span>Instagram</span>
              </a>
              <a href="#" class="social-btn facebook">
                <i class="mdi mdi-facebook"></i>
                <span>Facebook</span>
              </a>
              <a href="#" class="social-btn twitter">
                <i class="mdi mdi-twitter"></i>
                <span>Twitter</span>
              </a>
              <a href="#" class="social-btn youtube">
                <i class="mdi mdi-youtube"></i>
                <span>YouTube</span>
              </a>
            </div>
          </div>
        </div>


      </div>

      <!-- 하단 저작권 영역 -->
      <div class="footer-bottom">
        <div class="footer-container">
          <div class="bottom-content">
            <div class="copyright">
              <p>© 2025 TourGuide. All rights reserved.</p>
            </div>
            <div class="legal-links">
              <a href="/terms" class="legal-link">이용약관</a>
              <a href="/privacy" class="legal-link">개인정보처리방침</a>
              <a href="/about" class="legal-link">회사소개</a>
            </div>
            <button class="back-to-top" @click="scrollToTop">
              <i class="mdi mdi-chevron-up"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref } from 'vue'

const email = ref('')

const subscribeNewsletter = () => {
  if (email.value) {
    // 뉴스레터 구독 로직
    console.log('뉴스레터 구독:', email.value)
    email.value = ''
    // 성공 메시지 표시 등
  }
}

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}
</script>

<style scoped>
/* 메인 footer 컨테이너 */
.modern-footer {
  position: relative;
  overflow: hidden;
}

.footer-gradient {
  background: linear-gradient(135deg, 
    #667eea 0%, 
    #764ba2 25%, 
    #4B6DFF 50%, 
    #6DC7FC 75%, 
    #667eea 100%);
  background-size: 400% 400%;
  animation: gradientShift 15s ease infinite;
  position: relative;
}

.footer-gradient::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(1px);
}

@keyframes gradientShift {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  position: relative;
  z-index: 2;
}

/* 그리드 레이아웃 */
.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 3rem;
  padding: 4rem 0 3rem;
  color: white;
}

/* 브랜드 섹션 */
.footer-brand {
  position: relative;
}

.brand-content {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 2rem;
  transition: all 0.3s ease;
}

.brand-content:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.logo-icon {
  width: 50px;
  height: 50px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.brand-name {
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.brand-tagline {
  font-size: 1rem;
  line-height: 1.6;
  margin-bottom: 1.5rem;
  opacity: 0.9;
}

.brand-stats {
  display: flex;
  gap: 2rem;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 1.5rem;
  font-weight: 700;
  color: #FFD700;
}

.stat-label {
  font-size: 0.8rem;
  opacity: 0.8;
}

/* 섹션 제목 */
.section-title {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  color: #FFD700;
  position: relative;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 30px;
  height: 2px;
  background: #FFD700;
  border-radius: 1px;
}

/* 링크 스타일 */
.link-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  padding: 0.5rem 0;
  transition: all 0.3s ease;
  border-radius: 8px;
}

.footer-link:hover {
  color: #FFD700;
  transform: translateX(5px);
  padding-left: 0.5rem;
}

.link-icon {
  font-size: 1rem;
  width: 20px;
}

/* 연락처 정보 */
.contact-info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.contact-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.contact-icon {
  font-size: 1.2rem;
  width: 24px;
  flex-shrink: 0;
}

.contact-details {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.contact-label {
  font-size: 0.8rem;
  opacity: 0.7;
}

.contact-value {
  color: white;
  text-decoration: none;
  font-weight: 500;
}

.contact-value:hover {
  color: #FFD700;
}

/* 소셜미디어 버튼 */
.social-buttons {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.social-btn {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  color: white;
  text-decoration: none;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.social-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.social-btn.instagram:hover { background: linear-gradient(45deg, #f09433, #e6683c, #dc2743, #cc2366, #bc1888); }
.social-btn.facebook:hover { background: #1877f2; }
.social-btn.twitter:hover { background: #1da1f2; }
.social-btn.youtube:hover { background: #ff0000; }

.social-btn i {
  font-size: 1.2rem;
}

.social-btn span {
  font-weight: 500;
}

/* 뉴스레터 섹션 */
.newsletter-section {
  margin: 2rem 0;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 2.5rem;
  text-align: center;
}

.newsletter-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: white;
}

.newsletter-icon {
  font-size: 1.8rem;
}

.newsletter-description {
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 2rem;
  line-height: 1.6;
}

.newsletter-form {
  max-width: 400px;
  margin: 0 auto;
}

.input-group {
  display: flex;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50px;
  padding: 0.3rem;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
}

.newsletter-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: 1rem 1.5rem;
  font-size: 1rem;
  color: #333;
  outline: none;
}

.newsletter-input::placeholder {
  color: #666;
}

.newsletter-btn {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  border-radius: 50px;
  padding: 1rem 2rem;
  color: white;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.newsletter-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
}

.btn-icon {
  transition: transform 0.3s ease;
}

.newsletter-btn:hover .btn-icon {
  transform: translateX(5px);
}

/* 하단 영역 */
.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.2);
  padding: 1.5rem 0;
  background: rgba(0, 0, 0, 0.2);
}

.bottom-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: rgba(255, 255, 255, 0.8);
}

.copyright p {
  margin: 0;
  font-size: 0.9rem;
}

.legal-links {
  display: flex;
  gap: 2rem;
}

.legal-link {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.3s ease;
}

.legal-link:hover {
  color: #FFD700;
}

.back-to-top {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.back-to-top:hover {
  background: #FFD700;
  color: #333;
  transform: translateY(-3px);
}

/* 반응형 디자인 */
@media (max-width: 1024px) {
  .footer-grid {
    grid-template-columns: 1fr 1fr;
    gap: 2rem;
  }
  
  .footer-brand {
    grid-column: 1 / -1;
  }
}

@media (max-width: 768px) {
  .footer-container {
    padding: 0 1rem;
  }
  
  .footer-grid {
    grid-template-columns: 1fr;
    gap: 2rem;
    padding: 2rem 0;
  }
  
  .brand-content {
    padding: 1.5rem;
  }
  
  .brand-stats {
    justify-content: center;
  }
  
  .newsletter-section {
    padding: 2rem;
  }
  
  .input-group {
    flex-direction: column;
    gap: 0.5rem;
    background: transparent;
  }
  
  .newsletter-input {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 25px;
  }
  
  .newsletter-btn {
    justify-content: center;
  }
  
  .bottom-content {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .legal-links {
    gap: 1rem;
  }
  
  .social-buttons {
    flex-direction: row;
    flex-wrap: wrap;
    gap: 0.5rem;
  }
  
  .social-btn {
    flex: 1;
    min-width: calc(50% - 0.25rem);
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .brand-stats {
    gap: 1rem;
  }
  
  .legal-links {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .social-btn {
    min-width: 100%;
  }
}
</style>
