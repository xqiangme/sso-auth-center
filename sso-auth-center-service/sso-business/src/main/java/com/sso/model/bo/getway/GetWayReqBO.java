package com.sso.model.bo.getway;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * GetWay 请求公共参数
 *
 * @author 程序员小强
 * @version GetWayReqBO.java
 */
@Data
public class GetWayReqBO implements Serializable {

	private static final long serialVersionUID = 2055965490262675225L;

	/**
	 * 请求唯一标识
	 * 推荐使用UUID，要求唯一性
	 */
	@NotBlank(message = "请求标识不能为空")
	private String apiRequestId;

	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	@NotBlank(message = "方法名不能为空")
	private String method;

	@NotBlank(message = "版本号不能为空")
	private String version;

	@Pattern(regexp = "[1-2]", message = "签名类型可选值为1~2")
	private String signType;

	@NotBlank(message = "请求时间戳不能为空")
	@Pattern(regexp = "^16\\d{11}$", message = "时间戳格式错误")
	private String timestamp;

	private String sign;

	private String content;

}
