package com.sso.model.bo.platform;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统管理权限移除BO
 *
 * @author 程序员小强
 */
@Data
public class SystemMgmtRemoveBO extends BaseOperateBO {

	private static final long serialVersionUID = 7991373166717611704L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不为空")
	private String sysCode;

	/**
	 * 用户ID
	 */
	@NotNull(message = "用户ID不为空")
	private Long userId;

	public String getLogValue() {
		return String.format("sysCode:%s, userId:%s , operateBy:%s", this.sysCode, this.userId, this.getOperateBy());
	}

}
