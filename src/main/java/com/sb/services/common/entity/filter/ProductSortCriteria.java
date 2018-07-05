package com.sb.services.common.entity.filter;

import com.sb.services.common.enums.SortCriteriaEnum;

public class ProductSortCriteria extends SortCriteria{

    /**By default sort by name, this will be fallback mechanism if user has not supplied the sorting column */
    /**By default sort by name, this will be fallback mechanism if user has not supplied the sorting column */
    private SortCriteriaEnum name = SortCriteriaEnum.name;

    private String sortableColumn;

    public ProductSortCriteria() {
    }

    public SortCriteriaEnum getName() {
        return name;
    }

    @Override
    public SortCriteriaEnum getSortFieldName() {
        return name;
    }

    public void setName(SortCriteriaEnum name) {
        this.name = name;
    }

    @Override
    public void setSortFieldName(SortCriteriaEnum sortFieldName) {
        name = sortFieldName;
    }

    ProductSortCriteria(String sortableColumn) {
        this.sortableColumn = sortableColumn;
    }

    public String getSortableColumn() {
        return sortableColumn;
    }
}
