package com.skmj.server.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.skmj.server.auth.service.SysUserService;
import com.skmj.server.auth.vo.LoginReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    SysUserService sysUserService;

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

    // 验证码校验方法
    private boolean validateCaptcha(String captcha) {
        // 实现验证码校验逻辑，例如从缓存中获取验证码并与用户输入进行比对
        return true; // 示例返回值
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
