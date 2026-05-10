package org.example.week10_2.config;

import org.example.week10_2.filter.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TestFilter> testFilterRegistration() {
        // 创建 FilterRegistrationBean 对象
        FilterRegistrationBean<TestFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置过滤器
        registrationBean.setFilter(new TestFilter());
        // 设置拦截的 URL 路径
        registrationBean.addUrlPatterns("/api/test");
        // 设置过滤器的执行顺序
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CustomFilter> customFilterRegistration() {
        // 创建 FilterRegistrationBean 对象
        FilterRegistrationBean<CustomFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置过滤器
        registrationBean.setFilter(new CustomFilter());
        // 设置拦截的 URL 路径
        registrationBean.addUrlPatterns("/api/test");
        // 设置过滤器的执行顺序
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LogFilter> LogFilterRegistration() {
        // 创建 FilterRegistrationBean 对象
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置过滤器
        registrationBean.setFilter(new LogFilter());
        // 设置拦截的 URL 路径
        registrationBean.addUrlPatterns("/api/test");
        // 设置过滤器的执行顺序
        registrationBean.setOrder(3);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> AuthFilterRegistration() {
        // 创建 FilterRegistrationBean 对象
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置过滤器
        registrationBean.setFilter(new AuthFilter());
        // 设置拦截的 URL 路径
        registrationBean.addUrlPatterns("/api/test");
        // 设置过滤器的执行顺序
        registrationBean.setOrder(4);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.addUrlPatterns("/api/test");
        // 数值越小越先执行：必须早于所有的过滤器，否则未认证/预检请求在中间被拦截时根本不会走到本过滤器
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}