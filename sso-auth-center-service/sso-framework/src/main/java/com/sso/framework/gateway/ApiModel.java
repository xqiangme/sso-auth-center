package com.sso.framework.gateway;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * api接口对象
 *
 * @author 程序员小强
 */
@Data
public class ApiModel implements Serializable {

	private static final long serialVersionUID = -1090071353564966615L;

	/**
	 * 类 spring bean
	 */
	private String beanName;

	/**
	 * 方法对象
	 */
	private Method method;

	/**
	 * 业务参数
	 */
	private String paramName;

	public ApiModel(String beanName, Method method, String paramName) {
		this.beanName = beanName;
		this.method = method;
		this.paramName = paramName;
	}
}
