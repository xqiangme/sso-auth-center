/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.framework.config;

import com.sso.common.constant.SsoConstants;
import com.sso.framework.config.property.SysConfigProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通用配置
 *
 * @author 程序员小强
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/// 文件上传路径
		registry.addResourceHandler(SsoConstants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + SysConfigProperty.getFileProfile() + "/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	/**
	 * 跨域配置
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		// 设置访问源地址
		config.addAllowedOrigin("*");
		// 设置访问源请求头
		config.addAllowedHeader("*");
		// 设置访问源请求方法
		config.addAllowedMethod("*");
		// 对接口配置跨域设置
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
