<template>
  <div class="apply-container">
    <div class="apply-header">
      <h2 class="apply-title">传承人申请</h2>
      <p class="apply-subtitle">成为非遗传承人，传承文化瑰宝</p>
    </div>
    
    <div class="apply-content">
      <div v-if="applyStatus && applyStatus.status !== 2" class="status-section">
        <div class="status-card" :class="{ pending: applyStatus.status === 0, approved: applyStatus.status === 1 }">
          <div class="status-icon">{{ applyStatus.status === 0 ? '审' : '承' }}</div>
          <div class="status-info">
            <h3>{{ applyStatus.status === 0 ? '申请审核中' : '您已是传承人' }}</h3>
            <p v-if="applyStatus.status === 0">您的申请正在审核中，请耐心等待</p>
          </div>
        </div>
        <div class="apply-detail" v-if="applyStatus.status === 0">
          <div class="detail-item">
            <span class="detail-label">申请时间</span>
            <span class="detail-value">{{ applyStatus.createTime }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">申请类型</span>
            <span class="detail-value">{{ applyStatus.heritageCategoryName }}</span>
          </div>
        </div>
      </div>
      
      <el-form v-else ref="applyFormRef" :model="applyForm" :rules="applyRules" class="apply-form" label-position="top">
        <div class="form-section">
          <div class="form-row">
            <el-form-item label="真实姓名" prop="realName" class="form-item">
              <el-input v-model="applyForm.realName" placeholder="请输入真实姓名" size="large" />
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard" class="form-item">
              <el-input v-model="applyForm.idCard" placeholder="请输入身份证号" size="large" />
            </el-form-item>
          </div>
          
          <el-form-item label="非遗类型" prop="heritageCategoryId">
            <el-select v-model="applyForm.heritageCategoryId" placeholder="请选择非遗类型" size="large" style="width: 100%">
              <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="资质证明" prop="certificateImages">
            <div class="upload-area">
              <el-upload
                action="#"
                :auto-upload="false"
                :on-change="handleFileChange"
                :file-list="fileList"
                list-type="picture-card"
                accept="image/*"
              >
                <div class="upload-trigger">
                  <span class="upload-icon">+</span>
                  <span class="upload-text">上传证明</span>
                </div>
              </el-upload>
            </div>
          </el-form-item>
          
          <el-form-item label="申请说明" prop="description">
            <el-input v-model="applyForm.description" type="textarea" :rows="4" placeholder="请输入申请说明" />
          </el-form-item>
        </div>
        
        <div class="form-actions">
          <el-button size="large" @click="$router.back()">返回</el-button>
          <el-button type="primary" size="large" @click="handleSubmit" :loading="loading">提交申请</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getInheritorApplyStatus, submitInheritorApply } from '@/api/inheritor'
import { uploadFile } from '@/api/upload'
import request from '@/utils/request'

const applyFormRef = ref(null)
const loading = ref(false)
const applyStatus = ref(null)
const categoryList = ref([])
const fileList = ref([])

const applyForm = ref({
  realName: '',
  idCard: '',
  heritageCategoryId: null,
  certificateImages: '',
  description: ''
})

const applyRules = {
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  heritageCategoryId: [{ required: true, message: '请选择非遗类型', trigger: 'change' }],
  certificateImages: [{ required: true, message: '请上传资质证明', trigger: 'change' }]
}

const handleFileChange = async (file, list) => {
  fileList.value = list
  if (file.status !== 'success') {
    try {
      const res = await uploadFile(file.raw)
      file.url = res.data
      file.status = 'success'
      const urls = fileList.value.filter(f => f.status === 'success').map(f => f.url)
      applyForm.value.certificateImages = JSON.stringify(urls)
    } catch (e) {
      ElMessage.error('文件上传失败')
    }
  }
}

const handleSubmit = async () => {
  const valid = await applyFormRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  try {
    await submitInheritorApply(applyForm.value)
    ElMessage.success('申请提交成功')
    loadApplyStatus()
  } finally {
    loading.value = false
  }
}

const loadApplyStatus = async () => {
  try {
    const res = await getInheritorApplyStatus()
    applyStatus.value = res.data
  } catch (e) {
    applyStatus.value = null
  }
}

const loadCategoryList = async () => {
  try {
    const res = await request({ url: '/api/heritage/category/list', method: 'get' })
    categoryList.value = res.data || []
  } catch (e) {
    categoryList.value = []
  }
}

onMounted(() => {
  loadApplyStatus()
  loadCategoryList()
})
</script>

<style scoped>
.apply-container {
  max-width: 800px;
  margin: 0 auto;
}

.apply-header {
  text-align: center;
  margin-bottom: 40px;
}

.apply-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--ink-black);
  letter-spacing: 4px;
  margin-bottom: 12px;
}

.apply-subtitle {
  font-size: 14px;
  color: var(--ink-light);
  letter-spacing: 1px;
}

.apply-content {
  background: #fff;
  border: 1px solid var(--paper-darker);
  padding: 40px;
}

.status-section {
  padding: 20px 0;
}

.status-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  border: 1px solid var(--paper-darker);
}

.status-card.pending {
  border-color: var(--gold);
  background: rgba(201, 169, 98, 0.05);
}

.status-card.approved {
  border-color: var(--jade);
  background: rgba(93, 140, 115, 0.05);
}

.status-icon {
  width: 56px;
  height: 56px;
  background: var(--paper);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: var(--ink-light);
}

.status-card.pending .status-icon {
  background: var(--gold);
  color: #fff;
}

.status-card.approved .status-icon {
  background: var(--jade);
  color: #fff;
}

.status-info h3 {
  font-size: 18px;
  font-weight: 600;
  color: var(--ink-black);
  margin-bottom: 4px;
}

.status-info p {
  font-size: 14px;
  color: var(--ink-light);
}

.apply-detail {
  margin-top: 24px;
  padding: 20px;
  background: var(--paper);
}

.detail-item {
  display: flex;
  gap: 16px;
  padding: 8px 0;
}

.detail-label {
  font-size: 14px;
  color: var(--ink-light);
  min-width: 80px;
}

.detail-value {
  font-size: 14px;
  color: var(--ink-black);
}

.apply-form :deep(.el-form-item__label) {
  font-size: 14px;
  color: var(--ink-medium);
  letter-spacing: 1px;
  padding-bottom: 8px;
}

.apply-form :deep(.el-input__wrapper) {
  background: #fff;
  border: 1px solid var(--paper-darker);
  box-shadow: none;
  border-radius: 0;
  transition: all 0.3s ease;
}

.apply-form :deep(.el-input__wrapper:hover) {
  border-color: var(--ink-light);
}

.apply-form :deep(.el-input__wrapper.is-focus) {
  border-color: var(--vermilion);
}

.apply-form :deep(.el-textarea__inner) {
  border: 1px solid var(--paper-darker);
  box-shadow: none;
  border-radius: 0;
  transition: all 0.3s ease;
}

.apply-form :deep(.el-textarea__inner:hover) {
  border-color: var(--ink-light);
}

.apply-form :deep(.el-textarea__inner:focus) {
  border-color: var(--vermilion);
}

.form-section {
  margin-bottom: 32px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.upload-area :deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
  border: 1px dashed var(--paper-darker);
  background: var(--paper);
  border-radius: 0;
}

.upload-area :deep(.el-upload--picture-card:hover) {
  border-color: var(--vermilion);
}

.upload-trigger {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.upload-icon {
  font-size: 24px;
  color: var(--ink-light);
}

.upload-text {
  font-size: 12px;
  color: var(--ink-light);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  padding-top: 24px;
  border-top: 1px solid var(--paper-darker);
}

.form-actions .el-button {
  min-width: 120px;
  border-radius: 0;
}

.form-actions .el-button--primary {
  background: var(--ink-black) !important;
  border-color: var(--ink-black) !important;
}

.form-actions .el-button--primary:hover {
  background: var(--vermilion) !important;
  border-color: var(--vermilion) !important;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .apply-content {
    padding: 24px;
  }
}
</style>
