/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.mapper;

import com.sso.dao.entity.SsoDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门祖先/后代关系
 *
 * @author 程序员小强
 * @date 2020-12-27 18:07:20
 */
public interface SsoDeptTreePathMapper {

    /**
     * 插入新节点
     *
     * @param deptId
     * @param deptParentId
     * @return 行数
     */
    int insertBatch(@Param("deptId") Long deptId, @Param("deptParentId") Long deptParentId);

    /**
     * 删除叶子节点
     *
     * @param deptId
     * @return 行数
     */
    int deleteLeaf(Long deptId);

    /**
     * 删除子树
     *
     * @param deptId
     * @return 行数
     */
    int deleteChildTree(Long deptId);

    /**
     * 查询所有祖先
     *
     * @param deptId
     * @return 部门列表
     */
    List<SsoDept> getAllParent(Long deptId);

    /**
     * 查询所有后代
     *
     * @param deptId
     * @return 部门列表
     */
    List<SsoDept> getAllChild(Long deptId);

}
