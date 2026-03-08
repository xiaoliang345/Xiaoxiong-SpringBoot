package com.oxn.xiaoxiong.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxn.xiaoxiong.domain.SysUserResourceRole;
import com.oxn.xiaoxiong.mapper.system.SysUserResourceRoleMapper;
import com.oxn.xiaoxiong.service.SysUserResourceRoleService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 用户资源角色关联服务实现类
 * 提供用户与资源角色关联关系的查询功能
 * 用于实现基于资源的权限控制（RBAC）
 *
 * @author xiaoxiong
 */
@Service
public class SysUserResourceRoleServiceImpl extends ServiceImpl<SysUserResourceRoleMapper, SysUserResourceRole>
        implements SysUserResourceRoleService {

    /**
     * 根据用户ID获取用户资源角色关联列表
     *
     * @param userId 用户ID
     * @return 用户资源角色关联列表
     */
    @Override
    public List<SysUserResourceRole> getByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<SysUserResourceRole>()
                .eq(SysUserResourceRole::getUserId, userId)
        );
    }

    /**
     * 获取当前登录用户的资源角色关联列表
     * 通过Sa-Token获取当前登录用户ID
     *
     * @return 当前用户的资源角色关联列表
     */
    @Override
    public List<SysUserResourceRole> getCurrentUserResourceRoles() {
        Object loginId = StpUtil.getLoginId();
        if (loginId == null) {
            return Collections.emptyList();
        }
        Long userId = Long.parseLong(loginId.toString());
        return getByUserId(userId);
    }

    /**
     * 根据资源类型和资源ID获取用户资源角色关联列表
     * 用于查询在某个资源上拥有角色的所有用户
     *
     * @param resourceType 资源类型（如：project、department等）
     * @param resourceId 资源ID
     * @return 用户资源角色关联列表
     */
    @Override
    public List<SysUserResourceRole> getByResource(String resourceType, Long resourceId) {
        if (resourceType == null || resourceType.trim().isEmpty() || resourceId == null) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<SysUserResourceRole>()
                .eq(SysUserResourceRole::getResourceType, resourceType)
                .eq(SysUserResourceRole::getResourceId, resourceId)
        );
    }

    /**
     * 根据用户ID、资源类型和资源ID获取用户资源角色关联列表
     * 用于查询用户在某个特定资源上拥有的角色
     *
     * @param userId 用户ID
     * @param resourceType 资源类型（如：project、department等）
     * @param resourceId 资源ID
     * @return 用户资源角色关联列表
     */
    @Override
    public List<SysUserResourceRole> getByUserIdAndResource(Long userId, String resourceType, Long resourceId) {
        if (userId == null || resourceType == null || resourceType.trim().isEmpty() || resourceId == null) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<SysUserResourceRole>()
                .eq(SysUserResourceRole::getUserId, userId)
                .eq(SysUserResourceRole::getResourceType, resourceType)
                .eq(SysUserResourceRole::getResourceId, resourceId)
        );
    }

    /**
     * 获取当前登录用户在指定资源上的资源角色关联列表
     * 通过Sa-Token获取当前登录用户ID
     *
     * @param resourceType 资源类型（如：project、department等）
     * @param resourceId 资源ID
     * @return 当前用户在指定资源上的资源角色关联列表
     */
    @Override
    public List<SysUserResourceRole> getCurrentUserResourceRolesByResource(String resourceType, Long resourceId) {
        Object loginId = StpUtil.getLoginId();
        if (loginId == null) {
            return Collections.emptyList();
        }
        Long userId = Long.parseLong(loginId.toString());
        return getByUserIdAndResource(userId, resourceType, resourceId);
    }
}
