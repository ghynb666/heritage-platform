<template>
  <div class="register-container">
    <div class="register-left">
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
    
    <div class="register-right">
      <div class="register-box">
        <div class="register-header">
          <h2 class="register-title">注册</h2>
          <p class="register-subtitle">加入非遗传承平台</p>
        </div>
        
        <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form">
          <el-form-item prop="username">
            <div class="form-label">用户名</div>
            <el-input 
              v-model="registerForm.username" 
              placeholder="请输入用户名" 
              size="large"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <div class="form-label">密码</div>
            <el-input 
              v-model="registerForm.password" 
              type="password" 
              placeholder="请输入密码" 
              size="large"
              show-password
            />
          </el-form-item>
          
          <el-form-item prop="confirmPassword">
            <div class="form-label">确认密码</div>
            <el-input 
              v-model="registerForm.confirmPassword" 
              type="password" 
              placeholder="请再次输入密码" 
              size="large"
              show-password
              @keyup.enter="handleRegister"
            />
          </el-form-item>
          
          <el-form-item prop="roleType">
            <div class="form-label">角色类型</div>
            <div class="role-selector">
              <div 
                class="role-option" 
                :class="{ active: registerForm.roleType === 0 }"
                @click="registerForm.roleType = 0"
              >
                <div class="role-icon">众</div>
                <div class="role-info">
                  <span class="role-name">普通用户</span>
                  <span class="role-desc">浏览非遗内容</span>
                </div>
              </div>
              <div 
                class="role-option" 
                :class="{ active: registerForm.roleType === 1 }"
                @click="registerForm.roleType = 1"
              >
                <div class="role-icon">承</div>
                <div class="role-info">
                  <span class="role-name">传承人</span>
                  <span class="role-desc">需提交资质审核</span>
                </div>
              </div>
            </div>
          </el-form-item>
          
          <el-form-item class="form-actions">
            <el-button type="primary" size="large" @click="handleRegister" :loading="loading" class="register-btn">
              注册
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="register-footer">
          <router-link to="/login" class="login-link">已有账号？立即登录</router-link>
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
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  roleType: 0
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.value.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  roleType: [{ required: true, message: '请选择角色类型', trigger: 'change' }]
}

const handleRegister = async () => {
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  try {
    await userStore.register(registerForm.value)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.register-left {
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

.register-right {
  background: var(--paper);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  overflow-y: auto;
}

.register-box {
  width: 100%;
  max-width: 420px;
}

.register-header {
  text-align: center;
  margin-bottom: 40px;
}

.register-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--ink-black);
  letter-spacing: 4px;
  margin-bottom: 12px;
}

.register-subtitle {
  font-size: 14px;
  color: var(--ink-light);
  letter-spacing: 1px;
}

.register-form {
  margin-bottom: 24px;
}

.form-label {
  font-size: 14px;
  color: var(--ink-medium);
  margin-bottom: 8px;
  letter-spacing: 1px;
}

.register-form :deep(.el-input__wrapper) {
  background: #fff;
  border: 1px solid var(--paper-darker);
  box-shadow: none;
  border-radius: 0;
  padding: 4px 16px;
  transition: all 0.3s ease;
}

.register-form :deep(.el-input__wrapper:hover) {
  border-color: var(--ink-light);
}

.register-form :deep(.el-input__wrapper.is-focus) {
  border-color: var(--vermilion);
}

.register-form :deep(.el-input__inner) {
  font-size: 15px;
  color: var(--ink-black);
}

.register-form :deep(.el-input__inner::placeholder) {
  color: var(--paper-darker);
}

.role-selector {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.role-option {
  padding: 16px;
  background: #fff;
  border: 1px solid var(--paper-darker);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 12px;
}

.role-option:hover {
  border-color: var(--ink-light);
}

.role-option.active {
  border-color: var(--vermilion);
  background: rgba(199, 62, 58, 0.02);
}

.role-icon {
  width: 40px;
  height: 40px;
  background: var(--paper);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: var(--ink-light);
  transition: all 0.3s ease;
}

.role-option.active .role-icon {
  background: var(--vermilion);
  color: #fff;
}

.role-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.role-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--ink-black);
}

.role-desc {
  font-size: 12px;
  color: var(--ink-light);
}

.form-actions {
  margin-top: 32px;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  letter-spacing: 4px;
  background: var(--ink-black) !important;
  border-color: var(--ink-black) !important;
  border-radius: 0;
  transition: all 0.3s ease;
}

.register-btn:hover {
  background: var(--vermilion) !important;
  border-color: var(--vermilion) !important;
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(199, 62, 58, 0.3);
}

.register-footer {
  text-align: center;
}

.login-link {
  font-size: 14px;
  color: var(--ink-light);
  transition: color 0.3s ease;
}

.login-link:hover {
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
  .register-container {
    grid-template-columns: 1fr;
  }
  
  .register-left {
    display: none;
  }
  
  .register-right {
    padding: 20px;
  }
  
  .role-selector {
    grid-template-columns: 1fr;
  }
}
</style>
