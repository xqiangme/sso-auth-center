package com.sso.service.admin;

import com.sso.common.model.result.ResultPageModel;
import com.sso.model.bo.role.*;
import com.sso.model.vo.role.RoleDetailVO;
import com.sso.model.vo.role.RoleOptionVO;
import com.sso.model.vo.role.RolePageVO;

import java.util.List;

/**
 * 角色相关接口
 *
 * @author 程序员小强
 */
public interface RoleService {

	/**
	 * 分页列表
	 *
	 * @param pageBO
	 * @return 角色列表
	 */
	ResultPageModel<RolePageVO> listPageRole(RoleListPageBO pageBO);

	/**
	 * 角色下拉选项列表
	 *
	 * @param optionBO
	 * @return 角色列表
	 */
	List<RoleOptionVO> listRoleOption(RoleListOptionBO optionBO);

	/**
	 * 角色详情
	 *
	 * @param roleId 角色ID
	 * @return 角色详情
	 * @author 程序员小强
	 */
	RoleDetailVO getDeptDetail(Long roleId);

	/**
	 * 新增角色
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	void addRole(RoleSaveBO saveBO);

	/**
	 * 修改角色
	 *
	 * @param updateBO
	 * @author 程序员小强
	 */
	void updateRole(RoleUpdateBO updateBO);

	/**
	 * 删除角色（逻辑删）
	 *
	 * @param deleteBO
	 * @author 程序员小强
	 */
	void deleteRole(RoleDeleteBO deleteBO);

	/**
	 * 移除角色用户绑定关系
	 *
	 * @param removeUserBO
	 * @author 程序员小强
	 */
	void removeUserRole(RoleRemoveUserBO removeUserBO);
}
