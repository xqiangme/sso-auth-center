package com.sso.common.model.result;

import com.sso.common.enums.exception.SysResStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author 程序员小强
 */
@Getter
@Setter
public class ResultPageModel<T> implements Serializable {

    private static final long serialVersionUID = -5317828223300876518L;

    /**
     * 消息状态码
     */
    private Integer code;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> data;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 表格数据对象
     */
    public ResultPageModel() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public ResultPageModel(List<T> list, int total) {
	this.data = list;
	this.total = total;
    }


    /**
     * 响应请求分页数据
     */
    public static <T> ResultPageModel<T> defaultValue() {
	return success(new ArrayList<>(0), 0);
    }

    /**
     * 响应请求分页数据
     */
    public static <T> ResultPageModel<T> success(List<T> list) {
	return success(list, list.size());
    }

    /**
     * 响应请求分页数据
     */
    public static <T> ResultPageModel<T> success(List<T> list, int total) {
	ResultPageModel<T> rspData = new ResultPageModel<>();
	rspData.setCode(SysResStatusEnum.SUCCESS.getCode());
	rspData.setMsg("操作成功");
	rspData.setData(list);
	rspData.setTotal(total);
	return rspData;
    }

    public static <T> ResultPageModel<T> error(String msg) {
	ResultPageModel<T> rspData = new ResultPageModel<T>();
	rspData.setCode(SysResStatusEnum.ERROR.getCode());
	rspData.setMsg(msg);
	rspData.setData(new ArrayList<>(0));
	rspData.setTotal(0);
	return rspData;
    }
}
