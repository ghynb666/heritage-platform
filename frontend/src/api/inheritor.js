import request from '@/utils/request'

export function getInheritorApplyStatus() {
  return request({
    url: '/api/user/inheritor/apply/status',
    method: 'get'
  })
}

export function submitInheritorApply(data) {
  return request({
    url: '/api/user/inheritor/apply',
    method: 'post',
    data
  })
}

export function getInheritorApplyList(params) {
  return request({
    url: '/api/admin/inheritor/apply/list',
    method: 'get',
    params
  })
}

export function getInheritorApplyDetail(id) {
  return request({
    url: `/api/admin/inheritor/apply/${id}`,
    method: 'get'
  })
}

export function approveInheritorApply(id) {
  return request({
    url: `/api/admin/inheritor/apply/${id}/approve`,
    method: 'post'
  })
}

export function rejectInheritorApply(id, reason) {
  return request({
    url: `/api/admin/inheritor/apply/${id}/reject`,
    method: 'post',
    params: { reason }
  })
}
