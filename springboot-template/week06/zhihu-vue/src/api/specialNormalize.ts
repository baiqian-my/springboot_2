import type { SpecialItem } from '../types/special'

/**
 * 安全将未知值转为普通对象（Record）
 * 仅当值是对象、非null、非数组时才返回对象，否则返回null
 * @param v 未知类型的值
 * @returns 普通对象 或 null
 */
export function asRecord(v: unknown): Record<string, unknown> | null {
  return v && typeof v === 'object' && !Array.isArray(v)
    ? (v as Record<string, unknown>)
    : null
}

/**
 * 从接口返回 payload 中智能提取列表数组（兼容多种字段名）
 * 优先取：list / records / rows / items / dataList
 * 其次取：data.list / data.records 等嵌套结构
 * @param payload 接口返回的原始数据
 * @returns 提取到的数组，无数据则返回空数组
 */
export function pickRows(payload: unknown): unknown[] {
  // 如果 payload 本身就是数组，直接返回
  if (Array.isArray(payload)) return payload

  // 转为根对象，转换失败则返回空数组
  const root = asRecord(payload)
  if (!root) return []

  // 定义常用列表字段名（通用列表接口命名）
  const keys = ['list', 'records', 'rows', 'items', 'dataList'] as const

  // 从对象中批量取出这些key对应的值
  const tryArrays = (o: Record<string, unknown>) => keys.map((k) => o[k])

  // 遍历根对象的列表字段，找到数组就返回
  for (const v of tryArrays(root)) {
    if (Array.isArray(v)) return v
  }

  // 兼容根节点下直接是 data 数组的情况
  if (Array.isArray(root.data)) return root.data

  // 尝试解析 data 嵌套对象
  const data = asRecord(root.data)
  if (data) {
    // 从 data 内部再次查找列表字段
    for (const v of tryArrays(data)) {
      if (Array.isArray(v)) return v
    }
  }

  // 都没找到，返回空数组
  return []
}

/**
 * 从接口返回中智能提取总数（total/totalCount）
 * 支持根节点 或 data 嵌套节点
 * @param payload 接口返回原始数据
 * @returns 合法数字总数，无数据则返回0
 */
export function pickTotal(payload: unknown): number {
  const root = asRecord(payload)
  if (!root) return 0

  // 尝试解析 data 字段为对象
  const dataRec = asRecord(root.data)

  // 依次检查可能的总数字段
  for (const c of [
    root.total,
    root.totalCount,
    dataRec?.total,
    dataRec?.totalCount,
  ]) {
    const n = Number(c)
    // 确保是有效非负数字
    if (Number.isFinite(n) && n >= 0) return n
  }

  return 0
}

/**
 * 详情接口：智能提取单条数据
 * 优先取 { data: {...} }，其次取根对象本身
 * 判断依据：包含 id 或 title 字段视为有效数据
 * @param payload 详情接口返回数据
 * @returns 单条对象 或 null
 */
export function pickOneRecord(payload: unknown): Record<string, unknown> | null {
  const root = asRecord(payload)
  if (!root) return null

  // 尝试取 data 内部对象
  const inner = asRecord(root.data)
  if (inner && ('id' in inner || 'title' in inner)) return inner

  // 否则判断根节点是否是有效数据
  if ('id' in root || 'title' in root) return root

  return null
}

/**
 * 格式化浏览量/关注数：显示万、千分位分隔
 * @param n 原始数字
 * @returns 格式化后字符串，无数据显示 —
 */
function formatVisit(n: unknown): string {
  const num = Number(n)
  // 非有效数字，返回占位符
  if (!Number.isFinite(num)) return String(n ?? '—')

  // 大于等于1万，显示 x.x万
  if (num >= 10_000) return `${(num / 10_000).toFixed(1)} 万`

  // 小于1万，显示千分位
  return num.toLocaleString('zh-CN')
}

/**
 * 格式化更新时间：时间戳 → YYYY-MM-DD 更新
 * 自动兼容秒/毫秒级时间戳
 * @param n 时间戳（秒/毫秒）
 * @returns 格式化文案 或 null（无效时间）
 */
function formatUpdatedField(n: unknown): string | null {
  const num = Number(n)
  if (!Number.isFinite(num) || num <= 0) return null

  // 小于1e12视为秒级时间戳，转毫秒
  const ms = num < 1e12 ? num * 1000 : num
  const d = new Date(ms)

  // 无效日期返回null
  if (Number.isNaN(d.getTime())) return null

  // 格式化日期
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')

  return `${y}-${m}-${day} 更新`
}

/**
 * 解析是否关注：兼容布尔、字符串、数字
 * @param v 原始值
 * @returns 布尔值是否关注
 */
function parseFollowing(v: unknown): boolean {
  if (typeof v === 'boolean') return v

  // 转小写并去空格，兼容多种传参
  const s = String(v).toLowerCase().trim()
  return s === 'true' || s === '1' || s === 'yes'
}

/**
 * 专题原始数据标准化：将后端杂乱字段映射为前端统一结构
 * 统一取标题、封面、ID、简介、更新时间、浏览量、关注数等
 * @param raw 后端返回的原始单条数据
 * @returns 标准化后的 SpecialItem
 */
export function normalizeItem(raw: Record<string, unknown>): SpecialItem {
  // 统一取标题：兼容多种命名
  const title = String(
    raw.title ?? raw.name ?? raw.specialTitle ?? '未命名专题',
  )

  // 统一取封面图：兼容多种字段
  const cover = String(
    raw.banner ??
      raw.bannerUrl ??
      raw.cover ??
      raw.coverUrl ??
      raw.image ??
      raw.pic ??
      raw.avatar ??
      '',
  )

  // 统一取ID
  const id = raw.id ?? raw.specialId ?? raw.topicId

  // 简介
  const introduction = String(raw.introduction ?? '').trim()

  // 格式化更新时间标签
  const updatedTs = formatUpdatedField(raw.updated)

  // 备用更新时间原始值
  const updateRaw =
    raw.updateTime ??
    raw.updatedAt ??
    raw.updateDate ??
    raw.gmtModified ??
    raw.lastUpdate

  // 最终展示的更新标签
  let updateLabel = '—'
  if (updatedTs) updateLabel = updatedTs
  else if (updateRaw != null && updateRaw !== '') {
    const s = String(updateRaw)
    updateLabel = s.includes('更新') ? s : `${s} 更新`
  }

  // 统一取浏览量：兼容 viewCount 对象嵌套 views
  const vc = raw.viewCount
  const visit =
    (typeof vc === 'object' && vc !== null && 'views' in (vc as object)
      ? (vc as { views?: unknown }).views
      : vc) ??
    raw.views ??
    raw.readCount ??
    raw.visitCount ??
    raw.count

  // 统一取关注数
  const followers = raw.followersCount ?? raw.followers ?? raw.followCount

  // 返回标准化结构
  return {
    id: id as string | number | undefined,
    title,
    cover,
    introduction,
    isFollowing: parseFollowing(raw.isFollowing),
    updateLabel,
    visitLabel: `${formatVisit(visit)} 次浏览`,
    followersLabel: `${formatVisit(followers)} 关注`,
  }
}