package com.liuluyi.springbootweek03.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.liuluyi.springbootweek03.common.Result;
import com.liuluyi.springbootweek03.config.AppConfig;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class BaseConfigController {

    @Value("${server.port}")
    private Integer port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${spring.application.name}")
    private String applicationName;

    private final AppConfig appConfig;

    public BaseConfigController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GetMapping("/base")
    public Result<Map<String, Object>> getBaseConfigInfo() {
        Map<String, Object> map = Map.of(
                "port", port,
                "contextPath", contextPath,
                "applicationName", applicationName,
                "appName", appConfig.getName(),
                "version", appConfig.getVersion(),
                "description", appConfig.getDescription(),
                "published", appConfig.getPublished(),
                "features", appConfig.getFeatures(),
                "author", appConfig.getAuthor()
        );
        return Result.success(map);
    }
}
