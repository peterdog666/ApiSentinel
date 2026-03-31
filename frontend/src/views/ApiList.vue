<template>
  <div class="api-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>接口管理</span>
          <el-button type="primary" @click="openDialog()">
            <el-icon><Plus /></el-icon> 新增接口
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="name" label="接口名称" min-width="140" />
        <el-table-column prop="url" label="URL" min-width="200" show-overflow-tooltip />
        <el-table-column prop="method" label="方法" width="80">
          <template #default="{ row }">
            <el-tag :type="methodTagType(row.method)" size="small">{{ row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkInterval" label="检查间隔" width="100">
          <template #default="{ row }">{{ row.checkInterval }}s</template>
        </el-table-column>
        <el-table-column prop="timeout" label="超时" width="80">
          <template #default="{ row }">{{ row.timeout }}s</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-switch v-model="row.enabled" :active-value="1" :inactive-value="0"
              @change="toggleEnabled(row)" />
          </template>
        </el-table-column>
        <el-table-column label="最后状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.lastStatus === 1" type="success" size="small">正常</el-tag>
            <el-tag v-else-if="row.lastStatus === 0" type="danger" size="small">异常</el-tag>
            <el-tag v-else type="info" size="small">未检测</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastResponseTime" label="响应时间" width="100">
          <template #default="{ row }">
            {{ row.lastResponseTime != null ? row.lastResponseTime + 'ms' : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="$router.push(`/apis/${row.id}`)">详情</el-button>
            <el-button type="warning" link size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination v-model:current-page="page" v-model:page-size="pageSize"
          :total="total" :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData" @current-change="loadData" />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑接口' : '新增接口'" width="600px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="接口名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入接口名称" />
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="form.url" placeholder="https://example.com/api/health" />
        </el-form-item>
        <el-form-item label="请求方法" prop="method">
          <el-select v-model="form.method" style="width:100%">
            <el-option v-for="m in ['GET','POST','PUT','DELETE','HEAD']" :key="m" :label="m" :value="m" />
          </el-select>
        </el-form-item>
        <el-form-item label="检查间隔(s)" prop="checkInterval">
          <el-input-number v-model="form.checkInterval" :min="10" :max="86400" style="width:100%" />
        </el-form-item>
        <el-form-item label="超时时间(s)" prop="timeout">
          <el-input-number v-model="form.timeout" :min="1" :max="60" style="width:100%" />
        </el-form-item>
        <el-form-item label="失败告警次数" prop="alertThreshold">
          <el-input-number v-model="form.alertThreshold" :min="1" :max="10" style="width:100%" />
        </el-form-item>
        <el-form-item label="请求头">
          <el-input v-model="form.headers" type="textarea" :rows="2"
            placeholder='{"Authorization": "Bearer token"}' />
        </el-form-item>
        <el-form-item label="请求体">
          <el-input v-model="form.body" type="textarea" :rows="3"
            placeholder='{"key": "value"}' />
        </el-form-item>
        <el-form-item label="钉钉Webhook">
          <el-input v-model="form.dingWebhook" placeholder="https://oapi.dingtalk.com/robot/send?access_token=..." />
        </el-form-item>
        <el-form-item label="启用">
          <el-switch v-model="form.enabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getApiList, createApi, updateApi, deleteApi, toggleApi } from '../api/index.js'

const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const formRef = ref()

const defaultForm = () => ({
  id: null, name: '', url: '', method: 'GET',
  checkInterval: 60, timeout: 10, alertThreshold: 3,
  headers: '', body: '', dingWebhook: '', enabled: 1
})
const form = ref(defaultForm())

const rules = {
  name: [{ required: true, message: '请输入接口名称', trigger: 'blur' }],
  url: [{ required: true, message: '请输入URL', trigger: 'blur' }],
  method: [{ required: true, message: '请选择请求方法', trigger: 'change' }],
}

const methodTagType = (method) => {
  const map = { GET: '', POST: 'success', PUT: 'warning', DELETE: 'danger' }
  return map[method] || 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getApiList({ page: page.value, pageSize: pageSize.value })
    if (res.code === 200) {
      tableData.value = res.data.records || res.data
      total.value = res.data.total || res.data.length
    }
  } finally {
    loading.value = false
  }
}

const openDialog = (row = null) => {
  form.value = row ? { ...row } : defaultForm()
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    const res = form.value.id ? await updateApi(form.value) : await createApi(form.value)
    if (res.code === 200) {
      ElMessage.success(form.value.id ? '更新成功' : '创建成功')
      dialogVisible.value = false
      loadData()
    }
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除接口「${row.name}」吗？`, '提示', { type: 'warning' })
  const res = await deleteApi(row.id)
  if (res.code === 200) {
    ElMessage.success('删除成功')
    loadData()
  }
}

const toggleEnabled = async (row) => {
  const res = await toggleApi(row.id, row.enabled)
  if (res.code === 200) {
    ElMessage.success(row.enabled === 1 ? '已启用' : '已禁用')
  } else {
    row.enabled = row.enabled === 1 ? 0 : 1
  }
}

onMounted(loadData)
</script>

<style scoped>
.api-list { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
