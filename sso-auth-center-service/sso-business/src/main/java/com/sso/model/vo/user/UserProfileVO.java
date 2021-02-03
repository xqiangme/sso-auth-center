package com.sso.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户个人中心详情
 *
 * @author 程序员小强
 */
@Data
public class UserProfileVO implements Serializable {

	private static final long serialVersionUID = 3347667088081428127L;

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
	 * 头像
	 */
	private String avatar;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 最后登录IP
	 */
	private String loginIp;

	/**
	 * 最后登录时间
	 */
	private Date lastLoginTime;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 最后修改者
	 */
	private String updateBy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

}
