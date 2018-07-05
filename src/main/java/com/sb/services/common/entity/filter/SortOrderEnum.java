package com.sb.services.common.entity.filter;

public enum SortOrderEnum {
    ASC("asc"),
    DESC("desc");

    private String sortOrder;
    private SortOrderEnum(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortOrder() {
        return sortOrder;
    }
}
