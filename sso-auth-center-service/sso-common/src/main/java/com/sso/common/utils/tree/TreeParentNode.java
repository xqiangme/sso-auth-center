package com.sso.common.utils.tree;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 通过id/ParentId方式
 * 实现树形结构的List转树形结构的定义接口
 *
 * @author 程序员小强
 */
public interface TreeParentNode<T> {

	/**
	 * 获取树形结构当前节点的标识
	 * 注：不参与 json序列化
	 *
	 * @return
	 */
	@JSONField(serialize = false)
	String getTreeNodeId();

	/**
	 * 获取树形结构当前节点父节点的标识
	 * 注：不参与 json序列化
	 *
	 * @return
	 */
	@JSONField(serialize = false)
	String getParentTreeNodeId();

	/**
	 * 获取树形结构当前节点的所有子节点
	 * 注：不参与 json序列化
	 *
	 * @return
	 */
	@JSONField(serialize = false)
	List<T> getChildTreeNodes();
}
