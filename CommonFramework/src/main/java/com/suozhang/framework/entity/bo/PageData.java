/*
 * Copyright (c) 2017. 深圳一七网络科技有限公司. All rights reserved.
 */

package com.suozhang.framework.entity.bo;

import java.util.List;

/**
 * 分页数据
 * @author Moodd
 * @e-mail 420410175@qq.com
 * @date 2017/3/28 10:23
 */

public class PageData<T> implements BaseEntity {

    private List<T> data;//数据
    private int totalCount;//总数据行数
    private int totalPageCount;//总页数
    private int pageIndex;//当前页的索引
    private int pageSize;//页面容量

    public PageData() {
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageData{" +
                "data=" + data +
                ", totalCount=" + totalCount +
                ", totalPageCount=" + totalPageCount +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}
