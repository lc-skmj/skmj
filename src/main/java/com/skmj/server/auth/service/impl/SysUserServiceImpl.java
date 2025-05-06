package com.skmj.server.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skmj.server.auth.entity.SysUser;
import com.skmj.server.auth.mapper.SysUserMapper;
import com.skmj.server.auth.service.SysUserService;
import com.skmj.server.auth.vo.LoginReq;
import org.springframework.stereotype.Service;

/**
 * @author lc
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public String login(LoginReq loginReq) {
        SysUser one = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginReq.getUsername()));
        StpUtil.login(one.getId());
        return StpUtil.getTokenValue();
    }
}
