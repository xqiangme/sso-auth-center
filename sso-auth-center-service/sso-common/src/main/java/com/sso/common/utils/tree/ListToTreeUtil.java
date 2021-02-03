package com.sso.common.utils.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * List转树形结构工具类 泛型对象必须继承
 */
public class ListToTreeUtil<T extends TreeParentNode> {

	/**
	 * 获取树形结构对象
	 */
	public T getTreeObject(List<T> objectList) {
		Map<String, T> objectMap = new HashMap<>();

		//第一次遍历列表，将列表转成Map
		for (T object : objectList) {
			objectMap.put(object.getTreeNodeId(), object);
		}

		T root = null;
		//第二次遍历列表，设置父子关系
		for (T object : objectList) {
			//获取当前节点对象的父节点标识
			String key = object.getParentTreeNodeId();
			if (key == null || !objectMap.containsKey(key)) {
				root = object;
			} else {
				//如果包含父节点标识的Key则获取父对象
				T parentObj = objectMap.get(key);
				//将当前节点保存入父对象子节点集合中
				parentObj.getChildTreeNodes().add(object);
			}
		}
		return root;
	}

	/**
	 * @param nodes
	 * @return
	 */
	public List<T> getTreeListObject(List<T> nodes) {
		List<T> result = new ArrayList<T>();
		Map<String, T> nodeMap = new HashMap<>();
		//第一次遍历列表，将列表转成Map
		for (T node : nodes) {
			nodeMap.put(node.getTreeNodeId(), node);
		}
		//第二次遍历列表，设置父子关系
		for (T node : nodes) {
			//获取当前节点对象的父节点标识
			String key = node.getParentTreeNodeId();
			if (nodeMap.containsKey(key)) {
				//如果包含父节点标识的Key则获取父对象
				T parentObj = nodeMap.get(key);
				//将当前节点保存入父对象子节点集合中
				parentObj.getChildTreeNodes().add(node);
			} else {
				//父节点标识的Key不存在表示当前节点是根节点
				result.add(node);
			}
		}
		return result;
	}
}
