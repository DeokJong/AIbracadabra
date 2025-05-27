<template>
  <div class="board-card">
    <!-- 클릭 조건 추가: 공개글이거나 본인 글일 때만 클릭 가능 -->
    <router-link 
      v-if="item.visibility === 'PUBLIC' || item.mno === userInfo?.mno"
      :to="`/board/${item.bno}`" 
      class="card-link"
    >
      <!-- 이미지 영역 -->
      <div class="card-image">
        <img 
          v-if="item.imageUrls && item.imageUrls.length > 0"
          :src="`/api/v1/board/images/${item.imageUrls}`"
          :alt="item.title"
          class="thumbnail"
        />
        <div v-else class="no-image">
          <i class="fas fa-file-text"></i>
        </div>
      </div>
      
      <!-- 콘텐츠 영역 -->
      <div class="card-content">
        <h3 class="card-title">{{ item.title }}</h3>
        <p class="card-summary">{{ truncatedContent }}</p>
        
        <div class="card-footer">
          <div class="author-info">
            <div class="avatar">{{ item.author.charAt(0) }}</div>
            <span class="author-name">{{ item.author }}</span>
          </div>
          <div class="meta-info">
            <span class="date">{{ formattedDate }}</span>
            <span class="views" v-if="item.views">
              <i class="fas fa-eye"></i> {{ item.views }}
            </span>
          </div>
        </div>
      </div>
    </router-link>

    <!-- 비공개 글일 때 클릭 불가능한 카드 -->
    <div v-else class="card-link private-card">
      <!-- 이미지 영역 -->
      <div class="card-image private-image">
        <div class="no-image private-overlay">
          <i class="fas fa-lock"></i>
        </div>
      </div>
      
      <!-- 콘텐츠 영역 -->
      <div class="card-content">
        <h3 class="card-title private-title">비공개 글입니다.</h3>
        <p class="card-summary private-summary">이 글은 비공개 설정되어 있습니다.</p>
        
        <div class="card-footer">
          <div class="author-info">
            <div class="avatar">{{ item.author.charAt(0) }}</div>
            <span class="author-name">{{ item.author }}</span>
          </div>
          <div class="meta-info">
            <span class="date">{{ formattedDate }}</span>
            <span class="private-badge">
              <i class="fas fa-lock"></i> 비공개
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps, computed } from 'vue'
import { useAuth } from '@/hooks/useAuth'
import type { Board } from '@/hooks/boardService'

const props = defineProps<{ item: Board }>()
const userInfo = useAuth().userInfo

const formattedDate = computed(() => {
  const d = new Date(props.item.createdDate)
  return `${d.getFullYear()}.${String(d.getMonth()+1).padStart(2,'0')}.${String(d.getDate()).padStart(2,'0')}`
})

const truncatedContent = computed(() => {
  // 비공개 글이면 내용 숨김
  if (props.item.visibility === 'PRIVATE' && props.item.mno !== userInfo?.mno) {
    return '이 글은 비공개 설정되어 있습니다.'
  }
  
  if (!props.item.content) return ''
  return props.item.content.length > 100 
    ? props.item.content.substring(0, 100) + '...'
    : props.item.content
})
</script>

<style scoped>
.board-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  height: 100%;
}

.board-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.card-link {
  text-decoration: none;
  color: inherit;
  display: block;
  height: 100%;
}

/* 비공개 카드 스타일 */
.private-card {
  opacity: 0.7;
  cursor: not-allowed;
}

.private-card:hover {
  transform: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-image {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #f8f9fa;
}

.private-image {
  background: linear-gradient(135deg, #95a5a6 0%, #7f8c8d 100%);
}

.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.board-card:hover .thumbnail {
  transform: scale(1.05);
}

.no-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 2rem;
}

.private-overlay {
  background: linear-gradient(135deg, #95a5a6 0%, #7f8c8d 100%) !important;
}

.card-content {
  padding: 1.2rem;
  display: flex;
  flex-direction: column;
  height: calc(100% - 200px);
}

.card-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 0.8rem 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.private-title {
  color: #7f8c8d;
}

.card-summary {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.5;
  margin: 0 0 1rem 0;
  flex-grow: 1;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.private-summary {
  color: #95a5a6;
  font-style: italic;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.8rem;
}

.author-name {
  font-size: 0.85rem;
  font-weight: 500;
  color: #555;
}

.meta-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 0.2rem;
}

.date {
  font-size: 0.8rem;
  color: #999;
}

.views {
  font-size: 0.75rem;
  color: #999;
  display: flex;
  align-items: center;
  gap: 0.3rem;
}

.private-badge {
  font-size: 0.75rem;
  color: #95a5a6;
  display: flex;
  align-items: center;
  gap: 0.3rem;
  background: #ecf0f1;
  padding: 2px 6px;
  border-radius: 4px;
}
</style>
