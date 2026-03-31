<template>
  <div class="api-detail">
    <div class="page-header">
      <el-button @click="$router.back()" :icon="ArrowLeft" plain>返回</el-button>
      <h2>{{ apiInfo.name }}</h2>
      <el-tag :type="apiInfo.lastStatus === 1 ? 'success' : apiInfo.lastStatus === 0 ? 'danger' : 'info'">
        {{ apiInfo.lastStatus === 1 ? '正常' : apiInfo.lastStatus === 0 ? '异常' : '未检测' }}
      </el-tag>
    </div>

    <!-- 基本信息 -->
    <el-row :gutter="20" class="info-row">
      <el-col :span="6">
        <el-card class="info-card">
          <div class="info-item">
            <div class="info-label">URL</div>
            <div class="info-value url-text">{{ apiInfo.url }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="3">
        <el-card class="info-card">
          <div class="info-item">
            <div class="info-label">请求方法</div>
            <el-tag :type="methodTagType(apiInfo.method)">{{ apiInfo.method }}</el-tag>
          </div>
        </el-card>
      </el-col>
      <el-col :span="3">
        <el-card class="info-card">
          <div class="info-item">
            <div class="info-label">最后响应时间</div>
            <div class="info-value" :class="responseTimeClass(apiInfo.lastResponseTime)">
              {{ apiInfo.lastResponseTime != null ? apiInfo.lastResponseTime + 'ms' : '-' }}
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="3">
        <el-card class="info-card">
          <div class="info-item">
            <div class="info-label">可用率(近7天)</div>
            <div class="info-value text-success">{{ availability }}%</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="3">
        <el-card class="info-card">
          <div class="info-item">
            <div class="info-label">检查间隔</div>
            <div class="info-value">{{ apiInfo.checkInterval }}s</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="info-card">
          <div class="info-item">
            <div class="info-label">最后检查时间</div>
            <div class="info-value">{{ apiInfo.lastCheckTime || '-' }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 响应时间趋势图 -->
    <el-card class="chart-card">
      <template #header>响应时间趋势（最近50次）</template>
      <div ref="chartRef" style="height: 300px;"></div>
    </el-card>

    <!-- 历史记录 -->
    <el-card class="history-card">
      <template #header>历史检查记录</template>
      <el-table :data="records" v-loading="recordLoading" stripe>
        <el-table-column prop="checkTime" label="检查时间" width="180" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.success ? 'success' : 'danger'" size="small">
              {{ row.success ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="statusCode" label="状态码" width="90" />
        <el-table-column prop="responseTime" label="响应时间" width="110">
          <template #default="{ row }">
            <span v-if="row.responseTime != null" :class="responseTimeClass(row.responseTime)">
              {{ row.responseTime }}ms
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="errorMsg" label="错误信息" show-overflow-tooltip />
      </el-table>
      <div class="pagination">
        <el-pagination v-model:current-page="recordPage" v-model:page-size="recordPageSize"
          :total="recordTotal" layout="total, prev, pager, next"
          @current-change="loadRecords" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getApiDetail, getCheckRecords } from '../api/index.js'

const route = useRoute()
const apiId = route.params.id

const apiInfo = ref({})
const records = ref([])
const recordLoading = ref(false)
const recordPage = ref(1)
const recordPageSize = ref(20)
const recordTotal = ref(0)
const availability = ref('--')
const chartRef = ref()
let chart = null

const methodTagType = (method) => {
  const map = { GET: '', POST: 'success', PUT: 'warning', DELETE: 'danger' }
  return map[method] || 'info'
}

const responseTimeClass = (ms) => {
  if (!ms) return ''
  if (ms < 500) return 'text-success'
  if (ms < 2000) return 'text-warning'
  return 'text-danger'
}

const loadDetail = async () => {
  const res = await getApiDetail(apiId)
  if (res.code === 200) apiInfo.value = res.data
}

const loadRecords = async () => {
  recordLoading.value = true
  try {
    const res = await getCheckRecords(apiId, { page: recordPage.value, pageSize: recordPageSize.value })
    if (res.code === 200) {
      records.value = res.data.records || res.data
      recordTotal.value = res.data.total || res.data.length
      calcAvailability()
      await nextTick()
      renderChart()
    }
  } finally {
    recordLoading.value = false
  }
}

const calcAvailability = () => {
  if (!records.value.length) return
  const success = records.value.filter(r => r.success).length
  availability.value = ((success / records.value.length) * 100).toFixed(1)
}

const renderChart = () => {
  if (!chartRef.value) return
  if (!chart) chart = echarts.init(chartRef.value)
  const data = [...records.value].reverse()
  chart.setOption({
    tooltip: { trigger: 'axis', formatter: (p) => `${p[0].name}<br/>响应时间: ${p[0].value}ms` },
    grid: { left: 60, right: 20, top: 20, bottom: 40 },
    xAxis: {
      type: 'category',
      data: data.map(r => r.checkTime?.substring(11, 19) || ''),
      axisLabel: { rotate: 30, fontSize: 11 }
    },
    yAxis: { type: 'value', name: 'ms', axisLabel: { formatter: '{value}ms' } },
    series: [{
      type: 'line', smooth: true, data: data.map(r => r.responseTime),
      itemStyle: { color: '#409eff' },
      areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [{ offset: 0, color: 'rgba(64,158,255,0.3)' }, { offset: 1, color: 'rgba(64,158,255,0.05)' }] } },
      markLine: {
        data: [{ type: 'average', name: '平均值' }],
        lineStyle: { color: '#e6a23c' }
      }
    }]
  })
}

onMounted(async () => {
  await loadDetail()
  await loadRecords()
})
</script>

<style scoped>
.api-detail { padding: 20px; }
.page-header { display: flex; align-items: center; gap: 12px; margin-bottom: 20px; }
.page-header h2 { margin: 0; font-size: 20px; }
.info-row { margin-bottom: 20px; }
.info-card .el-card__body { padding: 16px; }
.info-item { text-align: center; }
.info-label { font-size: 12px; color: #909399; margin-bottom: 6px; }
.info-value { font-size: 16px; font-weight: 600; }
.url-text { font-size: 12px; word-break: break-all; font-weight: normal; }
.chart-card { margin-bottom: 20px; }
.history-card { margin-bottom: 20px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.text-success { color: #67c23a; }
.text-warning { color: #e6a23c; }
.text-danger { color: #f56c6c; }
</style>
