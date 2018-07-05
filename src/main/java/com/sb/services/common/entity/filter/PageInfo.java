package com.sb.services.common.entity.filter;

import java.io.Serializable;

public class PageInfo implements Serializable{

    private int pageNumber = 1;

    private int limit = 50;

    private Boolean skipTotalRowCount;

    public PageInfo() {

    }

    public PageInfo(int pageNumber, int limit) {
        this.pageNumber = pageNumber;
        this.limit = limit;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    public Boolean getSkipTotalRowCount() {
        return skipTotalRowCount;
    }

    public void setSkipTotalRowCount(Boolean skipTotalRowCount) {
        this.skipTotalRowCount = skipTotalRowCount;
    }

    public static PageInfo validationUtil = new PageInfo();

}
