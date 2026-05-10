package org.example.filterinterceptor.config;

import org.example.filterinterceptor.filter.RateLimitFilter;
import org.example.filterinterceptor.filter.TestFilter;
import org.example.filterinterceptor.filter.TestFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRegistration() {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter());
        // 过滤请求
        registrationBean.addUrlPatterns("/api/hello");
        registrationBean.setOrder(1);
        registrationBean.setName("rateLimitFilter");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<TestFilter> testFilterRegistration() {
        FilterRegistrationBean<TestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TestFilter());
        // 匹配所有请求
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        registrationBean.setName("testFilter");
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<TestFilter2> testFilterRegistration2() {
        FilterRegistrationBean<TestFilter2> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TestFilter2());
        // 匹配所有请求
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        registrationBean.setName("testFilter2");
        return registrationBean;
    }
}