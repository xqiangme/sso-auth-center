/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * controller 日志打印
 *
 * @author 程序员小强
 */
@Slf4j
@Aspect
@Component
public class LogPrintlnAspect {

	/**
	 * 配置织入点
	 */
	@Pointcut("execution(public * com.sso.controller.admin..*.*(..))")
	public void allMethod() {
	}

	@Around("allMethod()")
	public Object doAround(ProceedingJoinPoint call) throws Throwable {
		MethodSignature signature = (MethodSignature) call.getSignature();
		Method method = signature.getMethod();

		String[] classNameArray = method.getDeclaringClass().getName().split("\\.");
		String methodName = classNameArray[classNameArray.length - 1] + "." + method.getName();

		String params = buildParamsDefault(call);

		long start = System.currentTimeMillis();
		Object result = null;
		try {
			MDC.put("logId", String.format("[%s]", RandomStringUtils.randomAlphanumeric(10)));
			log.info("[ {} ] requestParam: {}", methodName, params);
			result = call.proceed();
			return result;
		} finally {
			log.info("[ {} ] runTime: {} ms", methodName, (System.currentTimeMillis() - start));
		}
	}

	private String buildParamsDefault(ProceedingJoinPoint call) {
		StringBuilder params = new StringBuilder(" [");
		for (int i = 0; i < call.getArgs().length; i++) {
			Object obj = call.getArgs()[i];
			if (null != obj) {
				if (obj instanceof HttpServletRequest) {
					continue;
				}
				if (obj instanceof HttpServletResponse) {
					continue;
				}
				String str = obj.toString();
				if (obj.getClass() != String.class) {
					str = ToStringBuilder.reflectionToString(obj, ToStringStyle.JSON_STYLE);
				}
				if (i != call.getArgs().length - 1) {
					params.append(str).append(",");
				} else {
					params.append(str).append(" ]");
				}
			}
			if (params.length() == 1) {
				params.append("]");
			}
		}
		return params.toString();
	}
}
