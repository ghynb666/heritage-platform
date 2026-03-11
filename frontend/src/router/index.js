import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/login/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/heritage',
    name: 'Heritage',
    component: () => import('@/views/heritage/Heritage.vue'),
    meta: { title: '非遗展示' }
  },
  {
    path: '/heritage/:id',
    name: 'HeritageDetail',
    component: () => import('@/views/heritage/HeritageDetail.vue'),
    meta: { title: '非遗详情' }
  },
  {
    path: '/inheritor-apply',
    name: 'InheritorApply',
    component: () => import('@/views/user/InheritorApply.vue'),
    meta: { title: '传承人申请', requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'AdminLayout',
    component: () => import('@/views/admin/Layout.vue'),
    meta: { title: '后台管理', requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '控制台' }
      },
      {
        path: 'user/list',
        name: 'UserList',
        component: () => import('@/views/admin/user/UserList.vue'),
        meta: { title: '用户列表' }
      },
      {
        path: 'inheritor/list',
        name: 'InheritorList',
        component: () => import('@/views/admin/user/InheritorList.vue'),
        meta: { title: '传承人账号' }
      },
      {
        path: 'inheritor/audit',
        name: 'InheritorAudit',
        component: () => import('@/views/admin/user/InheritorAudit.vue'),
        meta: { title: '传承人审核' }
      },
      {
        path: 'region',
        name: 'RegionList',
        component: () => import('@/views/admin/region/RegionList.vue'),
        meta: { title: '地区管理' }
      },
      {
        path: 'heritage/list',
        name: 'HeritageList',
        component: () => import('@/views/admin/heritage/HeritageList.vue'),
        meta: { title: '非遗项目' }
      },
      {
        path: 'heritage/category',
        name: 'CategoryList',
        component: () => import('@/views/admin/heritage/CategoryList.vue'),
        meta: { title: '非遗分类' }
      },
      {
        path: 'product/list',
        name: 'ProductList',
        component: () => import('@/views/admin/product/ProductList.vue'),
        meta: { title: '鍟嗗搧鍒楄〃' }
      },
      {
        path: 'product/category',
        name: 'ProductCategoryList',
        component: () => import('@/views/admin/product/ProductCategoryList.vue'),
        meta: { title: '鍟嗗搧绫诲瀷' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 非遗平台` : '非遗平台'
  
  const userStore = useUserStore()
  const token = userStore.token || localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }

  if (to.meta.requiresAdmin && !userStore.hasRole('ADMIN')) {
    next('/')
    return
  }

  if ((to.path === '/login' || to.path === '/register') && token) {
    userStore.redirectByRole()
    return
  }

  next()
})

export default router
