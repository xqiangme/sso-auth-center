/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.framework.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 从applicationContext中得到Bean 工具类
 *
 * @author 程序员小强
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static Object getBean(String beanName) {
		return context != null ? context.getBean(beanName) : null;
	}

	public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> var1) {
		return context != null ? context.getBeansWithAnnotation(var1) : null;
	}

}
