package com.sso.model.bo.platform;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 修改平台新增bo
 *
 * @author 程序员小强
 */
@Data
public class SystemUpdateBO extends SystemSaveBO {

	private static final long serialVersionUID = 1163633182173991403L;

	/**
	 * 系统平台ID
	 */
	@NotNull(message = "系统平台ID不为空")
	private Long sysId;

	/**
	 * 状态
	 */
	@NotNull(message = "状态不为空")
	private Integer status;

	@Override
	public String getLogValue() {
		return String.format("sysId:%d, sysCode:%s , sysName:%s , operateBy:%s", this.sysId, this.getSysCode(), this.getSysName(), this.getOperateBy());
	}
}
