package org.example.controller;

import lombok.Data;
import org.example.common.Result;
import org.example.entity.SysUser;
import org.example.service.SysUserService;
import org.example.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证控制器 - 匹配 maku-boot 前端规范
 */
@RestController
@RequestMapping("/sys/auth")
public class AuthController {
    
    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Value("${captcha.enabled:true}")
    private Boolean captchaEnabled;
    
    // 验证码缓存 (生产环境应使用 Redis)
    private static final ConcurrentHashMap<String, String> captchaCache = new ConcurrentHashMap<>();
    
    /**
     * 账号密码登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        // 验证码校验
        if (captchaEnabled) {
            if (loginDTO.getKey() == null || loginDTO.getCaptcha() == null) {
                return Result.badRequest("请输入验证码");
            }
            String cachedCaptcha = captchaCache.get(loginDTO.getKey());
            if (cachedCaptcha == null || !cachedCaptcha.equalsIgnoreCase(loginDTO.getCaptcha())) {
                return Result.error(400, "验证码错误或已过期");
            }
            captchaCache.remove(loginDTO.getKey());
        }
        
        // 查询用户
        SysUser user = sysUserService.getByUsername(loginDTO.getUsername());
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        // 验证密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        
        // 生成 Token 响应
        Map<String, Object> tokenData = jwtUtils.generateTokenResponse(user.getId(), user.getUsername());
        
        // 添加用户信息
        Map<String, Object> result = new HashMap<>();
        result.putAll(tokenData);
        result.put("user_info", convertToUserInfo(user));
        
        return Result.success(result);
    }
    
    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> getCaptcha() throws IOException {
        // 生成验证码
        String captcha = generateCaptchaCode();
        String key = java.util.UUID.randomUUID().toString();
        
        // 缓存验证码 (5分钟)
        captchaCache.put(key, captcha);
        
        // 清除过期验证码的简单机制
        clearExpiredCaptcha();
        
        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        result.put("image", "data:image/png;base64," + generateCaptchaImage(captcha));
        
        return Result.success(result);
    }
    
    /**
     * 验证码是否启用
     */
    @GetMapping("/captcha/enabled")
    public Result<Boolean> isCaptchaEnabled() {
        return Result.success(captchaEnabled);
    }
    
    /**
     * 刷新 Token
     */
    @PostMapping("/token")
    public Result<Map<String, Object>> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        try {
            Map<String, Object> tokenData = jwtUtils.refreshAccessToken(refreshToken);
            return Result.success(tokenData);
        } catch (Exception e) {
            return Result.unauthorized("刷新令牌已过期或无效");
        }
    }
    
    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        // 可选：将 token 加入黑名单 (使用 Redis)
        // String token = jwtUtils.extractTokenFromHeader(request.getHeader("Authorization"));
        // 在实际项目中，可以将 token 加入 Redis 黑名单
        return Result.success("退出成功", null);
    }
    
    /**
     * 手机号登录 (预留接口)
     */
    @PostMapping("/mobile")
    public Result<Map<String, Object>> mobileLogin(@RequestBody MobileLoginDTO loginDTO) {
        // TODO: 实现手机号登录逻辑
        return Result.error("手机号登录暂未实现");
    }
    
    /**
     * 第三方登录 (预留接口)
     */
    @PostMapping("/third")
    public Result<Map<String, Object>> thirdLogin(@RequestBody Map<String, String> params) {
        // TODO: 实现第三方登录逻辑
        return Result.error("第三方登录暂未实现");
    }
    
    /**
     * 发送验证码 (预留接口)
     */
    @PostMapping("/send/code")
    public Result<Void> sendCode(@RequestParam("mobile") String mobile) {
        // TODO: 实现短信发送逻辑
        return Result.success("发送成功", null);
    }
    
    // ==================== 私有方法 ====================
    
    private Map<String, Object> convertToUserInfo(SysUser user) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("username", user.getUsername());
        info.put("real_name", user.getRealName());
        info.put("avatar", user.getAvatar());
        return info;
    }
    
    private String generateCaptchaCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    private String generateCaptchaImage(String code) throws IOException {
        int width = 100;
        int height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g = image.createGraphics();
        
        // 背景
        g.setColor(new java.awt.Color(240, 240, 240));
        g.fillRect(0, 0, width, height);
        
        // 干扰线
        Random random = new Random();
        g.setColor(new java.awt.Color(200, 200, 200));
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
        
        // 文字
        g.setColor(new java.awt.Color(50, 50, 50));
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        g.drawString(code, 20, 28);
        g.dispose();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(image, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
    
    private void clearExpiredCaptcha() {
        // 简化处理：如果缓存超过100条，清空一半
        if (captchaCache.size() > 100) {
            captchaCache.clear();
        }
    }
    
    // ==================== DTO ====================
    
    @Data
    public static class LoginDTO {
        private String username;
        private String password;
        private String key;      // 验证码key
        private String captcha;  // 验证码
    }
    
    @Data
    public static class MobileLoginDTO {
        private String mobile;
        private String code;
    }
}
