package com.oxn.xiaoxiong.sa;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oxn.xiaoxiong.domain.SysPermission;
import com.oxn.xiaoxiong.domain.SysRole;
import com.oxn.xiaoxiong.domain.SysRolePermission;
import com.oxn.xiaoxiong.domain.SysUser;
import com.oxn.xiaoxiong.domain.SysUserRole;
import com.oxn.xiaoxiong.mapper.system.SysPermissionMapper;
import com.oxn.xiaoxiong.mapper.system.SysRoleMapper;
import com.oxn.xiaoxiong.mapper.system.SysRolePermissionMapper;
import com.oxn.xiaoxiong.mapper.system.SysUserMapper;
import com.oxn.xiaoxiong.mapper.system.SysUserRoleMapper;
import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 基于数据库的 RBAC 权限查询服务
 * <p>
 * 当前项目中 {@link com.oxn.xiaoxiong.sa.StpInterfaceImpl} 里是写死的权限示例，
 * 如果以后希望从数据库动态加载权限，可以改为委托给本 Service。
 */
@Service
public class RbacPermissionService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 根据 loginId 查询该用户拥有的角色列表（基于 sys_user_role、sys_role）
     */
    public List<String> listRoleKeysByLoginId(Object loginId) {
        SysUser user = loadUser(loginId);
        if (user == null) {
            return Collections.emptyList();
        }

        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, user.getId())
                        .eq(SysUserRole::getIsDeleted, 0)
        );

        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);
        return roles.stream()
                .map(SysRole::getRoleKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 根据 loginId 查询该用户拥有的权限标识集合（基于 sys_user_role、sys_role_permission、sys_permission）
     */
    public List<String> listPermissionKeysByLoginId(Object loginId) {
        SysUser user = loadUser(loginId);
        if (user == null) {
            return Collections.emptyList();
        }

        // 1. 用户对应的角色
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, user.getId())
                        .eq(SysUserRole::getIsDeleted, 0)
        );
        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 角色对应的权限
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.selectList(
                new LambdaQueryWrapper<SysRolePermission>()
                        .in(SysRolePermission::getRoleId, roleIds)
                        .eq(SysRolePermission::getIsDeleted, 0)
        );
        if (rolePermissions.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> permissionIds = rolePermissions.stream()
                .map(SysRolePermission::getPermissionId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (permissionIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 查询权限标识
        List<SysPermission> permissions = sysPermissionMapper.selectBatchIds(permissionIds);
        return permissions.stream()
                .map(SysPermission::getPermissionKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SysUser loadUser(Object loginId) {
        if (loginId == null) {
            return null;
        }
        try {
            Long id = Long.parseLong(loginId.toString());
            return sysUserMapper.selectById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
