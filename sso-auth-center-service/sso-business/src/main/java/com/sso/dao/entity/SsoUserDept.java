/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户部门关系实体类
 *
 * @author 程序员小强
 * @date 2021-01-02 17:20:41
 */
@Data
public class SsoUserDept implements Serializable {

    private static final long serialVersionUID = -8391256763118936557L;

    /**
     * 系统编码
     */
    private String sysCode;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


}
