package com.skmj.server.auth.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lc
 */
@Data
public class RegisterReq implements Serializable {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String mobile;
}