package com.oxn.xiaoxiong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxn.xiaoxiong.domain.SysPermission;

import java.util.List;

/**
 * 系统权限服务接口
 * 提供权限查询、菜单树构建等功能
 *
 * @author xiaoxiong
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 根据用户ID获取权限列表
     *
     * @param userId 用户ID
     * @return 权限列表，仅返回状态为启用且未删除的权限
     */
    List<SysPermission> getPermissionsByUserId(Long userId);

    /**
     * 获取当前登录用户的权限列表
     *
     * @return 当前用户的权限列表
     */
    List<SysPermission> getCurrentUserPermissions();

    /**
     * 根据角色ID获取权限列表
     *
     * @param roleId 角色ID
     * @return 该角色拥有的权限列表，仅返回状态为启用且未删除的权限
     */
    List<SysPermission> getPermissionsByRoleId(Long roleId);

    /**
     * 根据用户ID获取菜单树
     *
     * @param userId 用户ID
     * @return 菜单树结构，按orderNum排序
     */
    List<SysPermission> getMenuTree(Long userId);

    /**
     * 获取当前登录用户的菜单树
     *
     * @return 当前用户的菜单树结构
     */
    List<SysPermission> getCurrentUserMenuTree();
}
