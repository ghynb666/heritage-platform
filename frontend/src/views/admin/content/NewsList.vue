<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">资讯管理</h2>
      <p class="page-subtitle">管理资讯内容、审核状态与置顶展示。</p>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="标题/摘要" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 140px">
            <el-option label="已发布" :value="1" />
            <el-option label="已拒绝" :value="2" />
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
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增资讯
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image v-if="row.coverImage" :src="row.coverImage" style="width: 60px; height: 60px" fit="cover" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="authorName" label="作者" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '已发布' : '已拒绝' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="置顶" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isTop === 1 ? 'warning' : 'info'">{{ row.isTop === 1 ? '置顶' : '普通' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="commentCount" label="评论数" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link size="small" @click="handleAudit(row, 1)">通过</el-button>
            <el-button type="danger" link size="small" @click="handleAudit(row, 2)">拒绝</el-button>
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑资讯' : '新增资讯'" width="720px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入资讯标题" />
        </el-form-item>
        <el-form-item label="封面">
          <div class="upload-row">
            <el-input v-model="form.coverImage" placeholder="请输入封面地址或上传图片" />
            <el-upload action="#" :show-file-list="false" :http-request="uploadCoverRequest" :before-upload="beforeImageUpload">
              <el-button type="primary">上传</el-button>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="请输入资讯摘要" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="8" placeholder="请输入资讯内容" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">已发布</el-radio>
            <el-radio :label="2">已拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectVisible" title="拒绝原因" width="420px">
      <el-input v-model="rejectReason" type="textarea" :rows="4" placeholder="请输入拒绝原因" />
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
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { addNews, auditNews, deleteNews, getNewsDetail, getNewsPage, updateNews, updateNewsTop } from '@/api/admin'
import { uploadFile } from '@/api/upload'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const rejectVisible = ref(false)
const rejectReason = ref('')
const pendingRejectId = ref(null)

const searchForm = reactive({ keyword: '', status: null })
const form = reactive({ id: null, title: '', coverImage: '', summary: '', content: '', status: 1 })
const rules = { title: [{ required: true, message: '请输入资讯标题', trigger: 'blur' }] }

const resetForm = () => {
  Object.assign(form, { id: null, title: '', coverImage: '', summary: '', content: '', status: 1 })
  formRef.value?.clearValidate()
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getNewsPage({ page: page.value, size: size.value, ...searchForm })
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
const handleAdd = () => { resetForm(); isEdit.value = false; dialogVisible.value = true }

const handleEdit = async (row) => {
  try {
    const res = await getNewsDetail(row.id)
    Object.assign(form, res.data || {})
    isEdit.value = true
    dialogVisible.value = true
  } catch {
    ElMessage.error('获取资讯详情失败')
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateNews(form.id, { ...form })
      ElMessage.success('更新成功')
    } else {
      await addNews({ ...form })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) ElMessage.error('操作失败')
  }
}

const handleAudit = async (row, status) => {
  if (status === 2) {
    pendingRejectId.value = row.id
    rejectReason.value = ''
    rejectVisible.value = true
    return
  }
  try {
    await auditNews(row.id, status, '')
    ElMessage.success('审核通过')
    loadData()
  } catch {
    ElMessage.error('审核失败')
  }
}

const confirmReject = async () => {
  try {
    await auditNews(pendingRejectId.value, 2, rejectReason.value)
    rejectVisible.value = false
    ElMessage.success('已拒绝')
    loadData()
  } catch {
    ElMessage.error('操作失败')
  }
}

const handleTop = async (row) => {
  try {
    await updateNewsTop(row.id, row.isTop === 1 ? 0 : 1)
    ElMessage.success('操作成功')
    loadData()
  } catch {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该资讯吗？', '警告', { type: 'warning' })
    await deleteNews(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

const beforeImageUpload = (file) => file.type.startsWith('image/')

const uploadCoverRequest = async ({ file }) => {
  try {
    const res = await uploadFile(file)
    form.coverImage = res.data || ''
    ElMessage.success('上传成功')
  } catch {
    ElMessage.error('上传失败')
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
.toolbar { margin-bottom: 16px; }
.pagination-wrapper { margin-top: 20px; display: flex; justify-content: flex-end; }
.upload-row { display: flex; gap: 12px; width: 100%; }
</style>
