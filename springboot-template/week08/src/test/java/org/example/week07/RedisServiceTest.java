package org.example.week07;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * RedisService 测试类
 * 测试 StringRedisTemplate 模板的基本操作
 */
@SpringBootTest
@Slf4j
public class RedisServiceTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 测试 StringRedisTemplate 模板的基本操作
     */
    @Test
    public void testStringTemplate() throws Exception {
        // 测试字符串操作
        stringRedisTemplate.opsForValue().set("hello", "world", 30, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set("code:13900001111", "1234");
        stringRedisTemplate.opsForValue().set("code:13900002222", "8899");

        // 取值
        String value = stringRedisTemplate.opsForValue().get("hello");
        log.info("Redis 字符串测试结果：{}", value);
        String code = stringRedisTemplate.opsForValue().get("code:13900001111");
        log.info("13900001111 验证码测试结果：{}", code);
        String code2 = stringRedisTemplate.opsForValue().get("code:13900002222");
        log.info("13900002222 验证码测试结果：{}", code2);
    }
}
