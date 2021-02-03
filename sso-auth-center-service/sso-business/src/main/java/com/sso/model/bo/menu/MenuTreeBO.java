package com.sso.model.bo.menu;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 菜单树查询BO
 *
 * @author 程序员小强
 */
@Data
public class MenuTreeBO implements Serializable {

	private static final long serialVersionUID = -4094219434105471621L;
	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 菜单名称
	 */
	private String menuNameLike;

	/**
	 * 状态 -1全部 0-启用;1-停用
	 */
	private Integer status;

}
