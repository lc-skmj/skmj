package com.skmj.server.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skmj.server.auth.entity.SysUser;
import com.skmj.server.auth.mapper.SysUserMapper;
import com.skmj.server.auth.service.LoginLogService;
import com.skmj.server.auth.service.SysUserService;
import com.skmj.server.auth.vo.LoginReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author lc
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginLogService loginLogService;

    @Override
    public String login(LoginReq loginReq) {
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, loginReq.getUsername()));

        if (user == null) {
            loginLogService.recordLoginLog(null, loginReq.getUsername(), "未知IP", "失败");
            throw new RuntimeException("用户不存在");
        }

        // 检查账户是否被锁定
        if (user.getLockTime() != null && user.getLockTime().isAfter(LocalDateTime.now())) {
            loginLogService.recordLoginLog(user.getId(), user.getUsername(), "未知IP", "失败");
            throw new RuntimeException("账户已被锁定，请稍后再试");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
            // 登录失败，增加失败次数
            user.setLoginFailCount((user.getLoginFailCount() == null ? 0 : user.getLoginFailCount()) + 1);
            if (user.getLoginFailCount() >= 5) { // 达到最大失败次数，锁定账户
                user.setLockTime(LocalDateTime.now().plusHours(1)); // 锁定1小时
                loginLogService.recordLoginLog(user.getId(), user.getUsername(), "未知IP", "失败");
                throw new RuntimeException("账户已被锁定，请稍后再试");
            }
            updateById(user); // 更新失败次数
            loginLogService.recordLoginLog(user.getId(), user.getUsername(), "未知IP", "失败");
            throw new RuntimeException("密码错误");
        }

        // 登录成功，重置失败次数
        user.setLoginFailCount(0);
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);

        loginLogService.recordLoginLog(user.getId(), user.getUsername(), "未知IP", "成功");

        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }
}