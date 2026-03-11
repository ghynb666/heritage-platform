<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">评论管理</h2>
      <p class="page-subtitle">查看评论内容并执行删除操作。</p>
    </div>

    <div class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="content" label="评论内容" min-width="260" show-overflow-tooltip />
        <el-table-column prop="userName" label="评论用户" width="140" />
        <el-table-column prop="targetTypeName" label="目标类型" width="120" />
        <el-table-column prop="targetId" label="目标ID" width="100" />
        <el-table-column prop="replyUserName" label="回复对象" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
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
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteComment, getCommentPage } from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getCommentPage({ page: page.value, size: size.value })
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该评论吗？', '警告', { type: 'warning' })
    await deleteComment(row.id)
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
