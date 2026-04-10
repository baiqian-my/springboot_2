<script setup lang="ts">
// Vue Router 路由视图容器
// 用于渲染当前路由匹配到的页面组件
import { RouterView } from 'vue-router'
</script>

<template>
  <!-- 路由出口：匹配到的页面组件将在这里渲染 -->
  <RouterView />
</template>

<style>
/* 全局根节点 #app 样式重置与基础布局 */
#app {
  width: 100%;                /* 全屏宽度 */
  max-width: none;            /* 取消最大宽度限制 */
  margin: 0;                  /* 清除默认外边距 */
  text-align: initial;        /* 重置文本对齐 */
  border: none;               /* 清除边框 */
  min-height: 100vh;          /* 最小高度撑满视口 */
  background: #f6f6f6;        /* 页面全局背景色 */
}
</style>