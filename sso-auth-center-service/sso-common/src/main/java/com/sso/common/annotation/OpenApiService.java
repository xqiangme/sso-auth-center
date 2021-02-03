package com.sso.common.annotation;

import java.lang.annotation.*;

/**
 * 开放接口实现类注解
 *
 * @author 程序员小强
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiService {
}
