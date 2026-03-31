import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import ApiList from '../views/ApiList.vue'
import ApiDetail from '../views/ApiDetail.vue'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard,
    meta: { title: 'Dashboard' }
  },
  {
    path: '/apis',
    name: 'ApiList',
    component: ApiList,
    meta: { title: '接口管理' }
  },
  {
    path: '/apis/:id',
    name: 'ApiDetail',
    component: ApiDetail,
    meta: { title: '接口详情' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
