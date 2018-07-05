package com.sb.services.common.entity.model;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

    private int totalRows;

    private List<T> items;

    private boolean nextPageExists;

    private boolean previousPageExists;

    public Page() {
        super();
        items = new ArrayList<T>();
    }

    public Page(int totalRows, List<T> items) {
        super();
        this.totalRows = totalRows;
        this.items = items;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void addItem(T item) {
        if(null == items) {
            items = new ArrayList<T>();
        }
        items.add(item);
    }

    public boolean isNextPageExists() {
        return nextPageExists;
    }

    public void setNextPageExists(boolean nextPageExists) {
        this.nextPageExists = nextPageExists;
    }

    public boolean isPreviousPageExists() {
        return previousPageExists;
    }

    public void setPreviousPageExists(boolean previousPageExists) {
        this.previousPageExists = previousPageExists;
    }

    public String toString(){
        return "[Page:totalRows= " + totalRows + ", numItems=" + items.size() + ", previousPageExists=" + previousPageExists + ", nextPageExists=" + nextPageExists + "]";
    }
}
