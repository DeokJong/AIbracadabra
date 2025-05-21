// 예: 현재 로그인된 사용자 정보를 localStorage에 저장했다고 가정
// 예시: localStorage.setItem("user", JSON.stringify({ userid: "홍길동" }));

const POSTS_PER_PAGE = 5;
let currentPage = 1;

// 글쓰기 폼 토글
const openFormBtn = document.getElementById('openFormBtn');
const postForm = document.getElementById('postForm');
let formVisible = false;
openFormBtn.addEventListener('click', () => {
 	if (!loggedIn) {
		if (confirm("로그인 후 이용해 주세요.")) {
			window.location.href = "/trip/member?action=authForm&login=";
		}
	} else {
	  formVisible = !formVisible;
	  postForm.style.display = formVisible ? 'block' : 'none';		
	}
});


/** 
// 로컬 스토리지에서 게시글 가져오기
function getPosts() {
  return JSON.parse(localStorage.getItem('posts')) || [];
}

// 로컬 스토리지에 게시글 저장
function savePosts(posts) {
  localStorage.setItem('posts', JSON.stringify(posts));
}
*/
// 게시글 테이블 렌더링
/** 
function renderPosts() {
  const postList = document.getElementById('postList');
  const posts = getPosts();  // 로컬 스토리지에서 posts 배열 불러오는 함수
  const totalPages = Math.ceil(posts.length / POSTS_PER_PAGE) || 1;
  
  // 현재 페이지가 총 페이지 수보다 크면 보정
  if (currentPage > totalPages) currentPage = totalPages;
  
  const startIndex = (currentPage - 1) * POSTS_PER_PAGE;
  const endIndex = startIndex + POSTS_PER_PAGE;
  const postsToDisplay = posts.slice(startIndex, endIndex);
  
  // localStorage의 "user"에서 현재 userid 가져오기
  const userData = JSON.parse(localStorage.getItem("user")) || {};
  const currentUserId = userData.userid || "";
  
  postList.innerHTML = '';
  
  
  renderPagination(posts.length);
}
*/
// 페이지네이션 렌더링
function renderPagination(totalPosts) {
  const pagination = document.getElementById('pagination');
  const totalPages = Math.ceil(totalPosts / POSTS_PER_PAGE) || 1;
  pagination.innerHTML = '';

  if (totalPages <= 1) return;
  
  const prevBtn = document.createElement('button');
  prevBtn.textContent = '이전';
  prevBtn.disabled = (currentPage === 1);
  prevBtn.addEventListener('click', () => {
    if (currentPage > 1) {
      currentPage--;
      renderPosts();
    }
  });
  pagination.appendChild(prevBtn);

  for (let i = 1; i <= totalPages; i++) {
    const pageBtn = document.createElement('button');
    pageBtn.textContent = i;
    if (i === currentPage) {
      pageBtn.classList.add('active');
    }
    pageBtn.addEventListener('click', () => {
      currentPage = i;
      renderPosts();
    });
    pagination.appendChild(pageBtn);
  }

  const nextBtn = document.createElement('button');
  nextBtn.textContent = '다음';
  nextBtn.disabled = (currentPage === totalPages);
  nextBtn.addEventListener('click', () => {
    if (currentPage < totalPages) {
      currentPage++;
      renderPosts();
    }
  });
  pagination.appendChild(nextBtn);
}

// 글쓰기 폼 제출 처리
/** 
postForm.addEventListener('submit', (e) => {
  e.preventDefault();
  const title = document.getElementById('title').value.trim();
  // 글쓴이 입력란 대신 localStorage에서 현재 userid 사용
  const userData = JSON.parse(localStorage.getItem("user")) || {};
  const currentUserId = userData.userid || "";
  const content = document.getElementById('content').value.trim();
  
  if (!title || !currentUserId || !content) return;

  const posts = getPosts();
  const newPost = {
    id: Date.now() + Math.random(),
    title,
    writer: currentUserId, // localStorage의 userid 사용
    content,
    timestamp: Date.now(),
    likes: 0
  };
  posts.push(newPost);
  savePosts(posts);

  postForm.reset();
  // 글쓰기 폼 재노출 시, 글쓴이 칸에 다시 userid를 채워넣음
  document.getElementById('writer').value = currentUserId;
  postForm.style.display = 'none';
  formVisible = false;
  currentPage = 1;
  renderPosts();
});
*/
// 좋아요 및 삭제 이벤트 (이벤트 위임)
/*
document.getElementById('postList').addEventListener('click', (e) => {
  const likeId = e.target.dataset.likeId;
  const deleteId = e.target.dataset.deleteId;
  let posts = getPosts();

  if (likeId) {
    const idx = posts.findIndex(p => p.id == likeId);
    if (idx !== -1) {
      posts[idx].likes += 1;
      savePosts(posts);
      renderPosts();
    }
  }

  if (deleteId) {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    posts = posts.filter(p => p.id != deleteId);
    savePosts(posts);
    renderPosts();
  }
});

renderPosts();
*/