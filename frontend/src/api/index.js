import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

// 监控接口 API
export const monitorApi = {
  // 分页查询
  list: (page = 1, size = 10) => api.get(`/monitor/list?page=${page}&size=${size}`),
  // 获取启用的接口
  listEnabled: () => api.get('/monitor/enabled'),
  // 获取详情
  getById: (id) => api.get(`/monitor/${id}`),
  // 添加
  add: (data) => api.post('/monitor', data),
  // 更新
  update: (id, data) => api.put(`/monitor/${id}`, data),
  // 删除
  delete: (id) => api.delete(`/monitor/${id}`),
  // 启用
  enable: (id) => api.put(`/monitor/${id}/enable`),
  // 禁用
  disable: (id) => api.put(`/monitor/${id}/disable`),
  // Dashboard统计
  getDashboardStats: () => api.get('/monitor/dashboard/stats')
}

// 检查记录 API
export const recordApi = {
  // 分页查询
  list: (apiId, page = 1, size = 10) => {
    let url = `/record/list?page=${page}&size=${size}`
    if (apiId) url += `&apiId=${apiId}`
    return api.get(url)
  },
  // 根据API ID查询
  listByApiId: (apiId) => api.get(`/record/api/${apiId}`),
  // 最近记录
  listRecent: (apiId, limit = 50) => api.get(`/record/api/${apiId}/recent?limit=${limit}`),
  // 可用率
  getAvailability: (apiId, startTime, endTime) => {
    let url = `/record/api/${apiId}/availability`
    if (startTime) url += `?startTime=${startTime}`
    if (endTime) url += `${startTime ? '&' : '?'}endTime=${endTime}`
    return api.get(url)
  },
  // 响应时间趋势
  getTrend: (apiId, startTime, endTime) => {
    let url = `/record/api/${apiId}/trend`
    if (startTime) url += `?startTime=${startTime}`
    if (endTime) url += `${startTime ? '&' : '?'}endTime=${endTime}`
    return api.get(url)
  },
  // 连续失败次数
  getConsecutiveFailures: (apiId) => api.get(`/record/api/${apiId}/consecutive-failures`)
}

// ---- 兼容具名导入（供 views 使用）----
export const getApiList = (params = {}) => {
  const { page = 1, pageSize = 10 } = params
  return monitorApi.list(page, pageSize).then(res => ({
    code: 200,
    data: res.data
  })).catch(() => ({ code: 200, data: { records: [], total: 0 } }))
}
export const getDashboardStats = () =>
  monitorApi.getDashboardStats().then(res => ({ code: 200, data: res.data }))
    .catch(() => ({ code: 200, data: { total: 0, normal: 0, abnormal: 0, todayChecks: 0 } }))
export const getApiDetail = (id) =>
  monitorApi.getById(id).then(res => ({ code: 200, data: res.data }))
export const createApi = (data) =>
  monitorApi.add(data).then(res => ({ code: 200, data: res.data }))
export const updateApi = (data) =>
  monitorApi.update(data.id, data).then(res => ({ code: 200, data: res.data }))
export const deleteApi = (id) =>
  monitorApi.delete(id).then(res => ({ code: 200, data: res.data }))
export const toggleApi = (id, enabled) =>
  (enabled === 1 ? monitorApi.enable(id) : monitorApi.disable(id))
    .then(res => ({ code: 200, data: res.data }))
export const getCheckRecords = (apiId, params = {}) => {
  const { page = 1, pageSize = 20 } = params
  return recordApi.list(apiId, page, pageSize).then(res => ({ code: 200, data: res.data }))
}

export default api
