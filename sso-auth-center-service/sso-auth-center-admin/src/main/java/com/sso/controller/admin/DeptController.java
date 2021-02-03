/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.admin;


import com.sso.common.model.result.ResultModel;
import com.sso.common.utils.SecurityUtils;
import com.sso.model.bo.SysCodeBO;
import com.sso.model.bo.dept.DeptDeleteBO;
import com.sso.model.bo.dept.DeptMgmtTreeBO;
import com.sso.model.bo.dept.DeptSaveBO;
import com.sso.model.bo.dept.DeptUpdateBO;
import com.sso.model.vo.dept.DeptDetailVO;
import com.sso.service.admin.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 部门管理接口
 *
 * @author 程序员小强
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

	@Autowired
	private DeptService deptService;

	/**
	 * 部门管理页-部门树
	 *
	 * @param treeBO
	 */
	@GetMapping("/listDeptMgmtTree")
	@PreAuthorize("@ss.hasPermission('dept:listDeptMgmtTree')")
	public ResultModel<?> listDeptMgmtTree(@Valid DeptMgmtTreeBO treeBO) {
		return ResultModel.success(deptService.listDeptMgmtTree(treeBO));
	}

	/**
	 * 部门下拉-部门树
	 *
	 * @param sysCodeBO
	 */
	@RequestMapping("/listDeptOptionTree")
	public ResultModel<?> listDeptOptionTree(@Valid SysCodeBO sysCodeBO) {
		return ResultModel.success(deptService.listDeptOptionTree(sysCodeBO.getSysCode()));
	}

	/**
	 * 部门详情
	 *
	 * @param deptId
	 */
	@RequestMapping("/detail/{deptId}")
	public ResultModel<DeptDetailVO> getDeptDetail(@PathVariable Long deptId) {
		return ResultModel.success(deptService.getDeptDetail(deptId));
	}

	/**
	 * 添加部门
	 *
	 * @param saveBO
	 */
	@PostMapping("/add")
	@PreAuthorize("@ss.hasPermission('dept:add')")
	public ResultModel<?> addDept(@Valid @RequestBody DeptSaveBO saveBO) {
		saveBO.setOperateBy(SecurityUtils.getOperateName());
		deptService.addDept(saveBO);
		return ResultModel.success();
	}

	/**
	 * 修改部门
	 *
	 * @param updateBO
	 */
	@PutMapping("/update")
	@PreAuthorize("@ss.hasPermission('dept:update')")
	public ResultModel<?> updateDept(@Valid @RequestBody DeptUpdateBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		deptService.updateDept(updateBO);
		return ResultModel.success();
	}

	/**
	 * 删除部门
	 *
	 * @param deptId 部门ID
	 */
	@DeleteMapping("/delete/{deptId}")
	@PreAuthorize("@ss.hasPermission('dept:delete')")
	public ResultModel<?> deleteDept(@PathVariable Long deptId) {
		DeptDeleteBO deptDeleteBO = new DeptDeleteBO();
		deptDeleteBO.setDeptId(deptId);
		deptDeleteBO.setOperateBy(SecurityUtils.getOperateName());
		deptService.deleteDept(deptDeleteBO);
		return ResultModel.success();
	}
}
