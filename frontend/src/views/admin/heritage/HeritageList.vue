<template>
  <div class="heritage-list-page">
    <div class="page-header">
      <h2 class="page-title">非遗项目管理</h2>
      <p class="page-subtitle">管理非遗项目信息，审核、推荐、上下架</p>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="待审核" :value="0" />
            <el-option label="已上架" :value="1" />
            <el-option label="已下架" :value="2" />
            <el-option label="审核拒绝" :value="3" />
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
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image :src="row.coverImage" style="width: 60px; height: 40px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="heritageCategoryName" label="分类" width="100" />
        <el-table-column prop="publisherName" label="发布者" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isRecommend" label="推荐" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isRecommend === 1 ? 'warning' : 'info'" size="small">
              {{ row.isRecommend === 1 ? '推荐' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="80" />
        <el-table-column prop="createTime" label="发布时间" width="170" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.status === 0" type="success" link size="small" @click="handleAudit(row, 1)">通过</el-button>
            <el-button v-if="row.status === 0" type="danger" link size="small" @click="handleAudit(row, 3)">拒绝</el-button>
            <el-button v-if="row.status === 1" type="warning" link size="small" @click="handleStatus(row, 2)">下架</el-button>
            <el-button v-if="row.status === 2" type="success" link size="small" @click="handleStatus(row, 1)">上架</el-button>
            <el-button :type="row.isRecommend === 1 ? 'info' : 'warning'" link size="small" @click="handleRecommend(row)">
              {{ row.isRecommend === 1 ? '取消推荐' : '推荐' }}
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

    <el-dialog v-model="detailVisible" title="项目详情" width="800px">
      <el-descriptions :column="2" border v-if="currentItem">
        <el-descriptions-item label="封面" :span="2">
          <el-image :src="currentItem.coverImage" style="max-width: 300px; max-height: 200px" fit="contain" />
        </el-descriptions-item>
        <el-descriptions-item label="标题" :span="2">{{ currentItem.title }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ currentItem.heritageCategoryName }}</el-descriptions-item>
        <el-descriptions-item label="发布者">{{ currentItem.publisherName }}</el-descriptions-item>
        <el-descriptions-item label="历史渊源" :span="2">{{ currentItem.historyOrigin || '-' }}</el-descriptions-item>
        <el-descriptions-item label="工艺特色" :span="2">{{ currentItem.craftFeature || '-' }}</el-descriptions-item>
        <el-descriptions-item label="详细内容" :span="2">
          <div v-html="currentItem.content" class="content-box"></div>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentItem.status)">{{ getStatusText(currentItem.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="推荐">
          <el-tag :type="currentItem.isRecommend === 1 ? 'warning' : 'info'">
            {{ currentItem.isRecommend === 1 ? '推荐' : '普通' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="浏览量">{{ currentItem.viewCount }}</el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ currentItem.likeCount }}</el-descriptions-item>
        <el-descriptions-item label="收藏数">{{ currentItem.favoriteCount }}</el-descriptions-item>
        <el-descriptions-item label="评论数">{{ currentItem.commentCount }}</el-descriptions-item>
        <el-descriptions-item label="发布时间" :span="2">{{ currentItem.createTime }}</el-descriptions-item>
        <el-descriptions-item label="图片" :span="2" v-if="currentItem.images?.length">
          <div class="image-list">
            <el-image v-for="img in currentItem.images" :key="img.id" :src="img.imageUrl" style="width: 100px; height: 80px; margin-right: 10px" fit="cover" :preview-src-list="currentItem.images.map(i => i.imageUrl)" />
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="视频" :span="2" v-if="currentItem.videos?.length">
          <div class="video-list">
            <div v-for="video in currentItem.videos" :key="video.id" class="video-item">
              <video :src="video.videoUrl" controls style="max-width: 300px; max-height: 200px"></video>
            </div>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="rejectVisible" title="审核拒绝" width="400px">
      <el-form>
        <el-form-item label="拒绝原因">
          <el-input v-model="rejectReason" type="textarea" :rows="3" placeholder="请输入拒绝原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmReject">确认拒绝</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getHeritagePage, getHeritageDetail, updateHeritageStatus, auditHeritage, updateHeritageRecommend, deleteHeritage } from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const currentItem = ref(null)
const rejectVisible = ref(false)
const rejectReason = ref('')
const pendingRejectId = ref(null)

const searchForm = reactive({
  status: null
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getHeritagePage({
      page: page.value,
      size: size.value,
      status: searchForm.status
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
  searchForm.status = null
  page.value = 1
  loadData()
}

const handleDetail = async (row) => {
  try {
    const res = await getHeritageDetail(row.id)
    currentItem.value = res.data
    detailVisible.value = true
  } catch (e) {
    ElMessage.error('获取详情失败')
  }
}

const handleAudit = async (row, status) => {
  if (status === 3) {
    pendingRejectId.value = row.id
    rejectReason.value = ''
    rejectVisible.value = true
    return
  }
  try {
    await ElMessageBox.confirm('确定审核通过该非遗项目吗？', '提示', { type: 'warning' })
    await auditHeritage(row.id, status, '')
    ElMessage.success('审核通过')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const confirmReject = async () => {
  try {
    await auditHeritage(pendingRejectId.value, 3, rejectReason.value)
    ElMessage.success('已拒绝')
    rejectVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleStatus = async (row, status) => {
  const action = status === 2 ? '下架' : '上架'
  try {
    await ElMessageBox.confirm(`确定要${action}该非遗项目吗？`, '提示', { type: 'warning' })
    await updateHeritageStatus(row.id, status)
    ElMessage.success(`${action}成功`)
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(`${action}失败`)
    }
  }
}

const handleRecommend = async (row) => {
  const newRecommend = row.isRecommend === 1 ? 0 : 1
  const action = newRecommend === 1 ? '推荐' : '取消推荐'
  try {
    await ElMessageBox.confirm(`确定要${action}该非遗项目吗？`, '提示', { type: 'warning' })
    await updateHeritageRecommend(row.id, newRecommend)
    ElMessage.success(`${action}成功`)
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(`${action}失败`)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该非遗项目吗？此操作不可恢复！', '警告', { type: 'error' })
    await deleteHeritage(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const getStatusType = (status) => {
  const types = { 0: 'warning', 1: 'success', 2: 'info', 3: 'danger' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待审核', 1: '已上架', 2: '已下架', 3: '审核拒绝' }
  return texts[status] || '未知'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.heritage-list-page {
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

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.content-box {
  max-height: 200px;
  overflow-y: auto;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
}

.video-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>
