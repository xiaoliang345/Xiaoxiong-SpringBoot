package com.oxn.xiaoxiong.enums;

import lombok.Getter;

/**
 * 系统角色枚举
 * 说明：
 * - code：数据库中存的整型标识（0/1/2 ...）
 * - roleKey：Sa-Token 中使用的角色字符串，如 "user" / "admin" / "super-admin"
 */
@Getter
public enum SysRoleEnum {

    /**
     * 普通用户
     */
    USER(0, "user", "普通用户"),

    /**
     * 管理员
     */
    ADMIN(1, "admin", "管理员"),

    /**
     * 超级管理员
     */
    SUPER_ADMIN(2, "super-admin", "超级管理员");

    /**
     * 数据库存储的整型值
     */
    private final int code;

    /**
     * Sa-Token 使用的角色标识
     */
    private final String roleKey;

    /**
     * 说明文案
     */
    private final String description;

    SysRoleEnum(int code, String roleKey, String description) {
        this.code = code;
        this.roleKey = roleKey;
        this.description = description;
    }

    /**
     * 根据整型 code 找枚举
     */
    public static SysRoleEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SysRoleEnum value : SysRoleEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据 Sa-Token 角色标识找枚举
     */
    public static SysRoleEnum fromRoleKey(String roleKey) {
        if (roleKey == null) {
            return null;
        }
        for (SysRoleEnum value : SysRoleEnum.values()) {
            if (value.roleKey.equals(roleKey)) {
                return value;
            }
        }
        return null;
    }
}
