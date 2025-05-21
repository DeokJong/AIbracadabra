document.getElementById('findPasswordForm').addEventListener('submit', function(e){
    e.preventDefault();
    // 예시에서는 userid 대신 이메일로 검증합니다.
    const email = document.getElementById('email').value;
    const user = JSON.parse(localStorage.getItem('user'));
    
    if(user && email === user.email){
      alert("회원님의 비밀번호는: " + user.password);
    } else {
      alert("입력하신 이메일과 일치하는 회원정보가 없습니다.");
    }
  });