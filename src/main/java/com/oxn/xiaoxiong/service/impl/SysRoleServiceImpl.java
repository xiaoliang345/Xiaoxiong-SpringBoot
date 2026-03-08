package com.oxn.xiaoxiong.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxn.xiaoxiong.domain.SysRole;
import com.oxn.xiaoxiong.domain.SysUserRole;
import com.oxn.xiaoxiong.mapper.system.SysRoleMapper;
import com.oxn.xiaoxiong.mapper.system.SysUserRoleMapper;
import com.oxn.xiaoxiong.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统角色服务实现类
 * 提供角色查询等功能
 *
 * @author xiaoxiong
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 根据用户ID获取角色列表
     * 通过用户角色关联表查询用户拥有的角色
     *
     * @param userId 用户ID
     * @return 角色列表，仅返回状态为启用且未删除的角色
     */
    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        // 查询用户角色关联关系，获取角色ID列表
        List<Long> roleIds = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        ).stream()
                .map(SysUserRole::getRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (roleIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 根据角色ID列表查询角色详情，仅返回启用且未删除的角色
        return list(new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getId, roleIds)
                .eq(SysRole::getStatus, 1)
                .eq(SysRole::getIsDeleted, 0)
        );
    }

    /**
     * 获取当前登录用户的角色列表
     * 通过Sa-Token获取当前登录用户ID
     *
     * @return 当前用户的角色列表
     */
    @Override
    public List<SysRole> getCurrentUserRoles() {
        Object loginId = StpUtil.getLoginId();
        if (loginId == null) {
            return Collections.emptyList();
        }
        Long userId = Long.parseLong(loginId.toString());
        return getRolesByUserId(userId);
    }

    /**
     * 根据角色标识获取角色信息
     *
     * @param roleKey 角色标识（如：admin、user等）
     * @return 角色信息，仅返回状态为启用且未删除的角色
     */
    @Override
    public SysRole getRoleByRoleKey(String roleKey) {
        if (roleKey == null || roleKey.trim().isEmpty()) {
            return null;
        }
        return getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleKey, roleKey)
                .eq(SysRole::getStatus, 1)
                .eq(SysRole::getIsDeleted, 0)
        );
    }

    /**
     * 获取所有启用的角色列表
     *
     * @return 所有启用且未删除的角色列表
     */
    @Override
    public List<SysRole> getActiveRoles() {
        return list(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getStatus, 1)
                .eq(SysRole::getIsDeleted, 0)
        );
    }
}
