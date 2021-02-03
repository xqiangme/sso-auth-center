package com.sso.model.vo.platform;

import lombok.Data;

import java.io.Serializable;


/**
 * 目标系统实体类
 *
 * @author 程序员小强
 * @date 2020-12-27 10:59:43
 */
@Data
public class SystemListVO implements Serializable {

	private static final long serialVersionUID = -2760156351359473263L;

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
	 * 排序，数字越小排在越前面
	 */
	private Integer sortNum;

	/**
	 * 系统权限编码
	 */
	private String sysGrantCode;

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
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

}
