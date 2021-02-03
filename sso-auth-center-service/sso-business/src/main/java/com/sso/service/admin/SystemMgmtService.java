package com.sso.service.admin;


import com.sso.common.model.result.ResultPageModel;
import com.sso.model.bo.platform.SystemMgmtListPageBO;
import com.sso.model.bo.platform.SystemMgmtRemoveBO;
import com.sso.model.bo.platform.SystemMgmtSaveBO;
import com.sso.model.bo.platform.SystemMgmtStatusBO;
import com.sso.model.vo.platform.SystemMgmtPageVO;

/**
 * 系统管理-数据权限相关接口
 *
 * @author 程序员小强
 */
public interface SystemMgmtService {

	/**
	 * 系统管理员-用户分页列表
	 *
	 * @param pageBO
	 * @return 用户
	 */
	ResultPageModel<SystemMgmtPageVO> listPageSysMgmtUser(SystemMgmtListPageBO pageBO);

	/**
	 * 新增-管理员权限
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	void addSystemMgmt(SystemMgmtSaveBO saveBO);

	/**
	 * 修改-管理员权限账号状态
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	void updateStatus(SystemMgmtStatusBO saveBO);

	/**
	 * 移除-用户管理员权限
	 *
	 * @param removeBO
	 * @author 程序员小强
	 */
	void removeUserSystemMgmt(SystemMgmtRemoveBO removeBO);
}
