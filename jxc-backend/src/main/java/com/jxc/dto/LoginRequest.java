package com.jxc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 登录请求DTO
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public class LoginRequest {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
    
    private String captcha;
    private String captchaKey;
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getCaptcha() {
        return captcha;
    }
    
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
    
    public String getCaptchaKey() {
        return captchaKey;
    }
    
    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }
}