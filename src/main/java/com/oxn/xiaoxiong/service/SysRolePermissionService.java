package com.oxn.xiaoxiong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxn.xiaoxiong.domain.SysRolePermission;

import java.util.List;

/**
 * 角色权限关联服务接口
 * 提供角色与权限关联关系的查询功能
 *
 * @author xiaoxiong
 */
public interface SysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 根据角色ID获取角色权限关联列表
     *
     * @param roleId 角色ID
     * @return 角色权限关联列表
     */
    List<SysRolePermission> getByRoleId(Long roleId);

    /**
     * 根据权限ID获取角色权限关联列表
     *
     * @param permissionId 权限ID
     * @return 角色权限关联列表
     */
    List<SysRolePermission> getByPermissionId(Long permissionId);

    /**
     * 根据角色ID获取权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> getPermissionIdsByRoleId(Long roleId);

    /**
     * 根据权限ID获取角色ID列表
     *
     * @param permissionId 权限ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdsByPermissionId(Long permissionId);
}
