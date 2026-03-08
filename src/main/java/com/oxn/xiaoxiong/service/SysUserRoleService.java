package com.oxn.xiaoxiong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxn.xiaoxiong.domain.SysUserRole;

import java.util.List;

/**
 * 用户角色关联服务接口
 * 提供用户与角色关联关系的查询功能
 *
 * @author xiaoxiong
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 根据用户ID获取用户角色关联列表
     *
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    List<SysUserRole> getByUserId(Long userId);

    /**
     * 获取当前登录用户的角色关联列表
     *
     * @return 当前用户的角色关联列表
     */
    List<SysUserRole> getCurrentUserRoles();

    /**
     * 根据用户ID获取角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 获取当前登录用户的角色ID列表
     *
     * @return 当前用户的角色ID列表
     */
    List<Long> getCurrentUserRoleIds();

    /**
     * 根据角色ID获取用户角色关联列表
     *
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    List<SysUserRole> getByRoleId(Long roleId);
}
