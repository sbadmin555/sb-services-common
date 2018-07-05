package com.sb.services.common.entity.filter;

import com.sb.services.common.enums.SortCriteriaEnum;

public abstract class BaseFilter {

    private PageInfo pageInfo = new PageInfo();


    public PageInfo getPageInfo() {
        return pageInfo;
    }
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public SortCriteria getSortCriteria() {
        return null;
    }

    public void setSortCriteria(SortCriteria sortCriteria){
        //to be implemented by sub classes
    }

    /**
     * It's only meant for system internal use. API callers should not be providing nor be aware of it
     * @return
     */
    public SortCriteriaEnum getDefaultSortColumn() {
        return SortCriteriaEnum.uid;
    }
}
