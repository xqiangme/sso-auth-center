/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.admin;

import com.sso.common.model.result.ResultModel;
import com.sso.common.utils.SecurityUtils;
import com.sso.model.bo.platform.*;
import com.sso.model.vo.platform.SystemDetailVO;
import com.sso.service.admin.FileUploadService;
import com.sso.service.admin.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 目标系统平台相关接口
 *
 * @author 程序员小强
 */
@RestController
@RequestMapping("/system")
public class SystemController {

	@Autowired
	private SystemService systemService;

	@Resource
	private FileUploadService fileUploadService;

	/**
	 * 我的平台列表
	 *
	 * @return 系统列表
	 */
	@GetMapping("/myList")
	public ResultModel<?> myList() {
		return ResultModel.success(systemService.listMySystem());
	}

	/**
	 * 我的平台-排序
	 *
	 * @return 系统列表
	 */
	@PostMapping("/sortMy")
	public ResultModel<?> sortMySystem(@Valid @RequestBody SystemSortBO sortBO) {
		systemService.sortMySystem(sortBO);
		return ResultModel.success();
	}

	/**
	 * 系统平台详情
	 *
	 * @param sysCode
	 * @return 系统平台详情
	 */
	@GetMapping("/getDetailBySysCode")
	@PreAuthorize("@ss.hasPermission('system:getDetailBySysCode')")
	public ResultModel<SystemDetailVO> getDetailBySysCode(String sysCode) {
		return ResultModel.success(systemService.getDetailBySysCode(sysCode));
	}

	/**
	 * 平台管理-列表
	 *
	 * @return 平台列表
	 */
	@GetMapping("/mgmtList")
	@PreAuthorize("@ss.hasPermission('system:mgmtList')")
	public ResultModel<?> list() {
		return ResultModel.success(systemService.listMyMgmtSystem());
	}

	/**
	 * 新增-目标系统
	 *
	 * @param saveBO
	 */
	@PostMapping("/add")
	@PreAuthorize("@ss.hasPermission('system:add')")
	public ResultModel<?> addSystem(@Valid @RequestBody SystemSaveBO saveBO) {
		saveBO.setOperateBy(SecurityUtils.getOperateName());
		systemService.addSystem(saveBO);
		return ResultModel.success();
	}

	/**
	 * 修改-目标系统
	 *
	 * @param updateBO
	 */
	@PutMapping("/update")
	@PreAuthorize("@ss.hasPermission('system:update')")
	public ResultModel<?> updateSystem(@Valid @RequestBody SystemUpdateBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		systemService.updateSystem(updateBO);
		return ResultModel.success();
	}

	/**
	 * 添加或修改-秘钥
	 *
	 * @param updateBO
	 */
	@PutMapping("/updateSecret")
	@PreAuthorize("@ss.hasPermission('system:updateSecret')")
	public ResultModel<?> updateSecret(@Valid @RequestBody SystemUpdateSecretBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		systemService.updateSystemSecret(updateBO);
		return ResultModel.success();
	}

	/**
	 * 修改系统图标
	 */
	@PostMapping("/uploadIcon/{sysId}")
	public ResultModel<?> uploadIcon(@PathVariable("sysId") Long sysId, @RequestParam("iconFile") MultipartFile iconFile) {
		String operateName = SecurityUtils.getOperateName();
		return ResultModel.success(fileUploadService.uploadSystemIcon(sysId, operateName, iconFile));
	}

	/**
	 * 删除-目标系统
	 *
	 * @param sysId 系统ID
	 */
	@DeleteMapping("/delete/{sysId}")
	@PreAuthorize("@ss.hasPermission('system:delete')")
	public ResultModel<?> deleteSystem(@PathVariable Long sysId) {
		SystemDeleteBO deptDeleteBO = new SystemDeleteBO();
		deptDeleteBO.setSysId(sysId);
		deptDeleteBO.setOperateBy(SecurityUtils.getOperateName());
		systemService.deleteSystem(deptDeleteBO);
		return ResultModel.success();
	}

}
