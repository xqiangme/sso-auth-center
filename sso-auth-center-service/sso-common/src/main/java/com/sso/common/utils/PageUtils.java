package com.sso.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 * @author 程序员小强
 */
public class PageUtils {

    /**
     * 默认第一页
     */
    private static final int PAGE = 1;

    /**
     * 默认一页10条
     */
    private static final int PAGE_SIZE = 10;


    public static Integer getStartRow(Integer pageNo, Integer pageSize) {
        if (null == pageNo) {
            pageNo = PAGE;
        }
        if (null == pageSize) {
            pageSize = PAGE_SIZE;
        }

        return pageSize * (pageNo - 1);
    }

    public static Integer getTotalPage(Integer rowCount, Integer pageSize) {
        if (null == rowCount) {
            rowCount = 0;
        }
        if (null == pageSize) {
            pageSize = PAGE_SIZE;
        }

        return (rowCount + pageSize - 1) / pageSize;
    }

    public static Integer getOffset(Integer pageSize) {
        if (null == pageSize) {
            pageSize = PAGE_SIZE;
        }
        return pageSize;
    }

    /**
     * list分页
     *
     * @param list
     * @param pageNo
     * @param pageSize
     */
    public static <T> List<T> listPage(List<T> list, Integer pageNo, Integer pageSize) {
        if (null == list || list.isEmpty()) {
            return list;
        }
        if (null == pageNo) {
            pageNo = PAGE;
        }
        if (null == pageSize) {
            pageSize = PAGE_SIZE;
        }
        int totalCount = list.size();
        pageNo = pageNo - 1;
        int fromIndex = pageNo * pageSize;
        if (fromIndex > totalCount) {
            return new ArrayList<>(0);
        }
        int toIndex = ((pageNo + 1) * pageSize);
        if (toIndex > totalCount) {
            toIndex = totalCount;
        }
        return list.subList(fromIndex, toIndex);
    }

}
