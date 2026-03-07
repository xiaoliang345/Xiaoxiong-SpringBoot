package com.oxn.xiaoxiong.sa.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户端登录 + 角色校验注解
 * <p>
 * 使用说明：
 * <ul>
 *     <li>先在切面中校验用户端账号体系是否已登录（loginType = "user"）</li>
 *     <li>再根据 value 指定的角色进行权限校验</li>
 * </ul>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SaUserCheckRole {

    /**
     * 需要的角色标识，如：user、admin、super-admin
     */
    String[] value();

    /**
     * 角色校验模式：
     * true：必须同时拥有所有角色（AND）
     * false：拥有任意一个即可（OR）
     */
    boolean modeAnd() default true;
}