const signInBtn = document.getElementById("signIn");
const signUpBtn = document.getElementById("signUp");
const fistForm = document.getElementById("form1");
const secondForm = document.getElementById("form2");
const container = document.querySelector(".container");

signInBtn.addEventListener("click", () => {
  container.classList.remove("right-panel-active");
});

signUpBtn.addEventListener("click", () => {
  container.classList.add("right-panel-active");
});

// fistForm.addEventListener("submit", (e) => e.preventDefault());
// secondForm.addEventListener("submit", (e) => e.preventDefault());

document.getElementById("toLoginLink").classList.add("right-panel-active");
// 토글 링크 연결 (오버레이 토글과 별개로 링크 클릭시 뷰 전환)
document.getElementById("toSignupLink").addEventListener("click", (e) => {
  e.preventDefault();
  document.querySelector(".container").classList.add("right-panel-active");
});
document.getElementById("toLoginLink").addEventListener("click", (e) => {
  e.preventDefault();
  document.querySelector(".container").classList.remove("right-panel-active");
});