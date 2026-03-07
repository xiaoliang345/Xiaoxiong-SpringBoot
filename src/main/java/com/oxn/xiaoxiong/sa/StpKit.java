package com.oxn.xiaoxiong.sa;

import cn.dev33.satoken.stp.StpLogic;
import com.oxn.xiaoxiong.enums.LoginTypeEnum;

/**
 * StpLogic 门面类，集中管理项目中的账号体系
 * 说明：
 * - SYS_ROLE：系统用户账号体系，对应 loginType = "user"
 *
 * 如果以后新增一套账号表，例如「商家端」，只需要：
 * 1. 在 {@link LoginTypeEnum} 中增加一个常量
 * 2. 在本类中新增一个 StpLogic 常量即可
 */
public final class StpKit {

    private StpKit() {
    }

    /**
     * 系统用户会话对象，管理系统用户表所有账号的登录、权限认证
     */
    public static final StpLogic SYS_ROLE = new StpLogic(LoginTypeEnum.USER.getValue());
}
