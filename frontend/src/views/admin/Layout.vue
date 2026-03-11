<template>
  <div class="admin-layout">
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="logo">
          <span class="logo-icon">墨</span>
          <span class="logo-text">管理后台</span>
        </div>
      </div>
      
      <nav class="sidebar-nav">
        <div class="nav-section">
          <div class="nav-section-title">数据总览</div>
          <router-link to="/admin/dashboard" class="nav-item" :class="{ active: $route.path === '/admin/dashboard' }">
            <span class="nav-icon">统</span>
            <span class="nav-text">数据统计</span>
          </router-link>
        </div>

        <div class="nav-section">
          <div class="nav-section-title">用户管理</div>
          <router-link to="/admin/user/list" class="nav-item" :class="{ active: $route.path === '/admin/user/list' }">
            <span class="nav-icon">众</span>
            <span class="nav-text">用户列表</span>
          </router-link>
          <router-link to="/admin/inheritor/list" class="nav-item" :class="{ active: $route.path === '/admin/inheritor/list' }">
            <span class="nav-icon">承</span>
            <span class="nav-text">传承人账号</span>
          </router-link>
          <router-link to="/admin/inheritor/audit" class="nav-item" :class="{ active: $route.path === '/admin/inheritor/audit' }">
            <span class="nav-icon">审</span>
            <span class="nav-text">传承人审核</span>
          </router-link>
        </div>
        
        <div class="nav-section">
          <div class="nav-section-title">非遗管理</div>
          <router-link to="/admin/heritage/list" class="nav-item" :class="{ active: $route.path === '/admin/heritage/list' }">
            <span class="nav-icon">遗</span>
            <span class="nav-text">项目列表</span>
          </router-link>
          <router-link to="/admin/heritage/category" class="nav-item" :class="{ active: $route.path === '/admin/heritage/category' }">
            <span class="nav-icon">类</span>
            <span class="nav-text">分类管理</span>
          </router-link>
        </div>
        
        <div class="nav-section">
          <div class="nav-section-title">文创管理</div>
          <router-link to="/admin/product/list" class="nav-item" :class="{ active: $route.path === '/admin/product/list' }">
            <span class="nav-icon">品</span>
            <span class="nav-text">商品列表</span>
          </router-link>
          <router-link to="/admin/product/category" class="nav-item" :class="{ active: $route.path === '/admin/product/category' }">
            <span class="nav-icon">型</span>
            <span class="nav-text">类型管理</span>
          </router-link>
        </div>
        
        <div class="nav-section">
          <div class="nav-section-title">内容管理</div>
          <router-link to="/admin/announcement" class="nav-item" :class="{ active: $route.path === '/admin/announcement' }">
            <span class="nav-icon">公</span>
            <span class="nav-text">公告管理</span>
          </router-link>
          <router-link to="/admin/banner" class="nav-item" :class="{ active: $route.path === '/admin/banner' }">
            <span class="nav-icon">图</span>
            <span class="nav-text">轮播图管理</span>
          </router-link>
          <router-link to="/admin/news" class="nav-item" :class="{ active: $route.path === '/admin/news' }">
            <span class="nav-icon">讯</span>
            <span class="nav-text">资讯管理</span>
          </router-link>
          <router-link to="/admin/forum" class="nav-item" :class="{ active: $route.path === '/admin/forum' }">
            <span class="nav-icon">坛</span>
            <span class="nav-text">论坛管理</span>
          </router-link>
          <router-link to="/admin/comment" class="nav-item" :class="{ active: $route.path === '/admin/comment' }">
            <span class="nav-icon">评</span>
            <span class="nav-text">评论管理</span>
          </router-link>
          <router-link to="/admin/message" class="nav-item" :class="{ active: $route.path === '/admin/message' }">
            <span class="nav-icon">言</span>
            <span class="nav-text">留言管理</span>
          </router-link>
          <router-link to="/admin/sensitive-word" class="nav-item" :class="{ active: $route.path === '/admin/sensitive-word' }">
            <span class="nav-icon">敏</span>
            <span class="nav-text">敏感词管理</span>
          </router-link>
        </div>
      </nav>
    </aside>
    
    <div class="main-area">
      <header class="topbar">
        <div class="topbar-left">
          <div class="breadcrumb">
            <span class="breadcrumb-item">{{ currentPageTitle }}</span>
          </div>
        </div>
        <div class="topbar-right">
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-dropdown">
              <div class="user-avatar">
                <span>{{ userStore.userInfo.nickname?.charAt(0) || '管' }}</span>
              </div>
              <span class="user-name">{{ userStore.userInfo.nickname || '管理员' }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'

const route = useRoute()
const userStore = useUserStore()

const pageTitles = {
  '/admin/dashboard': '数据统计',
  '/admin/user/list': '用户列表',
  '/admin/inheritor/list': '传承人账号',
  '/admin/inheritor/audit': '传承人审核',
  '/admin/heritage/list': '项目列表',
  '/admin/heritage/category': '分类管理',
  '/admin/product/list': '商品列表',
  '/admin/product/category': '类型管理',
  '/admin/announcement': '公告管理',
  '/admin/banner': '轮播图管理',
  '/admin/news': '资讯管理',
  '/admin/forum': '论坛管理',
  '/admin/comment': '评论管理',
  '/admin/message': '留言管理',
  '/admin/sensitive-word': '敏感词管理'
}

const currentPageTitle = computed(() => pageTitles[route.path] || '控制台')

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
  }
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  display: flex;
  background: var(--paper);
}

.sidebar {
  width: 240px;
  background: var(--ink-black);
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 100;
}

.sidebar-header {
  padding: 24px;
  border-bottom: 1px solid rgba(247, 244, 239, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
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

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: var(--paper);
  letter-spacing: 2px;
}

.sidebar-nav {
  flex: 1;
  overflow-y: auto;
  padding: 16px 0;
}

.nav-section {
  margin-bottom: 8px;
}

.nav-section-title {
  padding: 12px 24px 8px;
  font-size: 12px;
  color: var(--paper-darker);
  letter-spacing: 1px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 24px;
  color: var(--paper-dark);
  transition: all 0.3s ease;
  position: relative;
}

.nav-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: var(--vermilion);
  transform: scaleY(0);
  transition: transform 0.3s ease;
}

.nav-item:hover {
  color: var(--paper);
  background: rgba(247, 244, 239, 0.05);
}

.nav-item.active {
  color: var(--paper);
  background: rgba(247, 244, 239, 0.08);
}

.nav-item.active::before {
  transform: scaleY(1);
}

.nav-icon {
  width: 28px;
  height: 28px;
  background: rgba(247, 244, 239, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  transition: all 0.3s ease;
}

.nav-item.active .nav-icon {
  background: var(--vermilion);
}

.nav-text {
  font-size: 14px;
  letter-spacing: 1px;
}

.main-area {
  flex: 1;
  margin-left: 240px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.topbar {
  height: 64px;
  background: #fff;
  border-bottom: 1px solid var(--paper-darker);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  position: sticky;
  top: 0;
  z-index: 50;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
}

.breadcrumb-item {
  font-size: 16px;
  font-weight: 500;
  color: var(--ink-black);
  letter-spacing: 1px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 8px 16px;
  transition: all 0.3s ease;
}

.user-dropdown:hover {
  background: var(--paper);
}

.user-avatar {
  width: 36px;
  height: 36px;
  background: var(--ink-black);
  color: var(--paper);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
}

.user-name {
  font-size: 14px;
  color: var(--ink-black);
}

.content {
  flex: 1;
  padding: 24px 32px;
  background: var(--paper);
}

:deep(.el-dropdown-menu__item) {
  font-size: 14px;
  padding: 10px 20px;
}

:deep(.el-dropdown-menu__item:hover) {
  background: var(--paper);
  color: var(--vermilion);
}
</style>
