package com.sso.model.bo.dept;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 部门树查询
 *
 * @author 程序员小强
 */
@Data
public class DeptMgmtTreeBO implements Serializable {

	private static final long serialVersionUID = 1912530599252047615L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 部门名称
	 */
	private String deptNameLike;

	/**
	 * 状态 -1全部 0-启用;1-停用
	 */
	private Integer status;

}
