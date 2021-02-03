package com.sso.model.bo.platform;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 目标平台新增bo
 *
 * @author 程序员小强
 */
@Data
public class SystemSaveBO extends BaseOperateBO {

	private static final long serialVersionUID = 1163633182173991403L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不为空")
	private String sysCode;

	/**
	 * 系统名称
	 */
	@NotBlank(message = "系统名称不为空")
	private String sysName;

	/**
	 * 系统链接
	 */
	@NotBlank(message = "系统跳转链接不为空")
	private String sysUrl;

	/**
	 * 系统图标
	 */
	private String sysIcon;

	/**
	 * 系统环境 0-测试 1-beta 2-生产
	 */
	@NotNull(message = "系统环境不为空")
	private Integer sysEnv;

	/**
	 * 排序，数字越小排在越前面
	 */
	@NotNull(message = "排序不为空")
	private Integer sortNum;

	/**
	 * 备注
	 */
	private String remarks;

	public String getLogValue() {
		return String.format("sysCode:%s , sysName:%s , operateBy:%s", this.sysCode, this.sysName, this.getOperateBy());
	}
}
