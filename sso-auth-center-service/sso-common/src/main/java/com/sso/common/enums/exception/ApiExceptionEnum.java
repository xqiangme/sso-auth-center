package com.sso.common.enums.exception;

/**
 * 异常枚举
 *
 * @author 程序员小强
 */
public enum ApiExceptionEnum implements EnumInterface {

	/**
	 * api异常枚举
	 */
	API_NOT_EXIST(9001, "API方法不存在"),
	INVALID_PUBLIC_PARAM(9002, "无效公共参数 >> {0}"),
	INVALID_REQUEST_ERROR(9003, " 请求方式 {0} 错误 ! 请使用 {1} 方式"),
	INVALID_PARAM(9004, "无效参数 >> 参数[{0}] >> 原因[{1}]"),
	INVALID_SIGN(9005, "无效签名"),
	SIGN_TYPE_NOT_EXIST(9001, "签名类型不存在"),
	SYSTEM_ERROR(9999, "系统繁忙请稍后再试!"),
	;

	ApiExceptionEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private Integer code;

	private String msg;

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

}
