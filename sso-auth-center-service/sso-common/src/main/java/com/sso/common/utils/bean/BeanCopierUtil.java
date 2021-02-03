/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.sso.common.utils.bean;

import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean 拷贝
 *
 * @author 程序员小强
 * @version BeanCopierUtil.java, v 0.1 2020-10-17
 */
public class BeanCopierUtil {

	/**
	 * 对象属性拷贝
	 *
	 * @param source
	 * @param targetClass
	 * @param <T>
	 */
	public static <T> T copy(Object source, Class<T> targetClass) {
		T t = null;
		try {
			t = targetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Create new instance of %s failed: %s");
		}
		copyProperty(source, t);
		return t;
	}

	/**
	 * 对象集合属性拷贝
	 *
	 * @param orig
	 * @param dest
	 * @param <T>
	 */
	public static <T> List<T> copyList(List orig, Class<T> dest) {
		try {
			List<T> resultList = new ArrayList();
			if (orig == null || orig.size() <= 0) {
				return resultList;
			}
			for (Object o : orig) {
				T destObject = dest.newInstance();
				if (o == null) {
					continue;
				}
				copyProperty(o, destObject);
				resultList.add(destObject);
			}
			return resultList;
		} catch (Exception e) {
			return new ArrayList<>(0);
		}
	}

	private static void copyProperty(Object source, Object target) {
		BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
		copier.copy(source, target, null);
	}

}
