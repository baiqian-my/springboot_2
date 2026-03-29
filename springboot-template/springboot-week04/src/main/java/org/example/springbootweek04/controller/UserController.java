package org.example.springbootweek04.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.springbootweek04.common.Result;
import org.example.springbootweek04.entity.User;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @GetMapping("/info")
    public Result<User> getUserInfo() {
        User user = new User();
        user.setId(1234567890123456789L);
        user.setUsername("springmvc-student");
        user.setCreateTime(LocalDateTime.now());
        return Result.success(user);
    }
}