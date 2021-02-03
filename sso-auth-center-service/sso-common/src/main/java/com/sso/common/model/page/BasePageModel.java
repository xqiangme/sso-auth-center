package com.sso.common.model.page;

import java.io.Serializable;

/**
 * 分页数据
 *
 * @author 程序员小强
 */
public class BasePageModel implements Serializable {

    private static final long serialVersionUID = -7185356231617358802L;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 页容量
     */
    private Integer pageSize;

    /**
     * 排序列名
     */
    private String sortName;

    /**
     * 排序的方向desc或者asc
     */
    private String sortBy = "asc";

    public Integer getPage() {
	return page;
    }

    public void setPage(Integer page) {
	this.page = page == null ? 1 : page;
    }

    public Integer getPageSize() {
	return pageSize;
    }

    public void setPageSize(Integer pageSize) {
	this.pageSize = pageSize == null ? 10 : pageSize;
    }

    public String getSortName() {
	return sortName;
    }

    public void setSortName(String sortName) {
	this.sortName = sortName;
    }

    public String getSortBy() {
	return sortBy;
    }

    public void setSortBy(String sortBy) {
	this.sortBy = sortBy;
    }

}
