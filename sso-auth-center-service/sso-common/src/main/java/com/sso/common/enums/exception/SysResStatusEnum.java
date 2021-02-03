package com.sso.common.enums.exception;

/**
 * @author 程序员小强
 */
public enum SysResStatusEnum implements EnumInterface {

    /**
     * 系统状态枚举
     */
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    UNAUTHORIZED(401, "Unauthorized"),

    ADD_FAIL(1000, "新增失败"),
    UPDATE_FAIL(1001, "修改失败"),
    DELETE_FAIL(1002, "删除失败"),

    LOGIN_SUCCESS(2000, "登录成功"),
    CAPTCHA_EXPIRE(2001, "验证码已失效"),
    CAPTCHA_ERROR(2002, "验证码错误"),
    SYS_NOT_FOUND(2003, "系统不存在"),
    USER_PASSWORD_NOT_MATCH(2004, "用户名或密码错误"),
    USER_NAME_NOT_EXISTS(2005, "用户名不存在"),
    USER_DISABLE(2006, "用户已停用"),

    SYSTEM_ERROR(6000, "网络繁忙，请稍后再试"),
    ;


    SysResStatusEnum(Integer code, String msg) {
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
