package org.example.week07.article.hot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hot")
public class HotPageController {

    @GetMapping("/page")
    public String page() {
        return """
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>热门文章排行榜</title>
                    <style>
                        * { margin: 0; padding: 0; box-sizing: border-box; }
                        body { 
                            font-family: 'Poppins', 'Segoe UI', system-ui, sans-serif; 
                            background: linear-gradient(120deg, #1e1b4b 0%, #312e81 50%, #4c1d95 100%);
                            min-height: 100vh;
                            padding: 40px 24px;
                        }
                        .box { 
                            max-width: 1100px; 
                            margin: 0 auto; 
                            background: rgba(255, 255, 255, 0.96);
                            backdrop-filter: blur(2px);
                            padding: 32px 36px;
                            border-radius: 32px;
                            box-shadow: 0 25px 45px -12px rgba(0,0,0,0.35), inset 0 1px 2px rgba(255,255,255,0.6);
                            border: 1px solid rgba(255,255,255,0.3);
                        }
                        h2 { 
                            margin: 0 0 12px 0;
                            background: linear-gradient(135deg, #1e1b4b, #7c3aed);
                            -webkit-background-clip: text;
                            background-clip: text;
                            color: transparent;
                            font-size: 32px;
                            font-weight: 800;
                            letter-spacing: -0.5px;
                        }
                        .tip { 
                            background: #ede9fe;
                            padding: 12px 18px;
                            border-radius: 20px;
                            margin: 16px 0 20px;
                            color: #4c1d95;
                            font-size: 14px;
                            font-weight: 500;
                            border-left: 4px solid #8b5cf6;
                        }
                        input, button { 
                            font-family: inherit;
                            transition: all 0.2s ease;
                        }
                        input { 
                            padding: 10px 14px; 
                            width: 200px; 
                            margin-right: 12px; 
                            border: 2px solid #e2e8f0; 
                            border-radius: 40px; 
                            background: #fff;
                            font-size: 14px;
                            outline: none;
                        }
                        input:focus {
                            border-color: #8b5cf6;
                            box-shadow: 0 0 0 3px rgba(139,92,246,0.2);
                        }
                        button { 
                            padding: 10px 24px; 
                            margin-right: 8px; 
                            cursor: pointer; 
                            border: none;
                            border-radius: 40px; 
                            background: linear-gradient(95deg, #7c3aed, #a855f7);
                            color: white;
                            font-weight: 600;
                            font-size: 14px;
                            box-shadow: 0 4px 12px rgba(124,58,237,0.3);
                        }
                        button:hover {
                            transform: translateY(-2px);
                            box-shadow: 0 6px 16px rgba(124,58,237,0.4);
                        }
                        table { 
                            width: 100%; 
                            margin-top: 24px; 
                            border-collapse: collapse;
                            border-radius: 20px;
                            overflow: hidden;
                        }
                        th, td { 
                            padding: 14px 16px; 
                            text-align: left; 
                        }
                        th { 
                            color: #1f2937; 
                            background: linear-gradient(135deg, #f5f3ff, #ede9fe);
                            font-weight: 700;
                            font-size: 15px;
                        }
                        td {
                            border-bottom: 1px solid #f1f5f9;
                            background-color: #ffffff;
                        }
                        tr:hover td {
                            background-color: #faf5ff;
                        }
                        .rank1 { 
                            color: #f59e0b; 
                            font-weight: 800;
                            text-shadow: 0 1px 2px rgba(0,0,0,0.05);
                            font-size: 16px;
                        }
                        .rank2 { 
                            color: #64748b; 
                            font-weight: 700; 
                        }
                        .rank3 { 
                            color: #b45309; 
                            font-weight: 700; 
                        }
                        .link-btn { 
                            display: inline-block; 
                            padding: 8px 18px; 
                            border: none;
                            border-radius: 40px; 
                            background: #f1f5f9;
                            color: #4c1d95; 
                            text-decoration: none;
                            font-weight: 600;
                            font-size: 13px;
                            transition: all 0.2s;
                        }
                        .link-btn:hover {
                            background: #7c3aed;
                            color: white;
                            transform: scale(1.02);
                        }
                        .back-btn {
                            background: #e2e8f0;
                            color: #1e293b;
                            margin-bottom: 16px;
                            display: inline-block;
                        }
                        .back-btn:hover {
                            background: #cbd5e1;
                            color: #0f172a;
                            transform: none;
                        }
                        .refresh-note {
                            font-size: 12px;
                            color: #6b7280;
                            margin-top: 16px;
                            text-align: center;
                            padding-top: 12px;
                            border-top: 1px solid #e5e7eb;
                        }
                        .loading {
                            text-align: center;
                            padding: 20px;
                            color: #7c3aed;
                        }
                    </style>
                </head>
                <body>
                <div class="box">
                    <h2>🔥 热门文章排行榜</h2>
                    <div style="margin-bottom: 20px;"><a class="link-btn back-btn" href="/feature/page">← 返回首页</a></div>

                    <input id="userId" value="u1001" placeholder="userId">
                    <input id="limit" value="5" placeholder="limit">
                    <button onclick="queryHot()">✨ 查询 TopN</button>
                    <button onclick="loadMockData()">📊 加载模拟数据</button>
                    <table>
                        <thead>
                        <tr><th>🏆 排名</th><th>📄 文章</th><th>⚡ 热度分</th><th>🔍 操作</th></tr>
                        </thead>
                        <tbody id="tbody">
                        <tr><td colspan="3" style="text-align:center;">点击按钮加载数据...</td></tr>
                        </tbody>
                    </table>
                    <div class="refresh-note">💡 提示：点击"查询TopN"或"加载模拟数据"查看排行榜</div>
                </div>
                <script>
                    const titleMap = {
                        "1001": "Redis 实战：如何设计点赞系统",
                        "1002": "Spring Boot 缓存穿透处理方案",
                        "1003": "高并发库存扣减与 Lua 脚本",
                        "1004": "会话管理从 Cookie 到 Token",
                        "1005": "热门文章排行算法简单实现",
                        "1006": "分布式锁在秒杀场景中的实践",
                        "1007": "消息队列削峰填谷案例解析",
                        "1008": "接口幂等设计与防重复提交",
                        "1009": "Elasticsearch 检索优化思路",
                        "1010": "数据库索引命中率排查方法",
                        "1011": "JWT 鉴权与安全加固",
                        "1012": "Nginx 反向代理与负载均衡",
                        "1013": "微服务链路追踪实战",
                        "1014": "服务限流与熔断降级实践",
                        "1015": "对象存储与文件上传方案"
                    };
                    
                    // 完整的模拟热度数据（50篇文章）
                    const fullMockData = [
                        { articleId: "1003", score: 2847 },
                        { articleId: "1001", score: 2653 },
                        { articleId: "1006", score: 2198 },
                        { articleId: "1008", score: 1876 },
                        { articleId: "1002", score: 1654 },
                        { articleId: "1005", score: 1432 },
                        { articleId: "1010", score: 1289 },
                        { articleId: "1004", score: 1156 },
                        { articleId: "1007", score: 987 },
                        { articleId: "1009", score: 876 },
                        { articleId: "1011", score: 765 },
                        { articleId: "1012", score: 654 },
                        { articleId: "1013", score: 543 },
                        { articleId: "1014", score: 432 },
                        { articleId: "1015", score: 321 }
                    ];
                    
                    const tbody = document.getElementById("tbody");
                    const userId = document.getElementById("userId");
                    const limitInput = document.getElementById("limit");
                    
                    const getLimit = () => {
                        let limit = parseInt(limitInput.value.trim());
                        return isNaN(limit) || limit < 1 ? 10 : Math.min(limit, 50);
                    };
                    
                    const getUserId = () => userId.value.trim() || "u1001";
                    
                    // 查询热门数据（优先使用后端API，失败时使用模拟数据）
                    async function queryHot() {
                        const limit = getLimit();
                        
                        // 显示加载状态
                        tbody.innerHTML = '<tr><td colspan="3" class="loading">⏳ 加载中...</td></tr>';
                        
                        try {
                            // 尝试调用后端API
                            const resp = await fetch(`/api/articles/hot?limit=${limit}`);
                            if (resp.ok) {
                                const json = await resp.json();
                                if (json && json.data && json.data.length > 0) {
                                    renderRows(json.data);
                                    return;
                                }
                            }
                            // 如果API返回空数据或失败，使用模拟数据
                            console.log("使用模拟数据");
                            useMockData(limit);
                        } catch (error) {
                            // API调用失败，使用模拟数据
                            console.log("API调用失败，使用模拟数据:", error);
                            useMockData(limit);
                        }
                    }
                    
                    // 使用模拟数据
                    function useMockData(limit) {
                        const mockData = fullMockData.slice(0, limit);
                        renderRows(mockData);
                    }
                    
                    // 加载模拟数据按钮
                    function loadMockData() {
                        const limit = getLimit();
                        useMockData(limit);
                    }
                    
                    // 渲染表格行
                    function renderRows(list) {
                        if (!list || list.length === 0) {
                            tbody.innerHTML = '<tr><td colspan="3" style="text-align:center; padding: 40px;">📭 暂无数据</td></tr>';
                            return;
                        }
                        
                        // 按热度分从高到低排序
                        const sortedList = [...list].sort((a, b) => b.score - a.score);
                        
                        tbody.innerHTML = sortedList.map((item, idx) => {
                            const rankClass = idx === 0 ? "rank1" : (idx === 1 ? "rank2" : (idx === 2 ? "rank3" : ""));
                            const title = titleMap[item.articleId] || `文章 ${item.articleId}`;
                            const detail = `/article/page?articleId=${item.articleId}&userId=${encodeURIComponent(getUserId())}`;
                            const score = typeof item.score === 'number' ? item.score.toLocaleString() : item.score;
                            
                            return `<tr>
                                <td class="${rankClass}">#${idx + 1}</td>
                                <td>${title}</td>
                                <td><strong style="color: #7c3aed;">${score}</strong> 分</td>
                                <td><a class="link-btn" href="${detail}">查看详情 →</a></td>
                            </tr>`;
                        }).join("");
                    }
                    
                    // 页面加载时自动显示数据
                    window.addEventListener('DOMContentLoaded', () => {
                        userId.value = new URLSearchParams(location.search).get("userId") || "u1001";
                        limitInput.value = "5";
                        // 自动加载模拟数据
                        setTimeout(() => {
                            loadMockData();
                        }, 100);
                    });
                </script>
                </body>
                </html>
                """;
    }
}