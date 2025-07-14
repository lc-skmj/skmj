package com.skmj.server.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.skmj.server.auth.service.SysUserService;
import com.skmj.server.auth.service.UserRegistrationService;
import com.skmj.server.auth.vo.LoginReq;
import com.skmj.server.auth.vo.RegisterReq;
import com.skmj.server.auth.util.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    SysUserService sysUserService;
    
    @Autowired
    private CaptchaUtil captchaUtil;
    
    @Autowired
    private UserRegistrationService userRegistrationService;
    
    // 用于存储生成的验证码
    private String currentCaptcha;

    /**
     * 登录
     *
     * @return 登录成功返回 Token，失败返回错误信息
     */
    @RequestMapping("login")
    public SaResult login(@RequestBody LoginReq loginReq) {
        // 校验验证码
        if (!validateCaptcha(loginReq.getCaptcha())) {
            return SaResult.error("验证码错误");
        }

        try {
            String token = sysUserService.login(loginReq);
            return SaResult.ok("登录成功").setData(token);
        } catch (RuntimeException e) {
            return SaResult.error(e.getMessage());
        }
    }

    /**
     * 注册
     */
    @RequestMapping("register")
    public SaResult register(@RequestBody RegisterReq registerReq) {
        userRegistrationService.register(registerReq);
        return SaResult.ok("注册成功");
    }

    /**
     * 生成验证码
     */
    @RequestMapping("generateCaptcha")
    public SaResult generateCaptcha() throws IOException {
        currentCaptcha = captchaUtil.generateCaptchaText();
        BufferedImage image = captchaUtil.generateCaptchaImage(currentCaptcha);
        
        // 将图片转换为Base64编码
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
        
        return SaResult.ok().setData("data:image/png;base64," + base64Image);
    }

    private boolean validateCaptcha(String userInputCaptcha) {
        // 简单校验用户输入的验证码是否与生成的一致
        return userInputCaptcha != null && userInputCaptcha.equalsIgnoreCase(currentCaptcha);
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

}
