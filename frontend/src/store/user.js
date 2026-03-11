import { defineStore } from 'pinia'
import { login, register, getUserInfo, getUserRoles } from '@/api/auth'
import router from '@/router'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    roles: JSON.parse(localStorage.getItem('roles') || '[]')
  }),

  actions: {
    async login(loginForm) {
      const res = await login(loginForm)
      this.token = res.data.token
      this.userInfo = res.data.userInfo
      this.roles = res.data.roles
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userInfo', JSON.stringify(res.data.userInfo))
      localStorage.setItem('roles', JSON.stringify(res.data.roles))
      this.redirectByRole()
    },

    async register(registerForm) {
      const res = await register(registerForm)
      this.token = res.data.token
      this.userInfo = res.data.userInfo
      this.roles = res.data.roles
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userInfo', JSON.stringify(res.data.userInfo))
      localStorage.setItem('roles', JSON.stringify(res.data.roles))
      this.redirectByRole()
    },

    async getUserInfo() {
      const res = await getUserInfo()
      this.userInfo = res.data
      localStorage.setItem('userInfo', JSON.stringify(res.data))
    },

    async getUserRoles() {
      const res = await getUserRoles()
      this.roles = res.data
      localStorage.setItem('roles', JSON.stringify(res.data))
    },

    logout() {
      this.token = ''
      this.userInfo = {}
      this.roles = []
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('roles')
      router.push('/login')
    },

    redirectByRole() {
      if (this.roles.includes('ADMIN')) {
        router.push('/admin')
      } else if (this.roles.includes('INHERITOR')) {
        router.push('/heritage')
      } else {
        router.push('/')
      }
    },

    hasRole(role) {
      return this.roles.includes(role)
    }
  }
})
