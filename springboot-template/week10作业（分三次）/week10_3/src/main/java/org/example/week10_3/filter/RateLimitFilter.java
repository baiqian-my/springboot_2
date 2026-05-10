package org.example.week10_3.filter;

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
    private static final Map<String, AtomicInteger> IP_COUNT_MAP = new ConcurrentHashMap<>();
    private static final int MAX_COUNT = 10;
    private static final long WINDOW_TIME = 60 * 1000;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("限流过滤器初始化完成");
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String ip = req.getRemoteAddr();
        String uri = req.getRequestURI();
        String method = req.getMethod();

        log.info("【过滤器】请求日志 - IP:{}, 请求方式：{}, 请求路径：{}", ip, method, uri);

        AtomicInteger count = IP_COUNT_MAP.getOrDefault(ip, new AtomicInteger(0));
        int currentCount = count.incrementAndGet();
        IP_COUNT_MAP.put(ip, count);

        if (currentCount > MAX_COUNT) {
            log.warn("【过滤器】IP:{}访问频率过高，已限流", ip);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"code\":429,\"msg\":\"请求过于频繁，请1分钟后再试\"}");
            resp.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }

        chain.doFilter(request, response);

        log.info("【过滤器】响应已返回 - IP:{}，路径：{}", ip, uri);
    }

    @Override
    public void destroy() {
        log.info("限流过滤器已销毁");
    }
}
