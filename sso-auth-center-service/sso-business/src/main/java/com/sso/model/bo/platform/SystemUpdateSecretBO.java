package com.sso.model.bo.platform;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改平台新增bo
 *
 * @author 程序员小强
 */
@Data
public class SystemUpdateSecretBO extends BaseOperateBO {

	private static final long serialVersionUID = -7361402011394142030L;

	/**
	 * 系统平台ID
	 */
	@NotNull(message = "系统平台ID不为空")
	private Long sysId;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不为空")
	private String sysCode;

	/**
	 * 签名类型  0-无;1-MD5;2-RSA;
	 */
	@NotNull(message = "签名类型不为空")
	private Integer signType;

	/**
	 * 网关验签公钥
	 */
	private String publicKey;


	public String getLogValue() {
		return String.format("sysId:%d, signType:%s , operateBy:%s", this.sysId, this.signType, this.getOperateBy());
	}

}
