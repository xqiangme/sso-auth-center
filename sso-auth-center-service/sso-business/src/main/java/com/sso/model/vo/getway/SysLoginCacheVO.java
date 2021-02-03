package com.sso.model.vo.getway;

import com.sso.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录子系统缓存对象
 *
 * @author 程序员小强
 */
@Data
public class SysLoginCacheVO implements Serializable {
	private static final long serialVersionUID = 3003483755940745510L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 退出登录地址
	 */
	private String loginOutUrl;

	/**
	 * 登录时间戳
	 */
	private Long loginTime;

	public SysLoginCacheVO() {
	}

	public SysLoginCacheVO(String sysCode, String loginOutUrl, Long loginTime) {
		this.sysCode = sysCode;
		this.loginOutUrl = StringUtils.defaultString(loginOutUrl);
		this.loginTime = loginTime;
	}
}
