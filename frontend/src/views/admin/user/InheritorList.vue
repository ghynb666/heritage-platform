<template>
  <div class="inheritor-list-page">
    <div class="page-header">
      <h2 class="page-title">传承人账号</h2>
      <p class="page-subtitle">管理传承人账号信息与非遗类型</p>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="用户名/昵称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="非遗分类">
          <el-select v-model="searchForm.heritageCategoryId" placeholder="全部" clearable style="width: 160px">
            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="传承人信息" min-width="200">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="40" :src="row.avatar" class="user-avatar">
                {{ row.nickname?.charAt(0) || row.username?.charAt(0) }}
              </el-avatar>
              <div class="user-detail">
                <div class="user-name">{{ row.nickname || row.username }}</div>
                <div class="user-account">@{{ row.username }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="heritageCategoryName" label="非遗分类" width="120">
          <template #default="{ row }">
            <el-tag type="warning" v-if="row.heritageCategoryName">{{ row.heritageCategoryName }}</el-tag>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="createTime" label="注册时间" width="170" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="warning" link size="small" @click="handleEditCategory(row)">修改类型</el-button>
            <el-button type="danger" link size="small" @click="handleRevoke(row)">取消资格</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>

    <el-dialog v-model="detailVisible" title="传承人详情" width="600px">
      <el-descriptions :column="2" border v-if="currentUser">
        <el-descriptions-item label="头像" :span="2">
          <el-avatar :size="60" :src="currentUser.avatar">
            {{ currentUser.nickname?.charAt(0) || currentUser.username?.charAt(0) }}
          </el-avatar>
        </el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ currentUser.nickname || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentUser.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentUser.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="非遗分类" :span="2">
          <el-tag type="warning" v-if="currentUser.heritageCategoryName">{{ currentUser.heritageCategoryName }}</el-tag>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="地区" :span="2">
          {{ [currentUser.province, currentUser.city, currentUser.area].filter(Boolean).join(' / ') || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ currentUser.createTime }}</el-descriptions-item>
        <el-descriptions-item label="最后登录">{{ currentUser.lastLoginTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="个性签名" :span="2">{{ currentUser.signature || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="categoryVisible" title="修改非遗类型" width="400px">
      <el-form label-width="100px">
        <el-form-item label="传承人">
          <span>{{ currentUser?.nickname || currentUser?.username }}</span>
        </el-form-item>
        <el-form-item label="非遗分类">
          <el-select v-model="newCategoryId" placeholder="请选择非遗分类" style="width: 100%">
            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="categoryVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCategory">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getInheritorPage, getInheritorDetail, updateInheritorCategory, revokeInheritor } from '@/api/admin'
import { getCategoryList } from '@/api/heritage'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const categoryVisible = ref(false)
const currentUser = ref(null)
const newCategoryId = ref(null)
const categoryList = ref([])

const searchForm = reactive({
  keyword: '',
  heritageCategoryId: null
})

const loadCategoryList = async () => {
  try {
    const res = await getCategoryList()
    categoryList.value = res.data || []
  } catch (e) {
    categoryList.value = []
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getInheritorPage({
      page: page.value,
      size: size.value,
      keyword: searchForm.keyword || undefined,
      heritageCategoryId: searchForm.heritageCategoryId
    })
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const handleReset = () => {
  searchForm.keyword = ''
  searchForm.heritageCategoryId = null
  page.value = 1
  loadData()
}

const handleDetail = async (row) => {
  try {
    const res = await getInheritorDetail(row.id)
    currentUser.value = res.data
    detailVisible.value = true
  } catch (e) {
    ElMessage.error('获取传承人详情失败')
  }
}

const handleEditCategory = (row) => {
  currentUser.value = row
  newCategoryId.value = row.heritageCategoryId
  categoryVisible.value = true
}

const submitCategory = async () => {
  if (!newCategoryId.value) {
    ElMessage.warning('请选择非遗分类')
    return
  }
  try {
    await updateInheritorCategory(currentUser.value.id, newCategoryId.value)
    ElMessage.success('修改成功')
    categoryVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('修改失败')
  }
}

const handleRevoke = async (row) => {
  try {
    await ElMessageBox.confirm('确定要取消该用户的传承人资格吗？取消后该用户将变为普通用户。', '警告', { type: 'warning' })
    await revokeInheritor(row.id)
    ElMessage.success('已取消传承人资格')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

onMounted(() => {
  loadCategoryList()
  loadData()
})
</script>

<style scoped>
.inheritor-list-page {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1a1a2e;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.search-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.table-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  flex-shrink: 0;
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: #fff;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-weight: 500;
  color: #1a1a2e;
}

.user-account {
  font-size: 12px;
  color: #9ca3af;
}

.text-muted {
  color: #9ca3af;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
