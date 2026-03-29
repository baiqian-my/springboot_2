package org.example.springbootweek04.controller;


import org.example.springbootweek04.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.springbootweek04.common.Result;
import org.example.springbootweek04.entity.Team;

@RestController
@RequestMapping("/api/team")
@Slf4j
public class TeamController {

    @PostMapping("/add")
    public Result<String> addTeam(@Validated @RequestBody Team team, HttpServletRequest request) {
        log.info("添加团队：{}", team);

        String token = request.getHeader("token");
        if (token == null || token.isBlank()) {
            throw new BusinessException(401, "请先登录");
        }
        if (!"admin".equals(token)) {
            throw new BusinessException(401, "token无效");
        }
//        int a = 1 / 0;

        return Result.success("添加成功");
    }
}