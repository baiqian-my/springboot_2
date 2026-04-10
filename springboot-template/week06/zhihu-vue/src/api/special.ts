import { httpGet } from '../utils/http'
import {
  normalizeItem,
  pickOneRecord,
  pickRows,
  pickTotal,
  asRecord,
} from './specialNormalize'
import type { SpecialItem, SpecialPageResult } from '../types/special'

/**
 * 根据专题标题【模糊搜索】+ 分页查询专题列表
 * 自动处理后端返回数据结构，统一返回标准化列表 + 总数
 * @param params 查询参数：标题 + 页码 + 每页条数
 * @returns 标准化分页结果 { list: SpecialItem[], total: number }
 */
export async function fetchSpecialByTitle(params: {
  title: string
  pageNum: number
  pageSize: number
}): Promise<SpecialPageResult> {
  // 调用 GET 接口，获取原始未知结构数据
  const data = await httpGet<unknown>('/v1/special/page', {
    params: {
      title: params.title,
      pageNum: params.pageNum,
      pageSize: params.pageSize,
    },
  })

  // 数据处理流水线：
  // 1. pickRows：从返回值中智能提取列表数组
  // 2. asRecord：确保每一项都是对象，过滤非对象
  // 3. filter(Boolean)：剔除 null/undefined 项
  // 4. normalizeItem：统一格式化为前端专用 SpecialItem
  const list = pickRows(data)
    .map((r) => asRecord(r))
    .filter(Boolean)
    .map((r) => normalizeItem(r!))

  // 提取总数
  let total = pickTotal(data)

  // 兼容兜底：后端没返回总数，但列表有数据时，用列表长度当总数
  if (total <= 0 && list.length > 0) total = list.length

  // 返回标准化分页数据
  return { list, total }
}

/**
 * 根据专题ID【查询专题详情】
 * 自动兼容后端多种返回结构 { data: {...} } / 扁平对象
 * @param id 专题ID
 * @returns 标准化详情对象 / null（无数据）
 */
export async function fetchSpecialById(id: string): Promise<SpecialItem | null> {
  // 请求详情接口，获取原始数据
  const data = await httpGet<unknown>('/v1/special/detail', {
    params: { id },
  })

  // 智能提取单条记录
  const row = pickOneRecord(data)

  // 存在记录则标准化，否则返回 null
  return row ? normalizeItem(row) : null
}