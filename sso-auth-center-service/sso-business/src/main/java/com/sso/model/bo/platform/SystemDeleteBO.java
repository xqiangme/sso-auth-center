package com.sso.model.bo.platform;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

/**
 * 修改平台新增bo
 *
 * @author 程序员小强
 */
@Data
public class SystemDeleteBO extends BaseOperateBO {

	private static final long serialVersionUID = 4340162202000538970L;

	/**
	 * 系统平台ID
	 */
	private Long sysId;


	public String getLogValue() {
		return String.format("sysId:%d, operateBy:%s", this.sysId, this.getOperateBy());
	}

}
