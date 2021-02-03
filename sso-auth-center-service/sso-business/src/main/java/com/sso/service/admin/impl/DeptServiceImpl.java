/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.service.admin.impl;


import com.sso.common.enums.DelFlagEnum;
import com.sso.common.enums.DeptStatusEnum;
import com.sso.common.enums.exception.DeptResStatusEnum;
import com.sso.common.enums.exception.SysResStatusEnum;
import com.sso.common.exception.BusinessException;
import com.sso.common.utils.ObjectUtils;
import com.sso.common.utils.bean.BeanCopierUtil;
import com.sso.common.utils.tree.ListToTreeUtil;
import com.sso.dao.entity.SsoDept;
import com.sso.dao.mapper.SsoDeptMapper;
import com.sso.dao.mapper.SsoDeptTreePathMapper;
import com.sso.dao.mapper.SsoUserDeptMapper;
import com.sso.dao.query.SsoDeptQuery;
import com.sso.model.bo.dept.DeptDeleteBO;
import com.sso.model.bo.dept.DeptMgmtTreeBO;
import com.sso.model.bo.dept.DeptSaveBO;
import com.sso.model.bo.dept.DeptUpdateBO;
import com.sso.model.vo.dept.DeptDetailVO;
import com.sso.model.vo.dept.DeptMgmtTreeNodeVO;
import com.sso.model.vo.dept.DeptOptionTreeNodeVO;
import com.sso.service.admin.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门管理接口实现类
 *
 * @author 程序员小强
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DeptServiceImpl implements DeptService {

	@Resource
	private SsoDeptMapper ssoDeptMapper;
	@Resource
	private SsoUserDeptMapper ssoUserDeptMapper;
	@Resource
	private SsoDeptTreePathMapper ssoDeptTreePathMapper;


	/**
	 * 获取部门树列表
	 *
	 * @author 程序员小强
	 */
	@Override
	public List<DeptMgmtTreeNodeVO> listDeptMgmtTree(DeptMgmtTreeBO treeBO) {
		SsoDeptQuery query = new SsoDeptQuery()
				.setSysCode(treeBO.getSysCode())
				.setStatus(treeBO.getStatus())
				.setDeptNameLike(treeBO.getDeptNameLike())
				.setDelFlag(DelFlagEnum.OK.getStatus());

		List<SsoDept> ssoDeptList = ssoDeptMapper.listByCondition(query);
		if (CollectionUtils.isEmpty(ssoDeptList)) {
			return new ArrayList<>(0);
		}

		//转换成树结构
		List<DeptMgmtTreeNodeVO> treeNodeVOList = BeanCopierUtil.copyList(ssoDeptList, DeptMgmtTreeNodeVO.class);
		ListToTreeUtil<DeptMgmtTreeNodeVO> result = new ListToTreeUtil<>();

		return result.getTreeListObject(treeNodeVOList);
	}

	/**
	 * 部门下拉选项树
	 * 状态：仅正常+未删除
	 *
	 * @param sysCode 系统编码
	 * @return 部门树
	 * @author 程序员小强
	 */
	@Override
	public List<DeptOptionTreeNodeVO> listDeptOptionTree(String sysCode) {
		SsoDeptQuery query = new SsoDeptQuery()
				.setSysCode(sysCode)
				.setStatus(DeptStatusEnum.OK.getStatus())
				.setDelFlag(DelFlagEnum.OK.getStatus());

		List<SsoDept> ssoDeptList = ssoDeptMapper.listByCondition(query);

		//转换成树结构
		List<DeptOptionTreeNodeVO> treeNodeVOList = BeanCopierUtil.copyList(ssoDeptList, DeptOptionTreeNodeVO.class);
		ListToTreeUtil<DeptOptionTreeNodeVO> result = new ListToTreeUtil<>();

		return result.getTreeListObject(treeNodeVOList);
	}

	/**
	 * 部门详情BO
	 *
	 * @author 程序员小强
	 */
	@Override
	public DeptDetailVO getDeptDetail(Long deptId) {
		SsoDept ssoDept = ssoDeptMapper.getByDeptId(deptId);

		//部门不存在
		if (null == ssoDept) {
			log.error("[ 部门不存在 ] >> deptId:{}", deptId);
			throw new BusinessException(DeptResStatusEnum.DEPT_NOT_EXISTS);
		}

		//部门已删除
		if (DelFlagEnum.DELETED.getStatus().equals(ssoDept.getDelFlag())) {
			log.error("[ 部门已删除 ] >> deptId:{}", deptId);
			throw new BusinessException(DeptResStatusEnum.ALREADY_DELETE);
		}

		return BeanCopierUtil.copy(ssoDept, DeptDetailVO.class);
	}

	/**
	 * 新增部门
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	@Override
	public void addDept(DeptSaveBO saveBO) {
		//部门名称判重
		if (this.checkNameExists(saveBO.getSysCode(), saveBO.getDeptName())) {
			log.error("[ 部门名称重复 ] >> {}", saveBO.getLogValue());
			throw new BusinessException(DeptResStatusEnum.REPEAT_NAME, saveBO.getDeptName());
		}

		//校验父部门
		this.checkDeptParent(saveBO.getDeptParentId());

		SsoDept ssoDept = BeanCopierUtil.copy(saveBO, SsoDept.class);
		ssoDept.setCreateBy(saveBO.getOperateBy());
		ssoDept.setUpdateBy(saveBO.getOperateBy());

		int row = ssoDeptMapper.insertSelective(ssoDept);
		if (row == 0) {
			log.error("[ 部门新增失败 ] >> {}", saveBO.getLogValue());
			throw new BusinessException(SysResStatusEnum.ADD_FAIL);
		}

		// 维护部门关系节点
		ssoDeptTreePathMapper.insertBatch(ssoDept.getDeptId(), ssoDept.getDeptParentId());
		log.info("[ 部门 ] >> [ 新增完成 ] >> {}", saveBO.getLogValue());
	}

	/**
	 * 修改部门
	 *
	 * @param updateBO
	 * @author 程序员小强
	 */
	@Override
	public void updateDept(DeptUpdateBO updateBO) {
		//查询旧的部门信息
		SsoDept oldDept = ssoDeptMapper.getByDeptId(updateBO.getDeptId());
		//部门不存在或者已删除
		if (null == oldDept || DelFlagEnum.DELETED.getStatus().equals(oldDept.getDelFlag())) {
			log.error("[ 部门不存在或者已删除 ] >> {}", updateBO.getLogValue());
			throw new BusinessException(DeptResStatusEnum.NOT_EXISTS_OR_DELETE);
		}

		//父部门不能选自己
		if (updateBO.getDeptId().equals(updateBO.getDeptParentId())) {
			log.error("[ 父部门不能选自己 ] >> {}", updateBO.getLogValue());
			throw new BusinessException(DeptResStatusEnum.PARENT_CANNOT_SELF);
		}

		//部门名称判重
		if (this.checkNameExists(updateBO.getSysCode(), updateBO.getDeptName(), updateBO.getDeptId())) {
			log.error("[ 部门名称重复 ] >> {}", updateBO.getLogValue());
			throw new BusinessException(DeptResStatusEnum.REPEAT_NAME, updateBO.getDeptName());
		}

		//校验父部门
		this.checkDeptParent(updateBO.getDeptParentId());

		//状态变为停用-校验是否存在未停用的子部门
		if (!oldDept.getStatus().equals(updateBO.getStatus()) && DeptStatusEnum.DISABLE.getStatus().equals(updateBO.getStatus())) {
			this.checkHasEnableChild(updateBO.getDeptId());
		}

		SsoDept ssoDept = BeanCopierUtil.copy(updateBO, SsoDept.class);
		ssoDept.setUpdateBy(updateBO.getOperateBy());
		int row = ssoDeptMapper.updateByDeptId(ssoDept);
		if (row == 0) {
			log.error("[ 部门修改失败 ] >> {}", updateBO.getLogValue());
			throw new BusinessException(SysResStatusEnum.UPDATE_FAIL);
		}

		//父部门无变化
		if (oldDept.getDeptParentId().equals(updateBO.getDeptParentId())) {
			log.info("[ 部门修改完成 ] 上级无变化 >> {}", updateBO.getLogValue());
			return;
		}

		//若链路变化-重新建立-祖先后代关系链路
		this.doRestDeptTreePath(updateBO, ssoDept);

		log.info("[ 部门 ] >> [ 修改完成 ] 部门修改完成 >> {}", updateBO.getLogValue());
	}

	/**
	 * 删除部门（部门逻辑删-用户部门关系物理删）
	 *
	 * @param deleteBO
	 * @author 程序员小强
	 */
	@Override
	public void deleteDept(DeptDeleteBO deleteBO) {

		//部门下存在子部门
		if (this.isExistChildren(deleteBO.getDeptId())) {
			log.error("[ 部门下存在子部门 ] >> {}", deleteBO.getLogValue());
			throw new BusinessException(DeptResStatusEnum.HASH_CHILD_DEPT);
		}

		//部门下存在用户
		if (this.isExistUser(deleteBO.getDeptId())) {
			log.error("[ 部门下存在用户 ] >> {}", deleteBO.getLogValue());
			throw new BusinessException(DeptResStatusEnum.HASH_CHILD_USER);
		}

		SsoDept ssoDept = new SsoDept();
		ssoDept.setDeptId(deleteBO.getDeptId());
		ssoDept.setUpdateBy(deleteBO.getOperateBy());
		ssoDept.setDelFlag(DelFlagEnum.DELETED.getStatus());
		ssoDeptMapper.updateByDeptIdSelective(ssoDept);
		log.info("[ 部门 ] >> [ 删除完成 ] >> {}", deleteBO.getLogValue());
	}

	/**
	 * 重新建立-部门祖先后代关系
	 *
	 * @param updateBO
	 * @param ssoDept
	 */
	private void doRestDeptTreePath(DeptUpdateBO updateBO, SsoDept ssoDept) {
		//查询当前所有子
		List<SsoDept> allChild = ssoDeptTreePathMapper.getAllChild(updateBO.getDeptId());

		// 删除子树
		ssoDeptTreePathMapper.deleteChildTree(updateBO.getDeptId());

		// 重新建立部门关系节点
		ssoDeptTreePathMapper.insertBatch(ssoDept.getDeptId(), ssoDept.getDeptParentId());

		//校验是否存在未停用的子部门
		if (DeptStatusEnum.OK.getStatus().equals(ssoDept.getStatus())) {
			//启用部门 > 启用该部门的所有上级部门
			ssoDeptMapper.updateAllParentStatus(ssoDept);
		}

		if (CollectionUtils.isEmpty(allChild)) {
			return;
		}

		for (SsoDept dept : allChild) {
			if (dept.getDeptId().equals(updateBO.getDeptId())) {
				continue;
			}
			// 删除子树
			ssoDeptTreePathMapper.deleteChildTree(dept.getDeptId());
			// 重新建立部门关系节点
			ssoDeptTreePathMapper.insertBatch(dept.getDeptId(), dept.getDeptParentId());
		}
		log.info("[ 部门 ] >> [ 修改 ] 更新链路 oldChildSize:{} ", allChild.size());
	}

	/**
	 * 判断是否存在人员
	 *
	 * @author 程序员小强
	 */
	private boolean isExistUser(Long deptId) {
		if (null == deptId) {
			return false;
		}
		return ssoUserDeptMapper.countByDeptId(deptId) > 0;
	}

	/**
	 * 判断是否存子部门
	 *
	 * @author 程序员小强
	 */
	private boolean isExistChildren(Long deptId) {
		SsoDeptQuery query = new SsoDeptQuery();
		query.setDeptParentId(deptId);
		query.setDelFlag(DelFlagEnum.OK.getStatus());
		return ssoDeptMapper.countByCondition(query) > 0;
	}

	/**
	 * 校验父部门
	 *
	 * @param deptParentId
	 */
	private void checkDeptParent(Long deptParentId) {
		//根部门
		if (deptParentId <= 0) {
			return;
		}
		SsoDept ssoDept = ssoDeptMapper.getByDeptId(deptParentId);
		//父部门不存在
		if (null == ssoDept) {
			log.error("[ 父部门不存在 ] >> deptParentId:{}", deptParentId);
			throw new BusinessException(DeptResStatusEnum.PARENT_NOT_EXISTS);
		}

		//父部门已删除
		if (DelFlagEnum.DELETED.getStatus().equals(ssoDept.getDelFlag())) {
			log.error("[ 父部门已删除 ] >> deptParentId:{}", deptParentId);
			throw new BusinessException(DeptResStatusEnum.PARENT_IS_DELETE);
		}
	}

	/**
	 * 校验是否存在启用的子部门
	 *
	 * @param deptParentId
	 */
	private void checkHasEnableChild(Long deptParentId) {
		//该部门包含未停用的子部门
		if (ssoDeptMapper.countByDeptParentIdAndStatus(deptParentId, DeptStatusEnum.OK.getStatus()) > 0) {
			throw new BusinessException(DeptResStatusEnum.HASH_ENABLE_CHILD);
		}
	}

	/**
	 * 部门名称判重
	 *
	 * @param sysCode
	 * @param deptName
	 * @param excludeDeptId 需要排除的部门ID
	 */
	private boolean checkNameExists(String sysCode, String deptName, Long... excludeDeptId) {
		SsoDeptQuery query = new SsoDeptQuery();
		query.setSysCode(sysCode);
		query.setDeptName(deptName);
		//未删除
		query.setDelFlag(DelFlagEnum.OK.getStatus());
		if (ObjectUtils.isNotNull(excludeDeptId) && excludeDeptId.length > 0) {
			query.setExcludeDeptId(excludeDeptId[0]);
		}
		return ssoDeptMapper.countByCondition(query) > 0;
	}
}
