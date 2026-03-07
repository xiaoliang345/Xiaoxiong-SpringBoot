package com.oxn.xiaoxiong.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import com.oxn.xiaoxiong.common.BaseResponse;
import com.oxn.xiaoxiong.common.ResultUtils;
import com.oxn.xiaoxiong.enums.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    /**
     * Sa-Token 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public BaseResponse<?> notLoginExceptionHandler(NotLoginException e) {
        log.warn("notLoginException: " + e.getMessage(), e);
        return ResultUtils.error(StatusCode.NOT_LOGIN_ERROR, StatusCode.NOT_LOGIN_ERROR.getMessage());
    }

    /**
     * Sa-Token 角色权限异常
     * 比如：无此角色、角色不足等情况，统一提示为无权限（403）
     */
    @ExceptionHandler(NotRoleException.class)
    public BaseResponse<?> notRoleExceptionHandler(NotRoleException e) {
        log.warn("notRoleException: " + e.getMessage(), e);
        return ResultUtils.error(StatusCode.NO_AUTH_ERROR, StatusCode.NO_AUTH_ERROR.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException: " + e.getMessage(), e);
        return ResultUtils.error(StatusCode.SYSTEM_ERROR, e.getMessage());
    }
}
