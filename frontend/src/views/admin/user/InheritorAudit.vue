<template>
  <div class="audit-container">
    <div class="audit-header">
      <h2 class="audit-title">传承人审核</h2>
    </div>
    
    <div class="audit-content">
      <div class="table-wrapper">
        <el-table :data="tableData" v-loading="loading" class="audit-table">
          <el-table-column prop="username" label="申请人" width="120" />
          <el-table-column prop="heritageCategoryName" label="申请类型" width="140" />
          <el-table-column prop="realName" label="真实姓名" width="100" />
          <el-table-column prop="createTime" label="申请时间" width="180" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <span class="status-tag" :class="{ pending: row.status === 0, approved: row.status === 1, rejected: row.status === 2 }">
                {{ row.status === 0 ? '待审核' : row.status === 1 ? '已通过' : '已拒绝' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-button type="primary" size="small" text @click="handleDetail(row)">查看详情</el-button>
              <el-button v-if="row.status === 0" type="success" size="small" text @click="handleApprove(row)">通过</el-button>
              <el-button v-if="row.status === 0" type="danger" size="small" text @click="handleReject(row)">拒绝</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>
    
    <el-dialog v-model="detailVisible" title="申请详情" width="600px" class="detail-dialog">
      <div class="detail-content" v-if="currentRow">
        <div class="detail-grid">
          <div class="detail-item">
            <span class="detail-label">申请人</span>
            <span class="detail-value">{{ currentRow.username }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">真实姓名</span>
            <span class="detail-value">{{ currentRow.realName }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">非遗类型</span>
            <span class="detail-value">{{ currentRow.heritageCategoryName }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">申请时间</span>
            <span class="detail-value">{{ currentRow.createTime }}</span>
          </div>
          <div class="detail-item full">
            <span class="detail-label">身份证号</span>
            <span class="detail-value">{{ currentRow.idCard || '未填写' }}</span>
          </div>
          <div class="detail-item full">
            <span class="detail-label">申请说明</span>
            <span class="detail-value">{{ currentRow.description || '未填写' }}</span>
          </div>
        </div>
        <div v-if="certificateImages && certificateImages.length" class="certificate-section">
          <h4>资质证明</h4>
          <div class="certificate-images">
            <el-image 
              v-for="(img, index) in certificateImages" 
              :key="index"
              :src="img" 
              :preview-src-list="certificateImages"
              class="certificate-image"
              fit="cover"
            />
          </div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailVisible = false">关闭</el-button>
          <el-button v-if="currentRow && currentRow.status === 0" type="success" @click="handleApprove(currentRow)">通过</el-button>
          <el-button v-if="currentRow && currentRow.status === 0" type="danger" @click="handleReject(currentRow)">拒绝</el-button>
        </div>
      </template>
    </el-dialog>
    
    <el-dialog v-model="rejectVisible" title="拒绝申请" width="400px" class="reject-dialog">
      <div class="reject-content">
        <div class="reject-label">拒绝原因</div>
        <el-input v-model="rejectReason" type="textarea" :rows="4" placeholder="请输入拒绝原因" />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="rejectVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmReject">确定</el-button>
        </div>
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
.audit-container {
  padding: 0;
}

.audit-header {
  margin-bottom: 24px;
}

.audit-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--ink-black);
  letter-spacing: 2px;
}

.audit-content {
  background: #fff;
  border: 1px solid var(--paper-darker);
}

.table-wrapper {
  padding: 0;
}

.audit-table :deep(.el-table__header th) {
  background: var(--paper);
  color: var(--ink-medium);
  font-weight: 500;
  font-size: 14px;
  letter-spacing: 1px;
}

.audit-table :deep(.el-table__row) {
  transition: background 0.3s ease;
}

.audit-table :deep(.el-table__row:hover > td) {
  background: var(--paper) !important;
}

.status-tag {
  display: inline-block;
  padding: 4px 12px;
  font-size: 12px;
  letter-spacing: 1px;
}

.status-tag.pending {
  background: rgba(201, 169, 98, 0.1);
  color: var(--gold);
}

.status-tag.approved {
  background: rgba(93, 140, 115, 0.1);
  color: var(--jade);
}

.status-tag.rejected {
  background: rgba(199, 62, 58, 0.1);
  color: var(--vermilion);
}

.pagination-wrapper {
  padding: 20px;
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid var(--paper-darker);
}

.detail-content {
  padding: 8px 0;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-item.full {
  grid-column: span 2;
}

.detail-label {
  font-size: 12px;
  color: var(--ink-light);
  letter-spacing: 1px;
}

.detail-value {
  font-size: 14px;
  color: var(--ink-black);
}

.certificate-section {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid var(--paper-darker);
}

.certificate-section h4 {
  font-size: 14px;
  font-weight: 500;
  color: var(--ink-medium);
  margin-bottom: 16px;
  letter-spacing: 1px;
}

.certificate-images {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.certificate-image {
  width: 100px;
  height: 100px;
  border: 1px solid var(--paper-darker);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.dialog-footer .el-button {
  border-radius: 0;
  min-width: 80px;
}

.dialog-footer .el-button--success {
  background: var(--jade) !important;
  border-color: var(--jade) !important;
}

.dialog-footer .el-button--danger {
  background: var(--vermilion) !important;
  border-color: var(--vermilion) !important;
}

.reject-content {
  padding: 8px 0;
}

.reject-label {
  font-size: 14px;
  color: var(--ink-medium);
  margin-bottom: 12px;
  letter-spacing: 1px;
}

.reject-content :deep(.el-textarea__inner) {
  border: 1px solid var(--paper-darker);
  box-shadow: none;
  border-radius: 0;
}

.reject-content :deep(.el-textarea__inner:focus) {
  border-color: var(--vermilion);
}
</style>
