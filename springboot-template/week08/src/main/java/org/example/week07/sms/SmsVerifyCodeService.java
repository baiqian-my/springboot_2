package org.example.week07.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.example.week07.sms.dto.SendCodeResponse;
import org.example.week07.util.RedisUtil;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class SmsVerifyCodeService {

    /** 验证码有效时间（秒） */
    public static final int DEFAULT_TTL_SECONDS = 5 * 60;

    private final RedisUtil redisUtil;

    /**
     * 生成验证码并写入 Redis。生产环境应在调用短信平台发送后或发送成功后再落库/写缓存，此处为模拟发短信
     */
    public SendCodeResponse sendCode(String phone) {
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
        String key = SmsRedisKey.ofPhone(phone);
        redisUtil.set(key, code, DEFAULT_TTL_SECONDS, TimeUnit.SECONDS);
        return new SendCodeResponse(phone, DEFAULT_TTL_SECONDS, code);
    }


    public boolean validateCode(String phone, String input) {
        String key = SmsRedisKey.ofPhone(phone);
        Object raw = redisUtil.get(key);
        if (raw == null) {
            return false;
        }
        String cached = Objects.toString(raw, "");
        if (cached.isEmpty() || !cached.equals(input)) {
            return false;
        }
        redisUtil.delete(key);
        return true;
    }
}