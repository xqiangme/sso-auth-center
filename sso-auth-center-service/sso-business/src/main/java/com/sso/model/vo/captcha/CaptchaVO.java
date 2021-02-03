package com.sso.model.vo.captcha;

import lombok.Data;

import java.io.Serializable;

/**
 * 部门详情VO
 *
 * @author 程序员小强
 */
@Data
public class CaptchaVO implements Serializable {

	private static final long serialVersionUID = 3727125898241907228L;

	/**
	 * 请求标识
	 */
	private String requestId;

	/**
	 * 图片验证码-base64格式
	 */
	private String base64Img;

	public CaptchaVO(String requestId, String base64Img) {
		this.requestId = requestId;
		this.base64Img = base64Img;
	}
}
