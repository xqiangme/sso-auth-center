package com.sso.common.model.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sso.common.enums.exception.SysResStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 返回统一对象
 *
 * @author 程序员小强
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultModel<T> implements Serializable {

    private static final long serialVersionUID = -296683281215956785L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回的的数据
     */
    private T data;

    /**
     * 描述
     */
    private String msg;


    public static <T> ResultModel<T> success() {
	return new ResultModel<>(SysResStatusEnum.SUCCESS.getCode(), null, "操作成功");
    }

    /**
     * 成功请求
     * code : 200
     * msg : 默认 ""
     */
    public static <T> ResultModel<T> success(T data) {
	return new ResultModel<>(SysResStatusEnum.SUCCESS.getCode(), data, "操作成功");
    }

    public static <T> ResultModel<T> success(Integer code, T data) {
	return new ResultModel<>(code, data, "操作失败");
    }

    public static <T> ResultModel<T> error(SysResStatusEnum exceptionEnum) {
	return new ResultModel<>(exceptionEnum.getCode(), null, exceptionEnum.getMsg());
    }

    public static <T> ResultModel<T> error(Integer code, String msg) {
	return new ResultModel<>(code, null, msg);
    }

    public static <T> ResultModel<T> error(String msg) {
	return new ResultModel<>(SysResStatusEnum.ERROR.getCode(), null, msg);
    }

    private ResultModel() {
    }

    private ResultModel(Integer code, T data, String msg) {
	this.code = code;
	this.data = data;
	this.msg = msg;
    }


}
