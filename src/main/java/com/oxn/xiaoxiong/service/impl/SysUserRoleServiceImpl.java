package com.oxn.xiaoxiong.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxn.xiaoxiong.domain.SysUserRole;
import com.oxn.xiaoxiong.mapper.system.SysUserRoleMapper;
import com.oxn.xiaoxiong.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户角色关联服务实现类
 * 提供用户与角色关联关系的查询功能
 *
 * @author xiaoxiong
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
        implements SysUserRoleService {

    /**
     * 根据用户ID获取用户角色关联列表
     *
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    @Override
    public List<SysUserRole> getByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );
    }

    /**
     * 获取当前登录用户的角色关联列表
     * 通过Sa-Token获取当前登录用户ID
     *
     * @return 当前用户的角色关联列表
     */
    @Override
    public List<SysUserRole> getCurrentUserRoles() {
        Object loginId = StpUtil.getLoginId();
        if (loginId == null) {
            return Collections.emptyList();
        }
        Long userId = Long.parseLong(loginId.toString());
        return getByUserId(userId);
    }

    /**
     * 根据用户ID获取角色ID列表
     * 提取用户角色关联关系中的角色ID，去重后返回
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return getByUserId(userId).stream()
                .map(SysUserRole::getRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 获取当前登录用户的角色ID列表
     * 通过Sa-Token获取当前登录用户ID
     *
     * @return 当前用户的角色ID列表
     */
    @Override
    public List<Long> getCurrentUserRoleIds() {
        Object loginId = StpUtil.getLoginId();
        if (loginId == null) {
            return Collections.emptyList();
        }
        Long userId = Long.parseLong(loginId.toString());
        return getRoleIdsByUserId(userId);
    }

    /**
     * 根据角色ID获取用户角色关联列表
     * 用于查询拥有某个角色的所有用户
     *
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    @Override
    public List<SysUserRole> getByRoleId(Long roleId) {
        if (roleId == null) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId)
        );
    }
}
