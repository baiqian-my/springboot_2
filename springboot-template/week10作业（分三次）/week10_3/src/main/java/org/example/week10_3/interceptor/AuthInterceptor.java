package org.example.week10_3.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.week10_3.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        log.info("【拦截器】进入请求：{}", uri);

        if ("/api/login".equals(uri)) {
            return true;
        }

        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            log.warn("【拦截器】未登录，请求被拦截：{}", uri);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\"}");
            return false;
        }

        Claims claims = jwtUtil.parseToken(token);
        if (claims == null) {
            log.warn("【拦截器】Token无效/已过期：{}", uri);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"登录已过期，请重新登录\"}");
            return false;
        }

        String role = claims.get("role", String.class);
        if (uri.startsWith("/api/admin") && !"admin".equals(role)) {
            log.warn("【拦截器】权限不足，禁止访问管理员接口：{}", uri);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"权限不足，无法访问\"}");
            return false;
        }

        request.setAttribute("username", claims.get("username"));
        request.setAttribute("role", role);
        log.info("【拦截器】认证通过，用户：{}，角色：{}", claims.get("username"), role);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("【拦截器】Controller执行完成");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("【拦截器】请求完全处理完毕");
    }
}
