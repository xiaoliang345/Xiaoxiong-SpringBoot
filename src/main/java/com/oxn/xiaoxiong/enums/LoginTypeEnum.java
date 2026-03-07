package com.oxn.xiaoxiong.enums;

import lombok.Getter;

/**
 * Sa-Token 登录类型枚举
 * 说明：
 * - value：对应 Sa-Token 的 loginType，例如 new StpLogic("user") 中的 "user"
 * - 以后如果增加多套账号体系，只需要在此枚举中追加常量即可
 */
@Getter
public enum LoginTypeEnum {

    /**
     * 用户端账号体系
     */
    USER("user"),

    /**
     * 管理端账号体系
     */
    ADMIN("admin");

    private final String value;

    LoginTypeEnum(String value) {
        this.value = value;
    }
}

