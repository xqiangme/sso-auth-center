package com.sso.model.vo.dept;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sso.common.utils.tree.TreeParentNode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 部门管理树-返回对象
 *
 * @author 程序员小强
 */
@Data
public class DeptMgmtTreeNodeVO implements Serializable, TreeParentNode<DeptMgmtTreeNodeVO> {

    private static final long serialVersionUID = 8016386138004004340L;
    /**
	 * 部门标识
	 */
	private Long deptId;

	/**
	 * 部门父标识
	 */
	private Long deptParentId;

	/**
	 * 部门名称
	 */
	private String deptName;

	/**
	 * 顺序
	 */
	private Integer sortNum;

	/**
	 * 状态 0-正常;1-停用
	 */
	private Integer status;

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

	private final List<DeptMgmtTreeNodeVO> children = new ArrayList<>();

	@JsonIgnore
	@Override
	public String getTreeNodeId() {
		return deptId.toString();
	}

	@JsonIgnore
	@Override
	public String getParentTreeNodeId() {
		return deptParentId.toString();
	}

	@JsonIgnore
	@Override
	public List<DeptMgmtTreeNodeVO> getChildTreeNodes() {
		return children;
	}


}
