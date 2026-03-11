<template>
  <div class="inheritor-audit">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>传承人审核</span>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="username" label="申请人" width="120" />
        <el-table-column prop="heritageCategoryName" label="申请类型" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="100" />
        <el-table-column prop="createTime" label="申请时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'warning' : row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 0 ? '待审核' : row.status === 1 ? '已通过' : '已拒绝' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleDetail(row)">查看详情</el-button>
            <el-button v-if="row.status === 0" type="success" size="small" @click="handleApprove(row)">通过</el-button>
            <el-button v-if="row.status === 0" type="danger" size="small" @click="handleReject(row)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        style="margin-top: 20px; justify-content: flex-end"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>
    
    <el-dialog v-model="detailVisible" title="申请详情" width="600px">
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="申请人">{{ currentRow.username }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ currentRow.realName }}</el-descriptions-item>
        <el-descriptions-item label="非遗类型">{{ currentRow.heritageCategoryName }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ currentRow.createTime }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ currentRow.idCard || '未填写' }}</el-descriptions-item>
        <el-descriptions-item label="申请说明">{{ currentRow.description || '未填写' }}</el-descriptions-item>
      </el-descriptions>
      <div v-if="certificateImages && certificateImages.length" class="certificate-images">
        <p>资质证明：</p>
        <el-image 
          v-for="(img, index) in certificateImages" 
          :key="index"
          :src="img" 
          :preview-src-list="certificateImages"
          style="width: 100px; height: 100px; margin-right: 10px"
          fit="cover"
        />
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button v-if="currentRow && currentRow.status === 0" type="success" @click="handleApprove(currentRow)">通过</el-button>
        <el-button v-if="currentRow && currentRow.status === 0" type="danger" @click="handleReject(currentRow)">拒绝</el-button>
      </template>
    </el-dialog>
    
    <el-dialog v-model="rejectVisible" title="拒绝申请" width="400px">
      <el-form>
        <el-form-item label="拒绝原因">
          <el-input v-model="rejectReason" type="textarea" :rows="4" placeholder="请输入拒绝原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReject">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getInheritorApplyList, approveInheritorApply, rejectInheritorApply } from '@/api/inheritor'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const detailVisible = ref(false)
const currentRow = ref(null)
const rejectVisible = ref(false)
const rejectReason = ref('')
const certificateImages = ref([])

const loadData = async () => {
  loading.value = true
  try {
    const res = await getInheritorApplyList({ page: page.value, size: size.value, status: 0 })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

const handleDetail = (row) => {
  currentRow.value = row
  try {
    certificateImages.value = row.certificateImages ? JSON.parse(row.certificateImages) : []
  } catch {
    certificateImages.value = []
  }
  detailVisible.value = true
}

const handleApprove = async (row) => {
  try {
    await ElMessageBox.confirm('确定通过该申请吗？', '提示', { type: 'warning' })
    await approveInheritorApply(row.id)
    ElMessage.success('审核通过')
    loadData()
    detailVisible.value = false
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleReject = (row) => {
  currentRow.value = row
  rejectReason.value = ''
  rejectVisible.value = true
}

const confirmReject = async () => {
  if (!rejectReason.value.trim()) {
    ElMessage.warning('请输入拒绝原因')
    return
  }
  try {
    await rejectInheritorApply(currentRow.value.id, rejectReason.value)
    ElMessage.success('已拒绝')
    loadData()
    rejectVisible.value = false
    detailVisible.value = false
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.card-header {
  font-size: 18px;
  font-weight: bold;
}

.certificate-images {
  margin-top: 20px;
}

.certificate-images p {
  margin-bottom: 10px;
  font-weight: bold;
}
</style>
