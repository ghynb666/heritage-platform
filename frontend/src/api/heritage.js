import request from '@/utils/request'

export function getHeritageCategoryList() {
  return request({
    url: '/api/heritage/category/list',
    method: 'get'
  })
}
