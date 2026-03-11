<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">留言管理</h2>
      <p class="page-subtitle">管理留言内容，并可直接回复用户留言。</p>
    </div>

    <div class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="content" label="留言内容" min-width="260" show-overflow-tooltip />
        <el-table-column prop="userName" label="用户" width="120" />
        <el-table-column prop="replyUserName" label="回复对象" width="120" />
        <el-table-column prop="parentId" label="父留言ID" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openReply(row)">回复</el-button>
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

    <el-dialog v-model="replyVisible" title="回复留言" width="480px">
      <el-input v-model="replyContent" type="textarea" :rows="4" placeholder="请输入回复内容" />
      <template #footer>
        <el-button @click="replyVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReply">发送回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteMessage, getMessagePage, replyMessage } from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const replyVisible = ref(false)
const replyId = ref(null)
const replyContent = ref('')

const loadData = async () => {
  loading.value = true
  try {
    const res = await getMessagePage({ page: page.value, size: size.value })
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const openReply = (row) => {
  replyId.value = row.id
  replyContent.value = ''
  replyVisible.value = true
}

const handleReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  try {
    await replyMessage(replyId.value, { content: replyContent.value })
    ElMessage.success('回复成功')
    replyVisible.value = false
    loadData()
  } catch {
    ElMessage.error('回复失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该留言吗？', '警告', { type: 'warning' })
    await deleteMessage(row.id)
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
.table-card { background: #fff; border-radius: 8px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,.05); }
.pagination-wrapper { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
