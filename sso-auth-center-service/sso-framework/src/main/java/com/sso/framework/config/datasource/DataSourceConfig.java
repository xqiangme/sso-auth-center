/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.framework.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 后台系统数据源 > 配置
 *
 * @author 程序员小强
 */
@Configuration
@MapperScan(basePackages = {"com.sso.dao.mapper"}, sqlSessionTemplateRef = "adminSqlSessionTemplate")
public class DataSourceConfig {

	/**
	 * 数据源配置
	 * 使用的连接池是 DruidDataSource
	 * <p>
	 * 注解ConfigurationProperties
	 * 作用就是将全局配置文件中的属性值注入到DruidDataSource 的同名参数
	 */
	@Primary
	@Bean(name = "adminDataSource")
	@Qualifier("adminDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.admin")
	public DataSource adminDataSource() throws SQLException {
		DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
		dataSource.setFilters("stat,wall,slf4j");
		return dataSource;
	}

	/**
	 * 创建 SqlSessionFactory 工厂
	 */
	@Primary
	@Bean(name = "adminSqlSessionFactory")
	public SqlSessionFactory adminSqlSessionFactory(@Qualifier("adminDataSource") DataSource dataSource) throws Exception {
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		//全局映射器启用缓存
		configuration.setCacheEnabled(true);
		//使用jdbc的getGeneratedKeys获取数据库自增主键值
		configuration.setUseGeneratedKeys(true);
		//配置默认的执行器
		configuration.setDefaultExecutorType(ExecutorType.REUSE);
		//configuration.setLogImpl(SLF4JImpl.class);
		//使用列别名替换列名 select user as User
		configuration.setUseColumnLabel(true);
		//设置启用数据库字段下划线映射到java对象的驼峰式命名属性
		configuration.setMapUnderscoreToCamelCase(true);

		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);

		sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/mybatis/**/*Mapper.xml"));
		sqlSessionFactory.setConfiguration(configuration);

		return sqlSessionFactory.getObject();
	}

	/**
	 * 事务管理
	 */
	@Primary
	@Bean(name = "adminTransactionManager")
	public DataSourceTransactionManager adminTransactionManager(@Qualifier("adminDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * MyBatis提供的持久层访问模板化的工具
	 * 线程安全，可通过构造参数或依赖注入SqlSessionFactory实例
	 */
	@Primary
	@Bean(name = "adminSqlSessionTemplate")
	public SqlSessionTemplate adminSqlSessionTemplate(@Qualifier("adminSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
