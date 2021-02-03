package com.sso.model.bo.dept;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 部门删除bo
 *
 * @author 程序员小强
 */
@Data
public class DeptDeleteBO extends BaseOperateBO {

	private static final long serialVersionUID = 1912530599252047615L;

	/**
	 * 部门ID
	 */
	@NotNull(message = "部门ID不为空")
	private Long deptId;


	public String getLogValue() {
		return String.format(" deptId:%d , , operateBy:%s", this.deptId, this.getOperateBy());
	}

}
