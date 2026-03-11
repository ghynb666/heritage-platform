import request from '@/utils/request'

export function getUserPage(params) {
  return request({
    url: '/api/admin/user/page',
    method: 'get',
    params
  })
}

export function getUserDetail(id) {
  return request({
    url: `/api/admin/user/${id}`,
    method: 'get'
  })
}

export function updateUser(id, data) {
  return request({
    url: `/api/admin/user/${id}`,
    method: 'put',
    data
  })
}

export function updateUserStatus(id, status) {
  return request({
    url: `/api/admin/user/${id}/status`,
    method: 'put',
    params: { status }
  })
}

export function deleteUser(id) {
  return request({
    url: `/api/admin/user/${id}`,
    method: 'delete'
  })
}

export function resetPassword(id) {
  return request({
    url: `/api/admin/user/${id}/reset-password`,
    method: 'put'
  })
}

export function getInheritorPage(params) {
  return request({
    url: '/api/admin/inheritor/page',
    method: 'get',
    params
  })
}

export function getInheritorDetail(id) {
  return request({
    url: `/api/admin/inheritor/${id}`,
    method: 'get'
  })
}

export function updateInheritorCategory(id, categoryId) {
  return request({
    url: `/api/admin/inheritor/${id}/category`,
    method: 'put',
    params: { categoryId }
  })
}

export function revokeInheritor(id) {
  return request({
    url: `/api/admin/inheritor/${id}/revoke`,
    method: 'put'
  })
}

export function getRegionTree() {
  return request({
    url: '/api/admin/region/tree',
    method: 'get'
  })
}

export function addRegion(data) {
  return request({
    url: '/api/admin/region',
    method: 'post',
    data
  })
}

export function updateRegion(data) {
  return request({
    url: '/api/admin/region',
    method: 'put',
    data
  })
}

export function deleteRegion(id) {
  return request({
    url: `/api/admin/region/${id}`,
    method: 'delete'
  })
}

export function getHeritagePage(params) {
  return request({
    url: '/api/admin/heritage/item/list',
    method: 'get',
    params
  })
}

export function getHeritageDetail(id) {
  return request({
    url: `/api/admin/heritage/item/${id}`,
    method: 'get'
  })
}

export function updateHeritageItem(id, data) {
  return request({
    url: `/api/admin/heritage/item/${id}`,
    method: 'put',
    data
  })
}

export function updateHeritageStatus(id, status) {
  return request({
    url: `/api/admin/heritage/item/${id}/status`,
    method: 'put',
    params: { status }
  })
}

export function auditHeritage(id, status, auditReason) {
  return request({
    url: `/api/admin/heritage/item/${id}/audit`,
    method: 'put',
    params: { status, auditReason }
  })
}

export function updateHeritageRecommend(id, isRecommend) {
  return request({
    url: `/api/admin/heritage/item/${id}/recommend`,
    method: 'put',
    params: { isRecommend }
  })
}

export function deleteHeritage(id) {
  return request({
    url: `/api/admin/heritage/item/${id}`,
    method: 'delete'
  })
}

export function getCategoryPage(params) {
  return request({
    url: '/api/heritage/category/page',
    method: 'get',
    params
  })
}

export function getCategoryList() {
  return request({
    url: '/api/heritage/category/list',
    method: 'get'
  })
}

export function addCategory(data) {
  return request({
    url: '/api/heritage/category',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/api/heritage/category',
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/api/heritage/category/${id}`,
    method: 'delete'
  })
}

export function updateCategorySort(categories) {
  return request({
    url: '/api/heritage/category/sort',
    method: 'put',
    data: categories
  })
}

export function getProductPage(params) {
  return request({
    url: '/api/admin/product/page',
    method: 'get',
    params
  })
}

export function getProductDetail(id) {
  return request({
    url: `/api/admin/product/${id}`,
    method: 'get'
  })
}

export function updateProductStatus(id, status) {
  return request({
    url: `/api/admin/product/${id}/status`,
    method: 'put',
    params: { status }
  })
}

export function updateProductRecommend(id, isRecommend) {
  return request({
    url: `/api/admin/product/${id}/recommend`,
    method: 'put',
    params: { isRecommend }
  })
}

export function deleteProduct(id) {
  return request({
    url: `/api/admin/product/${id}`,
    method: 'delete'
  })
}

export function getProductCategoryList() {
  return request({
    url: '/api/admin/product/category/list',
    method: 'get'
  })
}

export function getProductCategoryPage(params) {
  return request({
    url: '/api/admin/product/category/page',
    method: 'get',
    params
  })
}

export function addProductCategory(data) {
  return request({
    url: '/api/admin/product/category',
    method: 'post',
    data
  })
}

export function updateProductCategory(data) {
  return request({
    url: '/api/admin/product/category',
    method: 'put',
    data
  })
}

export function deleteProductCategory(id) {
  return request({
    url: `/api/admin/product/category/${id}`,
    method: 'delete'
  })
}

export function updateProductCategorySort(categories) {
  return request({
    url: '/api/admin/product/category/sort',
    method: 'put',
    data: categories
  })
}

export function getAnnouncementPage(params) {
  return request({
    url: '/api/admin/announcement/page',
    method: 'get',
    params
  })
}

export function addAnnouncement(data) {
  return request({
    url: '/api/admin/announcement',
    method: 'post',
    data
  })
}

export function updateAnnouncement(data) {
  return request({
    url: '/api/admin/announcement',
    method: 'put',
    data
  })
}

export function deleteAnnouncement(id) {
  return request({
    url: `/api/admin/announcement/${id}`,
    method: 'delete'
  })
}

export function updateAnnouncementTop(id, isTop) {
  return request({
    url: `/api/admin/announcement/${id}/top`,
    method: 'put',
    params: { isTop }
  })
}

export function getBannerList() {
  return request({
    url: '/api/admin/banner/list',
    method: 'get'
  })
}

export function addBanner(data) {
  return request({
    url: '/api/admin/banner',
    method: 'post',
    data
  })
}

export function updateBanner(data) {
  return request({
    url: '/api/admin/banner',
    method: 'put',
    data
  })
}

export function deleteBanner(id) {
  return request({
    url: `/api/admin/banner/${id}`,
    method: 'delete'
  })
}

export function updateBannerSort(data) {
  return request({
    url: '/api/admin/banner/sort',
    method: 'put',
    data
  })
}

export function getNewsPage(params) {
  return request({
    url: '/api/admin/news/page',
    method: 'get',
    params
  })
}

export function getNewsDetail(id) {
  return request({
    url: `/api/admin/news/${id}`,
    method: 'get'
  })
}

export function addNews(data) {
  return request({
    url: '/api/admin/news',
    method: 'post',
    data
  })
}

export function updateNews(id, data) {
  return request({
    url: `/api/admin/news/${id}`,
    method: 'put',
    data
  })
}

export function deleteNews(id) {
  return request({
    url: `/api/admin/news/${id}`,
    method: 'delete'
  })
}

export function auditNews(id, status, auditReason) {
  return request({
    url: `/api/admin/news/${id}/audit`,
    method: 'put',
    params: { status, auditReason }
  })
}

export function updateNewsTop(id, isTop) {
  return request({
    url: `/api/admin/news/${id}/top`,
    method: 'put',
    params: { isTop }
  })
}

export function getForumPage(params) {
  return request({
    url: '/api/admin/forum/page',
    method: 'get',
    params
  })
}

export function getForumDetail(id) {
  return request({
    url: `/api/admin/forum/${id}`,
    method: 'get'
  })
}

export function deleteForum(id) {
  return request({
    url: `/api/admin/forum/${id}`,
    method: 'delete'
  })
}

export function updateForumTop(id, isTop) {
  return request({
    url: `/api/admin/forum/${id}/top`,
    method: 'put',
    params: { isTop }
  })
}

export function getCommentPage(params) {
  return request({
    url: '/api/admin/comment/page',
    method: 'get',
    params
  })
}

export function deleteComment(id) {
  return request({
    url: `/api/admin/comment/${id}`,
    method: 'delete'
  })
}

export function getMessagePage(params) {
  return request({
    url: '/api/admin/message/page',
    method: 'get',
    params
  })
}

export function deleteMessage(id) {
  return request({
    url: `/api/admin/message/${id}`,
    method: 'delete'
  })
}

export function replyMessage(id, data) {
  return request({
    url: `/api/admin/message/${id}/reply`,
    method: 'post',
    data
  })
}

export function getStatisticsOverview() {
  return request({
    url: '/api/admin/statistics/overview',
    method: 'get'
  })
}

export function getHeritageCategoryDistribution() {
  return request({
    url: '/api/admin/statistics/heritage-category-distribution',
    method: 'get'
  })
}

export function getHotContent() {
  return request({
    url: '/api/admin/statistics/hot-content',
    method: 'get'
  })
}

export function getStatisticsTrend(params) {
  return request({
    url: '/api/admin/statistics/trend',
    method: 'get',
    params
  })
}

export function getSensitiveWordPage(params) {
  return request({
    url: '/api/admin/sensitive-word/page',
    method: 'get',
    params
  })
}

export function addSensitiveWord(data) {
  return request({
    url: '/api/admin/sensitive-word',
    method: 'post',
    data
  })
}

export function updateSensitiveWord(id, data) {
  return request({
    url: `/api/admin/sensitive-word/${id}`,
    method: 'put',
    data
  })
}

export function deleteSensitiveWord(id) {
  return request({
    url: `/api/admin/sensitive-word/${id}`,
    method: 'delete'
  })
}

export function importSensitiveWord(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/api/admin/sensitive-word/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
