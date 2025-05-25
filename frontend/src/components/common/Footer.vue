<template>
  <footer class="footer">
    <div class="footer-top">
      <div class="footer-section logo">
        <!-- <img src="/logo.png" alt="YourSite Logo" class="logo-img" /> -->
        <p class="tagline">여행의 모든 순간을 함께 합니다.</p>
      </div>

      <div class="footer-section links">
        <h4>사이트</h4>
        <ul>
          <li><a href="/about">회사소개</a></li>
          <li><a href="/terms">이용약관</a></li>
          <li><a href="/privacy">개인정보처리방침</a></li>
        </ul>
      </div>

      <div class="footer-section contact">
        <h4>고객지원</h4>
        <p>Email: support@yoursite.com</p>
        <p>Tel: 02-123-4567</p>
      </div>

      <div class="footer-section social">
        <h4>팔로우</h4>
        <div class="social-icons">
          <a href="#"><i class="mdi mdi-instagram"></i></a>
          <a href="#"><i class="mdi mdi-facebook"></i></a>
          <a href="#"><i class="mdi mdi-twitter"></i></a>
        </div>
      </div>
    </div>

    <div class="footer-newsletter">
      <label for="email">뉴스레터 구독</label>
      <div class="newsletter-form">
        <input id="email" type="email" placeholder="이메일을 입력하세요" />
        <button>구독하기</button>
      </div>
    </div>

    <div class="footer-bottom">
      © 2025 YourSite. All rights reserved.
    </div>
  </footer>
</template>

<script setup>
// 로직 없음
</script>

<style scoped>
.footer {
  background: #f8f9fa;
  color: black;
  padding: 2rem 1rem;
  font-size: 0.9rem;
}
.footer a {
  color: black;
  text-decoration: none;
}
.footer a:hover {
  color: #83bcea;
}
.footer-top {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  margin-bottom: 2rem;
}
.footer-section {
  flex: 1 1 200px;
}
.logo-img {
  width: 120px;
  margin-bottom: 0.5rem;
}
.tagline {
  font-style: italic;
}
.links ul {
  list-style: none;
  padding: 0;
}
.links li {
  margin: 0.5rem 0;
}
.social-icons a {
  margin-right: 0.5rem;
  font-size: 1.2rem;
}
.footer-newsletter {
  text-align: center;
  margin-bottom: 2rem;
}
.newsletter-form {
  display: inline-flex;
  margin-top: 0.5rem;
}
.newsletter-form input {
  padding: 0.5rem;
  border: none;
  border-radius: 4px 0 0 4px;
}
.newsletter-form button {
  padding: 0.5rem 1rem;
  border: none;
  background: #ff6b6b;
  color: #fff;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
}
.footer-bottom {
  text-align: center;
  border-top: 1px solid #444;
  padding-top: 1rem;
  font-size: 0.8rem;
}

/* 반응형 */
@media (max-width: 768px) {
  .footer-top {
    flex-direction: column;
  }
}
</style>
