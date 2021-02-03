/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.security.handle;

import com.alibaba.fastjson.JSON;
import com.sso.common.enums.exception.SysResStatusEnum;
import com.sso.common.model.result.ResultModel;
import com.sso.common.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author 程序员小强
 */
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -6355792444057575911L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
		int code = SysResStatusEnum.UNAUTHORIZED.getCode();
		String msg = String.format("请求访问：%s，认证失败，无法访问系统资源", request.getRequestURI());
		log.error("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
		ServletUtils.renderString(response, JSON.toJSONString(ResultModel.error(code, msg)));
	}
}
