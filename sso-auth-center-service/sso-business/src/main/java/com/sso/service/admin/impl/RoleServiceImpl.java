package com.sso.service.admin.impl;

import com.sso.common.enums.DelFlagEnum;
import com.sso.common.enums.exception.RoleResStatusEnum;
import com.sso.common.exception.BusinessException;
import com.sso.common.model.result.ResultPageModel;
import com.sso.common.utils.StringUtils;
import com.sso.common.utils.bean.BeanCopierUtil;
import com.sso.dao.dto.RoleCountDTO;
import com.sso.dao.entity.SsoRole;
import com.sso.dao.entity.SsoRoleMenu;
import com.sso.dao.mapper.SsoRoleMapper;
import com.sso.dao.mapper.SsoRoleMenuMapper;
import com.sso.dao.mapper.SsoUserRoleMapper;
import com.sso.dao.query.SsoRoleQuery;
import com.sso.model.bo.role.*;
import com.sso.model.vo.role.RoleDetailVO;
import com.sso.model.vo.role.RoleOptionVO;
import com.sso.model.vo.role.RolePageVO;
import com.sso.service.admin.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色相关接口
 *
 * @author 程序员小强
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

	@Resource
	private SsoRoleMapper ssoRoleMapper;
	@Resource
	private SsoRoleMenuMapper ssoRoleMenuMapper;
	@Resource
	private SsoUserRoleMapper ssoUserRoleMapper;

	/**
	 * 分页列表
	 *
	 * @param pageBO
	 * @return 角色列表
	 */
	@Override
	public ResultPageModel<RolePageVO> listPageRole(RoleListPageBO pageBO) {
		//构建参数参数
		SsoRoleQuery query = new SsoRoleQuery(pageBO)
				.setSysCode(pageBO.getSysCode())
				.setRoleNameLike(pageBO.getRoleNameLike())
				.setRoleKeyLike(pageBO.getRoleKeyLike())
				.setStatus(pageBO.getStatus())
				.setDelFlag(DelFlagEnum.OK.getStatus());

		//统计行数
		int total = ssoRoleMapper.countByCondition(query);
		if (total <= 0) {
			return ResultPageModel.defaultValue();
		}

		//分页查询
		List<SsoRole> roleList = ssoRoleMapper.listPageByCondition(query);
		if (CollectionUtils.isEmpty(roleList)) {
			return ResultPageModel.defaultValue();
		}

		//提取当前页角色ID
		List<Long> roleIdList = roleList.stream().map(SsoRole::getRoleId).collect(Collectors.toList());

		//用户角色使用量统计
		Map<Long, Integer> userRoleCountMap = this.getUserRoleCountMap(roleIdList);

		return ResultPageModel.success(this.buildRolePageResult(roleList, userRoleCountMap), total);
	}

	/**
	 * 角色下拉选项列表
	 *
	 * @param optionBO
	 * @return 角色列表
	 */
	@Override
	public List<RoleOptionVO> listRoleOption(RoleListOptionBO optionBO) {
		//构建参数参数
		SsoRoleQuery query = new SsoRoleQuery(optionBO)
				.setSysCode(optionBO.getSysCode())
				.setRoleNameLike(optionBO.getRoleNameLike())
				//未删除
				.setDelFlag(DelFlagEnum.OK.getStatus());

		//分页查询
		List<SsoRole> roleList = ssoRoleMapper.listPageByCondition(query);
		if (CollectionUtils.isEmpty(roleList)) {
			return new ArrayList<>();
		}

		return BeanCopierUtil.copyList(roleList, RoleOptionVO.class);
	}


	/**
	 * 角色详情
	 *
	 * @param roleId 角色ID
	 * @return 角色详情
	 * @author 程序员小强
	 */
	@Override
	public RoleDetailVO getDeptDetail(Long roleId) {
		SsoRole ssoRole = ssoRoleMapper.getByRoleId(roleId);

		//角色已删除
		if (DelFlagEnum.DELETED.getStatus().equals(ssoRole.getDelFlag())) {
			throw new BusinessException(RoleResStatusEnum.ALREADY_DELETE);
		}

		RoleDetailVO result = BeanCopierUtil.copy(ssoRole, RoleDetailVO.class);
		List<Long> menuIdList = ssoRoleMenuMapper.getMenuIdListByRoleId(roleId);
		if (CollectionUtils.isEmpty(menuIdList)) {
			result.setMenuIdList(new ArrayList<>(0));
			return result;
		}
		result.setMenuIdList(menuIdList);
		return result;
	}

	/**
	 * 新增角色
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	@Override
	public void addRole(RoleSaveBO saveBO) {

		//角色名称判重
		this.checkRoleNameUniq(saveBO.getSysCode(), saveBO.getRoleName());

		//角色key判重
		this.checkRoleKeyUniq(saveBO.getSysCode(), saveBO.getRoleKey());

		//执行添加角色
		SsoRole ssoRole = this.doInsertRole(saveBO);

		//添加角色菜单关系
		this.doBatchSaveRoleMenuRelation(saveBO.getSysCode(), ssoRole.getRoleId(), saveBO.getMenuIdList());

		log.info("[ 角色 ] >> [ 新增完成 ] >> {} ", saveBO.getLogValue());

	}

	/**
	 * 修改角色
	 *
	 * @param updateBO
	 * @author 程序员小强
	 */
	@Override
	public void updateRole(RoleUpdateBO updateBO) {

		//排除自身-角色名称判重
		this.checkRoleNameUniq(updateBO.getSysCode(), updateBO.getRoleName(), updateBO.getRoleId());

		//排除自身-角色key判重
		this.checkRoleKeyUniq(updateBO.getSysCode(), updateBO.getRoleKey(), updateBO.getRoleId());

		//执行-修改角色
		this.doUpdateRole(updateBO);

		//删除角色菜单关系
		ssoRoleMenuMapper.deleteByRoleId(updateBO.getRoleId());

		//批量添加角色菜单关系
		this.doBatchSaveRoleMenuRelation(updateBO.getSysCode(), updateBO.getRoleId(), updateBO.getMenuIdList());

		log.info("[ 角色 ] >> [ 修改完成 ] >> {} ", updateBO.getLogValue());

	}

	/**
	 * 删除角色（逻辑删）
	 *
	 * @param deleteBO
	 * @author 程序员小强
	 */
	@Override
	public void deleteRole(RoleDeleteBO deleteBO) {
		SsoRole ssoRole = ssoRoleMapper.getByRoleId(deleteBO.getRoleId());
		//该角色不存在
		if (null == ssoRole) {
			log.error("[ 该角色不存在 ] >> {} ", deleteBO.getLogValue());
			throw new BusinessException(RoleResStatusEnum.ROLE_NOT_EXISTS);
		}
		//该角色已经删除
		if (DelFlagEnum.DELETED.getStatus().equals(ssoRole.getDelFlag())) {
			log.error("[ 该角色已经删除 ] >> {} ", deleteBO.getLogValue());
			throw new BusinessException(RoleResStatusEnum.ALREADY_DELETE);
		}

		//校验是否已经存在用户使用
		if (ssoUserRoleMapper.countByRoleId(deleteBO.getRoleId()) > 0) {
			log.error("[ 校验是否已经存在用户使用 ] 不可删除 >> {} ", deleteBO.getLogValue());
			throw new BusinessException(RoleResStatusEnum.ALREADY_BIND_USER);
		}

		//标记为删除状态
		SsoRole ssoUpdateRole = new SsoRole();
		ssoUpdateRole.setRoleId(deleteBO.getRoleId());
		ssoUpdateRole.setDelFlag(DelFlagEnum.DELETED.getStatus());
		ssoUpdateRole.setUpdateBy(deleteBO.getOperateBy());
		ssoRoleMapper.updateByRoleIdSelective(ssoUpdateRole);

		//删除角色菜单关系
		ssoRoleMenuMapper.deleteByRoleId(deleteBO.getRoleId());
		log.info("[ 角色 ] >> [ 删除完成 ] >> {} ", deleteBO.getLogValue());

	}

	/**
	 * 移除角色用户绑定关系
	 *
	 * @param removeUserBO
	 * @author 程序员小强
	 */
	@Override
	public void removeUserRole(RoleRemoveUserBO removeUserBO) {
		ssoUserRoleMapper.deleteByRoleIdAndUserId(removeUserBO.getRoleId(), removeUserBO.getUserId());
		log.info("[ 角色 ] >> [ 移除角色用户绑定关系 ] >> {} ", removeUserBO.getLogValue());
	}

	/**
	 * 执行-添加角色
	 *
	 * @param saveBO
	 */
	private SsoRole doInsertRole(RoleSaveBO saveBO) {
		SsoRole ssoRole = this.buildSsoRole(saveBO);
		ssoRoleMapper.insertSelective(ssoRole);
		return ssoRole;
	}

	/**
	 * 执行-修改角色
	 *
	 * @param updateBO
	 */
	private void doUpdateRole(RoleUpdateBO updateBO) {
		SsoRole ssoRole = this.buildSsoRole(updateBO);
		ssoRole.setRoleId(updateBO.getRoleId());
		ssoRoleMapper.updateByRoleId(ssoRole);
	}

	/**
	 * 批量新增角色菜单关系
	 *
	 * @param sysCode
	 * @param roleId
	 * @param menuIdList
	 */
	private void doBatchSaveRoleMenuRelation(String sysCode, Long roleId, List<Long> menuIdList) {
		if (CollectionUtils.isEmpty(menuIdList)) {
			return;
		}
		//添加角色菜单关系
		SsoRoleMenu ssoRoleMenu = null;
		List<SsoRoleMenu> ssoRoleMenuList = new ArrayList<>(menuIdList.size());
		for (Long menuId : menuIdList) {
			ssoRoleMenu = new SsoRoleMenu();
			ssoRoleMenu.setSysCode(sysCode);
			ssoRoleMenu.setRoleId(roleId);
			ssoRoleMenu.setMenuId(menuId);
			ssoRoleMenuList.add(ssoRoleMenu);
		}
		ssoRoleMenuMapper.batchSave(ssoRoleMenuList);
		log.info("[ 新增角色-批量新增角色菜单关系 ] >> roleId:{} , menuIdSize:{}", roleId, menuIdList.size());
	}


	/**
	 * 校验 系统下角色名 唯一性
	 *
	 * @param sysCode       系统编码
	 * @param roleName      角色名称
	 * @param excludeRoleId 需要排除的角色ID（修改时用）
	 */
	private void checkRoleNameUniq(String sysCode, String roleName, Long... excludeRoleId) {
		if (StringUtils.isEmpty(sysCode) || StringUtils.isEmpty(roleName)) {
			return;
		}
		SsoRole ssoRole = ssoRoleMapper.getBySysCodeAndName(sysCode, roleName);
		if (null == ssoRole) {
			return;
		}
		if (!ObjectUtils.isEmpty(excludeRoleId) && ssoRole.getRoleId().equals(excludeRoleId[0])) {
			return;
		}
		log.error("[ 角色名称已经存在 ] >> sysCode:{} , roleName:{} excludeRoleId:{}", sysCode, roleName, excludeRoleId);
		//角色名称已经存在
		throw new BusinessException(RoleResStatusEnum.ROLE_NAME_EXISTS, roleName);
	}

	/**
	 * 校验 系统下角色key 唯一性
	 *
	 * @param sysCode       系统编码
	 * @param roleKey       角色key
	 * @param excludeRoleId 需要排除的角色ID（修改时用）
	 */
	private void checkRoleKeyUniq(String sysCode, String roleKey, Long... excludeRoleId) {
		if (StringUtils.isEmpty(sysCode) || StringUtils.isEmpty(roleKey)) {
			return;
		}
		SsoRole ssoRole = ssoRoleMapper.getBySysCodeAndRoleKey(sysCode, roleKey);
		if (null == ssoRole) {
			return;
		}
		if (!ObjectUtils.isEmpty(excludeRoleId) && ssoRole.getRoleId().equals(excludeRoleId[0])) {
			return;
		}

		log.error("[ 角色key已经存在 ] >> sysCode:{} , roleName:{} excludeRoleId:{}", sysCode, roleKey, excludeRoleId);
		//角色 key 已经存在
		throw new BusinessException(RoleResStatusEnum.KEY_NAME_EXISTS, roleKey);
	}

	/**
	 * 构建角色分页结果集
	 *
	 * @param roleList
	 * @param userRoleCountMap
	 * @return
	 */
	private List<RolePageVO> buildRolePageResult(List<SsoRole> roleList, Map<Long, Integer> userRoleCountMap) {
		RolePageVO itemVo = null;
		List<RolePageVO> resultList = new ArrayList<>(roleList.size());
		for (SsoRole ssoRole : roleList) {
			itemVo = BeanCopierUtil.copy(ssoRole, RolePageVO.class);
			itemVo.setUserCount(userRoleCountMap.getOrDefault(ssoRole.getRoleId(), 0));
			resultList.add(itemVo);
		}
		return resultList;
	}

	/**
	 * 用户角色关系
	 *
	 * @param roleIdList
	 */
	private Map<Long, Integer> getUserRoleCountMap(List<Long> roleIdList) {
		if (CollectionUtils.isEmpty(roleIdList)) {
			return new HashMap<>(0);
		}
		List<RoleCountDTO> countByRoleList = ssoUserRoleMapper.countByRoleIdList(roleIdList);
		if (CollectionUtils.isEmpty(countByRoleList)) {
			return new HashMap<>(0);
		}
		return countByRoleList.stream().collect(Collectors.toMap(RoleCountDTO::getRoleId, RoleCountDTO::getCount, (k1, k2) -> k1));
	}

	/**
	 * 构建 SsoRole 对象
	 *
	 * @param saveBO
	 * @return
	 */
	private SsoRole buildSsoRole(RoleSaveBO saveBO) {
		SsoRole ssoRole = new SsoRole();
		ssoRole.setSysCode(saveBO.getSysCode());
		ssoRole.setRoleName(saveBO.getRoleName());
		ssoRole.setRoleKey(saveBO.getRoleKey());
		ssoRole.setStatus(saveBO.getStatus());
		ssoRole.setSortNum(saveBO.getSortNum());
		ssoRole.setRemarks(saveBO.getRemarks());
		ssoRole.setCreateBy(saveBO.getOperateBy());
		ssoRole.setUpdateBy(saveBO.getOperateBy());
		return ssoRole;
	}

}
