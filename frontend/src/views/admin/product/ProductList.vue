<template>
  <div class="product-list-page">
    <div class="page-header">
      <h2 class="page-title">文创商品管理</h2>
      <p class="page-subtitle">管理文创商品信息，审核、上下架、推荐</删除</p>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="传承人">
          <el-input v-model="searchForm.inheritor" placeholder="请输入传承人名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="全部" clearable style="width: 140px">
            <el-option v-for="cat in categoryList" :key="cat.id" :label="cat.name" :value="cat.id" />
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
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image :src="row.coverImage" style="width: 60px; height: 60px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="productCategoryName" label="分类" width="100" />
        <el-table-column prop="inheritorName" label="传承人" width="100" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            <span>¥{{ row.price }}</span>
            <span v-if="row.originalPrice" class="original-price">¥{{ row.originalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="sales" label="销量" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isRecommend" label="推荐" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isRecommend === 1 ? 'warning' : 'info'" size="small">
              {{ row.isRecommend === 1 ? '推荐' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button :type="row.status === 1 ? 'warning' : 'success'" link size="small" @click="handleStatus(row)">
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button :type="row.isRecommend === 1 ? 'info' : 'warning'" link size="small" @click="handleRecommend(row)">
              {{ row.isRecommend === 1 ? '取消推荐' : '推荐' }}
            </el-button>
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

    <el-dialog v-model="detailVisible" title="商品详情" width="700px">
      <el-descriptions :column="2" border v-if="currentProduct">
        <el-descriptions-item label="封面" :span="2">
          <el-image :src="currentProduct.coverImage" style="max-width: 200px; max-height: 150px" fit="contain" />
        </el-descriptions-item>
        <el-descriptions-item label="商品名称" :span="2">{{ currentProduct.name }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ currentProduct.productCategoryName }}</el-descriptions-item>
        <el-descriptions-item label="传承人">{{ currentProduct.inheritorName }}</el-descriptions-item>
        <el-descriptions-item label="价格">{{ currentProduct.price }}</el-descriptions-item>
        <el-descriptions-item label="原价">{{ currentProduct.originalPrice || '-' }}</el-descriptions-item>
        <el-descriptions-item label="库存">{{ currentProduct.stock }}</el-descriptions-item>
        <el-descriptions-item label="销量">{{ currentProduct.sales }}</el-descriptions-item>
        <el-descriptions-item label="浏览量">{{ currentProduct.viewCount }}</el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ currentProduct.likeCount }}</el-descriptions-item>
        <el-descriptions-item label="收藏数">{{ currentProduct.favoriteCount }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentProduct.status === 1 ? 'success' : 'info'">
            {{ currentProduct.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="推荐">
          <el-tag :type="currentProduct.isRecommend === 1 ? 'warning' : 'info'">
            {{ currentProduct.isRecommend === 1 ? '推荐' : '普通' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ currentProduct.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="商品图片" :span="2" v-if="currentProduct.images?.length">
          <div class="image-list">
            <el-image v-for="(img, index) in currentProduct.images" :key="index" :src="img" style="width: 100px; height: 80px; margin-right: 10px" fit="cover" :preview-src-list="currentProduct.images" />
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ currentProduct.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getProductPage, getProductDetail, updateProductStatus, updateProductRecommend, deleteProduct, getProductCategoryList } from '@/api/admin'

import { useRoute } from 'vue-router'

const route = useRoute()
const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const currentProduct = ref(null)
const categoryList = ref([])

const searchForm = reactive({
  name: '',
  inheritor: '',
  status: null,
  categoryId: null
})
const loadCategoryList = async () => {
  try {
    const res = await getProductCategoryList()
    categoryList.value = res.data || []
  } catch (e) {
    categoryList.value = []
  }
}
const loadData = async () => {
  loading.value = true
  try {
    const res = await getProductPage({
      page: page.value,
      size: size.value,
      name: searchForm.name || undefined,
      inheritor: searchForm.inheritor || undefined,
      status: searchForm.status,
      categoryId: searchForm.categoryId
    })
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
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
  searchForm.name = ''
  searchForm.inheritor = ''
  searchForm.status = null
  searchForm.categoryId = null
  page.value = 1
  loadData()
}
const handleDetail = async (row) => {
  try {
    const res = await getProductDetail(row.id)
    currentProduct.value = res.data
    detailVisible.value = true
  } catch (e) {
    ElMessage.error('获取详情失败')
  }
}
const handleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  const action = newStatus === 0 ? '下架' : '上架'
  try {
    await ElMessageBox.confirm(`确定要${action}该商品吗？`, '提示', { type: 'warning' })
    await updateProductStatus(row.id, newStatus)
    ElMessage.success(`${action}成功`)
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(`${action}失败`)
    }
  }
}
const handleRecommend = async (row) => {
  const newRecommend = row.isRecommend === 1 ? 0 : 1
  const action = newRecommend === 1 ? '推荐' : '取消推荐'
  try {
    await ElMessageBox.confirm(`确定要${action}该商品吗？`, '提示', { type: 'warning' })
    await updateProductRecommend(row.id, newRecommend)
    ElMessage.success(`${action}成功`)
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(`${action}失败`)
    }
  }
}
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该商品吗？此操作不可恢复！', '警告', { type: 'error' })
    await deleteProduct(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}
onMounted(() => {
  loadCategoryList()
  loadData()
})
</script>

<style scoped>
.product-list-page {
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
.search-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}
.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.table-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}
.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.original-price {
  text-decoration: line-through;
  color: #9ca3af;
  font-size: 12px;
  margin-left: 8px;
}
.image-list {
  display: flex;
  flex-wrap: wrap;
}
</style>
