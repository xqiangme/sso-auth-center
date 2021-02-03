package com.sso.service.admin.impl;

import com.sso.common.constant.SsoConstants;
import com.sso.common.enums.DelFlagEnum;
import com.sso.common.enums.exception.MenuResStatusEnum;
import com.sso.common.exception.BusinessException;
import com.sso.common.utils.StringUtils;
import com.sso.common.utils.bean.BeanCopierUtil;
import com.sso.common.utils.tree.ListToTreeUtil;
import com.sso.dao.entity.SsoMenu;
import com.sso.dao.mapper.SsoMenuMapper;
import com.sso.dao.mapper.SsoRoleMenuMapper;
import com.sso.dao.query.SsoMenuQuery;
import com.sso.model.bo.menu.MenuDeleteBO;
import com.sso.model.bo.menu.MenuSaveBO;
import com.sso.model.bo.menu.MenuTreeBO;
import com.sso.model.bo.menu.MenuUpdateBO;
import com.sso.model.vo.menu.MenuDetailVO;
import com.sso.model.vo.menu.MenuOptionTreeNodeVO;
import com.sso.model.vo.menu.MenuTreeNodeVO;
import com.sso.service.admin.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;


/**
 * 菜单管理接口
 *
 * @author 程序员小强
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl implements MenuService {

	@Resource
	private SsoMenuMapper ssoMenuMapper;

	@Resource
	private SsoRoleMenuMapper ssoRoleMenuMapper;

	/**
	 * 菜单树
	 *
	 * @param treeBO
	 * @return 菜单树
	 */
	@Override
	public List<MenuTreeNodeVO> listMenuTree(MenuTreeBO treeBO) {
		SsoMenuQuery query = new SsoMenuQuery()
				.setSysCode(treeBO.getSysCode())
				.setMenuNameLike(treeBO.getMenuNameLike())
				.setStatus(treeBO.getStatus())
				.setDelFlag(DelFlagEnum.OK.getStatus());

		List<SsoMenu> ssoMenuList = ssoMenuMapper.listByCondition(query);
		if (CollectionUtils.isEmpty(ssoMenuList)) {
			return new ArrayList<>(0);
		}

		//转换成树结构
		List<MenuTreeNodeVO> treeNodeVOList = BeanCopierUtil.copyList(ssoMenuList, MenuTreeNodeVO.class);
		ListToTreeUtil<MenuTreeNodeVO> result = new ListToTreeUtil<>();

		return result.getTreeListObject(treeNodeVOList);
	}

	/**
	 * 菜单树下拉选项列表
	 * 注:只查启用
	 *
	 * @param treeBO
	 * @return 菜单树
	 */
	@Override
	public List<MenuOptionTreeNodeVO> listMenuOptionTree(MenuTreeBO treeBO) {
		SsoMenuQuery query = new SsoMenuQuery()
				.setSysCode(treeBO.getSysCode())
				.setStatus(treeBO.getStatus())
				.setDelFlag(DelFlagEnum.OK.getStatus());

		List<SsoMenu> ssoMenuList = ssoMenuMapper.listByCondition(query);
		if (CollectionUtils.isEmpty(ssoMenuList)) {
			return new ArrayList<>(0);
		}

		//转换成树结构
		List<MenuOptionTreeNodeVO> treeNodeVOList = BeanCopierUtil.copyList(ssoMenuList, MenuOptionTreeNodeVO.class);
		ListToTreeUtil<MenuOptionTreeNodeVO> result = new ListToTreeUtil<>();

		return result.getTreeListObject(treeNodeVOList);
	}

	/**
	 * 菜单详情
	 *
	 * @param menuId
	 * @return 菜单详情
	 */
	@Override
	public MenuDetailVO getMenuDetail(Long menuId) {
		SsoMenu ssoMenu = ssoMenuMapper.getByMenuId(menuId);

		//菜单已删除
		if (DelFlagEnum.DELETED.getStatus().equals(ssoMenu.getDelFlag())) {
			throw new BusinessException(MenuResStatusEnum.ALREADY_DELETE);
		}

		MenuDetailVO result = BeanCopierUtil.copy(ssoMenu, MenuDetailVO.class);
		//处理权限
		if (StringUtils.isNotBlank(ssoMenu.getPermissions())) {
			result.setPermissionList(Arrays.asList(ssoMenu.getPermissions().split(SsoConstants.SPLIT_ESCAPE)));
		}

		return result;
	}

	/**
	 * 新增菜单
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	@Override
	public void addMenu(MenuSaveBO saveBO) {

		//菜单名称唯一性校验
		this.checkMenuNameUniq(saveBO);

		//执行保存
		this.doInsertMenu(saveBO);

		log.info("[ 菜单 ] >> [ 新增完成 ] >> {}", saveBO.getLogValue());
	}

	/**
	 * 修改菜单
	 *
	 * @param updateBO
	 * @author 程序员小强
	 */
	@Override
	public void updateMenu(MenuUpdateBO updateBO) {
		SsoMenu ssoMenu = ssoMenuMapper.getByMenuId(updateBO.getMenuId());
		//菜单已删除
		if (null == ssoMenu || DelFlagEnum.DELETED.getStatus().equals(ssoMenu.getDelFlag())) {
			log.error("[ 菜单已删除 ] >> {}", updateBO.getLogValue());
			throw new BusinessException(MenuResStatusEnum.ALREADY_DELETE);
		}

		//父菜单不能选自己
		if (updateBO.getMenuId().equals(updateBO.getMenuParentId())) {
			log.error("[ 父菜单不能选自己 ] >> {}", updateBO.getLogValue());
			throw new BusinessException(MenuResStatusEnum.PARENT_CANNOT_SELF);
		}

		//菜单名称唯一性校验
		this.checkMenuNameUniq(updateBO, updateBO.getMenuId());

		//执行保存
		this.doUpdateMenu(updateBO);

		log.info("[ 菜单 ] >> [ 修改完成 ] >> {}", updateBO.getLogValue());
	}

	/**
	 * 删除菜单（逻辑删）
	 *
	 * @param deleteBO
	 * @author 程序员小强
	 */
	@Override
	public void deleteMenu(MenuDeleteBO deleteBO) {
		//菜单下存在子菜单
		if (this.isExistChildren(deleteBO.getMenuId())) {
			log.error("[ 菜单下存在子菜单 ] >> {}", deleteBO.getLogValue());
			throw new BusinessException(MenuResStatusEnum.HASH_CHILD_DEPT);
		}

		SsoMenu ssoMenu = new SsoMenu();
		ssoMenu.setMenuId(deleteBO.getMenuId());
		ssoMenu.setUpdateBy(deleteBO.getOperateBy());
		//标记菜单为删除态
		ssoMenu.setDelFlag(DelFlagEnum.DELETED.getStatus());
		ssoMenuMapper.updateByMenuIdSelective(ssoMenu);

		//删除菜单角色关系
		ssoRoleMenuMapper.deleteByMenuId(deleteBO.getMenuId());

		log.info("[ 菜单 ] >> [ 删除完成 ] >> {}", deleteBO.getLogValue());
	}


	/**
	 * 判断是否存在子菜单
	 *
	 * @author 程序员小强
	 */
	private boolean isExistChildren(Long menuId) {
		SsoMenuQuery query = new SsoMenuQuery();
		query.setMenuParentId(menuId);
		query.setDelFlag(DelFlagEnum.OK.getStatus());
		return ssoMenuMapper.countByCondition(query) > 0;
	}

	/**
	 * 执行新增菜单
	 *
	 * @param saveBO
	 */
	private void doInsertMenu(MenuSaveBO saveBO) {
		SsoMenu ssoMenu = this.doBuildEditSsoMenu(saveBO);
		ssoMenuMapper.insertSelective(ssoMenu);
	}

	/**
	 * 执行修改菜单
	 *
	 * @param updateBO
	 */
	private void doUpdateMenu(MenuUpdateBO updateBO) {
		SsoMenu ssoMenu = this.doBuildEditSsoMenu(updateBO);
		ssoMenu.setMenuId(updateBO.getMenuId());
		ssoMenuMapper.updateByMenuId(ssoMenu);
	}

	/**
	 * 校验 系统下-同父级下-菜单名称的 唯一性
	 *
	 * @param saveBO
	 * @param excludeMenuId 需要排除的菜单ID（修改时用）
	 */
	private void checkMenuNameUniq(MenuSaveBO saveBO, Long... excludeMenuId) {
		if (StringUtils.isEmpty(saveBO.getSysCode()) || StringUtils.isEmpty(saveBO.getMenuName())) {
			return;
		}

		SsoMenuQuery query = new SsoMenuQuery()
				.setSysCode(saveBO.getSysCode())
				.setMenuName(saveBO.getMenuName())
				.setMenuParentId(saveBO.getMenuParentId())
				.setDelFlag(DelFlagEnum.OK.getStatus());

		//需要排除
		if (!ObjectUtils.isEmpty(excludeMenuId)) {
			query.setExcludeMenuId(excludeMenuId[0]);
			return;
		}

		if (ssoMenuMapper.countByCondition(query) > 0) {
			//同一个父级下菜单名称已经存在
			throw new BusinessException(MenuResStatusEnum.MENU_NAME_EXISTS_IN_SAME_PARENT, saveBO.getMenuName());
		}
	}

	/**
	 * 构建SsoMenu 对象
	 *
	 * @param saveBO
	 * @return
	 */
	private SsoMenu doBuildEditSsoMenu(MenuSaveBO saveBO) {
		SsoMenu ssoMenu = new SsoMenu();
		ssoMenu.setSysCode(saveBO.getSysCode());
		ssoMenu.setMenuName(saveBO.getMenuName());
		ssoMenu.setMenuType(saveBO.getMenuType());
		ssoMenu.setMenuParentId(saveBO.getMenuParentId());
		ssoMenu.setSortNum(saveBO.getSortNum());
		ssoMenu.setPath(saveBO.getPath());
		ssoMenu.setIcon(saveBO.getIcon());
		ssoMenu.setComponent(saveBO.getComponent());
		ssoMenu.setUseType(saveBO.getUseType());
		ssoMenu.setVisible(saveBO.getVisible());
		ssoMenu.setStatus(saveBO.getStatus());
		ssoMenu.setRemarks(saveBO.getRemarks());
		ssoMenu.setCreateBy(saveBO.getOperateBy());
		ssoMenu.setUpdateBy(saveBO.getOperateBy());
		ssoMenu.setPermissions("");
		//权限标识处理
		if (!CollectionUtils.isEmpty(saveBO.getPermissionList())) {
			StringJoiner permissions = new StringJoiner(SsoConstants.PERMISSIONS_SPLIT);
			saveBO.getPermissionList().forEach(permissions::add);
			ssoMenu.setPermissions(permissions.toString());
		}
		return ssoMenu;
	}

}
