package com.sso.model.vo.role;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色分页列表返回对象
 *
 * @author 程序员小强
 */
@Data
public class RolePageVO implements Serializable {

	private static final long serialVersionUID = -6710365268589472423L;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色标识key
	 */
	private String roleKey;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

	/**
	 * 显示顺序
	 */
	private Integer sortNum;

	/**
	 * 使用的用户数
	 */
	private Integer userCount;

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
