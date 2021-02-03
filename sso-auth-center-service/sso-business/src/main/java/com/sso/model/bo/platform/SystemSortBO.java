package com.sso.model.bo.platform;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 目标平台排序bo
 *
 * @author 程序员小强
 */
@Data
public class SystemSortBO implements Serializable {

	private static final long serialVersionUID = 2105261198839002918L;

	/**
	 * 系统编码集合
	 */
	@NotNull(message = "系统编码集不为空")
	@Size(min = 1, message = "系统编码集不为空")
	private List<String> sysCodeList;
}
