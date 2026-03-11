<template>
  <div class="heritage-list-page">
    <div class="page-header">
      <h2 class="page-title">项目列表</h2>
      <p class="page-subtitle">管理非遗项目，支持查看、修改、审核、上下架、推荐和删除。</p>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 140px">
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
            <el-image
              v-if="row.coverImage"
              :src="row.coverImage"
              style="width: 60px; height: 40px"
              fit="cover"
            />
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="heritageCategoryName" label="分类" width="120" />
        <el-table-column prop="publisherName" label="发布者" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="推荐" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isRecommend === 1 ? 'warning' : 'info'" size="small">
              {{ row.isRecommend === 1 ? '推荐' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="90" />
        <el-table-column prop="createTime" label="发布时间" width="180" />
        <el-table-column label="操作" width="340" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">修改</el-button>
            <el-button v-if="row.status === 0" type="success" link size="small" @click="handleAudit(row, 1)">通过</el-button>
            <el-button v-if="row.status === 0" type="danger" link size="small" @click="handleAudit(row, 3)">拒绝</el-button>
            <el-button v-if="row.status === 1" type="warning" link size="small" @click="handleStatus(row, 2)">下架</el-button>
            <el-button v-if="row.status === 2" type="success" link size="small" @click="handleStatus(row, 1)">上架</el-button>
            <el-button
              :type="row.isRecommend === 1 ? 'info' : 'warning'"
              link
              size="small"
              @click="handleRecommend(row)"
            >
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

    <el-dialog v-model="detailVisible" title="项目详情" width="900px">
      <el-descriptions v-if="currentItem" :column="2" border>
        <el-descriptions-item label="封面" :span="2">
          <el-image
            v-if="currentItem.coverImage"
            :src="currentItem.coverImage"
            style="max-width: 320px; max-height: 200px"
            fit="contain"
          />
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="标题" :span="2">{{ currentItem.title || '-' }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ currentItem.heritageCategoryName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="发布者">{{ currentItem.publisherName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="历史渊源" :span="2">{{ currentItem.historyOrigin || '-' }}</el-descriptions-item>
        <el-descriptions-item label="工艺特色" :span="2">{{ currentItem.craftFeature || '-' }}</el-descriptions-item>
        <el-descriptions-item label="详细内容" :span="2">
          <div class="content-box" v-html="currentItem.content || '-'"></div>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentItem.status)">{{ getStatusText(currentItem.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="推荐">
          <el-tag :type="currentItem.isRecommend === 1 ? 'warning' : 'info'">
            {{ currentItem.isRecommend === 1 ? '推荐' : '普通' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="浏览量">{{ currentItem.viewCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ currentItem.likeCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="收藏数">{{ currentItem.favoriteCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="评论数">{{ currentItem.commentCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="发布时间" :span="2">{{ currentItem.createTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="editVisible" title="修改项目" width="860px" @closed="resetEditForm">
      <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="90px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="editForm.title" placeholder="请输入项目标题" />
        </el-form-item>
        <el-form-item label="分类" prop="heritageCategoryId">
          <el-select v-model="editForm.heritageCategoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="封面" prop="coverImage">
          <div class="cover-upload-row">
            <el-upload
              class="cover-uploader"
              action="#"
              :show-file-list="false"
              :auto-upload="true"
              :limit="1"
              accept="image/png,image/jpeg,image/jpg,image/gif"
              :before-upload="beforeCoverUpload"
              :http-request="uploadCoverRequest"
            >
              <div class="cover-uploader__trigger">
                <div v-if="editForm.coverImage" class="cover-preview-wrap">
                  <img :src="editForm.coverImage" alt="cover" class="cover-preview" />
                </div>
                <div v-else class="cover-placeholder">
                  <span class="cover-plus">+</span>
                  <span class="cover-tip">上传封面</span>
                </div>
              </div>
            </el-upload>
            <div class="cover-upload__meta">
              <div class="cover-upload__hint">封面将上传到本地 `uploads/files/年/月/日/` 目录。</div>
              <div class="cover-upload__hint">支持 jpg、jpeg、png、gif，大小不超过 5MB。</div>
              <el-button v-if="editForm.coverImage" type="danger" link size="small" @click="clearCover">
                清除封面
              </el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="历史渊源">
          <el-input
            v-model="editForm.historyOrigin"
            type="textarea"
            :rows="3"
            placeholder="请输入历史渊源"
          />
        </el-form-item>
        <el-form-item label="工艺特色">
          <el-input
            v-model="editForm.craftFeature"
            type="textarea"
            :rows="3"
            placeholder="请输入工艺特色"
          />
        </el-form-item>
        <el-form-item label="详细内容" prop="content">
          <el-input
            v-model="editForm.content"
            type="textarea"
            :rows="8"
            placeholder="请输入项目详细内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmitEdit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectVisible" title="审核拒绝" width="420px">
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
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import {
  auditHeritage,
  deleteHeritage,
  getCategoryList,
  getHeritageDetail,
  getHeritagePage,
  updateHeritageItem,
  updateHeritageRecommend,
  updateHeritageStatus
} from '@/api/admin'
import { uploadFile } from '@/api/upload'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const categoryOptions = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const editVisible = ref(false)
const currentItem = ref(null)
const editFormRef = ref(null)
const editingItemId = ref(null)
const rejectVisible = ref(false)
const rejectReason = ref('')
const pendingRejectId = ref(null)

const searchForm = reactive({
  status: null
})

const editForm = reactive({
  title: '',
  heritageCategoryId: null,
  coverImage: '',
  historyOrigin: '',
  craftFeature: '',
  content: '',
  images: [],
  videos: []
})

const editRules = {
  title: [{ required: true, message: '请输入项目标题', trigger: 'blur' }],
  heritageCategoryId: [{ required: true, message: '请选择项目分类', trigger: 'change' }],
  content: [{ required: true, message: '请输入项目内容', trigger: 'blur' }]
}

const resetEditForm = () => {
  editingItemId.value = null
  editForm.title = ''
  editForm.heritageCategoryId = null
  editForm.coverImage = ''
  editForm.historyOrigin = ''
  editForm.craftFeature = ''
  editForm.content = ''
  editForm.images = []
  editForm.videos = []
  editFormRef.value?.clearValidate()
}

const fillEditForm = (item) => {
  editingItemId.value = item.id
  editForm.title = item.title || ''
  editForm.heritageCategoryId = item.heritageCategoryId ?? null
  editForm.coverImage = item.coverImage || ''
  editForm.historyOrigin = item.historyOrigin || ''
  editForm.craftFeature = item.craftFeature || ''
  editForm.content = item.content || ''
  editForm.images = Array.isArray(item.images) ? item.images : []
  editForm.videos = Array.isArray(item.videos) ? item.videos : []
}

const validateCoverFile = (rawFile) => {
  if (!rawFile) return false
  const isImage = ['image/jpeg', 'image/png', 'image/gif'].includes(rawFile.type)
  const isLt5M = rawFile.size / 1024 / 1024 < 5
  if (!isImage) {
    ElMessage.error('封面只支持 JPG、PNG、GIF 格式')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('封面大小不能超过 5MB')
    return false
  }
  return true
}

const clearCover = () => {
  editForm.coverImage = ''
}

const beforeCoverUpload = (rawFile) => validateCoverFile(rawFile)

const uploadCoverRequest = async ({ file }) => {
  if (!file) return
  submitLoading.value = true
  try {
    const res = await uploadFile(file)
    editForm.coverImage = res.data || ''
    ElMessage.success('封面上传成功')
  } catch {
    ElMessage.error('封面上传失败')
  } finally {
    submitLoading.value = false
  }
}

const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    categoryOptions.value = Array.isArray(res.data) ? res.data : []
  } catch {
    categoryOptions.value = []
  }
}

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
  } catch {
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
  } catch {
    ElMessage.error('获取项目详情失败')
  }
}

const handleEdit = async (row) => {
  try {
    const res = await getHeritageDetail(row.id)
    fillEditForm(res.data || {})
    editVisible.value = true
  } catch {
    ElMessage.error('获取项目信息失败')
  }
}

const handleSubmitEdit = async () => {
  if (!editingItemId.value) return
  try {
    await editFormRef.value.validate()
    submitLoading.value = true
    await updateHeritageItem(editingItemId.value, {
      id: editingItemId.value,
      title: editForm.title,
      heritageCategoryId: editForm.heritageCategoryId,
      coverImage: editForm.coverImage || null,
      historyOrigin: editForm.historyOrigin || null,
      craftFeature: editForm.craftFeature || null,
      content: editForm.content
    })
    ElMessage.success('项目已更新')
    editVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('保存失败')
    }
  } finally {
    submitLoading.value = false
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
    await ElMessageBox.confirm('确认审核通过该项目吗？', '提示', { type: 'warning' })
    await auditHeritage(row.id, status, '')
    ElMessage.success('审核通过')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
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
  } catch {
    ElMessage.error('操作失败')
  }
}

const handleStatus = async (row, status) => {
  const actionText = status === 2 ? '下架' : '上架'
  try {
    await ElMessageBox.confirm(`确认要${actionText}该项目吗？`, '提示', { type: 'warning' })
    await updateHeritageStatus(row.id, status)
    ElMessage.success(`${actionText}成功`)
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionText}失败`)
    }
  }
}

const handleRecommend = async (row) => {
  const nextRecommend = row.isRecommend === 1 ? 0 : 1
  const actionText = nextRecommend === 1 ? '推荐' : '取消推荐'
  try {
    await ElMessageBox.confirm(`确认要${actionText}该项目吗？`, '提示', { type: 'warning' })
    await updateHeritageRecommend(row.id, nextRecommend)
    ElMessage.success(`${actionText}成功`)
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionText}失败`)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该项目吗？此操作不可恢复。', '警告', { type: 'error' })
    await deleteHeritage(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
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
  loadCategories()
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
  margin: 0 0 8px;
  font-size: 24px;
  color: #1a1a2e;
}

.page-subtitle {
  margin: 0;
  color: #6b7280;
}

.search-card,
.table-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.content-box {
  max-height: 220px;
  overflow-y: auto;
}

.text-muted {
  color: #9ca3af;
}

.cover-upload-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.cover-uploader {
  flex: 0 0 180px;
  width: 180px;
  max-width: 180px;
  overflow: hidden;
}

.cover-uploader :deep(.el-upload) {
  width: 180px;
  height: 120px;
  max-width: 180px;
  max-height: 120px;
  display: inline-flex;
  overflow: hidden;
  border-radius: 8px;
  flex: 0 0 180px;
}

.cover-uploader__trigger {
  width: 180px;
  height: 120px;
  max-width: 180px;
  max-height: 120px;
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
  overflow: hidden;
  background: #fafafa;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.2s ease;
}

.cover-uploader__trigger:hover {
  border-color: var(--el-color-primary);
}

.cover-preview-wrap {
  width: 180px;
  height: 120px;
  max-width: 180px;
  max-height: 120px;
  overflow: hidden;
  flex: 0 0 180px;
}

.cover-preview {
  width: 100%;
  height: 100%;
  max-width: 180px;
  max-height: 120px;
  object-fit: cover;
  display: block;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.cover-plus {
  font-size: 28px;
  line-height: 1;
}

.cover-tip {
  margin-top: 8px;
  font-size: 12px;
}

.cover-upload__meta {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.cover-upload__hint {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
}
</style>
