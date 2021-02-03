package com.sso.common.exception;

import java.text.MessageFormat;

/**
 * 基础异常
 *
 * @author 程序员小强iang
 */
public class BaseException extends RuntimeException {

    protected Integer code;
    protected String msg;

    public BaseException() {
	super();
    }

    protected BaseException(String message) {
	super(message);
    }

    protected BaseException(Integer code, String msgFormat, Object... args) {
	super(MessageFormat.format(msgFormat, args));
	this.code = code;
	this.msg = MessageFormat.format(msgFormat, args);
    }

    public String getMsg() {
	return this.msg;
    }

    public Integer getCode() {
	return this.code;
    }
}
