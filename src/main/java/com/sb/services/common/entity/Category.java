package com.sb.services.common.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY")
public class Category extends SBBase{
    protected String description ;
    protected String name ;
    protected long createdtime;
    protected long lastmodified ;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Category category = (Category) o;

        if (getCreatedtime() != category.getCreatedtime()) return false;
        if (getLastmodified() != category.getLastmodified()) return false;
        if (getDescription() != null ? !getDescription().equals(category.getDescription()) : category.getDescription() != null)
            return false;
        return getName() != null ? getName().equals(category.getName()) : category.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (int) (getCreatedtime() ^ (getCreatedtime() >>> 32));
        result = 31 * result + (int) (getLastmodified() ^ (getLastmodified() >>> 32));
        return result;
    }
}
