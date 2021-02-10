/**
 * haifenbb.com
 * Copyright (C) 2019-2020 All Rights Reserved.
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * 目标系统实体类
 *
 * @author 程序员小强
 * @date 2020-12-27 10:59:43
 */
@Data
public class SsoSystem implements Serializable {

	private static final long serialVersionUID = -4911126816387035L;

	/**
	 * 系统平台ID
	 */
	private Long sysId;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 系统名称
	 */
	private String sysName;

	/**
	 * 系统链接
	 */
	private String sysUrl;

	/**
	 * 系统图标
	 */
	private String sysIcon;

	/**
	 * 系统环境 0-测试 1-beta 2-生产
	 */
	private Integer sysEnv;

	/**
	 * 排序，数字越小排在越前面
	 */
	private Integer sortNum;

	/**
	 * 签名类型 0-无;1-MD5;2-RSA;
	 */
	private Integer signType;

	/**
	 * 网关验签公钥
	 */
	private String publicKey;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remarks;


	/**
	 * 删除标志 0-正常;1-删除
	 */
	private Integer delFlag;

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
	private String createTime;

	/**
	 * 更新时间
	 */
	private String updateTime;


}
