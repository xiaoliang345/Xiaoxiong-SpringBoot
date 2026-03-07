package com.oxn.xiaoxiong.sa;

import cn.dev33.satoken.stp.StpInterface;
import com.oxn.xiaoxiong.domain.SysUser;
import com.oxn.xiaoxiong.enums.SysRoleEnum;
import com.oxn.xiaoxiong.service.SysUserService;
import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * 自定义权限加载接口实现类
 * 说明：
 * - Sa-Token 会在需要进行权限 / 角色校验时，回调本实现类
 * - 我们根据 loginId 查询出用户信息，再根据角色返回对应的权限、角色标识集合
 * 如果以后有多套账号体系（多张表、多种 loginType），可以在这里根据 loginType 做分支处理。
 */
@Component // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private SysUserService sysUserService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        SysRoleEnum roleEnum = loadRoleEnum(loginId);
        if (roleEnum == null) {
            return Collections.emptyList();
        }
        return buildPermissionsByRole(roleEnum);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     *
     * 这里实现了简单的「角色继承」：
     * USER 拥有：user
     * ADMIN 拥有：admin + user
     * SUPER_ADMIN 拥有：super-admin + admin + user
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SysRoleEnum roleEnum = loadRoleEnum(loginId);
        if (roleEnum == null) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<>();
        switch (roleEnum) {
            case USER -> {
                list.add(SysRoleEnum.USER.getRoleKey());
            }
            case ADMIN -> {
                // 管理员：自身角色 + 普通用户角色
                list.add(SysRoleEnum.ADMIN.getRoleKey());
                list.add(SysRoleEnum.USER.getRoleKey());
            }
            case SUPER_ADMIN -> {
                // 超级管理员：拥有所有下级角色
                list.add(SysRoleEnum.SUPER_ADMIN.getRoleKey());
                list.add(SysRoleEnum.ADMIN.getRoleKey());
                list.add(SysRoleEnum.USER.getRoleKey());
            }
            default -> {
            }
        }
        return list;
    }

    /**
     * 根据 loginId 查询用户信息
     *
     * 目前只有一张 sys_user 表，所以直接查询；如果以后有多张账号表，
     * 可结合 loginType 决定从哪张表中查询。
     */
    private SysUser loadUser(Object loginId) {
        if (loginId == null) {
            return null;
        }
        try {
            Long id = Long.parseLong(loginId.toString());
            return sysUserService.getById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 根据 loginId 加载并解析出用户角色枚举
     */
    private SysRoleEnum loadRoleEnum(Object loginId) {
        SysUser user = loadUser(loginId);
        if (user == null) {
            return null;
        }
        Integer role = user.getRole();
        if (role == null) {
            return null;
        }
        return SysRoleEnum.fromCode(role);
    }

    /**
     * 根据角色枚举构建权限集合
     * 说明：
     * - 这里给出一个简单示例映射，实际项目中可改为从数据库 / 配置中心加载
     * - 如果以后新增角色，只需要在 {@link SysRoleEnum} 中增加常量，
     * 并在本方法中补充该角色对应的权限集合即可
     */
    private List<String> buildPermissionsByRole(SysRoleEnum roleEnum) {
        List<String> list = new ArrayList<>();
        switch (roleEnum) {
            case USER -> {
                // 普通用户：只读权限示例
                list.add("user.get");
            }
            case ADMIN -> {
                // 管理员：增删改查部分权限示例
                list.add("user.add");
                list.add("user.update");
                list.add("user.get");
                list.add("art.*");
            }
            case SUPER_ADMIN -> {
                // 超级管理员：拥有全部权限示例
                list.add("user.add");
                list.add("user.update");
                list.add("user.get");
                list.add("user.delete");
                list.add("art.*");
                list.add("*"); // 约定 * 表示拥有所有权限
            }
            default -> {
            }
        }
        return list;
    }
}