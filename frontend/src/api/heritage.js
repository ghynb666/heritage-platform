import request from '@/utils/request'

export function getHeritageCategoryList() {
  return request({
    url: '/api/heritage/category/list',
    method: 'get'
  })
}

export function getHeritageItemList(params) {
  return request({
    url: '/api/heritage/item/list',
    method: 'get',
    params
  })
}

export function getHeritageItemDetail(id) {
  return request({
    url: `/api/heritage/item/${id}`,
    method: 'get'
  })
}

export function searchHeritageItems(params) {
  return request({
    url: '/api/heritage/item/search',
    method: 'get',
    params
  })
}

export function getMyHeritageItems(params) {
  return request({
    url: '/api/inheritor/heritage/item/list',
    method: 'get',
    params
  })
}

export function publishHeritageItem(data) {
  return request({
    url: '/api/inheritor/heritage/item',
    method: 'post',
    data
  })
}

export function updateHeritageItem(data) {
  return request({
    url: '/api/inheritor/heritage/item',
    method: 'put',
    data
  })
}

export function deleteHeritageItem(id) {
  return request({
    url: `/api/inheritor/heritage/item/${id}`,
    method: 'delete'
  })
}

export function getAdminHeritageItems(params) {
  return request({
    url: '/api/admin/heritage/item/list',
    method: 'get',
    params
  })
}

export function adminPublishHeritageItem(data) {
  return request({
    url: '/api/admin/heritage/item',
    method: 'post',
    data
  })
}

export function updateHeritageItemStatus(id, status) {
  return request({
    url: `/api/admin/heritage/item/${id}/status`,
    method: 'put',
    params: { status }
  })
}
