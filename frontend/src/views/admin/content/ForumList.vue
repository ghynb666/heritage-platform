<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">论坛管理</h2>
      <p class="page-subtitle">查看帖子详情并执行置顶或删除操作。</p>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="标题/作者" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 140px">
            <el-option label="正常" :value="1" />
            <el-option label="待处理" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="authorName" label="作者" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'">{{ row.status === 1 ? '正常' : '待处理' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="置顶" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isTop === 1 ? 'warning' : 'info'">{{ row.isTop === 1 ? '置顶' : '普通' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="commentCount" label="评论数" width="100" />
        <el-table-column prop="createTime" label="发布时间" width="180" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="warning" link size="small" @click="handleTop(row)">
              {{ row.isTop === 1 ? '取消置顶' : '置顶' }}
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
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

    <el-dialog v-model="detailVisible" title="帖子详情" width="820px">
      <el-descriptions v-if="currentItem" :column="2" border>
        <el-descriptions-item label="标题" :span="2">{{ currentItem.title || '-' }}</el-descriptions-item>
        <el-descriptions-item label="作者">{{ currentItem.authorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentItem.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="浏览量">{{ currentItem.viewCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ currentItem.likeCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="评论数">{{ currentItem.commentCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">{{ currentItem.content || '-' }}</el-descriptions-item>
        <el-descriptions-item label="图片" :span="2">
          <div class="image-list">
            <el-image v-for="(item, index) in currentItem.images || []" :key="index" :src="item" style="width: 100px; height: 80px" fit="cover" />
            <span v-if="!(currentItem.images || []).length">-</span>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import { deleteForum, getForumDetail, getForumPage, updateForumTop } from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const currentItem = ref(null)
const searchForm = reactive({ keyword: '', status: null })

const loadData = async () => {
  loading.value = true
  try {
    const res = await getForumPage({ page: page.value, size: size.value, ...searchForm })
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { page.value = 1; loadData() }
const handleReset = () => { searchForm.keyword = ''; searchForm.status = null; page.value = 1; loadData() }

const handleDetail = async (row) => {
  try {
    const res = await getForumDetail(row.id)
    currentItem.value = res.data
    detailVisible.value = true
  } catch {
    ElMessage.error('获取详情失败')
  }
}

const handleTop = async (row) => {
  try {
    await updateForumTop(row.id, row.isTop === 1 ? 0 : 1)
    ElMessage.success('操作成功')
    loadData()
  } catch {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该帖子吗？', '警告', { type: 'warning' })
    await deleteForum(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

onMounted(loadData)
</script>

<style scoped>
.page { padding: 20px; background: #f5f7fa; min-height: calc(100vh - 60px); }
.page-header { margin-bottom: 20px; }
.page-title { margin: 0 0 8px; font-size: 24px; color: #1a1a2e; }
.page-subtitle { margin: 0; color: #6b7280; }
.search-card, .table-card { background: #fff; border-radius: 8px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,.05); }
.search-card { margin-bottom: 20px; }
.search-form { display: flex; flex-wrap: wrap; gap: 10px; }
.pagination-wrapper { margin-top: 20px; display: flex; justify-content: flex-end; }
.image-list { display: flex; gap: 12px; flex-wrap: wrap; }
</style>
