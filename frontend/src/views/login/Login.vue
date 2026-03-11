<template>
  <div class="login-container">
    <div class="login-left">
      <div class="decorative-bg">
        <div class="ink-circle"></div>
        <div class="ink-circle small"></div>
        <div class="bamboo-lines"></div>
      </div>
      <div class="brand-content">
        <div class="brand-logo">墨</div>
        <h1 class="brand-title">非遗传承</h1>
        <p class="brand-desc">传承千年文脉 · 守护文化瑰宝</p>
      </div>
    </div>
    
    <div class="login-right">
      <div class="login-box">
        <div class="login-header">
          <h2 class="login-title">登录</h2>
          <p class="login-subtitle">欢迎回到非遗传承平台</p>
        </div>
        
        <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
          <el-form-item prop="username">
            <div class="form-label">用户名</div>
            <el-input 
              v-model="loginForm.username" 
              placeholder="请输入用户名" 
              size="large"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <div class="form-label">密码</div>
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="请输入密码" 
              size="large"
              show-password
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          
          <el-form-item class="form-actions">
            <el-button type="primary" size="large" @click="handleLogin" :loading="loading" class="login-btn">
              登录
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="login-footer">
          <router-link to="/register" class="register-link">还没有账号？立即注册</router-link>
        </div>
      </div>
      
      <div class="footer-decoration">
        <span class="decoration-line"></span>
        <span class="decoration-dot"></span>
        <span class="decoration-line"></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = ref({
  username: '',
  password: ''
})

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  try {
    await userStore.login(loginForm.value)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.login-left {
  position: relative;
  background: var(--ink-black);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.decorative-bg {
  position: absolute;
  inset: 0;
}

.ink-circle {
  position: absolute;
  width: 500px;
  height: 500px;
  border: 1px solid rgba(247, 244, 239, 0.1);
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: pulse 4s ease-in-out infinite;
}

.ink-circle.small {
  width: 300px;
  height: 300px;
  animation-delay: 1s;
}

@keyframes pulse {
  0%, 100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.3;
  }
  50% {
    transform: translate(-50%, -50%) scale(1.1);
    opacity: 0.1;
  }
}

.bamboo-lines {
  position: absolute;
  top: 0;
  left: 20%;
  width: 1px;
  height: 100%;
  background: linear-gradient(180deg, transparent 0%, rgba(247, 244, 239, 0.1) 50%, transparent 100%);
}

.bamboo-lines::before,
.bamboo-lines::after {
  content: '';
  position: absolute;
  width: 1px;
  height: 100%;
  background: linear-gradient(180deg, transparent 0%, rgba(247, 244, 239, 0.08) 50%, transparent 100%);
}

.bamboo-lines::before {
  left: 40px;
}

.bamboo-lines::after {
  left: -40px;
}

.brand-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.brand-logo {
  width: 80px;
  height: 80px;
  background: var(--vermilion);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  font-weight: 600;
  margin: 0 auto 24px;
  animation: logoFloat 3s ease-in-out infinite;
}

@keyframes logoFloat {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.brand-title {
  font-size: 36px;
  font-weight: 600;
  color: var(--paper);
  letter-spacing: 8px;
  margin-bottom: 16px;
}

.brand-desc {
  font-size: 14px;
  color: var(--paper-darker);
  letter-spacing: 2px;
}

.login-right {
  background: var(--paper);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-box {
  width: 100%;
  max-width: 380px;
}

.login-header {
  text-align: center;
  margin-bottom: 48px;
}

.login-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--ink-black);
  letter-spacing: 4px;
  margin-bottom: 12px;
}

.login-subtitle {
  font-size: 14px;
  color: var(--ink-light);
  letter-spacing: 1px;
}

.login-form {
  margin-bottom: 32px;
}

.form-label {
  font-size: 14px;
  color: var(--ink-medium);
  margin-bottom: 8px;
  letter-spacing: 1px;
}

.login-form :deep(.el-input__wrapper) {
  background: #fff;
  border: 1px solid var(--paper-darker);
  box-shadow: none;
  border-radius: 0;
  padding: 4px 16px;
  transition: all 0.3s ease;
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: var(--ink-light);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: var(--vermilion);
}

.login-form :deep(.el-input__inner) {
  font-size: 15px;
  color: var(--ink-black);
}

.login-form :deep(.el-input__inner::placeholder) {
  color: var(--paper-darker);
}

.form-actions {
  margin-top: 40px;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  letter-spacing: 4px;
  background: var(--ink-black) !important;
  border-color: var(--ink-black) !important;
  border-radius: 0;
  transition: all 0.3s ease;
}

.login-btn:hover {
  background: var(--vermilion) !important;
  border-color: var(--vermilion) !important;
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(199, 62, 58, 0.3);
}

.login-footer {
  text-align: center;
}

.register-link {
  font-size: 14px;
  color: var(--ink-light);
  transition: color 0.3s ease;
}

.register-link:hover {
  color: var(--vermilion);
}

.footer-decoration {
  position: absolute;
  bottom: 40px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.decoration-line {
  width: 40px;
  height: 1px;
  background: var(--paper-darker);
}

.decoration-dot {
  width: 6px;
  height: 6px;
  background: var(--vermilion);
  transform: rotate(45deg);
}

@media (max-width: 968px) {
  .login-container {
    grid-template-columns: 1fr;
  }
  
  .login-left {
    display: none;
  }
  
  .login-right {
    padding: 20px;
  }
}
</style>
