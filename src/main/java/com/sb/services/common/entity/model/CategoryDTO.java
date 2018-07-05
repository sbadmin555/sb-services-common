package com.sb.services.common.entity.model;

public class CategoryDTO extends SBBaseModel{

    private String description ;
    private String name ;
    private long createdtime;
    private long lastmodified ;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(long createdtime) {
        this.createdtime = createdtime;
    }

    public long getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(long lastmodified) {
        this.lastmodified = lastmodified;
    }
}
