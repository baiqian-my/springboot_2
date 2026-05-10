package org.example.week10_2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String test() {
        log.info("执行 test 接口");
        return "测试成功！过滤器已生效";
    }
    @GetMapping("/test/sleep")
    public String testSleep() throws InterruptedException {
        Thread.sleep(5000);
        return "请求完成";
    }
}
