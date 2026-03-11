<template>
  <div class="home-container">
    <header class="header">
      <div class="header-inner">
        <div class="logo">
          <span class="logo-icon">墨</span>
          <span class="logo-text">非遗传承</span>
        </div>
        <nav class="nav-menu">
          <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">首页</router-link>
          <router-link to="/heritage" class="nav-item">非遗展示</router-link>
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
                  <el-dropdown-item command="inheritor-apply" v-if="!userStore.hasRole('INHERITOR') && !userStore.hasRole('ADMIN')">传承人申请</el-dropdown-item>
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

    <section class="hero">
      <div class="hero-bg">
        <div class="ink-wash"></div>
        <div class="mountain-silhouette"></div>
      </div>
      <div class="hero-content">
        <h1 class="hero-title">
          <span class="title-char" style="animation-delay: 0.1s">传</span>
          <span class="title-char" style="animation-delay: 0.2s">承</span>
          <span class="title-char" style="animation-delay: 0.3s">千</span>
          <span class="title-char" style="animation-delay: 0.4s">年</span>
          <span class="title-char" style="animation-delay: 0.5s">文</span>
          <span class="title-char" style="animation-delay: 0.6s">脉</span>
        </h1>
        <p class="hero-subtitle">探索中国非物质文化遗产的璀璨世界</p>
        <div class="hero-actions">
          <router-link to="/heritage" class="btn-primary">探索非遗</router-link>
        </div>
      </div>
      <div class="scroll-hint">
        <span class="scroll-text">向下探索</span>
        <div class="scroll-line"></div>
      </div>
    </section>

    <main class="main-content">
      <section class="announcement-section">
        <AnnouncementScroll />
      </section>
      
      <section class="section categories">
        <div class="section-header">
          <h2 class="section-title">非遗分类</h2>
          <div class="title-decoration"></div>
        </div>
        <div class="category-grid">
          <div class="category-card" v-for="(cat, index) in categories" :key="index" :style="{ animationDelay: index * 0.1 + 's' }">
            <div class="card-icon">{{ cat.icon }}</div>
            <h3 class="card-title">{{ cat.name }}</h3>
            <p class="card-desc">{{ cat.desc }}</p>
          </div>
        </div>
      </section>

      <section class="section featured">
        <div class="section-header">
          <h2 class="section-title">精选非遗</h2>
          <div class="title-decoration"></div>
        </div>
        <div class="featured-grid">
          <div class="featured-card large">
            <div class="card-image">
              <div class="placeholder-image">景泰蓝</div>
            </div>
            <div class="card-content">
              <span class="card-tag">传统技艺</span>
              <h3 class="card-title">景泰蓝制作技艺</h3>
              <p class="card-desc">国家级非物质文化遗产，始于元代，盛行于明清</p>
            </div>
          </div>
          <div class="featured-card">
            <div class="card-image">
              <div class="placeholder-image">苏绣</div>
            </div>
            <div class="card-content">
              <span class="card-tag">传统美术</span>
              <h3 class="card-title">苏州刺绣</h3>
            </div>
          </div>
          <div class="featured-card">
            <div class="card-image">
              <div class="placeholder-image">剪纸</div>
            </div>
            <div class="card-content">
              <span class="card-tag">传统美术</span>
              <h3 class="card-title">中国剪纸</h3>
            </div>
          </div>
        </div>
      </section>

      <section class="section stats">
        <div class="stats-grid">
          <div class="stat-item">
            <span class="stat-number">1557</span>
            <span class="stat-label">国家级非遗项目</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-number">3610</span>
            <span class="stat-label">代表性传承人</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-number">42</span>
            <span class="stat-label">人类非遗项目</span>
          </div>
        </div>
      </section>
    </main>

    <footer class="footer">
      <div class="footer-content">
        <div class="footer-brand">
          <span class="footer-logo">墨</span>
          <span class="footer-name">非遗传承平台</span>
        </div>
        <p class="footer-copyright">© 2024 非物质文化遗产展示与交流平台 版权所有</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import AnnouncementScroll from '@/components/AnnouncementScroll.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const categories = ref([
  { icon: '瓷', name: '传统技艺', desc: '陶瓷、漆器、金属工艺' },
  { icon: '绣', name: '传统美术', desc: '刺绣、剪纸、年画' },
  { icon: '戏', name: '传统戏剧', desc: '京剧、昆曲、豫剧' },
  { icon: '医', name: '传统医药', desc: '中医、针灸、正骨' },
  { icon: '乐', name: '传统音乐', desc: '古琴、琵琶、二胡' },
  { icon: '舞', name: '传统舞蹈', desc: '秧歌、狮舞、龙舞' }
])

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'inheritor-apply':
      router.push('/inheritor-apply')
      break
    case 'logout':
      userStore.logout()
      break
  }
}
</script>

<style scoped>
.home-container {
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
  transition: transform 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.05);
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

.hero {
  height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, var(--paper) 0%, var(--paper-dark) 100%);
}

.ink-wash {
  position: absolute;
  top: 20%;
  right: 10%;
  width: 600px;
  height: 600px;
  background: radial-gradient(ellipse at center, rgba(26, 26, 26, 0.03) 0%, transparent 70%);
  animation: float 8s ease-in-out infinite;
}

.mountain-silhouette {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 200px;
  background: linear-gradient(180deg, transparent 0%, rgba(26, 26, 26, 0.02) 100%);
}

.hero-content {
  position: relative;
  text-align: center;
  z-index: 1;
}

.hero-title {
  font-size: 72px;
  font-weight: 700;
  letter-spacing: 20px;
  margin-bottom: 24px;
}

.title-char {
  display: inline-block;
  opacity: 0;
  animation: charReveal 0.8s ease forwards;
}

@keyframes charReveal {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-subtitle {
  font-size: 18px;
  color: var(--ink-light);
  letter-spacing: 4px;
  margin-bottom: 48px;
}

.hero-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.btn-primary {
  padding: 16px 48px;
  font-size: 16px;
  background: var(--ink-black);
  color: var(--paper);
  letter-spacing: 2px;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background: var(--vermilion);
  color: #fff;
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(199, 62, 58, 0.3);
}

.scroll-hint {
  position: absolute;
  bottom: 40px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.scroll-text {
  font-size: 12px;
  color: var(--ink-light);
  letter-spacing: 2px;
}

.scroll-line {
  width: 1px;
  height: 60px;
  background: linear-gradient(180deg, var(--ink-light) 0%, transparent 100%);
  animation: scrollPulse 2s ease-in-out infinite;
}

@keyframes scrollPulse {
  0%, 100% { opacity: 0.3; height: 60px; }
  50% { opacity: 1; height: 80px; }
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 80px 40px;
}

.announcement-section {
  margin-bottom: 40px;
}

.section {
  margin-bottom: 100px;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-title {
  font-size: 32px;
  font-weight: 600;
  letter-spacing: 4px;
  margin-bottom: 16px;
}

.title-decoration {
  width: 60px;
  height: 3px;
  background: var(--vermilion);
  margin: 0 auto;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 24px;
}

.category-card {
  background: #fff;
  padding: 32px 20px;
  text-align: center;
  border: 1px solid var(--paper-darker);
  transition: all 0.4s ease;
  opacity: 0;
  animation: cardFadeIn 0.6s ease forwards;
}

@keyframes cardFadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.category-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px var(--shadow-ink);
  border-color: var(--vermilion);
}

.card-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 16px;
  background: var(--paper);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: var(--vermilion);
  transition: all 0.3s ease;
}

.category-card:hover .card-icon {
  background: var(--vermilion);
  color: #fff;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.card-desc {
  font-size: 13px;
  color: var(--ink-light);
}

.featured-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: repeat(2, 240px);
  gap: 24px;
}

.featured-card {
  background: #fff;
  border: 1px solid var(--paper-darker);
  overflow: hidden;
  transition: all 0.4s ease;
}

.featured-card.large {
  grid-row: span 2;
}

.featured-card:hover {
  box-shadow: 0 20px 40px var(--shadow-ink);
}

.card-image {
  height: 60%;
  background: var(--paper-dark);
  position: relative;
  overflow: hidden;
}

.large .card-image {
  height: 70%;
}

.placeholder-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
  color: var(--paper-darker);
  background: linear-gradient(135deg, var(--paper-dark) 0%, var(--paper-darker) 100%);
}

.card-content {
  padding: 20px;
}

.card-tag {
  display: inline-block;
  padding: 4px 12px;
  font-size: 12px;
  background: var(--paper);
  color: var(--vermilion);
  margin-bottom: 12px;
}

.stats {
  background: var(--ink-black);
  padding: 60px 40px;
  margin: 0 -40px;
}

.stats-grid {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 60px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 48px;
  font-weight: 700;
  color: var(--paper);
  letter-spacing: 2px;
}

.stat-label {
  font-size: 14px;
  color: var(--paper-darker);
  letter-spacing: 2px;
}

.stat-divider {
  width: 1px;
  height: 60px;
  background: var(--ink-medium);
}

.footer {
  background: var(--ink-black);
  padding: 40px;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  text-align: center;
}

.footer-brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 16px;
}

.footer-logo {
  width: 36px;
  height: 36px;
  background: var(--vermilion);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 600;
}

.footer-name {
  font-size: 18px;
  color: var(--paper);
  letter-spacing: 2px;
}

.footer-copyright {
  font-size: 13px;
  color: var(--ink-light);
}

@media (max-width: 1200px) {
  .category-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .header-inner {
    padding: 0 20px;
  }
  
  .nav-menu {
    display: none;
  }
  
  .hero-title {
    font-size: 48px;
    letter-spacing: 10px;
  }
  
  .category-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .featured-grid {
    grid-template-columns: 1fr;
  }
  
  .featured-card.large {
    grid-row: span 1;
  }
  
  .stats-grid {
    flex-direction: column;
    gap: 30px;
  }
  
  .stat-divider {
    width: 60px;
    height: 1px;
  }
}
</style>
