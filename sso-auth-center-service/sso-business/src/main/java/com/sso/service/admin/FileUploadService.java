/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.service.admin;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 * 注：目前上传到了本机，建议上传到固定的图片服务器
 *
 * @author 程序员小强
 */
public interface FileUploadService {

	/**
	 * 上传用户头像
	 *
	 * @param userId      用户ID
	 * @param operateName 操作者用户名
	 * @param avatarFile  图片文件
	 * @return 图片地址
	 */
	String uploadUserAvatar(Long userId, String operateName, MultipartFile avatarFile);

	/**
	 * 系统图标Icon
	 *
	 * @param sysId       系统ID
	 * @param operateName 操作者用户名
	 * @param iconFile    图片文件
	 * @return 图标地址
	 */
	String uploadSystemIcon(Long sysId, String operateName, MultipartFile iconFile);

}
