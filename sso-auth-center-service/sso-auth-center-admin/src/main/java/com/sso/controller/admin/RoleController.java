/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.admin;


import com.sso.common.model.result.ResultModel;
import com.sso.common.model.result.ResultPageModel;
import com.sso.common.utils.SecurityUtils;
import com.sso.model.bo.role.*;
import com.sso.model.vo.role.RoleDetailVO;
import com.sso.model.vo.role.RolePageVO;
import com.sso.service.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 角色接口
 *
 * @author 程序员小强
 */
@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	/**
	 * 角色分页列表
	 *
	 * @param pageBO
	 */
	@RequestMapping("/listPage")
	@PreAuthorize("@ss.hasPermission('role:listPage')")
	public ResultPageModel<RolePageVO> listPage(@Valid RoleListPageBO pageBO) {
		return roleService.listPageRole(pageBO);
	}

	/**
	 * 角色-下拉选项列表
	 *
	 * @param optionBO
	 */
	@RequestMapping("/listOption")
	public ResultModel<?> listRoleOption(@Valid RoleListOptionBO optionBO) {
		return ResultModel.success(roleService.listRoleOption(optionBO));
	}

	/**
	 * 角色详情
	 *
	 * @param roleId
	 */
	@RequestMapping("/detail/{roleId}")
	public ResultModel<RoleDetailVO> getDeptDetail(@PathVariable Long roleId) {
		return ResultModel.success(roleService.getDeptDetail(roleId));
	}

	/**
	 * 添加角色
	 *
	 * @param saveBO
	 */
	@PostMapping("/add")
	@PreAuthorize("@ss.hasPermission('role:add')")
	public ResultModel<?> addRole(@Valid @RequestBody RoleSaveBO saveBO) {
		saveBO.setOperateBy(SecurityUtils.getOperateName());
		roleService.addRole(saveBO);
		return ResultModel.success();
	}

	/**
	 * 修改角色
	 *
	 * @param updateBO
	 */
	@PutMapping("/update")
	@PreAuthorize("@ss.hasPermission('role:update')")
	public ResultModel<?> updateRole(@Valid @RequestBody RoleUpdateBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		roleService.updateRole(updateBO);
		return ResultModel.success();
	}

	/**
	 * 删除角色
	 *
	 * @param roleId 角色ID
	 */
	@DeleteMapping("/delete/{roleId}")
	@PreAuthorize("@ss.hasPermission('role:delete')")
	public ResultModel<?> deleteDept(@PathVariable Long roleId) {
		RoleDeleteBO deptDeleteBO = new RoleDeleteBO();
		deptDeleteBO.setRoleId(roleId);
		deptDeleteBO.setOperateBy(SecurityUtils.getOperateName());
		roleService.deleteRole(deptDeleteBO);
		return ResultModel.success();
	}

	/**
	 * 移除角色用户绑定关系
	 *
	 * @param removeUserBO
	 */
	@PutMapping("/removeUser")
	@PreAuthorize("@ss.hasPermission('role:removeUser')")
	public ResultModel<?> removeUserRole(@Valid @RequestBody RoleRemoveUserBO removeUserBO) {
		removeUserBO.setOperateBy(SecurityUtils.getOperateName());
		roleService.removeUserRole(removeUserBO);
		return ResultModel.success();
	}

}
