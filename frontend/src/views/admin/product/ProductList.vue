<template>
  <div class="product-list-page">
    <div class="page-header">
      <h2 class="page-title">商品列表</h2>
      <p class="page-subtitle">查看商品、上下架、推荐和删除操作。</p>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入商品名称"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="全部" clearable style="width: 160px">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            查询
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
            <el-image
              v-if="row.coverImage"
              :src="row.coverImage"
              style="width: 60px; height: 60px"
              fit="cover"
            />
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="productCategoryName" label="分类" width="120" />
        <el-table-column prop="inheritorName" label="传承人" width="120" />
        <el-table-column prop="price" label="价格" width="120" />
        <el-table-column prop="stock" label="库存" width="90" />
        <el-table-column prop="sales" label="销量" width="90" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="推荐" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isRecommend === 1 ? 'warning' : 'info'" size="small">
              {{ row.isRecommend === 1 ? '推荐' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button
              :type="row.status === 1 ? 'warning' : 'success'"
              link
              size="small"
              @click="handleStatus(row)"
            >
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button
              :type="row.isRecommend === 1 ? 'info' : 'warning'"
              link
              size="small"
              @click="handleRecommend(row)"
            >
              {{ row.isRecommend === 1 ? '取消推荐' : '设为推荐' }}
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

    <el-dialog v-model="detailVisible" title="商品详情" width="720px">
      <el-descriptions v-if="currentProduct" :column="2" border>
        <el-descriptions-item label="商品名称" :span="2">
          {{ currentProduct.name || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="封面" :span="2">
          <el-image
            v-if="currentProduct.coverImage"
            :src="currentProduct.coverImage"
            style="width: 180px; height: 120px"
            fit="contain"
          />
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="分类">
          {{ currentProduct.productCategoryName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="传承人">
          {{ currentProduct.inheritorName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="价格">
          {{ currentProduct.price ?? '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="原价">
          {{ currentProduct.originalPrice ?? '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="库存">
          {{ currentProduct.stock ?? 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="销量">
          {{ currentProduct.sales ?? 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="浏览量">
          {{ currentProduct.viewCount ?? 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="收藏数">
          {{ currentProduct.favoriteCount ?? 0 }}
        </el-descriptions-item>
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
        <el-descriptions-item label="描述" :span="2">
          {{ currentProduct.description || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import {
  deleteProduct,
  getProductCategoryList,
  getProductDetail,
  getProductPage,
  updateProductRecommend,
  updateProductStatus
} from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const categoryList = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const currentProduct = ref(null)

const searchForm = reactive({
  keyword: '',
  status: null,
  categoryId: null
})

const loadCategoryList = async () => {
  try {
    const res = await getProductCategoryList()
    categoryList.value = Array.isArray(res.data) ? res.data : []
  } catch {
    categoryList.value = []
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getProductPage({
      page: page.value,
      size: size.value,
      status: searchForm.status,
      categoryId: searchForm.categoryId,
      keyword: searchForm.keyword || undefined
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
  searchForm.categoryId = null
  page.value = 1
  loadData()
}

const handleDetail = async (row) => {
  try {
    const res = await getProductDetail(row.id)
    currentProduct.value = res.data
    detailVisible.value = true
  } catch {
    ElMessage.error('获取商品详情失败')
  }
}

const handleStatus = async (row) => {
  const nextStatus = row.status === 1 ? 0 : 1
  const actionText = nextStatus === 1 ? '上架' : '下架'
  try {
    await ElMessageBox.confirm(`确认要${actionText}该商品吗？`, '提示', { type: 'warning' })
    await updateProductStatus(row.id, nextStatus)
    ElMessage.success(`${actionText}成功`)
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionText}失败`)
    }
  }
}

const handleRecommend = async (row) => {
  const nextRecommend = row.isRecommend === 1 ? 0 : 1
  const actionText = nextRecommend === 1 ? '设为推荐' : '取消推荐'
  try {
    await ElMessageBox.confirm(`确认要${actionText}吗？`, '提示', { type: 'warning' })
    await updateProductRecommend(row.id, nextRecommend)
    ElMessage.success(`${actionText}成功`)
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionText}失败`)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该商品吗？此操作不可恢复。', '警告', { type: 'error' })
    await deleteProduct(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
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

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.text-muted {
  color: #9ca3af;
}
</style>
