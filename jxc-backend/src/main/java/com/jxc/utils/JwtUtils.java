package com.jxc.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT工具类
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Component
public class JwtUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    @Value("${app.jwt.secret:jxcSecretKeyForJwtTokenGenerationThatShouldBeAtLeast256BitsLong}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration:86400000}") // 24小时
    private long jwtExpirationMs;
    
    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    /**
     * 根据用户名生成JWT Token
     */
    public String generateToken(String username) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);
        
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    /**
     * 根据用户名和用户ID生成JWT Token
     */
    public String generateToken(String username, Long userId) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);
        
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    /**
     * 从JWT Token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getSubject();
    }
    
    /**
     * 从JWT Token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.get("userId", Long.class);
    }
    
    /**
     * 验证JWT Token
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException ex) {
            logger.error("JWT签名无效: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("JWT格式错误: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("JWT已过期: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("不支持的JWT: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT声明字符串为空: {}", ex.getMessage());
        }
        return false;
    }
    
    /**
     * 获取JWT Token过期时间
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getExpiration();
    }
    
    /**
     * 检查JWT Token是否过期
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }
}