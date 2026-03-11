<template>
  <div class="inheritor-apply-container">
    <el-card class="apply-card">
      <template #header>
        <div class="card-header">
          <span>传承人申请</span>
        </div>
      </template>
      
      <div v-if="applyStatus && applyStatus.status !== 2" class="status-info">
        <el-alert
          :title="applyStatus.status === 0 ? '您的申请正在审核中' : '您已是传承人'"
          :type="applyStatus.status === 0 ? 'warning' : 'success'"
          show-icon
          :closable="false"
        />
        <div class="apply-detail" v-if="applyStatus.status === 0">
          <p><strong>申请时间：</strong>{{ applyStatus.createTime }}</p>
          <p><strong>申请类型：</strong>{{ applyStatus.heritageCategoryName }}</p>
        </div>
      </div>
      
      <el-form v-else ref="applyFormRef" :model="applyForm" :rules="applyRules" label-width="120px">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="applyForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="applyForm.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        
        <el-form-item label="非遗类型" prop="heritageCategoryId">
          <el-select v-model="applyForm.heritageCategoryId" placeholder="请选择非遗类型" style="width: 100%">
            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="资质证明" prop="certificateImages">
          <el-upload
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
            list-type="picture-card"
            accept="image/*"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="申请说明" prop="description">
          <el-input v-model="applyForm.description" type="textarea" :rows="4" placeholder="请输入申请说明" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">提交申请</el-button>
          <el-button @click="$router.back()">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
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
.inheritor-apply-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.apply-card {
  margin-top: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.status-info {
  padding: 20px 0;
}

.apply-detail {
  margin-top: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.apply-detail p {
  margin: 8px 0;
}
</style>
