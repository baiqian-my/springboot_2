package org.example.week10_2.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    /**
     * request 属性名：preHandle 写入起始时间，afterCompletion 读取计算耗时
     */
    public static final String ATTR_START_MS = "interceptor.log.startMs";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(ATTR_START_MS, System.currentTimeMillis());
        log.info("[日志拦截器] 请求进入 path={}, method={}, ip={}, time={}", request.getRequestURI(), request.getMethod(), request.getRemoteAddr(), LocalDateTime.now());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startMs = (Long) request.getAttribute(ATTR_START_MS);
        long cost = startMs != null ? System.currentTimeMillis() - startMs : -1L;
        log.info("[日志拦截器] 响应结束 path={}, status={}, 耗时={} ms, time={}, ex={}",
                request.getRequestURI(),
                response.getStatus(),
                cost,
                LocalDateTime.now(),
                ex != null ? ex.getMessage() : null);
        if (ex != null) {
            log.warn("[日志拦截器] 请求处理异常", ex);
        }
    }
}