package com.skmj.server.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skmj.server.auth.entity.SysUser;
import com.skmj.server.auth.mapper.SysUserMapper;
import com.skmj.server.auth.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @author lc
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
