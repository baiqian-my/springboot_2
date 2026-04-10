<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import type { SpecialItem } from '../types/special'
// 封面图占位图生成工具
import { coverPlaceholder } from '../utils/coverPlaceholder'

// 组件 Props：接收标准化后的专题项
const props = defineProps<{ item: SpecialItem }>()

// 计算属性：生成详情页路由跳转对象
const to = computed(() => ({
  name: 'special-detail' as const, // 路由名称（固定）
  params: { id: String(props.item.id ?? '') }, // 动态路由参数：专题ID
}))

// 生成封面占位图（使用标题生成）
const ph = () => coverPlaceholder(props.item.title)
</script>

<template>
  <!-- 自定义路由链接：使用 custom 实现完全自定义点击区域 -->
  <RouterLink v-slot="{ navigate }" :to="to" custom>
    <!-- 专题卡片容器 -->
    <li class="card-row" role="link" @click="navigate">
      <!-- 封面图区域 -->
      <div class="cover-wrap">
        <img
          class="cover"
          :src="item.cover || ph()"
          :alt="item.title"
          loading="lazy"
          @error="(e) => ((e.target as HTMLImageElement).src = ph())"
        />
      </div>

      <!-- 内容主体区域 -->
      <div class="body">
        <!-- 标题 -->
        <h2 class="card-title">{{ item.title }}</h2>
        <!-- 简介（有值才显示） -->
        <p v-if="item.introduction" class="intro">{{ item.introduction }}</p>
        <!-- 元信息：更新时间 + 浏览量 + 关注数 -->
        <p class="meta">
          <span>{{ item.updateLabel }}</span>
          <span class="dot">·</span>
          <span>{{ item.visitLabel }}</span>
          <span class="dot">·</span>
          <span>{{ item.followersLabel }}</span>
        </p>
        <!-- 关注按钮 -->
        <el-button
          class="follow-btn"
          round
          :class="{ 'follow-btn--following': item.isFollowing }"
          @click.stop.prevent
        >
          {{ item.isFollowing ? '已关注' : '关注专题' }}
        </el-button>
      </div>
    </li>
  </RouterLink>
</template>

<style scoped>
/* 专题卡片整体样式 */
.card-row {
  display: flex;
  gap: 20px;
  cursor: pointer;
  padding: 20px;
  margin-bottom: 12px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(18, 18, 18, 0.06);
  border: 1px solid #f0f0f0;
  list-style: none;
  transition: box-shadow 0.2s ease;
}
/* 卡片 hover 悬浮效果 */
.card-row:hover {
  box-shadow: 0 4px 12px rgba(18, 18, 18, 0.08);
}

/* 封面图容器 */
.cover-wrap {
  flex-shrink: 0;
  width: 200px;
  border-radius: 4px;
  overflow: hidden;
  background: #f6f6f6;
}
/* 封面图 */
.cover {
  display: block;
  width: 100%;
  height: 120px;
  object-fit: cover;
}

/* 内容主体 */
.body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  text-align: left;
}

/* 标题 */
.card-title {
  margin: 0 0 10px;
  font-size: 18px;
  font-weight: 600;
  color: #121212;
  line-height: 1.35;
}

/* 简介：最多显示2行，超出省略 */
.intro {
  margin: 0 0 10px;
  font-size: 14px;
  color: #646464;
  line-height: 1.55;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 底部元信息 */
.meta {
  margin: 0 0 16px;
  font-size: 14px;
  color: #8590a6;
  line-height: 1.5;
}
/* 分隔点 */
.dot {
  margin: 0 6px;
  opacity: 0.7;
}

/* 关注按钮默认样式 */
.follow-btn {
  --el-button-border-color: #0084ff;
  --el-button-text-color: #0084ff;
  --el-button-bg-color: #fff;
  --el-button-hover-text-color: #0084ff;
  --el-button-hover-bg-color: rgba(0, 132, 255, 0.06);
  --el-button-hover-border-color: #0084ff;
}
/* 已关注状态按钮样式 */
.follow-btn.follow-btn--following {
  --el-button-border-color: #ebebeb;
  --el-button-text-color: #8590a6;
  --el-button-bg-color: #f6f6f6;
  --el-button-hover-text-color: #8590a6;
  --el-button-hover-bg-color: #f0f0f0;
  --el-button-hover-border-color: #e0e0e0;
}

/* 移动端响应式适配 */
@media (max-width: 640px) {
  .card-row {
    flex-direction: column;
  }
  .cover-wrap {
    width: 100%;
  }
  .cover {
    height: 160px;
  }
}
</style>