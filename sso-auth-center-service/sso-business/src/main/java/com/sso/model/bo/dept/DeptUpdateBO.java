package com.sso.model.bo.dept;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 部门修改BO
 *
 * @author 程序员小强
 */
@Data
public class DeptUpdateBO extends BaseOperateBO {

	private static final long serialVersionUID = 6373997550677086520L;

	/**
	 * 部门ID
	 */
	@NotNull(message = "部门ID不为空")
	private Long deptId;

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门名称不能为空")
	private String deptName;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 父部门ID
	 */
	@NotNull(message = "父部门ID不为空")
	private Long deptParentId;

	/**
	 * 顺序
	 */
	@NotNull(message = "顺序不为空")
	private Integer sortNum;

	/**
	 * 状态 0-启用;1-停用
	 */
	@NotNull(message = "状态不为空")
	private Integer status;

	/**
	 * 备注
	 */
	@Size(max = 200, message = "备注最大长度不超{max}")
	private String remarks;


	public String getLogValue() {
		return String.format("sysCode:%s , deptId:%d , deptName:%s , operateBy:%s", this.sysCode, this.deptId, this.deptName, this.getOperateBy());
	}

}
