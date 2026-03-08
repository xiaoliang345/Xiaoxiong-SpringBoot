package com.oxn.xiaoxiong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxn.xiaoxiong.domain.SysRolePermission;
import com.oxn.xiaoxiong.mapper.system.SysRolePermissionMapper;
import com.oxn.xiaoxiong.service.SysRolePermissionService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色权限关联服务实现类
 * 提供角色与权限关联关系的查询功能
 *
 * @author xiaoxiong
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission>
        implements SysRolePermissionService {

    /**
     * 根据角色ID获取角色权限关联列表
     *
     * @param roleId 角色ID
     * @return 角色权限关联列表
     */
    @Override
    public List<SysRolePermission> getByRoleId(Long roleId) {
        if (roleId == null) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId)
        );
    }

    /**
     * 根据权限ID获取角色权限关联列表
     *
     * @param permissionId 权限ID
     * @return 角色权限关联列表
     */
    @Override
    public List<SysRolePermission> getByPermissionId(Long permissionId) {
        if (permissionId == null) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getPermissionId, permissionId)
        );
    }

    /**
     * 根据角色ID获取权限ID列表
     * 提取角色权限关联关系中的权限ID，去重后返回
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    @Override
    public List<Long> getPermissionIdsByRoleId(Long roleId) {
        return getByRoleId(roleId).stream()
                .map(SysRolePermission::getPermissionId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 根据权限ID获取角色ID列表
     * 提取角色权限关联关系中的角色ID，去重后返回
     *
     * @param permissionId 权限ID
     * @return 角色ID列表
     */
    @Override
    public List<Long> getRoleIdsByPermissionId(Long permissionId) {
        return getByPermissionId(permissionId).stream()
                .map(SysRolePermission::getRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
