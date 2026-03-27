package com.liuluyi.springbootweek03.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class AppConfigTest {

    @Autowired
    private AppConfig appConfig;

    @Test
    void testAppConfig() {
        log.info("应用名称：{}", appConfig.getName());
        log.info("版本号：{}", appConfig.getVersion());
        log.info("描述：{}", appConfig.getDescription());
        log.info("发布状态：{}", appConfig.getPublished());
        log.info("特性：{}", appConfig.getFeatures());

    }
}
