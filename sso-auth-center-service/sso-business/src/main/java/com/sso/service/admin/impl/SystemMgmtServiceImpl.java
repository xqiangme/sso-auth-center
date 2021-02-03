package com.sso.service.admin.impl;

import com.sso.common.enums.DelFlagEnum;
import com.sso.common.exception.BusinessException;
import com.sso.common.model.result.ResultPageModel;
import com.sso.common.utils.bean.BeanCopierUtil;
import com.sso.dao.dto.SsoUserMgmtDTO;
import com.sso.dao.entity.SsoSystemManager;
import com.sso.dao.mapper.SsoSystemManagerMapper;
import com.sso.dao.query.SystemMgmtPageQuery;
import com.sso.model.bo.platform.SystemMgmtListPageBO;
import com.sso.model.bo.platform.SystemMgmtRemoveBO;
import com.sso.model.bo.platform.SystemMgmtSaveBO;
import com.sso.model.bo.platform.SystemMgmtStatusBO;
import com.sso.model.vo.platform.SystemMgmtPageVO;
import com.sso.service.admin.SystemMgmtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


/**
 * 系统管理-数据权限相关接口
 *
 * @author 程序员小强
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemMgmtServiceImpl implements SystemMgmtService {

	@Resource
	private SsoSystemManagerMapper ssoSystemManagerMapper;

	/**
	 * 系统管理员-用户分页列表
	 *
	 * @param pageBO
	 * @return 用户
	 */
	@Override
	public ResultPageModel<SystemMgmtPageVO> listPageSysMgmtUser(SystemMgmtListPageBO pageBO) {
		//构建参数参数
		SystemMgmtPageQuery query = this.buildUserPageQuery(pageBO);

		//统计行数
		int total = ssoSystemManagerMapper.countSysMgmtUser(query);
		if (total <= 0) {
			return ResultPageModel.defaultValue();
		}


		//分页查询
		List<SsoUserMgmtDTO> userList = ssoSystemManagerMapper.listPageSysMgmtUser(query);
		if (CollectionUtils.isEmpty(userList)) {
			return ResultPageModel.defaultValue();
		}

		return ResultPageModel.success(BeanCopierUtil.copyList(userList, SystemMgmtPageVO.class), total);
	}

	/**
	 * 新增-管理员权限
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	@Override
	public void addSystemMgmt(SystemMgmtSaveBO saveBO) {
		int count = ssoSystemManagerMapper.countBySysCodeAndUserId(saveBO.getSysCode(), saveBO.getUserId());
		if (count > 0) {
			log.error("[ 该用户管理权限关系已经存在 ] >> {} ", saveBO.getLogValue());
			throw new BusinessException("该用户管理权限关系已经存在");
		}

		SsoSystemManager saveEntity = new SsoSystemManager();
		saveEntity.setUserId(saveBO.getUserId());
		saveEntity.setStatus(saveBO.getStatus());
		saveEntity.setSysCode(saveBO.getSysCode());
		saveEntity.setCreateBy(saveBO.getOperateBy());
		saveEntity.setUpdateBy(saveBO.getOperateBy());
		ssoSystemManagerMapper.insertSelective(saveEntity);
		log.info("[ 新增-管理员权限完成 ] >> {} ", saveBO.getLogValue());

	}

	/**
	 * 修改-管理员权限账号状态
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	@Override
	public void updateStatus(SystemMgmtStatusBO saveBO) {
		SsoSystemManager updateEntity = new SsoSystemManager();
		updateEntity.setId(saveBO.getId());
		updateEntity.setStatus(saveBO.getStatus());
		ssoSystemManagerMapper.updateById(updateEntity);
		log.info("[ 修改-管理员权限账号状态完成 ] >> {} ", saveBO.getLogValue());
	}

	/**
	 * 移除管理员权限
	 *
	 * @param removeBO
	 * @author 程序员小强
	 */
	@Override
	public void removeUserSystemMgmt(SystemMgmtRemoveBO removeBO) {
		int count = ssoSystemManagerMapper.countBySysCodeAndUserId(removeBO.getSysCode(), removeBO.getUserId());
		if (count <= 0) {
			log.error("[ 该用户管理权限不存在或已移除 ] >> {} ", removeBO.getLogValue());
			throw new BusinessException("该用户管理权限不存在或已移除");
		}

		SsoSystemManager updateEntity = new SsoSystemManager();
		updateEntity.setUserId(removeBO.getUserId());
		updateEntity.setSysCode(removeBO.getSysCode());
		updateEntity.setDelFlag(DelFlagEnum.DELETED.getStatus());
		updateEntity.setUpdateBy(removeBO.getOperateBy());
		ssoSystemManagerMapper.updateBySysCodeAndUserId(updateEntity);
		log.info("[ 移除-管理员权限完成 ] >> {} ", removeBO.getLogValue());

	}

	/**
	 * 构建查询参数
	 *
	 * @param pageBO
	 * @return
	 */
	private SystemMgmtPageQuery buildUserPageQuery(SystemMgmtListPageBO pageBO) {
		SystemMgmtPageQuery query = new SystemMgmtPageQuery()
				.setSysCode(pageBO.getSysCode())
				.setUsernameLike(pageBO.getUsernameLike())
				.setNickNameLike(pageBO.getNickNameLike())
				.setRealNameLike(pageBO.getRealNameLike())
				.setPhoneLike(pageBO.getPhoneLike())
				.setUserStatus(pageBO.getUserStatus())
				.setRelationStatus(pageBO.getRelationStatus())
				//未删除
				.setDelFlag(DelFlagEnum.OK.getStatus());

		query.setPage(pageBO.getPage());
		query.setPageSize(pageBO.getPageSize());
		return query;
	}
}
