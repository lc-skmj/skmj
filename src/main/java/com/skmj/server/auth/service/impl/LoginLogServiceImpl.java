package com.skmj.server.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skmj.server.auth.entity.LoginLog;
import com.skmj.server.auth.mapper.LoginLogMapper;
import com.skmj.server.auth.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {
    
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public void recordLoginLog(String userId, String username, String ip, String status) {
        LoginLog loginLog = new LoginLog();
        loginLog.setId(userId);
        loginLog.setUsername(username);
        loginLog.setIp(ip);
        loginLog.setStatus(status);
        loginLog.setCreateTime(LocalDateTime.now());
        loginLogMapper.insert(loginLog);
    }
}