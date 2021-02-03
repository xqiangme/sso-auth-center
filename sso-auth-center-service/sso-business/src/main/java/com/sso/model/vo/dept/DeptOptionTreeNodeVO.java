package com.sso.model.vo.dept;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sso.common.utils.tree.TreeParentNode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门下拉选项树-返回对象
 *
 * @author 程序员小强
 */
@Data
public class DeptOptionTreeNodeVO implements Serializable, TreeParentNode<DeptOptionTreeNodeVO> {

	private static final long serialVersionUID = 8930822109458637455L;

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

	private final List<DeptOptionTreeNodeVO> children = new ArrayList<>();

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
	public List<DeptOptionTreeNodeVO> getChildTreeNodes() {
		return children;
	}


}
