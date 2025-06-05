package com.skmj.server.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skmj.server.auth.entity.SysUser;
import com.skmj.server.auth.vo.LoginReq;

/**
 * @author lc
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 登录
     * @param loginReq
     * @return 登录成功返回 Token，失败抛出异常
     */
    String login(LoginReq loginReq);
}