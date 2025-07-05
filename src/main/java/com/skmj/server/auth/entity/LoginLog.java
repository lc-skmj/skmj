package com.skmj.server.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("login_log")
public class LoginLog {
    private String id;
    private String userId;
    private String username;
    private String ip;
    private String status; // 登录状态：成功/失败
    private LocalDateTime createTime;
}