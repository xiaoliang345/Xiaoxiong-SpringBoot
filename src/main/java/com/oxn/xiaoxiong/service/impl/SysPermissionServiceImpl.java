package com.oxn.xiaoxiong.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxn.xiaoxiong.domain.SysPermission;
import com.oxn.xiaoxiong.mapper.system.SysPermissionMapper;
import com.oxn.xiaoxiong.mapper.system.SysRolePermissionMapper;
import com.oxn.xiaoxiong.mapper.system.SysUserRoleMapper;
import com.oxn.xiaoxiong.service.SysPermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统权限服务实现类
 * 提供权限查询、菜单树构建等功能
 *
 * @author xiaoxiong
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission>
        implements SysPermissionService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * 根据用户ID获取权限列表
     * 查询流程：用户 -> 角色 -> 权限
     *
     * @param userId 用户ID
     * @return 权限列表，仅返回状态为启用且未删除的权限
     */
    @Override
    public List<SysPermission> getPermissionsByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        // 第一步：根据用户ID查询用户角色关联关系，获取角色ID列表
        List<Long> roleIds = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<com.oxn.xiaoxiong.domain.SysUserRole>()
                        .eq(com.oxn.xiaoxiong.domain.SysUserRole::getUserId, userId)
        ).stream()
                .map(com.oxn.xiaoxiong.domain.SysUserRole::getRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 第二步：根据角色ID列表查询角色权限关联关系，获取权限ID列表
        List<Long> permissionIds = sysRolePermissionMapper.selectList(
                new LambdaQueryWrapper<com.oxn.xiaoxiong.domain.SysRolePermission>()
                        .in(com.oxn.xiaoxiong.domain.SysRolePermission::getRoleId, roleIds)
        ).stream()
                .map(com.oxn.xiaoxiong.domain.SysRolePermission::getPermissionId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (permissionIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 第三步：根据权限ID列表查询权限详情，仅返回启用且未删除的权限
        return list(new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, permissionIds)
                .eq(SysPermission::getStatus, 1)
                .eq(SysPermission::getIsDeleted, 0)
        );
    }

    /**
     * 获取当前登录用户的权限列表
     * 通过Sa-Token获取当前登录用户ID
     *
     * @return 当前用户的权限列表
     */
    @Override
    public List<SysPermission> getCurrentUserPermissions() {
        Object loginId = StpUtil.getLoginId();
        if (loginId == null) {
            return Collections.emptyList();
        }
        Long userId = Long.parseLong(loginId.toString());
        return getPermissionsByUserId(userId);
    }

    /**
     * 根据角色ID获取权限列表
     *
     * @param roleId 角色ID
     * @return 该角色拥有的权限列表，仅返回状态为启用且未删除的权限
     */
    @Override
    public List<SysPermission> getPermissionsByRoleId(Long roleId) {
        if (roleId == null) {
            return Collections.emptyList();
        }
        // 查询角色权限关联关系，获取权限ID列表
        List<Long> permissionIds = sysRolePermissionMapper.selectList(
                new LambdaQueryWrapper<com.oxn.xiaoxiong.domain.SysRolePermission>()
                        .eq(com.oxn.xiaoxiong.domain.SysRolePermission::getRoleId, roleId)
        ).stream()
                .map(com.oxn.xiaoxiong.domain.SysRolePermission::getPermissionId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (permissionIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 查询权限详情，仅返回启用且未删除的权限
        return list(new LambdaQueryWrapper<SysPermission>()
                .in(SysPermission::getId, permissionIds)
                .eq(SysPermission::getStatus, 1)
                .eq(SysPermission::getIsDeleted, 0)
        );
    }

    /**
     * 根据用户ID获取菜单树
     *
     * @param userId 用户ID
     * @return 菜单树结构，按orderNum排序
     */
    @Override
    public List<SysPermission> getMenuTree(Long userId) {
        List<SysPermission> permissions = getPermissionsByUserId(userId);
        return buildMenuTree(permissions);
    }

    /**
     * 获取当前登录用户的菜单树
     *
     * @return 当前用户的菜单树结构
     */
    @Override
    public List<SysPermission> getCurrentUserMenuTree() {
        List<SysPermission> permissions = getCurrentUserPermissions();
        return buildMenuTree(permissions);
    }

    /**
     * 构建菜单树结构
     * 将扁平化的权限列表转换为树形结构
     *
     * @param permissions 权限列表
     * @return 树形结构的根节点列表
     */
    private List<SysPermission> buildMenuTree(List<SysPermission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptyList();
        }
        // 按父ID分组，构建子节点映射表
        Map<Long, List<SysPermission>> childrenMap = permissions.stream()
                .filter(p -> p.getParentId() != null)
                .collect(Collectors.groupingBy(SysPermission::getParentId));

        // 获取所有根节点（parentId为null或0），并按orderNum排序
        List<SysPermission> roots = permissions.stream()
                .filter(p -> p.getParentId() == null || p.getParentId() == 0)
                .sorted(Comparator.comparing(SysPermission::getOrderNum, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());

        // 递归构建每个根节点的子节点树
        for (SysPermission root : roots) {
            buildChildren(root, childrenMap);
        }

        return roots;
    }

    /**
     * 递归构建子节点树
     *
     * @param parent 父节点
     * @param childrenMap 子节点映射表（key: 父ID, value: 子节点列表）
     */
    private void buildChildren(SysPermission parent, Map<Long, List<SysPermission>> childrenMap) {
        List<SysPermission> children = childrenMap.get(parent.getId());
        if (children == null || children.isEmpty()) {
            return;
        }
        // 按orderNum排序
        children.sort(Comparator.comparing(SysPermission::getOrderNum, Comparator.nullsLast(Integer::compareTo)));
        // 递归构建每个子节点的子树
        for (SysPermission child : children) {
            buildChildren(child, childrenMap);
        }
    }
}
