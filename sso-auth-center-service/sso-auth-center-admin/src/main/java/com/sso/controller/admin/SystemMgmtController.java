/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.admin;

import com.sso.common.model.result.ResultModel;
import com.sso.common.model.result.ResultPageModel;
import com.sso.common.utils.SecurityUtils;
import com.sso.model.bo.platform.SystemMgmtListPageBO;
import com.sso.model.bo.platform.SystemMgmtRemoveBO;
import com.sso.model.bo.platform.SystemMgmtSaveBO;
import com.sso.model.bo.platform.SystemMgmtStatusBO;
import com.sso.model.vo.platform.SystemMgmtPageVO;
import com.sso.service.admin.SystemMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统管理员相关操作
 *
 * @author 程序员小强
 */
@RestController
@RequestMapping("/systemMgmt")
public class SystemMgmtController {

	@Autowired
	private SystemMgmtService systemMgmtService;

	/**
	 * 系统管理员-用户分页列表
	 *
	 * @param pageBO
	 * @return 用户
	 */
	@RequestMapping("/listPage")
	@PreAuthorize("@ss.hasPermission('systemMgmt:updateSystemMgmt')")
	public ResultPageModel<SystemMgmtPageVO> listPage(@Valid SystemMgmtListPageBO pageBO) {
		return systemMgmtService.listPageSysMgmtUser(pageBO);
	}

	/**
	 * 新增-管理员权限
	 *
	 * @param addBO
	 */
	@PostMapping("/add")
	@PreAuthorize("@ss.hasPermission('systemMgmt:updateSystemMgmt')")
	public ResultModel<?> addSystemMgmt(@Valid @RequestBody SystemMgmtSaveBO addBO) {
		addBO.setOperateBy(SecurityUtils.getOperateName());
		systemMgmtService.addSystemMgmt(addBO);
		return ResultModel.success();
	}

	/**
	 * 修改-管理员权限账号状态
	 *
	 * @param addBO
	 */
	@PutMapping("/updateStatus")
	@PreAuthorize("@ss.hasPermission('systemMgmt:updateSystemMgmt')")
	public ResultModel<?> updateStatus(@Valid @RequestBody SystemMgmtStatusBO addBO) {
		addBO.setOperateBy(SecurityUtils.getOperateName());
		systemMgmtService.updateStatus(addBO);
		return ResultModel.success();
	}

	/**
	 * 移除-用户管理员权限
	 *
	 * @param removeBO
	 */
	@RequestMapping("/remove")
	@PreAuthorize("@ss.hasPermission('systemMgmt:updateSystemMgmt')")
	public ResultModel<?> removeUserSystemMgmt(@Valid @RequestBody SystemMgmtRemoveBO removeBO) {
		removeBO.setOperateBy(SecurityUtils.getOperateName());
		systemMgmtService.removeUserSystemMgmt(removeBO);
		return ResultModel.success();
	}
}
