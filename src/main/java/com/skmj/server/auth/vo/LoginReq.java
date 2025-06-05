package com.skmj.server.auth.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lc
 */
@Data
public class LoginReq implements Serializable {
    private String username;
    private String password;
    private String captcha; // 验证码

}
