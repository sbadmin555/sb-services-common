/**
 * Copyright 2005 Jasper Systems, Inc. All rights reserved.
 *
 * This software code is the confidential and proprietary information of
 * Jasper Systems, Inc. ("Confidential Information"). Any unauthorized
 * review, use, copy, disclosure or distribution of such Confidential
 * Information is strictly prohibited.
 */
package com.sb.services.common.search;

import com.sb.services.common.entity.Persistent;
import org.hibernate.criterion.Order;

import java.util.Map;

/**
 * @author prinaray
 * @version $Id$
 */
public class OrderBy {
    public final static String ORDER_BY=" ORDER BY ";
    public final static String ASC=" ASC ";
    public final static String DESC=" DESC ";
    private String propertyName;
    private boolean ascending;

    public OrderBy() {
        this.propertyName = Persistent.DATE_ADDED;
        this.ascending = false;
    }

    public OrderBy(String propertyName) {
        this.propertyName = propertyName;
    }

    public OrderBy(String propertyName, boolean ascending) {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }

    /**
     * Creates an OrderBy with the given property and direction. Defaults to DateAdded descending.
     * @param propertyName the property
     * @param direction the direction
     */
    public OrderBy(String propertyName, SortDirection direction) {
        this(propertyName, direction.isAscending());
    }

    public Order getOrder(Map<String, String> aliases) {
        return (ascending) ? Order.asc(resolvePropertyName(aliases)) : Order.desc(resolvePropertyName(aliases));
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public String toString() {
        return "OrderBy{" +
                "propertyName='" + propertyName + '\'' +
                ", ascending=" + ascending +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final OrderBy orderBy = (OrderBy) o;

        if (ascending != orderBy.ascending) return false;
        if (propertyName != null ? !propertyName.equals(orderBy.propertyName) : orderBy.propertyName != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (propertyName != null ? propertyName.hashCode() : 0);
        result = 29 * result + (ascending ? 1 : 0);
        return result;
    }

    // Support for aliases for multi-level entities
    protected String resolvePropertyName(Map<String, String> aliases) {
        if (aliases == null)
            return propertyName;
        int separatorIndex = propertyName.lastIndexOf('.');
        if (separatorIndex >0) {  // has entity (pos 0 is just wrong but followed alias code above)
            String entityName = propertyName.substring(0, separatorIndex);
            String newPropertyName = propertyName.substring(separatorIndex + 1);
            if (aliases.containsKey(entityName))
                return aliases.get(entityName) + "." + newPropertyName;
        }
        return propertyName;
    }
}
