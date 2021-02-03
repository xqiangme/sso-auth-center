package com.sso.service.admin;

import com.sso.model.bo.menu.MenuDeleteBO;
import com.sso.model.bo.menu.MenuSaveBO;
import com.sso.model.bo.menu.MenuTreeBO;
import com.sso.model.bo.menu.MenuUpdateBO;
import com.sso.model.vo.menu.MenuOptionTreeNodeVO;
import com.sso.model.vo.menu.MenuTreeNodeVO;
import com.sso.model.vo.menu.MenuDetailVO;

import java.util.List;

/**
 * 菜单管理接口
 *
 * @author 程序员小强
 */
public interface MenuService {


	/**
	 * 菜单树
	 *
	 * @param treeBO
	 * @return 菜单树
	 */
	List<MenuTreeNodeVO> listMenuTree(MenuTreeBO treeBO);

	/**
	 * 菜单树下拉选项列表
	 * 注:只查启用
	 *
	 * @param treeBO
	 * @return 菜单树
	 */
	List<MenuOptionTreeNodeVO> listMenuOptionTree(MenuTreeBO treeBO);


	/**
	 * 菜单详情
	 *
	 * @param menuId
	 * @return 菜单详情
	 */
	MenuDetailVO getMenuDetail(Long menuId);

	/**
	 * 新增菜单
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	void addMenu(MenuSaveBO saveBO);

	/**
	 * 修改菜单
	 *
	 * @param updateBO
	 * @author 程序员小强
	 */
	void updateMenu(MenuUpdateBO updateBO);

	/**
	 * 删除菜单（逻辑删）
	 *
	 * @param deleteBO
	 * @author 程序员小强
	 */
	void deleteMenu(MenuDeleteBO deleteBO);

}
