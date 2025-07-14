package com.skmj.server.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skmj.server.auth.entity.LoginLog;

public interface LoginLogService extends IService<LoginLog> {
    void recordLoginLog(String userId, String username, String ip, String status);
}