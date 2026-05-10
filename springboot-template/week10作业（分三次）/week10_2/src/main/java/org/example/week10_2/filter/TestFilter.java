package org.example.week10_2.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class TestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("测试过滤器初始化完成");
    }

    @Override
    public void destroy() {
        log.info("测试过滤器销毁完成");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("执行 TestFilter 逻辑！");
//        log.info("请求路径: {}", servletRequest.getServletContext().getContextPath());

        filterChain.doFilter(servletRequest, servletResponse);

        log.info("响应完成");
    }
}
