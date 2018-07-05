package com.sb.services.common.search;

/**
 * @author prinaray
 * @version
 */
public enum SortDirection {
    ASC(true),
    DESC(false);

    private boolean ascending;

    SortDirection(boolean ascending) {
        this.ascending = ascending;
    }

    public boolean isAscending() {
        return ascending;
    }
}
