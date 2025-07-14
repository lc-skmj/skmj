package com.skmj.server.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skmj.server.auth.entity.SysUser;
import com.skmj.server.auth.mapper.SysUserMapper;
import com.skmj.server.auth.service.UserRegistrationService;
import com.skmj.server.auth.vo.RegisterReq;
import com.skmj.server.auth.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserRegistrationServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserRegistrationService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private LoginLogService loginLogService;

    @Override
    public void register(RegisterReq registerReq) {
        // 校验用户名是否已存在
        if (existsUsername(registerReq.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 校验密码和确认密码是否一致
        if (!registerReq.getPassword().equals(registerReq.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 创建新用户
        SysUser newUser = new SysUser();
        newUser.setUsername(registerReq.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerReq.getPassword()));
        newUser.setEmail(registerReq.getEmail());
        newUser.setMobile(registerReq.getMobile());
        newUser.setNickname(registerReq.getUsername());  // 默认昵称为用户名
        newUser.setStatus("active");  // 默认状态为激活
        newUser.setCreateTime(LocalDateTime.now());
        newUser.setUpdateTime(LocalDateTime.now());
        newUser.setCreateBy("system");
        newUser.setUpdateBy("system");
        newUser.setLastLoginTime(null);
        newUser.setLoginFailCount(0);
        newUser.setLockTime(null);

        // 保存用户
        if (!save(newUser)) {
            throw new RuntimeException("注册失败，请稍后再试");
        }

        // 记录登录日志（注册成功视为一次登录）
        loginLogService.recordLoginLog(newUser.getId(), newUser.getUsername(), "未知IP", "成功");
    }

    /**
     * 检查用户名是否存在
     */
    private boolean existsUsername(String username) {
        return count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)) > 0;
    }
}