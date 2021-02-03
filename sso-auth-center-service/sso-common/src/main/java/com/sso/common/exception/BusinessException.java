package com.sso.common.exception;


import com.sso.common.enums.exception.EnumInterface;

import java.text.MessageFormat;

/**
 * 业务异常
 *
 * @author 程序员小强iang
 * @version BusinessException.java
 */
public class BusinessException extends BaseException {

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Integer code, String message) {
		super(code, message);
	}

	public static BaseException newInstance(String msgFormat, Object... args) {
		return new BusinessException(MessageFormat.format(msgFormat, args));
	}

	public BusinessException(EnumInterface enumInterface) {
		super(enumInterface.getCode(), enumInterface.getMsg());
	}

	public BusinessException(EnumInterface enumInterface, Object... args) {
		super(enumInterface.getCode(), MessageFormat.format(enumInterface.getMsg(), args));
	}


}
