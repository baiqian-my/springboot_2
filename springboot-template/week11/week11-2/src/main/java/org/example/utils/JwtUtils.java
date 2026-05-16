package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类 - 匹配 maku-boot 前端规范
 * access_token: 访问令牌
 * refresh_token: 刷新令牌
 */
@Component
public class JwtUtils {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;
    
    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;
    
    /**
     * 生成访问令牌
     */
    public String generateAccessToken(Long userId, String username) {
        return generateToken(userId, username, "access", accessTokenExpiration);
    }

    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(Long userId, String username) {
        return generateToken(userId, username, "refresh", refreshTokenExpiration);
    }
    
    /**
     * 生成完整的 Token 响应（匹配前端期望格式）
     */
    public Map<String, Object> generateTokenResponse(Long userId, String username) {
        Map<String, Object> result = new HashMap<>();
        result.put("access_token", generateAccessToken(userId, username));
        result.put("refresh_token", generateRefreshToken(userId, username));
        result.put("expires_in", accessTokenExpiration / 1000);
        return result;
    }
    
    /**
     * 刷新访问令牌
     */
    public Map<String, Object> refreshAccessToken(String refreshToken) {
        Claims claims = parseToken(refreshToken);
        
        // 验证是否是 refresh token
        String tokenType = claims.get("type", String.class);
        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("无效的刷新令牌");
        }
        
        Long userId = Long.valueOf(claims.getSubject());
        String username = claims.get("username", String.class);
        
        return generateTokenResponse(userId, username);
    }
    
    private String generateToken(Long userId, String username, String type, Long expiration) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("type", type)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * 从 Bearer token 中提取
     */
    public String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return header;
    }
    
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.getSubject());
    }
    
    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }
    
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }
    
    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
