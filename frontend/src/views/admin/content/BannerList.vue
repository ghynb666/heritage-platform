<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">轮播图管理</h2>
      <p class="page-subtitle">维护轮播图图片、跳转信息和展示顺序。</p>
    </div>

    <div class="table-card">
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增轮播图
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe row-key="id">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="图片" width="120">
          <template #default="{ row }">
            <el-image v-if="row.imageUrl" :src="row.imageUrl" style="width: 80px; height: 48px" fit="cover" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="160" show-overflow-tooltip />
        <el-table-column prop="linkUrl" label="跳转地址" min-width="180" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="排序" width="120">
          <template #default="{ row }">
            <el-input-number v-model="row.sort" :min="0" :max="999" size="small" @change="handleSortChange(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑轮播图' : '新增轮播图'" width="680px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="图片地址" prop="imageUrl">
          <div class="upload-row">
            <el-input v-model="form.imageUrl" placeholder="请输入图片地址或上传图片" />
            <el-upload action="#" :show-file-list="false" :http-request="uploadImageRequest" :before-upload="beforeImageUpload">
              <el-button type="primary">上传</el-button>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="跳转地址">
          <el-input v-model="form.linkUrl" placeholder="请输入跳转地址" />
        </el-form-item>
        <el-form-item label="跳转类型">
          <el-select v-model="form.linkType" style="width: 100%">
            <el-option label="无跳转" :value="0" />
            <el-option label="外链" :value="1" />
            <el-option label="站内内容" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联ID">
          <el-input-number v-model="form.linkId" :min="0" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.startTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" placeholder="可选" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" placeholder="可选" style="width: 100%" />
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
import { Plus } from '@element-plus/icons-vue'
import { addBanner, deleteBanner, getBannerList, updateBanner, updateBannerSort } from '@/api/admin'
import { uploadFile } from '@/api/upload'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  title: '',
  imageUrl: '',
  linkUrl: '',
  linkType: 0,
  linkId: null,
  sort: 0,
  status: 1,
  startTime: '',
  endTime: ''
})

const rules = {
  imageUrl: [{ required: true, message: '请上传轮播图', trigger: 'blur' }]
}

const resetForm = () => {
  form.id = null
  form.title = ''
  form.imageUrl = ''
  form.linkUrl = ''
  form.linkType = 0
  form.linkId = null
  form.sort = 0
  form.status = 1
  form.startTime = ''
  form.endTime = ''
  formRef.value?.clearValidate()
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getBannerList()
    tableData.value = Array.isArray(res.data) ? res.data : []
  } catch {
    tableData.value = []
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, {
    id: row.id,
    title: row.title || '',
    imageUrl: row.imageUrl || '',
    linkUrl: row.linkUrl || '',
    linkType: row.linkType ?? 0,
    linkId: row.linkId ?? null,
    sort: row.sort ?? 0,
    status: row.status ?? 1,
    startTime: row.startTime || '',
    endTime: row.endTime || ''
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateBanner({ ...form })
      ElMessage.success('更新成功')
    } else {
      await addBanner({ ...form })
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
    await ElMessageBox.confirm('确认删除该轮播图吗？', '警告', { type: 'warning' })
    await deleteBanner(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSortChange = async (row) => {
  try {
    await updateBannerSort([{ id: row.id, sort: row.sort }])
    ElMessage.success('排序已更新')
  } catch {
    ElMessage.error('排序更新失败')
    loadData()
  }
}

const beforeImageUpload = (file) => file.type.startsWith('image/')

const uploadImageRequest = async ({ file }) => {
  try {
    const res = await uploadFile(file)
    form.imageUrl = res.data || ''
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
.table-card { background: #fff; border-radius: 8px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,.05); }
.toolbar { margin-bottom: 16px; }
.upload-row { display: flex; gap: 12px; width: 100%; }
</style>
