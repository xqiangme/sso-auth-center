/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.service.admin.impl;

import com.sso.common.constant.CacheConstants;
import com.sso.common.constant.SsoConstants;
import com.sso.common.exception.BusinessException;
import com.sso.common.model.login.LoginResultVO;
import com.sso.common.utils.FileUploadUtils;
import com.sso.common.utils.StringUtils;
import com.sso.dao.mapper.SsoSystemMapper;
import com.sso.dao.mapper.SsoUserMapper;
import com.sso.framework.config.property.SysConfigProperty;
import com.sso.framework.redis.RedisCache;
import com.sso.service.admin.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 文件上传接口
 * 注：目前上传到了本机，建议上传到固定的图片服务器
 *
 * @author 程序员小强
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileUploadServiceImpl implements FileUploadService {

	@Autowired
	private RedisCache redisCache;

	@Resource
	private SsoUserMapper ssoUserMapper;

	@Resource
	private SsoSystemMapper ssoSystemMapper;

	/**
	 * 上传用户头像
	 *
	 * @param userId      用户ID
	 * @param operateName 操作者用户名
	 * @param avatarFile  图片文件
	 * @return 图片地址
	 */
	@Override
	public String uploadUserAvatar(Long userId, String operateName, MultipartFile avatarFile) {
		if (avatarFile.isEmpty()) {
			throw new BusinessException("头像文件不能为空！");
		}

		String avatar = "";
		try {
			avatar = FileUploadUtils.upload(SysConfigProperty.getFileProfile(), SsoConstants.AVATAR_PATH, avatarFile);
		} catch (Exception e) {
			throw new BusinessException("头像上传失败！");
		}

		if (StringUtils.isBlank(avatar)) {
			throw new BusinessException("头像修改失败！");
		}

		//更新用户头像地址
		ssoUserMapper.updateAvatar(userId, avatar, operateName);

		//若用户已登录-刷新当前登录用户-缓存中的头像信息
		this.doUpdateOnlineUserAvatar(userId, avatar);

		log.info("[ 上传用户头像 ] >> userId:{} , operateName:{}", userId, operateName);
		return avatar;
	}

	/**
	 * 系统图标Icon
	 *
	 * @param sysId       系统ID
	 * @param operateName 操作者用户名
	 * @param iconFile    图片文件
	 * @return 图标地址
	 */
	@Override
	public String uploadSystemIcon(Long sysId, String operateName, MultipartFile iconFile) {
		if (iconFile.isEmpty()) {
			throw new BusinessException("图标文件不能为空！");
		}

		String sysIcon = "";
		try {
			sysIcon = FileUploadUtils.upload(SysConfigProperty.getFileProfile(), SsoConstants.SYSTEM_ICON_PATH, iconFile);
		} catch (Exception e) {
			throw new BusinessException("系统图标上传失败！");
		}

		if (StringUtils.isBlank(sysIcon)) {
			throw new BusinessException("系统图标修改失败！");
		}

		//更新用户头像地址
		ssoSystemMapper.updateIconBySysId(sysId, sysIcon, operateName);

		log.info("[ 上传系统图标Icon ] >> userId:{} , operateName:{}", sysId, operateName);
		return sysIcon;
	}

	/**
	 * 若用户已登录-刷新当前登录用户-缓存中的头像信息
	 *
	 * @param userId
	 * @param avatar
	 */
	private void doUpdateOnlineUserAvatar(Long userId, String avatar) {
		String userIdCacheKey = CacheConstants.getLoginUserIdKey(userId);
		//根据unionId查询redis，若用户信息存在
		LoginResultVO lastLoginUser = redisCache.get(userIdCacheKey, LoginResultVO.class);
		if (null == lastLoginUser) {
			return;
		}

		//刷新当前登录用户-缓存中的头像信息
		lastLoginUser.getUser().setAvatar(avatar);
		String userTokenCacheKey = CacheConstants.getLoginUserTokenKey(lastLoginUser.getToken());
		redisCache.set(userIdCacheKey, lastLoginUser);
		redisCache.set(userTokenCacheKey, lastLoginUser);
	}
}
