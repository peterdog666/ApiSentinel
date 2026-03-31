<template>
  <div class="dashboard">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card class="stat-card total">
          <div class="stat-icon"><el-icon size="32"><Monitor /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">监控总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success">
          <div class="stat-icon"><el-icon size="32"><CircleCheck /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.normal }}</div>
            <div class="stat-label">正常</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card danger">
          <div class="stat-icon"><el-icon size="32"><CircleClose /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.abnormal }}</div>
            <div class="stat-label">异常</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card info">
          <div class="stat-icon"><el-icon size="32"><DataLine /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.todayChecks }}</div>
            <div class="stat-label">今日检查次数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 接口状态列表 -->
    <el-card class="api-list-card">
      <template #header>
        <div class="card-header">
          <span>接口状态总览</span>
          <el-button type="primary" size="small" @click="$router.push('/apis')">管理接口</el-button>
        </div>
      </template>
      <el-table :data="apiList" v-loading="loading" stripe>
        <el-table-column prop="name" label="接口名称" min-width="150" />
        <el-table-column prop="url" label="URL" min-width="200" show-overflow-tooltip />
        <el-table-column prop="method" label="方法" width="80">
          <template #default="{ row }">
            <el-tag :type="methodTagType(row.method)" size="small">{{ row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-badge :type="row.lastStatus === 1 ? 'success' : 'danger'" is-dot>
              <span :class="row.lastStatus === 1 ? 'text-success' : 'text-danger'">
                {{ row.lastStatus === 1 ? '正常' : row.lastStatus === 0 ? '异常' : '未检测' }}
              </span>
            </el-badge>
          </template>
        </el-table-column>
        <el-table-column prop="lastResponseTime" label="响应时间" width="110">
          <template #default="{ row }">
            <span v-if="row.lastResponseTime !== null">
              <span :class="responseTimeClass(row.lastResponseTime)">{{ row.lastResponseTime }}ms</span>
            </span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastCheckTime" label="最后检查" width="170">
          <template #default="{ row }">
            {{ row.lastCheckTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="$router.push(`/apis/${row.id}`)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Monitor, CircleCheck, CircleClose, DataLine } from '@element-plus/icons-vue'
import { getApiList, getDashboardStats } from '../api/index.js'

const loading = ref(false)
const apiList = ref([])
const stats = ref({ total: 0, normal: 0, abnormal: 0, todayChecks: 0 })

const methodTagType = (method) => {
  const map = { GET: '', POST: 'success', PUT: 'warning', DELETE: 'danger' }
  return map[method] || 'info'
}

const responseTimeClass = (ms) => {
  if (ms < 500) return 'text-success'
  if (ms < 2000) return 'text-warning'
  return 'text-danger'
}

const loadData = async () => {
  loading.value = true
  try {
    const [listRes, statsRes] = await Promise.all([getApiList(), getDashboardStats()])
    if (listRes.code === 200) apiList.value = listRes.data
    if (statsRes.code === 200) stats.value = statsRes.data
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.dashboard { padding: 20px; }
.stat-cards { margin-bottom: 20px; }
.stat-card { cursor: default; }
.stat-card .el-card__body { display: flex; align-items: center; gap: 16px; padding: 20px; }
.stat-icon { opacity: 0.8; }
.stat-value { font-size: 28px; font-weight: bold; line-height: 1; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
.stat-card.total .stat-icon { color: #409eff; }
.stat-card.success .stat-icon { color: #67c23a; }
.stat-card.danger .stat-icon { color: #f56c6c; }
.stat-card.info .stat-icon { color: #909399; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.api-list-card { margin-top: 0; }
.text-success { color: #67c23a; }
.text-warning { color: #e6a23c; }
.text-danger { color: #f56c6c; }
.text-muted { color: #c0c4cc; }
</style>
