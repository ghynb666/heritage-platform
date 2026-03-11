<template>
  <div class="user-list-page">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <p class="page-subtitle">管理系统用户账号信息，并支持编辑基础资料。</p>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="用户名/昵称/手机号"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="全部" clearable style="width: 160px">
            <el-option label="普通用户" value="USER" />
            <el-option label="传承人" value="INHERITOR" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="用户信息" min-width="220">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="40" :src="row.avatar" class="user-avatar">
                {{ row.nickname?.charAt(0) || row.username?.charAt(0) || '?' }}
              </el-avatar>
              <div class="user-detail">
                <div class="user-name">{{ row.nickname || row.username }}</div>
                <div class="user-account">@{{ row.username }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column label="角色" width="180">
          <template #default="{ row }">
            <el-tag v-if="row.roleNames" :type="getRoleType(row.roles)" effect="plain">
              {{ row.roleNames }}
            </el-tag>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="非遗分类" width="120">
          <template #default="{ row }">
            <span v-if="row.heritageCategoryName">{{ row.heritageCategoryName }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">修改</el-button>
            <el-button
              :type="row.status === 1 ? 'warning' : 'success'"
              link
              size="small"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button type="info" link size="small" @click="handleResetPwd(row)">重置密码</el-button>
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

    <el-dialog v-model="detailVisible" title="用户详情" width="680px">
      <el-descriptions v-if="currentUser" :column="2" border>
        <el-descriptions-item label="头像" :span="2">
          <el-avatar :size="60" :src="currentUser.avatar">
            {{ currentUser.nickname?.charAt(0) || currentUser.username?.charAt(0) || '?' }}
          </el-avatar>
        </el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ currentUser.nickname || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ getGenderText(currentUser.gender) }}</el-descriptions-item>
        <el-descriptions-item label="生日">{{ formatDate(currentUser.birthday) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentUser.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentUser.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="地区" :span="2">
          {{ [currentUser.province, currentUser.city, currentUser.area].filter(Boolean).join(' / ') || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="详细地址" :span="2">{{ currentUser.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="非遗分类">{{ currentUser.heritageCategoryName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ currentUser.roleNames || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentUser.status === 1 ? 'success' : 'danger'">
            {{ currentUser.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ currentUser.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="最后登录">{{ currentUser.lastLoginTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="个性签名" :span="2">{{ currentUser.signature || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="editVisible" title="修改用户" width="760px" @closed="resetEditForm">
      <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="用户名">
              <el-input :model-value="editForm.username" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="editForm.nickname" placeholder="请输入昵称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="editForm.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="editForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="editForm.gender" placeholder="请选择" clearable style="width: 100%">
                <el-option label="未知" :value="0" />
                <el-option label="男" :value="1" />
                <el-option label="女" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生日">
              <el-date-picker
                v-model="editForm.birthday"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择生日"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="省份">
              <el-input v-model="editForm.province" placeholder="请输入省份" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城市">
              <el-input v-model="editForm.city" placeholder="请输入城市" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区县">
              <el-input v-model="editForm.area" placeholder="请输入区县" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="非遗分类">
              <el-select v-model="editForm.heritageCategoryId" placeholder="请选择" clearable style="width: 100%">
                <el-option
                  v-for="item in categoryOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="头像">
              <div class="avatar-upload-row">
                <el-upload
                  class="avatar-uploader"
                  action="#"
                  :show-file-list="false"
                  :auto-upload="false"
                  :limit="1"
                  accept="image/png,image/jpeg,image/jpg,image/gif"
                  :on-change="handleAvatarChange"
                >
                  <div class="avatar-uploader__trigger">
                    <img v-if="editForm.avatar" :src="editForm.avatar" alt="avatar" class="avatar-preview" />
                    <div v-else class="avatar-placeholder">
                      <span class="avatar-plus">+</span>
                      <span class="avatar-tip">上传头像</span>
                    </div>
                  </div>
                </el-upload>
                <div class="avatar-upload__meta">
                  <div class="avatar-upload__hint">上传后会保存到本地 `uploads/avatar/年/月/日/` 目录。</div>
                  <div class="avatar-upload__hint">支持 jpg、jpeg、png、gif，大小不超过 5MB。</div>
                  <el-button
                    v-if="editForm.avatar"
                    type="danger"
                    link
                    size="small"
                    @click="clearAvatar"
                  >
                    清除头像
                  </el-button>
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="详细地址">
              <el-input v-model="editForm.address" placeholder="请输入详细地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="个性签名">
              <el-input v-model="editForm.signature" type="textarea" :rows="3" placeholder="请输入个性签名" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmitEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import {
  deleteUser,
  getCategoryList,
  getUserDetail,
  getUserPage,
  resetPassword,
  updateUser,
  updateUserStatus
} from '@/api/admin'
import { uploadAvatar } from '@/api/upload'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const categoryOptions = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const editVisible = ref(false)
const currentUser = ref(null)
const editFormRef = ref(null)
const editingUserId = ref(null)

const searchForm = reactive({
  keyword: '',
  status: null,
  role: ''
})

const editForm = reactive({
  username: '',
  nickname: '',
  avatar: '',
  email: '',
  phone: '',
  gender: 0,
  birthday: '',
  province: '',
  city: '',
  area: '',
  address: '',
  signature: '',
  heritageCategoryId: null
})

const editRules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  phone: [{ pattern: /^$|^1\d{10}$/, message: '请输入正确手机号', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确邮箱', trigger: 'blur' }]
}

const formatDate = (value) => {
  if (!value) return ''
  return String(value).slice(0, 10)
}

const resetEditForm = () => {
  editingUserId.value = null
  editForm.username = ''
  editForm.nickname = ''
  editForm.avatar = ''
  editForm.email = ''
  editForm.phone = ''
  editForm.gender = 0
  editForm.birthday = ''
  editForm.province = ''
  editForm.city = ''
  editForm.area = ''
  editForm.address = ''
  editForm.signature = ''
  editForm.heritageCategoryId = null
  editFormRef.value?.clearValidate()
}

const fillEditForm = (user) => {
  editingUserId.value = user.id
  editForm.username = user.username || ''
  editForm.nickname = user.nickname || ''
  editForm.avatar = user.avatar || ''
  editForm.email = user.email || ''
  editForm.phone = user.phone || ''
  editForm.gender = user.gender ?? 0
  editForm.birthday = formatDate(user.birthday)
  editForm.province = user.province || ''
  editForm.city = user.city || ''
  editForm.area = user.area || ''
  editForm.address = user.address || ''
  editForm.signature = user.signature || ''
  editForm.heritageCategoryId = user.heritageCategoryId ?? null
}

const clearAvatar = () => {
  editForm.avatar = ''
}

const validateAvatarFile = (rawFile) => {
  if (!rawFile) return false
  const isImage = ['image/jpeg', 'image/png', 'image/gif'].includes(rawFile.type)
  const isLt5M = rawFile.size / 1024 / 1024 < 5
  if (!isImage) {
    ElMessage.error('头像只支持 JPG、PNG、GIF 格式')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('头像大小不能超过 5MB')
    return false
  }
  return true
}

const handleAvatarChange = async (file) => {
  if (!file?.raw) return
  if (!validateAvatarFile(file.raw)) return
  submitLoading.value = true
  try {
    const res = await uploadAvatar(file.raw)
    editForm.avatar = res.data || ''
    ElMessage.success('头像上传成功')
  } catch {
    ElMessage.error('头像上传失败')
  } finally {
    submitLoading.value = false
  }
}

const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    categoryOptions.value = Array.isArray(res.data) ? res.data : []
  } catch {
    categoryOptions.value = []
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getUserPage({
      page: page.value,
      size: size.value,
      keyword: searchForm.keyword || undefined,
      status: searchForm.status,
      role: searchForm.role || undefined
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
  searchForm.role = ''
  page.value = 1
  loadData()
}

const handleDetail = async (row) => {
  try {
    const res = await getUserDetail(row.id)
    currentUser.value = res.data
    detailVisible.value = true
  } catch {
    ElMessage.error('获取用户详情失败')
  }
}

const handleEdit = async (row) => {
  try {
    const res = await getUserDetail(row.id)
    fillEditForm(res.data || {})
    editVisible.value = true
  } catch {
    ElMessage.error('获取用户信息失败')
  }
}

const handleSubmitEdit = async () => {
  if (!editingUserId.value) return
  try {
    await editFormRef.value.validate()
    submitLoading.value = true
    await updateUser(editingUserId.value, {
      nickname: editForm.nickname,
      avatar: editForm.avatar || null,
      email: editForm.email || null,
      phone: editForm.phone || null,
      gender: editForm.gender,
      birthday: editForm.birthday || null,
      province: editForm.province || null,
      city: editForm.city || null,
      area: editForm.area || null,
      address: editForm.address || null,
      signature: editForm.signature || null,
      heritageCategoryId: editForm.heritageCategoryId
    })
    ElMessage.success('用户信息已更新')
    editVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('保存失败')
    }
  } finally {
    submitLoading.value = false
  }
}

const handleToggleStatus = async (row) => {
  const nextStatus = row.status === 1 ? 0 : 1
  const actionText = nextStatus === 1 ? '启用' : '禁用'
  try {
    await ElMessageBox.confirm(`确认要${actionText}该用户吗？`, '提示', { type: 'warning' })
    await updateUserStatus(row.id, nextStatus)
    ElMessage.success(`${actionText}成功`)
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionText}失败`)
    }
  }
}

const handleResetPwd = async (row) => {
  try {
    await ElMessageBox.confirm('确认要重置该用户密码吗？密码将重置为 123456。', '提示', { type: 'warning' })
    await resetPassword(row.id)
    ElMessage.success('密码已重置为 123456')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重置密码失败')
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该用户吗？此操作不可恢复。', '警告', { type: 'error' })
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const getRoleType = (roles) => {
  if (roles?.includes('ADMIN')) return 'danger'
  if (roles?.includes('INHERITOR')) return 'warning'
  return 'info'
}

const getGenderText = (gender) => {
  if (gender === 1) return '男'
  if (gender === 2) return '女'
  return '未知'
}

onMounted(() => {
  loadCategories()
  loadData()
})
</script>

<style scoped>
.user-list-page {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  margin: 0 0 8px;
  font-size: 24px;
  color: #1a1a2e;
}

.page-subtitle {
  margin: 0;
  color: #6b7280;
}

.search-card,
.table-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  flex-shrink: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-weight: 500;
  color: #1a1a2e;
}

.user-account {
  font-size: 12px;
  color: #9ca3af;
}

.text-muted {
  color: #9ca3af;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.avatar-upload-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.avatar-uploader {
  flex: 0 0 112px;
}

.avatar-uploader :deep(.el-upload) {
  width: 112px;
  height: 112px;
  display: inline-flex;
  overflow: hidden;
  border-radius: 8px;
}

.avatar-uploader__trigger {
  width: 112px;
  height: 112px;
  max-width: 112px;
  max-height: 112px;
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
  overflow: hidden;
  background: #fafafa;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.2s ease;
}

.avatar-uploader__trigger:hover {
  border-color: var(--el-color-primary);
}

.avatar-preview {
  width: 100%;
  height: 100%;
  max-width: 112px;
  max-height: 112px;
  display: block;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.avatar-plus {
  font-size: 28px;
  line-height: 1;
}

.avatar-tip {
  margin-top: 8px;
  font-size: 12px;
}

.avatar-upload__meta {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.avatar-upload__hint {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
}
</style>
