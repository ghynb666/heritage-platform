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
