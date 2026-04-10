<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
// 专题列表接口请求方法
import { fetchSpecialByTitle } from '../api/special'
// 专题数据类型
import type { SpecialItem } from '../types/special'
// 请求错误信息格式化工具
import { axiosErrorMessage } from '../utils/httpError'
// 专题卡片子组件
import SpecialCard from '../components/SpecialCard.vue'

// 搜索关键词
const titleQuery = ref('')
// 当前页码
const pageNum = ref(1)
// 每页条数
const pageSize = ref(10)
// 加载状态（控制骨架屏/按钮禁用）
const loading = ref(false)
// 专题列表数据
const list = ref<SpecialItem[]>([])
// 总条数
const total = ref(0)

// 计算属性：总条数字符串（带千分位）
const totalText = computed(() =>
  total.value > 0 ? `共有 ${total.value.toLocaleString('zh-CN')} 个专题` : '',
)

/**
 * 加载列表数据（搜索 + 分页通用）
 * 1. 开启loading
 * 2. 请求接口
 * 3. 赋值列表/总数
 * 4. 异常捕获并提示
 * 5. 关闭loading
 */
async function load() {
  loading.value = true
  try {
    const res = await fetchSpecialByTitle({
      title: titleQuery.value.trim() || '',
      pageNum: pageNum.value,
      pageSize: pageSize.value,
    })
    list.value = res.list
    total.value = res.total
  } catch (e) {
    console.error(e)
    ElMessage.error(axiosErrorMessage(e, '加载失败'))
    list.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

/**
 * 搜索事件：重置为第1页并重新加载
 */
function onSearch() {
  pageNum.value = 1
  load()
}

/**
 * 每页条数改变：重置页码为1并重新加载
 */
function onSizeChange(s: number) {
  pageSize.value = s
  pageNum.value = 1
  load()
}

// 页面挂载后自动加载第一页数据
onMounted(load)
</script>

<template>
  <div class="page">
    <!-- 页面头部：标题 + 搜索栏 -->
    <header class="head">
      <h1 class="title-all">全部专题</h1>
      <p v-if="totalText" class="sub">{{ totalText }}</p>
      <div class="toolbar">
        <el-input
          v-model="titleQuery"
          class="search"
          clearable
          placeholder="关键词，如：安全"
          @keyup.enter="onSearch"
        />
        <el-button type="primary" @click="onSearch">搜索</el-button>
      </div>
    </header>

    <!-- 骨架屏 + 列表区域 -->
    <el-skeleton :loading="loading" animated :count="3">
      <!-- 加载中骨架模板 -->
      <template #template>
        <div class="sk">
          <el-skeleton-item variant="image" style="width: 200px; height: 120px" />
          <div style="flex: 1; padding-left: 20px">
            <el-skeleton-item variant="h3" style="width: 50%" />
            <el-skeleton-item variant="text" style="margin-top: 16px" />
          </div>
        </div>
      </template>

      <!-- 加载完成：显示列表 / 空状态 -->
      <template #default>
        <ul class="list">
          <SpecialCard
            v-for="it in list"
            :key="String(it.id ?? it.title)"
            :item="it"
          />
        </ul>
        <el-empty v-if="!loading && list.length === 0" description="暂无数据" />
      </template>
    </el-skeleton>

    <!-- 分页组件：有数据时显示 -->
    <footer v-if="total > 0" class="pager">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        background
        @current-change="load"
        @size-change="onSizeChange"
      />
    </footer>
  </div>
</template>

<style scoped>
/* 页面容器：居中、最大宽度、内边距 */
.page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px 20px 48px;
  box-sizing: border-box;
}
/* 头部区域 */
.head {
  margin-bottom: 28px;
}
/* 主标题 */
.title-all {
  margin: 0 0 8px;
  font-size: 26px;
  font-weight: 600;
  color: #1a1a1a;
}
/* 总数副标题 */
.sub {
  margin: 0 0 20px;
  font-size: 14px;
  color: #8590a6;
}
/* 搜索栏容器 */
.toolbar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
/* 搜索框宽度 */
.search {
  width: min(420px, 100%);
}
/* 列表清除默认样式 */
.list {
  margin: 0;
  padding: 0;
}
/* 骨架屏项样式 */
.sk {
  display: flex;
  padding: 20px;
  margin-bottom: 12px;
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
}
/* 分页居中 */
.pager {
  display: flex;
  justify-content: center;
  margin-top: 28px;
}
</style>