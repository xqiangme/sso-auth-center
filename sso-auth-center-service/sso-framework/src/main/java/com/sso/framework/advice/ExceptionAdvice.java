/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.framework.advice;

import cn.hutool.core.date.DateException;
import com.sso.common.exception.BaseException;
import com.sso.common.model.result.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


/**
 * 全局统一异常处理
 *
 * @author 程序员小强
 * @version ExceptionAdvice.java, v 1.0
 */
@Slf4j
@ControllerAdvice
@EnableAspectJAutoProxy
public class ExceptionAdvice {


	@CrossOrigin
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public ResultModel<?> exceptionHandler(Exception exception, HttpServletResponse response) throws Exception {
		ResultModel<?> result = ResultModel.error("系统繁忙，请稍后再试");
		try {
			throw exception;
		} catch (BaseException e) {
			log.error("自定义异常  msg:{}", e.getMessage());
			result = ResultModel.error(e.getMessage());
		} catch (MethodArgumentNotValidException argEx) {
			FieldError fieldError = argEx.getBindingResult().getFieldError();
			String errorMsg = String.format("%s >> %s", fieldError.getField(),
					fieldError.getDefaultMessage());
			log.error("参数校验异常  >>  {}", errorMsg);
			result = ResultModel.error(errorMsg);
		} catch (BindException argEx) {
			FieldError fieldError = argEx.getBindingResult().getFieldError();
			String errorMsg = String.format("%s >> %s", fieldError.getField(),
					fieldError.getDefaultMessage());
			log.error("参数校验异常  >> {}", errorMsg);
			result = ResultModel.error(errorMsg);
		} catch (DateException argex) {
			log.error("时间格式化参数校验异常  >>  {}", argex.getMessage());
			result = ResultModel.error(argex.getMessage());
		} catch (HttpRequestMethodNotSupportedException e) {
			String errorMsg = String.format("请求方式 %s 错误 ! 请使用 %s 方式", e.getMethod(), e.getSupportedHttpMethods());
			result = ResultModel.error(errorMsg);
			log.error("请求方式异常  stack=   ", e);
		} catch (HttpMediaTypeNotSupportedException e) {
			String errorMsg = String.format("请求类型 %s 错误 ! 请使用 %s 方式", e.getContentType(), e.getSupportedMediaTypes());
			result = ResultModel.error(errorMsg);
			log.error("请求方式异常, {}  ", errorMsg);
		} catch (AccessDeniedException e) {
			log.error("权限不足异常  >>  ", e);
			result = ResultModel.error("权限不足,请联系管理员授权");
		} catch (HttpMessageNotReadableException e) {
			String jsonParseError = "JSON parse error";
			String bodyMissing = "Required request body is missing";
			if (Objects.requireNonNull(e.getMessage()).startsWith(bodyMissing)) {
				result = ResultModel.error("参数异常,缺少请求body");
			} else if (Objects.requireNonNull(e.getMessage()).startsWith(jsonParseError)) {
				result = ResultModel.error("JSON参数格式化异常");
			}
			log.error("参数异常  >>  ", e);
		} catch (Exception e) {
			log.error("未知异常  >>  ", e);
		}
		return result;
	}

}
