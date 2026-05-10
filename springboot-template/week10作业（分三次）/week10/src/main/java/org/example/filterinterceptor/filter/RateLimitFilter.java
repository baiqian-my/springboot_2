package org.example.filterinterceptor.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RateLimitFilter implements Filter {

    // 存储IP访问次数（线程安全）
    private static final Map<String, AtomicInteger> IP_COUNT_MAP = new ConcurrentHashMap<>();
    // 最大访问次数
    private static final int MAX_COUNT = 10;
    // 时间窗口：1分钟（毫秒）
    private static final long WINDOW_TIME = 60 * 1000;

    // 初始化：项目启动时执行一次
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("限流过滤器初始化完成");
        // 定时清空Map（防止内存溢出）
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(WINDOW_TIME);
                    IP_COUNT_MAP.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 核心过滤逻辑
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 1. 获取请求IP
        String ip = req.getRemoteAddr();
        String uri = req.getRequestURI();
        String method = req.getMethod();

        // 2. 打印请求日志
        log.info("【过滤器】请求日志 -> IP:{}, 请求方式:{}, 请求路径:{}", ip, method, uri);

        // 3. 限流逻辑
        AtomicInteger count = IP_COUNT_MAP.getOrDefault(ip, new AtomicInteger(0));
        int currentCount = count.incrementAndGet();
        IP_COUNT_MAP.put(ip, count);

        // 超过最大次数，直接返回错误
        if (currentCount > MAX_COUNT) {
            log.warn("【过滤器】IP:{} 访问频率过高，已限流", ip);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"code\":429,\"msg\":\"请求过于频繁，请1分钟后再试\"}");
            resp.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }

        // 4. 放行请求（进入下一个过滤器/Controller）
        chain.doFilter(request, response);

        // 5. 响应返回后执行（后置处理）
        log.info("【过滤器】响应已返回 -> IP:{}, 路径:{}", ip, uri);
    }

    // 销毁：项目停止时执行
    @Override
    public void destroy() {
        log.info("限流过滤器已销毁");
    }
}