<template>
  <div class="heritage-detail-container">
    <header class="header">
      <div class="header-inner">
        <div class="logo" @click="router.push('/')">
          <span class="logo-icon">墨</span>
          <span class="logo-text">非遗传承</span>
        </div>
        <nav class="nav-menu">
          <router-link to="/" class="nav-item">首页</router-link>
          <router-link to="/heritage" class="nav-item active">非遗展示</router-link>
          <router-link to="/product" class="nav-item">文创商品</router-link>
          <router-link to="/news" class="nav-item">资讯</router-link>
          <router-link to="/forum" class="nav-item">论坛</router-link>
          <router-link to="/message" class="nav-item">留言板</router-link>
        </nav>
        <div class="user-area">
          <template v-if="userStore.token">
            <el-dropdown @command="handleCommand" trigger="click">
              <div class="user-avatar">
                <span class="avatar-text">{{ userStore.userInfo.nickname?.charAt(0) || '用' }}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="btn-login">登录</router-link>
            <router-link to="/register" class="btn-register">注册</router-link>
          </template>
        </div>
      </div>
    </header>

    <main class="main-content" v-loading="loading">
      <div class="detail-wrapper" v-if="item">
        <div class="detail-header">
          <div class="breadcrumb">
            <router-link to="/heritage">非遗展示</router-link>
            <span class="separator">/</span>
            <span class="current">{{ item.title }}</span>
          </div>
          <h1 class="detail-title">{{ item.title }}</h1>
          <div class="detail-meta">
            <span class="meta-item">
              <el-icon><FolderOpened /></el-icon>
              {{ item.heritageCategoryName }}
            </span>
            <span class="meta-item">
              <el-icon><View /></el-icon>
              {{ item.viewCount }} 次浏览
            </span>
            <span class="meta-item">
              <el-icon><Calendar /></el-icon>
              {{ formatDate(item.createTime) }}
            </span>
          </div>
        </div>

        <div class="detail-body">
          <div class="content-section">
            <h2 class="section-title">历史渊源</h2>
            <div class="section-content" v-html="item.historyOrigin"></div>
          </div>

          <div class="content-section">
            <h2 class="section-title">技艺特点</h2>
            <div class="section-content" v-html="item.craftFeature"></div>
          </div>

          <div class="content-section" v-if="item.content">
            <h2 class="section-title">详细介绍</h2>
            <div class="section-content" v-html="item.content"></div>
          </div>

          <div class="content-section" v-if="item.images && item.images.length > 0">
            <h2 class="section-title">高清大图</h2>
            <div class="image-gallery">
              <el-image
                v-for="img in item.images"
                :key="img.id"
                :src="img.imageUrl"
                :preview-src-list="item.images.map(i => i.imageUrl)"
                :initial-index="item.images.findIndex(i => i.id === img.id)"
                fit="cover"
                class="gallery-image"
              />
            </div>
          </div>

          <div class="content-section" v-if="item.videos && item.videos.length > 0">
            <h2 class="section-title">制作视频</h2>
            <div class="video-list">
              <div v-for="video in item.videos" :key="video.id" class="video-item">
                <video 
                  :src="video.videoUrl" 
                  controls 
                  class="video-player"
                  :poster="video.coverUrl"
                />
                <p class="video-title" v-if="video.title">{{ video.title }}</p>
              </div>
            </div>
          </div>
        </div>

        <div class="detail-sidebar">
          <div class="publisher-card" v-if="item.publisherType === 1">
            <h3 class="card-title">发布者</h3>
            <div class="publisher-info" @click="goToInheritor(item.publisherId)">
              <div class="publisher-avatar">
                <img v-if="item.publisherAvatar" :src="item.publisherAvatar" alt="">
                <span v-else>{{ item.publisherName?.charAt(0) }}</span>
              </div>
              <div class="publisher-detail">
                <p class="publisher-name">{{ item.publisherName }}</p>
                <p class="publisher-role">传承人</p>
              </div>
            </div>
          </div>

          <div class="action-card">
            <h3 class="card-title">互动</h3>
            <div class="action-buttons">
              <el-button :type="isLiked ? 'danger' : 'default'" @click="handleLike">
                <el-icon><Pointer /></el-icon>
                点赞 ({{ item.likeCount }})
              </el-button>
              <el-button :type="isFavorited ? 'warning' : 'default'" @click="handleFavorite">
                <el-icon><Star /></el-icon>
                收藏 ({{ item.favoriteCount }})
              </el-button>
            </div>
          </div>

          <div class="stats-card">
            <h3 class="card-title">数据统计</h3>
            <div class="stats-list">
              <div class="stat-item">
                <span class="stat-value">{{ item.viewCount }}</span>
                <span class="stat-label">浏览量</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ item.likeCount }}</span>
                <span class="stat-label">点赞数</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ item.favoriteCount }}</span>
                <span class="stat-label">收藏数</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ item.commentCount || 0 }}</span>
                <span class="stat-label">评论数</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-else-if="!loading" description="内容不存在" />
    </main>

    <footer class="footer">
      <div class="footer-content">
        <p class="footer-copyright">© 2024 非物质文化遗产展示与交流平台 版权所有</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getHeritageItemDetail } from '@/api/heritage'
import { FolderOpened, View, Calendar, Pointer, Star } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const item = ref(null)
const loading = ref(false)
const isLiked = ref(false)
const isFavorited = ref(false)

const loadDetail = async () => {
  loading.value = true
  try {
    const res = await getHeritageItemDetail(route.params.id)
    item.value = res.data
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN')
}

const goToInheritor = (id) => {
  router.push(`/inheritor/${id}`)
}

const handleLike = () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    return
  }
  isLiked.value = !isLiked.value
  item.value.likeCount += isLiked.value ? 1 : -1
  ElMessage.success(isLiked.value ? '点赞成功' : '已取消点赞')
}

const handleFavorite = () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    return
  }
  isFavorited.value = !isFavorited.value
  item.value.favoriteCount += isFavorited.value ? 1 : -1
  ElMessage.success(isFavorited.value ? '收藏成功' : '已取消收藏')
}

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
  } else if (command === 'profile') {
    router.push('/profile')
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped>
.heritage-detail-container {
  min-height: 100vh;
  background: var(--paper);
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  background: rgba(247, 244, 239, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--paper-darker);
}

.header-inner {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 40px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: var(--ink-black);
  color: var(--paper);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 600;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: var(--ink-black);
  letter-spacing: 2px;
}

.nav-menu {
  display: flex;
  gap: 8px;
}

.nav-item {
  padding: 10px 20px;
  font-size: 15px;
  color: var(--ink-medium);
  position: relative;
  transition: color 0.3s ease;
}

.nav-item::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: var(--vermilion);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-item:hover,
.nav-item.active {
  color: var(--ink-black);
}

.nav-item:hover::after,
.nav-item.active::after {
  width: 24px;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  background: var(--ink-black);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.avatar-text {
  color: var(--paper);
  font-size: 16px;
  font-weight: 500;
}

.btn-login {
  padding: 10px 24px;
  font-size: 14px;
  color: var(--ink-black);
  border: 1px solid var(--ink-black);
  transition: all 0.3s ease;
}

.btn-login:hover {
  background: var(--ink-black);
  color: var(--paper);
}

.btn-register {
  padding: 10px 24px;
  font-size: 14px;
  background: var(--vermilion);
  color: #fff;
  transition: all 0.3s ease;
}

.btn-register:hover {
  background: var(--vermilion-light);
}

.main-content {
  padding-top: 70px;
  min-height: calc(100vh - 70px);
}

.detail-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px;
  display: flex;
  gap: 40px;
}

.detail-header {
  margin-bottom: 40px;
}

.breadcrumb {
  font-size: 14px;
  color: var(--ink-light);
  margin-bottom: 16px;
}

.breadcrumb a {
  color: var(--ink-medium);
}

.breadcrumb a:hover {
  color: var(--vermilion);
}

.separator {
  margin: 0 8px;
}

.current {
  color: var(--ink-black);
}

.detail-title {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 16px;
  letter-spacing: 2px;
}

.detail-meta {
  display: flex;
  gap: 24px;
  font-size: 14px;
  color: var(--ink-light);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.detail-body {
  flex: 1;
}

.content-section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid var(--vermilion);
  display: inline-block;
}

.section-content {
  font-size: 15px;
  line-height: 1.8;
  color: var(--ink-medium);
}

.image-gallery {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.gallery-image {
  width: 100%;
  height: 200px;
  cursor: pointer;
  border: 1px solid var(--paper-darker);
}

.video-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.video-item {
  background: #fff;
  border: 1px solid var(--paper-darker);
  padding: 16px;
}

.video-player {
  width: 100%;
  max-height: 400px;
}

.video-title {
  margin-top: 12px;
  font-size: 14px;
  color: var(--ink-medium);
}

.detail-sidebar {
  width: 300px;
  flex-shrink: 0;
}

.publisher-card,
.action-card,
.stats-card {
  background: #fff;
  border: 1px solid var(--paper-darker);
  padding: 20px;
  margin-bottom: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--paper-darker);
}

.publisher-info {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.publisher-avatar {
  width: 50px;
  height: 50px;
  background: var(--ink-black);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--paper);
  font-size: 20px;
  overflow: hidden;
}

.publisher-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.publisher-name {
  font-size: 15px;
  font-weight: 500;
}

.publisher-role {
  font-size: 13px;
  color: var(--vermilion);
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.action-buttons .el-button {
  width: 100%;
}

.stats-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 24px;
  font-weight: 600;
  color: var(--ink-black);
}

.stat-label {
  font-size: 13px;
  color: var(--ink-light);
}

.footer {
  background: var(--ink-black);
  padding: 40px;
  margin-top: 60px;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  text-align: center;
}

.footer-copyright {
  font-size: 13px;
  color: var(--ink-light);
}

@media (max-width: 768px) {
  .detail-wrapper {
    flex-direction: column;
  }
  
  .detail-sidebar {
    width: 100%;
  }
  
  .image-gallery {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
