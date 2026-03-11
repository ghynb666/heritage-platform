<template>
  <div class="dashboard-page" v-loading="loading">
    <section class="hero-panel">
      <div class="hero-copy">
        <p class="eyebrow">DATA ATLAS</p>
        <h2 class="hero-title">后台运营全景</h2>
        <p class="hero-desc">聚合用户增长、内容热度、商品交易与站内访问，给管理端一个可直接用于决策的观测面板。</p>
      </div>
      <div class="hero-metrics">
        <div class="hero-metric">
          <span class="hero-label">近 7 日新增用户</span>
          <strong>{{ formatNumber(overview.newUsersIn7Days) }}</strong>
        </div>
        <div class="hero-metric">
          <span class="hero-label">累计访问量</span>
          <strong>{{ formatNumber(overview.totalVisits) }}</strong>
        </div>
      </div>
    </section>

    <section class="stats-grid">
      <article v-for="item in summaryCards" :key="item.key" class="stat-card">
        <div class="stat-head">
          <span class="stat-icon">{{ item.icon }}</span>
          <span class="stat-name">{{ item.label }}</span>
        </div>
        <div class="stat-value">{{ item.value }}</div>
        <p class="stat-note">{{ item.note }}</p>
      </article>
    </section>

    <section class="content-grid">
      <article class="panel chart-panel wide-panel">
        <div class="panel-header">
          <div>
            <p class="panel-kicker">趋势分析</p>
            <h3 class="panel-title">关键指标波动</h3>
          </div>
          <div class="panel-actions">
            <el-segmented v-model="metric" :options="metricOptions" @change="loadTrend" />
            <el-segmented v-model="granularity" :options="granularityOptions" @change="loadTrend" />
          </div>
        </div>
        <div ref="trendChartRef" class="chart-box"></div>
      </article>

      <article class="panel chart-panel">
        <div class="panel-header">
          <div>
            <p class="panel-kicker">分类占比</p>
            <h3 class="panel-title">非遗分类分布</h3>
          </div>
        </div>
        <div ref="categoryChartRef" class="chart-box compact-chart"></div>
      </article>

      <article class="panel rank-panel">
        <div class="panel-header">
          <div>
            <p class="panel-kicker">内容热度</p>
            <h3 class="panel-title">热门内容排行</h3>
          </div>
        </div>
        <div class="rank-list">
          <div v-for="(item, index) in hotContent" :key="`${item.type}-${item.id}`" class="rank-item">
            <div class="rank-index">{{ String(index + 1).padStart(2, '0') }}</div>
            <div class="rank-main">
              <div class="rank-topline">
                <span class="rank-type">{{ item.type }}</span>
                <span class="rank-views">{{ formatNumber(item.viewCount) }} 浏览</span>
              </div>
              <div class="rank-title">{{ item.title }}</div>
              <div class="rank-extra">{{ item.extra || '暂无补充信息' }}</div>
            </div>
          </div>
          <div v-if="!hotContent.length" class="empty-state">暂无排行数据</div>
        </div>
      </article>
    </section>
  </div>
</template>

<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import * as echarts from 'echarts'
import {
  getHeritageCategoryDistribution,
  getHotContent,
  getStatisticsOverview,
  getStatisticsTrend
} from '@/api/admin'

const loading = ref(false)
const metric = ref('users')
const granularity = ref('day')
const trendChartRef = ref(null)
const categoryChartRef = ref(null)
const hotContent = ref([])

const overview = reactive({
  totalUsers: 0,
  newUsersIn7Days: 0,
  inheritorCount: 0,
  heritageItemCount: 0,
  productCount: 0,
  productSales: 0,
  orderCount: 0,
  totalAmount: 0,
  totalVisits: 0
})

const categoryDistribution = ref([])
const trendData = ref([])

const metricOptions = [
  { label: '用户', value: 'users' },
  { label: '订单', value: 'orders' },
  { label: '金额', value: 'amount' },
  { label: '访问', value: 'visits' }
]

const granularityOptions = [
  { label: '日', value: 'day' },
  { label: '周', value: 'week' },
  { label: '月', value: 'month' }
]

const metricLabelMap = {
  users: '新增用户',
  orders: '新增订单',
  amount: '成交金额',
  visits: '访问量'
}

const summaryCards = computed(() => ([
  {
    key: 'users',
    icon: '众',
    label: '用户',
    value: formatNumber(overview.totalUsers),
    note: `传承人 ${formatNumber(overview.inheritorCount)} 人`
  },
  {
    key: 'heritage',
    icon: '遗',
    label: '非遗项目',
    value: formatNumber(overview.heritageItemCount),
    note: '覆盖全部已录入项目'
  },
  {
    key: 'product',
    icon: '品',
    label: '文创商品',
    value: formatNumber(overview.productCount),
    note: `累计销量 ${formatNumber(overview.productSales)}`
  },
  {
    key: 'trade',
    icon: '贸',
    label: '交易',
    value: formatCurrency(overview.totalAmount),
    note: `订单 ${formatNumber(overview.orderCount)} 笔`
  }
]))

let trendChart
let categoryChart
let resizeHandler

const formatNumber = (value) => Number(value || 0).toLocaleString('zh-CN')

const formatCurrency = (value) => {
  const amount = Number(value || 0)
  return `¥ ${amount.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
}

const loadOverview = async () => {
  const res = await getStatisticsOverview()
  Object.assign(overview, res.data || {})
}

const loadCategoryDistribution = async () => {
  const res = await getHeritageCategoryDistribution()
  categoryDistribution.value = res.data || []
  renderCategoryChart()
}

const loadHotContent = async () => {
  const res = await getHotContent()
  hotContent.value = res.data || []
}

const loadTrend = async () => {
  const res = await getStatisticsTrend({
    metric: metric.value,
    granularity: granularity.value
  })
  trendData.value = res.data || []
  renderTrendChart()
}

const renderTrendChart = async () => {
  await nextTick()
  if (!trendChartRef.value) return
  if (!trendChart) {
    trendChart = echarts.init(trendChartRef.value)
  }
  trendChart.setOption({
    grid: { left: 28, right: 18, top: 26, bottom: 28, containLabel: true },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(15, 23, 42, 0.92)',
      borderWidth: 0,
      textStyle: { color: '#f8fafc' }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: trendData.value.map(item => item.label),
      axisLine: { lineStyle: { color: '#d0d8e4' } },
      axisLabel: { color: '#52606d' }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      splitLine: { lineStyle: { color: '#e7edf5' } },
      axisLabel: { color: '#52606d' }
    },
    series: [
      {
        name: metricLabelMap[metric.value],
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        data: trendData.value.map(item => Number(item.value || 0)),
        lineStyle: {
          width: 4,
          color: '#b42318'
        },
        itemStyle: {
          color: '#b42318',
          borderColor: '#fff',
          borderWidth: 2
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(180, 35, 24, 0.28)' },
              { offset: 1, color: 'rgba(180, 35, 24, 0.02)' }
            ]
          }
        }
      }
    ]
  })
}

const renderCategoryChart = async () => {
  await nextTick()
  if (!categoryChartRef.value) return
  if (!categoryChart) {
    categoryChart = echarts.init(categoryChartRef.value)
  }
  categoryChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      bottom: 0,
      icon: 'circle',
      textStyle: { color: '#52606d' }
    },
    series: [
      {
        type: 'pie',
        radius: ['42%', '72%'],
        center: ['50%', '46%'],
        padAngle: 2,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 4
        },
        label: {
          color: '#334155',
          formatter: '{b}\n{c}'
        },
        data: categoryDistribution.value.map((item, index) => ({
          value: Number(item.value || 0),
          name: item.categoryName,
          itemStyle: {
            color: ['#b42318', '#155eef', '#0f766e', '#f79009', '#7a5af8', '#087443'][index % 6]
          }
        }))
      }
    ]
  })
}

const loadAll = async () => {
  loading.value = true
  try {
    await Promise.all([
      loadOverview(),
      loadCategoryDistribution(),
      loadHotContent(),
      loadTrend()
    ])
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadAll()
  resizeHandler = () => {
    trendChart?.resize()
    categoryChart?.resize()
  }
  window.addEventListener('resize', resizeHandler)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeHandler)
  trendChart?.dispose()
  categoryChart?.dispose()
})
</script>

<style scoped>
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.hero-panel {
  display: grid;
  grid-template-columns: minmax(0, 1.8fr) minmax(320px, 1fr);
  gap: 20px;
  padding: 28px 30px;
  background:
    radial-gradient(circle at top right, rgba(180, 35, 24, 0.18), transparent 35%),
    linear-gradient(135deg, #fffdf8 0%, #f5efe8 100%);
  border: 1px solid rgba(109, 40, 30, 0.14);
  border-radius: 24px;
  overflow: hidden;
  position: relative;
}

.hero-panel::after {
  content: '';
  position: absolute;
  inset: auto -12% -58% auto;
  width: 280px;
  height: 280px;
  background: radial-gradient(circle, rgba(180, 35, 24, 0.14) 0%, rgba(180, 35, 24, 0) 70%);
}

.eyebrow {
  margin: 0 0 10px;
  font-size: 12px;
  letter-spacing: 0.28em;
  color: #9b6b62;
}

.hero-title {
  margin: 0;
  font-size: 34px;
  line-height: 1.1;
  color: #1f2937;
}

.hero-desc {
  max-width: 620px;
  margin: 14px 0 0;
  color: #52606d;
  line-height: 1.7;
}

.hero-metrics {
  display: grid;
  grid-template-columns: 1fr;
  gap: 14px;
  align-self: center;
}

.hero-metric {
  padding: 18px 20px;
  background: rgba(255, 255, 255, 0.75);
  border: 1px solid rgba(15, 23, 42, 0.08);
  border-radius: 18px;
  backdrop-filter: blur(10px);
}

.hero-label {
  display: block;
  margin-bottom: 8px;
  color: #7c8795;
  font-size: 13px;
}

.hero-metric strong {
  font-size: 30px;
  color: #111827;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

.stat-card {
  padding: 22px;
  background: #ffffff;
  border: 1px solid #e7edf5;
  border-radius: 20px;
  box-shadow: 0 14px 32px rgba(15, 23, 42, 0.04);
}

.stat-head {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.stat-icon {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  background: linear-gradient(135deg, #b42318 0%, #dd6b20 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  box-shadow: 0 12px 24px rgba(180, 35, 24, 0.22);
}

.stat-name {
  color: #52606d;
  letter-spacing: 0.08em;
}

.stat-value {
  font-size: 30px;
  font-weight: 700;
  color: #111827;
}

.stat-note {
  margin: 8px 0 0;
  color: #7c8795;
  font-size: 13px;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.35fr) minmax(360px, 0.9fr);
  gap: 20px;
}

.panel {
  background: #fff;
  border: 1px solid #e7edf5;
  border-radius: 24px;
  padding: 22px;
  box-shadow: 0 14px 32px rgba(15, 23, 42, 0.04);
}

.wide-panel {
  grid-column: 1 / 2;
}

.chart-panel {
  min-height: 380px;
}

.rank-panel {
  grid-column: 2 / 3;
  grid-row: 1 / span 2;
}

.panel-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 18px;
}

.panel-kicker {
  margin: 0 0 6px;
  font-size: 12px;
  letter-spacing: 0.24em;
  color: #9b6b62;
  text-transform: uppercase;
}

.panel-title {
  margin: 0;
  font-size: 22px;
  color: #111827;
}

.panel-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.chart-box {
  width: 100%;
  height: 320px;
}

.compact-chart {
  height: 340px;
}

.rank-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.rank-item {
  display: grid;
  grid-template-columns: 54px minmax(0, 1fr);
  gap: 14px;
  padding: 16px;
  border-radius: 18px;
  background: linear-gradient(180deg, #fffdfa 0%, #f8fafc 100%);
  border: 1px solid #ecf1f6;
}

.rank-index {
  width: 54px;
  height: 54px;
  border-radius: 16px;
  background: #111827;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 700;
}

.rank-topline {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 8px;
}

.rank-type {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: 999px;
  background: rgba(180, 35, 24, 0.08);
  color: #b42318;
  font-size: 12px;
}

.rank-views {
  color: #52606d;
  font-size: 12px;
}

.rank-title {
  color: #111827;
  font-weight: 600;
  line-height: 1.5;
}

.rank-extra {
  margin-top: 6px;
  color: #7c8795;
  font-size: 13px;
}

.empty-state {
  padding: 32px 16px;
  text-align: center;
  color: #7c8795;
}

:deep(.el-segmented) {
  --el-segmented-item-selected-bg-color: #111827;
  --el-segmented-item-selected-color: #fff;
  --el-segmented-bg-color: #f1f5f9;
}

@media (max-width: 1280px) {
  .stats-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .content-grid {
    grid-template-columns: 1fr;
  }

  .rank-panel,
  .wide-panel {
    grid-column: auto;
    grid-row: auto;
  }
}

@media (max-width: 920px) {
  .hero-panel {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .panel-header {
    flex-direction: column;
  }

  .rank-topline {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
