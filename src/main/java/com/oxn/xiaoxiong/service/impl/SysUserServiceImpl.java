package com.oxn.xiaoxiong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxn.xiaoxiong.domain.SysUser;
import com.oxn.xiaoxiong.mapper.system.SysUserMapper;
import com.oxn.xiaoxiong.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * 系统用户 Service 实现类
 * <p>
 * 目前仅继承 MyBatis-Plus 的 {@link ServiceImpl}，直接复用其提供的
 * 通用 CRUD / 分页 等功能，如有自定义业务逻辑可在此类中继续扩展。
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}

