package com.sso.service.base;

import com.sso.common.constant.SsoConstants;
import com.sso.common.utils.StringUtils;
import com.sso.framework.config.property.SysConfigProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 获取配置参数公共接口
 *
 * @author 程序员小强
 */
@Slf4j
@Component
public class SysConfigService {

	@Resource
	private SysConfigProperty sysConfigProperty;

	/**
	 * 获取系统管理员用户ID
	 */
	public Long getSupperAdminUserId() {
		String supperAdminUser = sysConfigProperty.getSupperAdminUser();
		if (StringUtils.isBlank(supperAdminUser)) {
			return null;
		}
		String[] splitUser = supperAdminUser.split(SsoConstants.SPLIT_ESCAPE);
		if (splitUser.length < 1) {
			return null;
		}
		return Long.parseLong(splitUser[0]);
	}
}
