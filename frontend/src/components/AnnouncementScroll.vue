<template>
  <div class="announcement-scroll" v-if="announcements.length">
    <div class="announcement-seal">
      <span class="seal-text">公告</span>
    </div>
    <div class="announcement-track">
      <el-carousel 
        height="44px" 
        direction="vertical" 
        :autoplay="true" 
        :interval="4000"
        indicator-position="none"
      >
        <el-carousel-item v-for="(item, index) in announcements" :key="item.id">
          <div class="announcement-item" @click="handleClick(item)">
            <span class="announcement-index">{{ String(index + 1).padStart(2, '0') }}</span>
            <span class="announcement-title">{{ item.title }}</span>
            <span class="announcement-date">{{ formatDate(item.createTime) }}</span>
            <span class="announcement-arrow">→</span>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    <div class="announcement-decoration">
      <svg viewBox="0 0 60 44" fill="none">
        <path d="M0 22 Q15 10, 30 22 T60 22" stroke="currentColor" stroke-width="1" fill="none" opacity="0.3"/>
        <path d="M0 26 Q15 14, 30 26 T60 26" stroke="currentColor" stroke-width="1" fill="none" opacity="0.2"/>
      </svg>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const announcements = ref([])

const formatDate = (date) => {
  if (!date) return ''
  return date.substring(0, 10)
}

const handleClick = (item) => {
  if (item.content) {
    ElMessage({
      message: item.content,
      type: 'info',
      customClass: 'heritage-message'
    })
  }
}

const loadAnnouncements = async () => {
  try {
    const res = await request({
      url: '/api/announcement/list',
      method: 'get',
      params: { status: 1, size: 5 }
    })
    announcements.value = res.data?.records || []
  } catch (e) {
    announcements.value = []
  }
}

onMounted(() => {
  loadAnnouncements()
})
</script>

<style scoped>
.announcement-scroll {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #fdfbf7 0%, #f8f4eb 50%, #fdfbf7 100%);
  border: 1px solid var(--paper-darker, #e8e0d0);
  border-radius: 4px;
  padding: 0 4px 0 0;
  position: relative;
  overflow: hidden;
  box-shadow: 
    inset 0 1px 0 rgba(255, 255, 255, 0.8),
    0 2px 8px rgba(139, 90, 43, 0.08);
}

.announcement-scroll::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noise'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.8' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noise)'/%3E%3C/svg%3E");
  opacity: 0.03;
  pointer-events: none;
}

.announcement-seal {
  width: 56px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  flex-shrink: 0;
  background: linear-gradient(180deg, 
    rgba(198, 56, 50, 0.08) 0%, 
    rgba(198, 56, 50, 0.03) 100%
  );
  border-right: 1px dashed rgba(198, 56, 50, 0.2);
}

.seal-text {
  font-family: 'Noto Serif SC', serif;
  font-size: 15px;
  font-weight: 700;
  color: var(--vermilion, #c63832);
  letter-spacing: 2px;
  text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
  position: relative;
}

.seal-text::before {
  content: '';
  position: absolute;
  top: -4px;
  left: -6px;
  right: -6px;
  bottom: -4px;
  border: 1.5px solid var(--vermilion, #c63832);
  border-radius: 2px;
  opacity: 0.4;
}

.seal-text::after {
  content: '';
  position: absolute;
  top: -2px;
  left: -4px;
  right: -4px;
  bottom: -2px;
  border: 0.5px solid var(--vermilion, #c63832);
  border-radius: 1px;
  opacity: 0.25;
}

.announcement-track {
  flex: 1;
  overflow: hidden;
  padding: 0 16px;
}

.announcement-item {
  display: flex;
  align-items: center;
  height: 44px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
}

.announcement-item::after {
  content: '';
  position: absolute;
  bottom: 8px;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, 
    transparent 0%, 
    var(--paper-darker, #e8e0d0) 20%, 
    var(--paper-darker, #e8e0d0) 80%, 
    transparent 100%
  );
  opacity: 0;
  transition: opacity 0.3s ease;
}

.announcement-item:hover::after {
  opacity: 1;
}

.announcement-index {
  font-family: 'Noto Serif SC', serif;
  font-size: 11px;
  color: var(--vermilion, #c63832);
  opacity: 0.6;
  margin-right: 12px;
  min-width: 18px;
}

.announcement-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 14px;
  color: var(--ink-black, #2c2c2c);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
  transition: color 0.3s ease;
  letter-spacing: 0.5px;
}

.announcement-item:hover .announcement-title {
  color: var(--vermilion, #c63832);
}

.announcement-date {
  font-family: 'Noto Serif SC', serif;
  font-size: 12px;
  color: var(--ink-light, #8b7355);
  margin-left: 16px;
  flex-shrink: 0;
  opacity: 0.8;
}

.announcement-arrow {
  font-size: 14px;
  color: var(--vermilion, #c63832);
  margin-left: 12px;
  opacity: 0;
  transform: translateX(-8px);
  transition: all 0.3s ease;
}

.announcement-item:hover .announcement-arrow {
  opacity: 0.8;
  transform: translateX(0);
}

.announcement-decoration {
  width: 60px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: var(--ink-light, #8b7355);
  opacity: 0.5;
}

.announcement-decoration svg {
  width: 50px;
  height: 30px;
}

:deep(.el-carousel__container) {
  height: 44px !important;
}

:deep(.el-carousel__item) {
  display: flex;
  align-items: center;
}
</style>
