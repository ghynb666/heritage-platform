<template>
  <div class="region-page">
    <div class="page-header">
      <h2 class="page-title">地区管理</h2>
      <p class="page-subtitle">管理系统省市区数据</p>
    </div>

    <div class="region-card">
      <div class="toolbar">
        <el-button type="primary" @click="handleAddRoot">
          <el-icon><Plus /></el-icon>
          添加省份
        </el-button>
        <el-button @click="loadData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>

      <el-tree
        ref="treeRef"
        :data="treeData"
        :props="{ label: 'name', children: 'children' }"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
        v-loading="loading"
        class="region-tree"
      >
        <template #default="{ node, data }">
          <div class="tree-node">
            <div class="node-content">
              <el-icon class="node-icon" :class="getLevelClass(data.level)">
                <Location />
              </el-icon>
              <span class="node-name">{{ data.name }}</span>
              <el-tag size="small" :type="getLevelType(data.level)" class="level-tag">
                {{ getLevelText(data.level) }}
              </el-tag>
              <el-tag v-if="data.code" size="small" type="info" class="code-tag">
                {{ data.code }}
              </el-tag>
            </div>
            <div class="node-actions">
              <el-button type="primary" link size="small" @click.stop="handleAdd(data)" v-if="data.level < 3">
                添加下级
              </el-button>
              <el-button type="primary" link size="small" @click.stop="handleEdit(data)">
                编辑
              </el-button>
              <el-button type="danger" link size="small" @click.stop="handleDelete(data)">
                删除
              </el-button>
            </div>
          </div>
        </template>
      </el-tree>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="450px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="地区名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入地区名称" />
        </el-form-item>
        <el-form-item label="地区编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入地区编码（选填）" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Location } from '@element-plus/icons-vue'
import { getRegionTree, addRegion, updateRegion, deleteRegion } from '@/api/admin'

const loading = ref(false)
const treeData = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)
const treeRef = ref(null)

const form = reactive({
  id: null,
  parentId: 0,
  name: '',
  code: '',
  level: 1,
  sort: 0,
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入地区名称', trigger: 'blur' }]
}

const isEdit = computed(() => !!form.id)
const dialogTitle = computed(() => isEdit.value ? '编辑地区' : '新增地区')

const loadData = async () => {
  loading.value = true
  try {
    const res = await getRegionTree()
    treeData.value = res.data || []
  } catch (e) {
    treeData.value = []
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  form.id = null
  form.parentId = 0
  form.name = ''
  form.code = ''
  form.level = 1
  form.sort = 0
  form.status = 1
}

const handleAddRoot = () => {
  resetForm()
  form.level = 1
  dialogVisible.value = true
}

const handleAdd = (parent) => {
  resetForm()
  form.parentId = parent.id
  form.level = parent.level + 1
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.id = row.id
  form.parentId = row.parentId || 0
  form.name = row.name
  form.code = row.code || ''
  form.level = row.level
  form.sort = row.sort || 0
  form.status = row.status ?? 1
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateRegion({ ...form })
      ElMessage.success('修改成功')
    } else {
      await addRegion({ ...form })
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleDelete = async (row) => {
  const hasChildren = row.children && row.children.length > 0
  const message = hasChildren
    ? '该地区下有子级地区，删除后子级地区也会被删除，确定要删除吗？'
    : '确定要删除该地区吗？'
  
  try {
    await ElMessageBox.confirm(message, '警告', { type: 'warning' })
    await deleteRegion(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const getLevelText = (level) => {
  const map = { 1: '省', 2: '市', 3: '区' }
  return map[level] || '未知'
}

const getLevelType = (level) => {
  const map = { 1: 'danger', 2: 'warning', 3: 'success' }
  return map[level] || 'info'
}

const getLevelClass = (level) => {
  const map = { 1: 'level-1', 2: 'level-2', 3: 'level-3' }
  return map[level] || ''
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.region-page {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1a1a2e;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.region-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.toolbar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.region-tree {
  background: transparent;
}

.tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 14px;
}

.node-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.node-icon {
  font-size: 16px;
}

.node-icon.level-1 {
  color: #ef4444;
}

.node-icon.level-2 {
  color: #f59e0b;
}

.node-icon.level-3 {
  color: #10b981;
}

.node-name {
  font-weight: 500;
  color: #1a1a2e;
}

.level-tag {
  margin-left: 4px;
}

.code-tag {
  margin-left: 4px;
}

.node-actions {
  display: flex;
  gap: 4px;
  opacity: 0;
  transition: opacity 0.2s;
}

.tree-node:hover .node-actions {
  opacity: 1;
}

:deep(.el-tree-node__content) {
  height: auto;
  padding: 4px 0;
}

:deep(.el-tree-node__content:hover) {
  background-color: #f9fafb;
}
</style>
