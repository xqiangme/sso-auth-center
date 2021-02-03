package com.sso.model.vo.dept;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门下拉列表VO
 *
 * @author 程序员小强
 */
@Data
public class DeptOptionVO implements Serializable {

    private static final long serialVersionUID = 6484839051092507626L;

    /**
     * 部门标识
     */
    private String deptId;
    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门下拉列表
     */
    private final List<DeptOptionVO> children = new ArrayList<>();

}
