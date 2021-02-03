package com.sso.service.admin;

import com.sso.model.bo.platform.*;
import com.sso.model.vo.platform.SystemDetailVO;
import com.sso.model.vo.platform.SystemListVO;

import java.util.List;

/**
 * 平台管理相关接口
 *
 * @author 程序员小强
 */
public interface SystemService {

	/**
	 * 我的平台列表
	 *
	 * @return 系统列表
	 */
	List<SystemListVO> listMySystem();

	/**
	 * 我的平台排序
	 *
	 * @param systemSortBO
	 */
	void sortMySystem(SystemSortBO systemSortBO);

	/**
	 * 我的平台管理列表
	 *
	 * @return 系统列表
	 */
	List<SystemListVO> listMyMgmtSystem();

	/**
	 * 系统平台详情
	 *
	 * @param sysCode
	 * @return 系统详情
	 */
	SystemDetailVO getDetailBySysCode(String sysCode);

	/**
	 * 新增-目标平台系统
	 *
	 * @param saveBO
	 * @author 程序员小强
	 */
	void addSystem(SystemSaveBO saveBO);

	/**
	 * 修改-目标平台系统
	 *
	 * @param updateBO
	 * @author 程序员小强
	 */
	void updateSystem(SystemUpdateBO updateBO);

	/**
	 * 修改-秘钥
	 *
	 * @param updateBO
	 * @author 程序员小强
	 */
	void updateSystemSecret(SystemUpdateSecretBO updateBO);

	/**
	 * 删除-目标平台系统（逻辑删）
	 *
	 * @param deleteBO
	 * @author 程序员小强
	 */
	void deleteSystem(SystemDeleteBO deleteBO);
}
