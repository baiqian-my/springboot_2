package org.example.filterinterceptor.controller;

import org.example.filterinterceptor.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "测试接口", description = "过滤器+拦截器综合案例测试接口")
public class TestController {

    private final JwtUtil jwtUtil;
    @GetMapping("/test")
    public String test() {
        return "hello test!!";
    }
}