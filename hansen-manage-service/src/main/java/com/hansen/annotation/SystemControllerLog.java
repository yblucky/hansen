package com.hansen.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解，适用于控制器层的方法
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemControllerLog {
    String description() default "";
}
