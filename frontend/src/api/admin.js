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
