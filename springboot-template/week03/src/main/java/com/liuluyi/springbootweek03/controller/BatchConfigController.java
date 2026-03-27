package com.liuluyi.springbootweek03.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.liuluyi.springbootweek03.common.Result;
import com.liuluyi.springbootweek03.config.AppConfig;

@RestController
@RequestMapping("/config")
public class BatchConfigController {

    @Resource
    private AppConfig appConfig;

    @RequestMapping("/batch")
    public Result<AppConfig> getBatchConfigInfo() {
        return Result.success(appConfig);
    }
}