<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">敏感词管理</h2>
      <p class="page-subtitle">维护互动内容审核词库，支持启停控制和批量导入。</p>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="搜索敏感词" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 140px">
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-card">
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">新增敏感词</el-button>
        <el-upload
          action="#"
          :show-file-list="false"
          :http-request="handleImportRequest"
          :before-upload="beforeImport"
        >
          <el-button>批量导入</el-button>
        </el-upload>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="word" label="敏感词" min-width="180" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'danger' : 'info'">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑敏感词' : '新增敏感词'" width="520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="敏感词" prop="word">
          <el-input v-model="form.word" maxlength="100" placeholder="请输入敏感词" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="可选备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  addSensitiveWord,
  deleteSensitiveWord,
  getSensitiveWordPage,
  importSensitiveWord,
  updateSensitiveWord
} from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  keyword: '',
  status: null
})

const form = reactive({
  id: null,
  word: '',
  status: 1,
  remark: ''
})

const rules = {
  word: [{ required: true, message: '请输入敏感词', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const resetForm = () => {
  form.id = null
  form.word = ''
  form.status = 1
  form.remark = ''
  formRef.value?.clearValidate()
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getSensitiveWordPage({
      page: page.value,
      size: size.value,
      keyword: searchForm.keyword,
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
  searchForm.keyword = ''
  searchForm.status = null
  page.value = 1
  loadData()
}

const handleAdd = () => {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.id = row.id
  form.word = row.word || ''
  form.status = row.status ?? 1
  form.remark = row.remark || ''
  isEdit.value = true
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    const payload = {
      word: form.word,
      status: form.status,
      remark: form.remark
    }
    if (isEdit.value) {
      await updateSensitiveWord(form.id, payload)
      ElMessage.success('更新成功')
    } else {
      await addSensitiveWord(payload)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('操作失败')
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除敏感词“${row.word}”吗？`, '警告', { type: 'warning' })
    await deleteSensitiveWord(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const beforeImport = (file) => {
  const lowerName = file.name.toLowerCase()
  const valid = lowerName.endsWith('.txt') || lowerName.endsWith('.csv')
  if (!valid) {
    ElMessage.error('仅支持 txt 或 csv 文件')
  }
  return valid
}

const handleImportRequest = async ({ file }) => {
  try {
    const res = await importSensitiveWord(file)
    const data = res.data || {}
    ElMessage.success(`导入完成：成功 ${data.successCount || 0} 条，跳过 ${data.skippedCount || 0} 条`)
    loadData()
  } catch {
    ElMessage.error('导入失败')
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
.toolbar { margin-bottom: 16px; display: flex; gap: 12px; }
.pagination-wrapper { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
