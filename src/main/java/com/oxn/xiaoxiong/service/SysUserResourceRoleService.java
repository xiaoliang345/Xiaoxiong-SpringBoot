package com.oxn.xiaoxiong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxn.xiaoxiong.domain.SysUserResourceRole;

import java.util.List;

/**
 * 用户资源角色关联服务接口
 * 提供用户与资源角色关联关系的查询功能
 * 用于实现基于资源的权限控制（RBAC）
 *
 * @author xiaoxiong
 */
public interface SysUserResourceRoleService extends IService<SysUserResourceRole> {

    /**
     * 根据用户ID获取用户资源角色关联列表
     *
     * @param userId 用户ID
     * @return 用户资源角色关联列表
     */
    List<SysUserResourceRole> getByUserId(Long userId);

    /**
     * 获取当前登录用户的资源角色关联列表
     *
     * @return 当前用户的资源角色关联列表
     */
    List<SysUserResourceRole> getCurrentUserResourceRoles();

    /**
     * 根据资源类型和资源ID获取用户资源角色关联列表
     *
     * @param resourceType 资源类型（如：project、department等）
     * @param resourceId 资源ID
     * @return 用户资源角色关联列表
     */
    List<SysUserResourceRole> getByResource(String resourceType, Long resourceId);

    /**
     * 根据用户ID、资源类型和资源ID获取用户资源角色关联列表
     *
     * @param userId 用户ID
     * @param resourceType 资源类型（如：project、department等）
     * @param resourceId 资源ID
     * @return 用户资源角色关联列表
     */
    List<SysUserResourceRole> getByUserIdAndResource(Long userId, String resourceType, Long resourceId);

    /**
     * 获取当前登录用户在指定资源上的资源角色关联列表
     *
     * @param resourceType 资源类型（如：project、department等）
     * @param resourceId 资源ID
     * @return 当前用户在指定资源上的资源角色关联列表
     */
    List<SysUserResourceRole> getCurrentUserResourceRolesByResource(String resourceType, Long resourceId);
}
