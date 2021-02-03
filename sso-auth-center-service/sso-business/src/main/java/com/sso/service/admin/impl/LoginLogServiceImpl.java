package com.sso.service.admin.impl;

import cn.hutool.core.date.DateUtil;
import com.sso.common.model.result.ResultPageModel;
import com.sso.common.utils.StringUtils;
import com.sso.common.utils.bean.BeanCopierUtil;
import com.sso.dao.entity.SsoLoginLog;
import com.sso.dao.mapper.SsoLoginLogMapper;
import com.sso.dao.query.LoginLogQuery;
import com.sso.model.bo.login.LoginLogListPageBO;
import com.sso.model.vo.login.LoginLogPageVO;
import com.sso.service.admin.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 登录日志接口
 *
 * @author 程序员小强
 */
@Slf4j
@Service
public class LoginLogServiceImpl implements LoginLogService {

	@Resource
	private SsoLoginLogMapper ssoLoginLogMapper;

	/**
	 * 登录日志分页列表
	 *
	 * @param pageBO
	 * @return 登录日志列表
	 */
	@Override
	public ResultPageModel<LoginLogPageVO> listPageUser(LoginLogListPageBO pageBO) {
		//构建参数参数
		LoginLogQuery query = this.buildPageQuery(pageBO);

		//统计行数
		int total = ssoLoginLogMapper.countByCondition(query);
		if (total <= 0) {
			return ResultPageModel.defaultValue();
		}

		//分页查询
		List<SsoLoginLog> loginLogList = ssoLoginLogMapper.listPageByCondition(query);
		if (CollectionUtils.isEmpty(loginLogList)) {
			return ResultPageModel.defaultValue();
		}

		return ResultPageModel.success(this.buildLoginLogPage(loginLogList), total);
	}

	/**
	 * 构建返回列表
	 *
	 * @param loginLogList
	 * @return
	 */
	private List<LoginLogPageVO> buildLoginLogPage(List<SsoLoginLog> loginLogList) {

		LoginLogPageVO item = null;
		List<LoginLogPageVO> resultList = new ArrayList<>();
		for (SsoLoginLog ssoLoginLog : loginLogList) {
			item = BeanCopierUtil.copy(ssoLoginLog, LoginLogPageVO.class);
			//登录时间
			if (!ObjectUtils.isEmpty(ssoLoginLog.getLoginTime()) && ssoLoginLog.getLoginTime() > 0) {
				item.setLoginTime(DateUtil.formatDateTime(new Date(ssoLoginLog.getLoginTime())));
			}
			resultList.add(item);
		}
		return resultList;
	}

	/**
	 * 构建查询参数
	 *
	 * @param pageBO
	 */
	private LoginLogQuery buildPageQuery(LoginLogListPageBO pageBO) {
		LoginLogQuery query = new LoginLogQuery()
				.setUsernameLike(pageBO.getUsernameLike())
				.setStatus(pageBO.getStatus());

		if (StringUtils.isNotBlank(pageBO.getLoginStartTime())) {
			query.setLoginStartTime(DateUtil.beginOfDay(DateUtil.parseDate(pageBO.getLoginStartTime())).getTime());
		}
		if (StringUtils.isNotBlank(pageBO.getLoginEndTime())) {
			query.setLoginEndTime(DateUtil.endOfDay(DateUtil.parseDate(pageBO.getLoginEndTime())).getTime());
		}
		query.setPage(pageBO.getPage());
		query.setPageSize(pageBO.getPageSize());
		return query;
	}
}
