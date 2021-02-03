package com.sso.model.bo.user;

import com.sso.common.model.BaseOperateBO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户修改个人信息 bo
 *
 * @author 程序员小强
 */
@Data
public class UserUpdateProfileBO extends BaseOperateBO {

	private static final long serialVersionUID = 6185270826197205522L;

	/**
	 * 用户ID
	 */
	@NotNull(message = "用户ID不为空")
	private Long userId;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 用户性别
	 */
	@NotNull(message = "用户性别不为空")
	private Integer sex;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	public String getLogValue() {
		return String.format("userId:%d, nickName:%s, realName:%s , phone:%s , sex%d ,  email:%s , operateBy:%s",
				this.userId, this.nickName, this.realName, this.phone, this.sex, this.email, this.getOperateBy());
	}

}
