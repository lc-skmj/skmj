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
     * @return
     */
    @RequestMapping("login")
    public SaResult login(@RequestBody LoginReq loginReq) {
        sysUserService.login(loginReq);
        return SaResult.ok("登录成功");
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
