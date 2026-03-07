package com.oxn.xiaoxiong.config;

import cn.dev33.satoken.strategy.SaAnnotationStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * Sa-Token 相关配置
 * <p>
 * 重写 Sa-Token 默认的注解处理器，支持注解合并功能（与官方示例一致）
 */
@Configuration
public class SaTokenConfigure {

    @PostConstruct
    public void rewriteSaStrategy() {
        // 重写 Sa-Token 的注解处理器，增加注解合并功能
        SaAnnotationStrategy.instance.getAnnotation = (element, annotationClass) ->
                AnnotatedElementUtils.getMergedAnnotation(element, annotationClass);
    }
}

