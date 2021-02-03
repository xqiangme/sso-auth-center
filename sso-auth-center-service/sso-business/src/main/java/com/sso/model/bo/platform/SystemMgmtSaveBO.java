package com.sso.model.bo.platform;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统管理权限-新增BO
 *
 * @author 程序员小强
 */
@Data
public class SystemMgmtSaveBO extends BaseOperateBO {

	private static final long serialVersionUID = -3496085022363517714L;
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

	/**
	 * 状态
	 */
	@NotNull(message = "状态不为空")
	private Integer status;


	public String getLogValue() {
		return String.format("userId:%d, sysCode:%s , operateBy:%s", this.userId, this.sysCode, this.getOperateBy());
	}
	
}
