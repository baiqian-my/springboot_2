<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
// 根据ID获取专题详情接口
import { fetchSpecialById } from '../api/special'
// 专题类型定义
import type { SpecialItem } from '../types/special'
// 请求错误提示格式化
import { axiosErrorMessage } from '../utils/httpError'
// 封面图占位图生成工具
import { coverPlaceholder } from '../utils/coverPlaceholder'

// 路由实例：获取当前路由参数
const route = useRoute()
// 路由跳转：返回列表页
const router = useRouter()

// 加载状态（控制骨架屏）
const loading = ref(true)
// 详情数据
const item = ref<SpecialItem | null>(null)

/**
 * 根据ID加载专题详情
 * @param id 专题ID
 */
async function load(id: string) {
  // 无ID直接清空数据
  if (!id) {
    item.value = null
    loading.value = false
    return
  }

  loading.value = true
  try {
    // 请求接口获取标准化详情数据
    item.value = await fetchSpecialById(id)
    // 未查询到数据给出提示
    if (!item.value) ElMessage.warning('未找到该专题')
  } catch (e) {
    console.error(e)
    ElMessage.error(axiosErrorMessage(e, '加载详情失败'))
    item.value = null
  } finally {
    // 无论成功失败，关闭loading
    loading.value = false
  }
}

// 页面挂载：根据路由ID加载数据
onMounted(() => load(String(route.params.id ?? '')))

// 监听路由ID变化：切换详情时自动刷新
watch(
  () => route.params.id,
  (id) => load(String(id ?? '')),
)

/** 返回专题列表页 */
function back() {
  router.push({ name: 'special-list' })
}

/** 生成详情页大图占位图 */
function ph(t: string) {
  return coverPlaceholder(t, 800, 260)
}
</script>

<template>
  <div class="page">
    <!-- 返回按钮 -->
    <el-button link type="primary" class="back" @click="back">← 返回列表</el-button>

    <!-- 骨架屏 + 详情内容 -->
    <el-skeleton :loading="loading" animated>
      <!-- 加载中骨架模板 -->
      <template #template>
        <el-skeleton-item variant="image" style="width: 100%; height: 200px" />
        <el-skeleton-item variant="h1" style="margin-top: 16px; width: 60%" />
        <el-skeleton-item variant="text" style="margin-top: 12px" />
      </template>

      <!-- 加载完成：显示详情 / 空状态 -->
      <template #default>
        <template v-if="item">
          <!-- 封面大图 -->
          <div class="banner-wrap">
            <img
              class="banner"
              :src="item.cover || ph(item.title)"
              :alt="item.title"
              @error="(e) => ((e.target as HTMLImageElement).src = ph(item!.title))"
            />
          </div>
          <!-- 标题 -->
          <h1 class="title">{{ item.title }}</h1>
          <!-- 元信息：更新时间 + 浏览 + 关注 -->
          <p class="meta">
            {{ item.updateLabel }} · {{ item.visitLabel }} · {{ item.followersLabel }}
          </p>
          <!-- 简介 -->
          <p v-if="item.introduction" class="intro">{{ item.introduction }}</p>
          <!-- 关注按钮 -->
          <el-button round :class="item.isFollowing ? 'fol-on' : 'fol'">
            {{ item.isFollowing ? '已关注' : '关注专题' }}
          </el-button>
        </template>

        <!-- 无数据空状态 -->
        <el-empty v-else-if="!loading" description="无数据" />
      </template>
    </el-skeleton>
  </div>
</template>

<style scoped>
/* 页面容器：居中、最大宽度 */
.page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px 20px 48px;
  box-sizing: border-box;
}
/* 返回按钮间距 */
.back {
  margin-bottom: 16px;
}
/* 封面图容器 */
.banner-wrap {
  border-radius: 6px;
  overflow: hidden;
  background: #f0f0f0;
}
/* 封面大图 */
.banner {
  display: block;
  width: 100%;
  height: 260px;
  object-fit: cover;
}
/* 标题样式 */
.title {
  margin: 20px 0 8px;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
}
/* 底部信息 */
.meta {
  margin: 0 0 16px;
  font-size: 14px;
  color: #8590a6;
}
/* 简介 */
.intro {
  margin: 0 0 20px;
  font-size: 15px;
  line-height: 1.6;
  color: #444;
}
/* 关注按钮-未关注 */
.fol {
  --el-button-border-color: #0084ff;
  --el-button-text-color: #0084ff;
}
/* 关注按钮-已关注 */
.fol-on {
  --el-button-text-color: #8590a6;
}
</style>