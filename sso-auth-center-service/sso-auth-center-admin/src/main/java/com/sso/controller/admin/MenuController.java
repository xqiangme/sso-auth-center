/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.controller.admin;

import com.sso.common.model.result.ResultModel;
import com.sso.common.utils.SecurityUtils;
import com.sso.model.bo.menu.MenuDeleteBO;
import com.sso.model.bo.menu.MenuSaveBO;
import com.sso.model.bo.menu.MenuTreeBO;
import com.sso.model.bo.menu.MenuUpdateBO;
import com.sso.model.vo.menu.MenuDetailVO;
import com.sso.service.admin.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 菜单管理接口
 *
 * @author 程序员小强
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	/**
	 * 菜单管理页-菜单树
	 *
	 * @param treeBO
	 */
	@GetMapping("/listMenuTree")
	@PreAuthorize("@ss.hasPermission('menu:listMenuTree')")
	public ResultModel<?> listDeptMgmtTree(@Valid MenuTreeBO treeBO) {
		return ResultModel.success(menuService.listMenuTree(treeBO));
	}

	/**
	 * 菜单管理页-菜单树下拉列表
	 *
	 * @param treeBO
	 */
	@GetMapping("/listMenuOptionTree")
	public ResultModel<?> listMenuOptionTree(@Valid MenuTreeBO treeBO) {
		return ResultModel.success(menuService.listMenuOptionTree(treeBO));
	}

	/**
	 * 菜单详情
	 *
	 * @param menuId
	 */
	@RequestMapping("/detail/{menuId}")
	public ResultModel<MenuDetailVO> getMenuDetail(@PathVariable Long menuId) {
		return ResultModel.success(menuService.getMenuDetail(menuId));
	}

	/**
	 * 添加菜单
	 *
	 * @param saveBO
	 */
	@PostMapping("/add")
	@PreAuthorize("@ss.hasPermission('menu:add')")
	public ResultModel<?> addDept(@Valid @RequestBody MenuSaveBO saveBO) {
		saveBO.setOperateBy(SecurityUtils.getOperateName());
		menuService.addMenu(saveBO);
		return ResultModel.success();
	}

	/**
	 * 修改菜单
	 *
	 * @param updateBO
	 */
	@PutMapping("/update")
	@PreAuthorize("@ss.hasPermission('menu:update')")
	public ResultModel<?> updateMenu(@Valid @RequestBody MenuUpdateBO updateBO) {
		updateBO.setOperateBy(SecurityUtils.getOperateName());
		menuService.updateMenu(updateBO);
		return ResultModel.success();
	}

	/**
	 * 删除菜单
	 *
	 * @param menuId 菜单ID
	 */
	@DeleteMapping("/delete/{menuId}")
	@PreAuthorize("@ss.hasPermission('menu:delete')")
	public ResultModel<?> deleteMenu(@PathVariable Long menuId) {
		MenuDeleteBO deptDeleteBO = new MenuDeleteBO();
		deptDeleteBO.setMenuId(menuId);
		deptDeleteBO.setOperateBy(SecurityUtils.getOperateName());
		menuService.deleteMenu(deptDeleteBO);
		return ResultModel.success();
	}

}
