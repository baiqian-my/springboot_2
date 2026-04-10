import { createRouter, createWebHistory } from 'vue-router'
// 专题列表页组件
import SpecialListView from '../views/SpecialListView.vue'
// 专题详情页组件
import SpecialDetailView from '../views/SpecialDetailView.vue'

// 创建 Vue Router 实例（使用 HTML5 历史模式）
export default createRouter({
  // 路由模式：history 模式（不带 #）
  history: createWebHistory(),
  
  // 路由配置表
  routes: [
    // 根路径自动重定向到专题列表页
    { path: '/', redirect: '/special' },
    
    // 专题列表路由
    {
      path: '/special',
      name: 'special-list',       // 路由名称（用于编程式导航）
      component: SpecialListView  // 列表页组件
    },
    
    // 专题详情路由（带动态 id 参数）
    {
      path: '/special/:id',       // 动态路由参数：id = 专题ID
      name: 'special-detail',     // 路由名称
      component: SpecialDetailView // 详情页组件
    }
  ]
})