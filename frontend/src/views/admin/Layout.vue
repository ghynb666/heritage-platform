<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="220px" class="aside">
        <div class="logo">非遗管理后台</div>
        <el-menu
          :default-active="activeMenu"
          class="side-menu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-sub-menu index="user">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin/user/list">用户列表</el-menu-item>
            <el-menu-item index="/admin/inheritor/list">传承人账号</el-menu-item>
            <el-menu-item index="/admin/inheritor/audit">传承人审核</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="heritage">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>非遗项目管理</span>
            </template>
            <el-menu-item index="/admin/heritage/list">项目列表</el-menu-item>
            <el-menu-item index="/admin/heritage/category">分类管理</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="product">
            <template #title>
              <el-icon><ShoppingCart /></el-icon>
              <span>文创管理</span>
            </template>
            <el-menu-item index="/admin/product/list">商品列表</el-menu-item>
            <el-menu-item index="/admin/product/category">类型管理</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="content">
            <template #title>
              <el-icon><Edit /></el-icon>
              <span>资讯管理</span>
            </template>
            <el-menu-item index="/admin/announcement">公告管理</el-menu-item>
            <el-menu-item index="/admin/banner">轮播图管理</el-menu-item>
            <el-menu-item index="/admin/news">资讯管理</el-menu-item>
            <el-menu-item index="/admin/forum">论坛管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header class="header">
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :size="32">{{ userStore.userInfo.nickname?.charAt(0) }}</el-avatar>
                <span class="username">{{ userStore.userInfo.nickname }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'

const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.aside {
  background: #304156;
  overflow-y: auto;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background: #263445;
}

.side-menu {
  border-right: none;
}

.header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.username {
  font-size: 14px;
}

.main {
  background: #f0f2f5;
  padding: 20px;
}
</style>
