package com.oxn.xiaoxiong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxn.xiaoxiong.domain.SysRole;

import java.util.List;

/**
 * 系统角色服务接口
 * 提供角色查询等功能
 *
 * @author xiaoxiong
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     * @return 角色列表，仅返回状态为启用且未删除的角色
     */
    List<SysRole> getRolesByUserId(Long userId);

    /**
     * 获取当前登录用户的角色列表
     *
     * @return 当前用户的角色列表
     */
    List<SysRole> getCurrentUserRoles();

    /**
     * 根据角色标识获取角色信息
     *
     * @param roleKey 角色标识（如：admin、user等）
     * @return 角色信息，仅返回状态为启用且未删除的角色
     */
    SysRole getRoleByRoleKey(String roleKey);

    /**
     * 获取所有启用的角色列表
     *
     * @return 所有启用且未删除的角色列表
     */
    List<SysRole> getActiveRoles();
}
