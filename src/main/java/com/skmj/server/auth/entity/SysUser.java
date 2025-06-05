package com.skmj.server.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lc
 */
@TableName(value = "sys_user")
@Data
public class SysUser {
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String avatar;
    private String mobile;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
    private LocalDateTime lastLoginTime;
    private Integer loginFailCount; // 登录失败次数
    private LocalDateTime lockTime; // 账户锁定时间

}
