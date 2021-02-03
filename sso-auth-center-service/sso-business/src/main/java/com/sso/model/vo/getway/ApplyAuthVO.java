package com.sso.model.vo.getway;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sso.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 申请认证参数
 *
 * @author 程序员小强
 * @version ApplyAuthBO.java
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplyAuthVO implements Serializable {

	private static final long serialVersionUID = -3755046680856409972L;

	/**
	 * 认证结果
	 */
	private Boolean authResult;

	/**
	 * 重定向url（认证结果仅为false时返回）
	 */
	private String redirectUrl;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户登录名
	 */
	private String username;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 用户性别 0-男;1-女;2-未知
	 */
	private Integer sex;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 系统名称
	 */
	private String sysName;

	/**
	 * 在当前系统下的-权限列表
	 */
	private Set<String> permissionList;

	/**
	 * 在当前系统下的-角色标识
	 */
	private List<String> roleKeyList;

	/**
	 * 在当前系统下的-菜单集
	 */
	private List<MenuTreeAuthVO> menuList;

	public static ApplyAuthVO authFailNeedLogin(String ssoLoginUrl) {
		return authFailNeedLogin(ssoLoginUrl, "");
	}

	public static ApplyAuthVO authFailNeedLogin(String ssoLoginUrl, String targetRedirectUrl) {
		ApplyAuthVO authVO = new ApplyAuthVO();
		authVO.setAuthResult(false);
		authVO.setRedirectUrl(ssoLoginUrl);
		if (StringUtils.isNotBlank(targetRedirectUrl)) {
			authVO.setRedirectUrl(ssoLoginUrl + "?redirectUrl=" + targetRedirectUrl);
		}
		return authVO;
	}

}
