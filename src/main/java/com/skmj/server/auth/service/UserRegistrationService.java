package com.skmj.server.auth.service;

import com.skmj.server.auth.vo.RegisterReq;

public interface UserRegistrationService {
    void register(RegisterReq registerReq);
}