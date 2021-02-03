package com.sso.model.vo.dept;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门详情VO
 *
 * @author 程序员小强
 */
@Data
public class DeptDetailVO implements Serializable {

	private static final long serialVersionUID = -8502275028952490758L;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 系统编码
	 */
	private String sysCode;

	/**
	 * 父部门ID
	 */
	private Long deptParentId;

	/**
	 * 顺序
	 */
	private Integer sortNum;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

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
