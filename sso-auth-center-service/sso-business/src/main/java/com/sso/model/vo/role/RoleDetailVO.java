package com.sso.model.vo.role;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色添加 bo
 *
 * @author 程序员小强
 */
@Data
public class RoleDetailVO implements Serializable {

	private static final long serialVersionUID = 5809339048919552524L;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 显示顺序
	 */
	private Integer sortNum;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色标识key
	 */
	private String roleKey;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 最后修改者
	 */
	private String updateBy;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 菜单ID集
	 */
	private List<Long> menuIdList;
}
