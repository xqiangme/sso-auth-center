/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 *
 * @author 程序员小强
 * @date 2021-01-02 17:20:41
 */
@Data
public class SsoUser implements Serializable {

    private static final long serialVersionUID = -3634941666611687795L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户性别 0-男;1-女;2-未知
     */
    private Integer sex;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态 0-正常;1-停用
     */
    private Integer status;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 删除标志 0-正常;1-删除
     */
    private Integer delFlag;

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
