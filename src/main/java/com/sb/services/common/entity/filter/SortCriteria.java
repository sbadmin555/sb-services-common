package com.sb.services.common.entity.filter;

import com.sb.services.common.enums.SortCriteriaEnum;

public abstract class SortCriteria {

    protected SortOrderEnum sortOrder = SortOrderEnum.ASC;

    public SortOrderEnum getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrderEnum sortOrder) {
        this.sortOrder = sortOrder;
    }

    public SortCriteriaEnum getSortFieldName() {
        return null;
    }

    public abstract void setSortFieldName(SortCriteriaEnum SortFieldName);
}
