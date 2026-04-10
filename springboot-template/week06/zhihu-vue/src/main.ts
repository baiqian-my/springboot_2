// Vue 核心创建应用方法
import { createApp } from 'vue'

// Element Plus 组件库 + 中文语言包
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

// Element Plus 全局样式
import 'element-plus/dist/index.css'

// 根组件
import App from './App.vue'

// 路由配置
import router from './router'

// 创建 Vue 应用实例
const app = createApp(App)

// 注册路由
app.use(router)

// 注册 Element Plus 并配置中文
app.use(ElementPlus, { locale: zhCn })

// 将应用挂载到页面 #app 节点
app.mount('#app')