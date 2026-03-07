package com.oxn.xiaoxiong.sa.aspect;

import com.oxn.xiaoxiong.sa.StpKit;
import com.oxn.xiaoxiong.sa.annotation.SaUserCheckRole;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 用户端权限切面：基于 SaUserCheckRole 做角色拦截
 */
@Aspect
@Component
public class SaUserAuthAspect {

    @Around("@annotation(saUserCheckRole)")
    public Object checkUserRole(ProceedingJoinPoint pjp, SaUserCheckRole saUserCheckRole) throws Throwable {
        // 1. 先校验用户端是否已登录
        StpKit.SYS_ROLE.checkLogin();

        // 2. 再做角色校验
        String[] roles = saUserCheckRole.value();
        if (roles != null && roles.length > 0) {
            if (saUserCheckRole.modeAnd()) {
                // 需要同时具备所有角色
                StpKit.SYS_ROLE.checkRoleAnd(roles);
            } else {
                // 具备任意一个角色即可
                StpKit.SYS_ROLE.checkRoleOr(roles);
            }
        }
        return pjp.proceed();
    }
}