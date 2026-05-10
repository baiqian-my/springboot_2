package org.example.week10_2.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CustomFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("自定义过滤器初始化完成");
    }

    @Override
    public void destroy() {
        log.info("自定义过滤器销毁完成");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("执行 CustomFilter2 逻辑！");
        log.info("在过滤器中处理请求");

        filterChain.doFilter(servletRequest, servletResponse);

        log.info("在过滤器中处理响应");
    }
}