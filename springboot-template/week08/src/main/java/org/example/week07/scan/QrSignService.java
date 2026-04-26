package org.example.week07.scan;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class QrSignService {

    private final StringRedisTemplate redisTemplate;


    public String generateQrCode(String activityId) {
        // 1. 生成6位签到码
        String signCode;
        do {
            signCode = RandomUtil.randomStringUpper(6);
        } while (redisTemplate.hasKey(SignConstant.SIGN_CODE_KEY + signCode));

        // 2. 存入Redis 10分钟
        redisTemplate.opsForValue().set(SignConstant.SIGN_CODE_KEY + signCode, activityId, SignConstant.EXPIRE_MIN, TimeUnit.MINUTES);

        // 3. 扫码地址：改成你自己的服务器公网/内网地址
        String scanUrl = "http://192.168.0.104:8080/sign/scan?code=" + signCode;

        // 4. Hutool 生成二维码转base64
        byte[] bytes = QrCodeUtil.generatePng(scanUrl, 300, 300);
        String base64 = Base64.getEncoder().encodeToString(bytes);

        // 返回二维码base64
        return "data:image/png;base64," + base64;
    }

    /**
     * 手机扫码签到校验
     */
    public boolean checkSignCode(String code, String userId) {
        if (!StringUtils.hasText(code)) {
            return false;
        }
        String key = SignConstant.SIGN_CODE_KEY + code;
        String activityId = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasText(activityId)) {
            // 过期/无效
            return false;
        }
        // 签到成功 删除二维码码 一次性
        redisTemplate.delete(key);
        return true;
    }
}