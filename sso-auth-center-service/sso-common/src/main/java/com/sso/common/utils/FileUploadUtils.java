/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.sso.common.constant.MimeTypeConstant;
import com.sso.common.constant.SsoConstants;
import com.sso.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 文件上传工具类
 *
 * @author 程序员小强
 */
@Slf4j
public class FileUploadUtils {

	/**
	 * 默认大小 50M
	 */
	public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

	/**
	 * 默认的文件名最大长度 100
	 */
	public static final int DEFAULT_FILE_NAME_LENGTH = 100;

	/**
	 * 上传
	 *
	 * @param rootPath 文件顶级附录
	 * @param bizPath  二级业务文件夹
	 * @param file     文件内容
	 * @return 文件地址
	 * @throws IOException
	 */
	public static String upload(String rootPath, String bizPath, MultipartFile file) throws IOException {
		if (null == file) {
			throw new BusinessException("文件不为空！");
		}
		String originalFilename = file.getOriginalFilename();
		if (StringUtils.isBlank(originalFilename)) {
			throw new BusinessException("获取文件名失败！");
		}
		int fileNameLength = originalFilename.length();
		if (fileNameLength > DEFAULT_FILE_NAME_LENGTH) {
			throw new BusinessException("超过最大长度");
		}
		if (file.getSize() > DEFAULT_MAX_SIZE) {
			throw new BusinessException("文件大小不能超过 50M");
		}

		//文件后缀
		String fileType = getExtension(file);
		String date = DateUtil.format(new Date(), "yyyyMMdd");
		String filePath = String.format("%s%s/%s", rootPath, bizPath, date);
		if (!FileUtil.exist(filePath)) {
			FileUtil.mkdir(filePath);
		}
		//文件名
		String fileName = String.format("%s.%s", UUIDUtil.getUUID(), fileType);
		//文件全路径名
		String fileFullName = String.format("%s/%s", filePath, fileName);

		log.info("[ 文件上传 ] originalFilename:{} , uploadFileName:{}", originalFilename, fileFullName);
		File desc = new File(fileFullName);
		file.transferTo(desc);
		return String.format("%s%s", SsoConstants.RESOURCE_PREFIX, fileFullName.replace(rootPath, ""));
	}

	/**
	 * 获取文件名的后缀
	 *
	 * @param file 表单文件
	 * @return 后缀名
	 */
	public static String getExtension(MultipartFile file) {
		if (null == file) {
			return "";
		}
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if (StringUtils.isEmpty(extension) && StringUtils.isNotBlank(file.getContentType())) {
			extension = MimeTypeConstant.geByPrefix(file.getContentType());
		}
		return extension;
	}
}
