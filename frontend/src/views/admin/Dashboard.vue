<template>
  <div class="dashboard">
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon">众</div>
        <div class="stat-info">
          <span class="stat-value">0</span>
          <span class="stat-label">用户总数</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">遗</div>
        <div class="stat-info">
          <span class="stat-value">0</span>
          <span class="stat-label">非遗项目</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">品</div>
        <div class="stat-info">
          <span class="stat-value">0</span>
          <span class="stat-label">文创商品</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">审</div>
        <div class="stat-info">
          <span class="stat-value">0</span>
          <span class="stat-label">待审核申请</span>
        </div>
      </div>
    </div>
    
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-icon">墨</div>
        <h2 class="welcome-title">欢迎使用非物质文化遗产管理平台</h2>
        <p class="welcome-desc">请通过左侧菜单进行管理操作</p>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  border: 1px solid var(--paper-darker);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 30px var(--shadow-ink);
}

.stat-icon {
  width: 56px;
  height: 56px;
  background: var(--paper);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: var(--vermilion);
  transition: all 0.3s ease;
}

.stat-card:hover .stat-icon {
  background: var(--vermilion);
  color: #fff;
}

.stat-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--ink-black);
  letter-spacing: 1px;
}

.stat-label {
  font-size: 14px;
  color: var(--ink-light);
  letter-spacing: 1px;
}

.welcome-section {
  background: #fff;
  border: 1px solid var(--paper-darker);
  padding: 60px 40px;
}

.welcome-content {
  text-align: center;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  background: var(--ink-black);
  color: var(--paper);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  font-weight: 600;
  margin: 0 auto 24px;
}

.welcome-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--ink-black);
  letter-spacing: 2px;
  margin-bottom: 12px;
}

.welcome-desc {
  font-size: 14px;
  color: var(--ink-light);
  letter-spacing: 1px;
}

@media (max-width: 1200px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: 1fr;
  }
}
</style>
