package org.example.week10_3.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.week10_3.util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public String login(String username, String password) {
        if ("admin".equals(username) && "123456".equals(password)) {
            String token = jwtUtil.generateToken(username, "admin");
            return "登录成功！管理员Token:" + token;
        }
        if ("user".equals(username) && "123456".equals(password)) {
            String token = jwtUtil.generateToken(username, "user");
            return "登录成功！普通用户Token:" + token;
        }
        return "用户名或密码错误";
    }

    @GetMapping("/user/info")
    public String userInfo(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");
        return "当前登录用户：" + username + ",角色：" + role;
    }

    @GetMapping("/admin/list")
    public String adminList() {
        return "管理员数据：用户列表、系统配置";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
