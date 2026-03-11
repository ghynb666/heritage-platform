<template>
  <div class="home-container">
    <el-header class="header">
      <div class="logo">非遗平台</div>
      <el-menu mode="horizontal" :default-active="activeMenu" class="nav-menu" router>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/heritage">非遗展示</el-menu-item>
        <el-menu-item index="/product">文创商品</el-menu-item>
        <el-menu-item index="/news">资讯</el-menu-item>
        <el-menu-item index="/forum">论坛</el-menu-item>
        <el-menu-item index="/message">留言板</el-menu-item>
      </el-menu>
      <div class="user-info">
        <template v-if="userStore.token">
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              <el-avatar :size="32" :src="userStore.userInfo.avatar">{{ userStore.userInfo.nickname?.charAt(0) }}</el-avatar>
              <span class="username">{{ userStore.userInfo.nickname }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="inheritor-apply" v-if="!userStore.hasRole('INHERITOR') && !userStore.hasRole('ADMIN')">传承人申请</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </el-header>
    
    <el-main class="main">
      <div class="announcement-bar" v-if="announcements.length">
        <el-carousel height="40px" direction="vertical" :autoplay="true" indicator-position="none">
          <el-carousel-item v-for="item in announcements" :key="item.id">
            <div class="announcement-item">{{ item.title }}</div>
          </el-carousel-item>
        </el-carousel>
      </div>
      
      <div class="content-section">
        <h3>推荐非遗项目</h3>
        <p class="placeholder-text">非遗项目展示区域（P1阶段实现）</p>
      </div>
      
      <div class="content-section">
        <h3>热门文创商品</h3>
        <p class="placeholder-text">文创商品展示区域（P2阶段实现）</p>
      </div>
      
      <div class="content-section">
        <h3>最新资讯</h3>
        <p class="placeholder-text">资讯展示区域（P3阶段实现）</p>
      </div>
    </el-main>
    
    <el-footer class="footer">
      <p>© 2024 非物质文化遗产展示与交流平台 版权所有</p>
    </el-footer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const announcements = ref([])

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
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  align-items: center;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  margin-right: 40px;
}

.nav-menu {
  flex: 1;
  border-bottom: none;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.username {
  font-size: 14px;
}

.main {
  flex: 1;
  background: #f5f7fa;
  padding: 20px;
}

.announcement-bar {
  background: #fff;
  padding: 10px 20px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.announcement-item {
  line-height: 40px;
  color: #409eff;
}

.content-section {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.content-section h3 {
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.placeholder-text {
  color: #999;
  text-align: center;
  padding: 40px 0;
}

.footer {
  background: #333;
  color: #fff;
  text-align: center;
  line-height: 60px;
}
</style>
