package com.sso.model.bo.user;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户删除bo
 *
 * @author 程序员小强
 */
@Data
public class UserBatchDeleteBO extends BaseOperateBO {

	private static final long serialVersionUID = 6811621712453725799L;

	/**
	 * 系统编码
	 */
	@NotBlank(message = "系统编码不能为空")
	private String sysCode;

	/**
	 * 用户ID
	 */
	@Size(min = 1, message = "用户ID不为空")
	private List<Long> userIdList;

}
