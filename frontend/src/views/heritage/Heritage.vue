<template>
  <div class="heritage-container">
    <header class="header">
      <div class="header-inner">
        <div class="logo">
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

    <main class="main-content">
      <div class="page-header">
        <h1 class="page-title">非遗展示</h1>
        <p class="page-subtitle">探索中国非物质文化遗产的璀璨世界</p>
      </div>

      <div class="content-wrapper">
        <aside class="sidebar">
          <div class="category-section">
            <h3 class="section-title">非遗分类</h3>
            <div class="category-list">
              <div 
                class="category-item" 
                :class="{ active: !selectedCategory }"
                @click="selectCategory(null)"
              >
                全部
              </div>
              <div 
                v-for="cat in categories" 
                :key="cat.id"
                class="category-item"
                :class="{ active: selectedCategory === cat.id }"
                @click="selectCategory(cat.id)"
              >
                {{ cat.name }}
              </div>
            </div>
          </div>
        </aside>

        <div class="main-area">
          <div class="toolbar">
            <div class="search-box">
              <el-input
                v-model="keyword"
                placeholder="搜索非遗项目"
                clearable
                @keyup.enter="handleSearch"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
            </div>
            <div class="sort-options">
              <span class="sort-label">排序：</span>
              <el-radio-group v-model="sortBy" size="small" @change="loadItems">
                <el-radio-button label="">最新</el-radio-button>
                <el-radio-button label="viewCount">点击量</el-radio-button>
                <el-radio-button label="likeCount">点赞数</el-radio-button>
                <el-radio-button label="favoriteCount">收藏数</el-radio-button>
              </el-radio-group>
            </div>
          </div>

          <div class="item-grid" v-loading="loading">
            <div 
              v-for="item in items" 
              :key="item.id" 
              class="item-card"
              @click="goToDetail(item.id)"
            >
              <div class="card-image">
                <img v-if="item.coverImage" :src="item.coverImage" alt="">
                <div v-else class="placeholder-image">{{ item.title?.charAt(0) }}</div>
              </div>
              <div class="card-content">
                <h3 class="card-title">{{ item.title }}</h3>
                <p class="card-category">{{ item.heritageCategoryName }}</p>
                <div class="card-meta">
                  <span class="meta-item">
                    <el-icon><View /></el-icon>
                    {{ item.viewCount || 0 }}
                  </span>
                  <span class="meta-item">
                    <el-icon><Star /></el-icon>
                    {{ item.favoriteCount || 0 }}
                  </span>
                  <span class="meta-item">
                    <el-icon><Pointer /></el-icon>
                    {{ item.likeCount || 0 }}
                  </span>
                </div>
                <div class="card-publisher" v-if="item.publisherType === 1">
                  <span class="publisher-label">传承人：</span>
                  <span class="publisher-name">{{ item.publisherName }}</span>
                </div>
              </div>
              <div class="card-action">
                <el-button type="primary" text>详情</el-button>
              </div>
            </div>
          </div>

          <div class="pagination-wrapper" v-if="total > 0">
            <el-pagination
              v-model:current-page="page"
              v-model:page-size="size"
              :total="total"
              :page-sizes="[12, 24, 36, 48]"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="loadItems"
              @current-change="loadItems"
            />
          </div>
        </div>
      </div>
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
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getHeritageCategoryList, getHeritageItemList } from '@/api/heritage'
import { Search, View, Star, Pointer } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const categories = ref([])
const items = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(12)
const total = ref(0)
const selectedCategory = ref(null)
const keyword = ref('')
const sortBy = ref('')

const loadCategories = async () => {
  try {
    const res = await getHeritageCategoryList()
    categories.value = res.data || []
  } catch (e) {
    console.error(e)
  }
}

const loadItems = async () => {
  loading.value = true
  try {
    const res = await getHeritageItemList({
      page: page.value,
      size: size.value,
      categoryId: selectedCategory.value,
      keyword: keyword.value,
      sortBy: sortBy.value
    })
    items.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const selectCategory = (id) => {
  selectedCategory.value = id
  page.value = 1
  loadItems()
}

const handleSearch = () => {
  page.value = 1
  loadItems()
}

const goToDetail = (id) => {
  router.push(`/heritage/${id}`)
}

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
  } else if (command === 'profile') {
    router.push('/profile')
  }
}

onMounted(() => {
  loadCategories()
  loadItems()
})
</script>

<style scoped>
.heritage-container {
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
}

.page-header {
  text-align: center;
  padding: 60px 40px 40px;
  background: linear-gradient(180deg, var(--paper-dark) 0%, var(--paper) 100%);
}

.page-title {
  font-size: 36px;
  font-weight: 600;
  letter-spacing: 4px;
  margin-bottom: 12px;
}

.page-subtitle {
  font-size: 16px;
  color: var(--ink-light);
  letter-spacing: 2px;
}

.content-wrapper {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px;
  display: flex;
  gap: 40px;
}

.sidebar {
  width: 240px;
  flex-shrink: 0;
}

.category-section {
  background: #fff;
  border: 1px solid var(--paper-darker);
  padding: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--paper-darker);
}

.category-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.category-item {
  padding: 12px 16px;
  font-size: 14px;
  color: var(--ink-medium);
  cursor: pointer;
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
}

.category-item:hover {
  background: var(--paper);
  color: var(--ink-black);
}

.category-item.active {
  background: var(--paper);
  color: var(--vermilion);
  border-left-color: var(--vermilion);
}

.main-area {
  flex: 1;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  gap: 20px;
}

.search-box {
  width: 300px;
}

.sort-options {
  display: flex;
  align-items: center;
  gap: 12px;
}

.sort-label {
  font-size: 14px;
  color: var(--ink-medium);
}

.item-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  min-height: 400px;
}

.item-card {
  background: #fff;
  border: 1px solid var(--paper-darker);
  cursor: pointer;
  transition: all 0.3s ease;
}

.item-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px var(--shadow-ink);
}

.card-image {
  height: 200px;
  background: var(--paper-dark);
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
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

.card-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-category {
  font-size: 13px;
  color: var(--vermilion);
  margin-bottom: 12px;
}

.card-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--ink-light);
}

.card-publisher {
  font-size: 13px;
  color: var(--ink-light);
}

.publisher-label {
  color: var(--ink-medium);
}

.card-action {
  padding: 0 20px 20px;
}

.pagination-wrapper {
  margin-top: 40px;
  display: flex;
  justify-content: center;
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

@media (max-width: 1200px) {
  .item-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
  }
  
  .item-grid {
    grid-template-columns: 1fr;
  }
  
  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box {
    width: 100%;
  }
}
</style>
