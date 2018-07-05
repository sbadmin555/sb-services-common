/**
 * Copyright 2005 Jasper Systems, Inc. All rights reserved.
 *
 * This software code is the confidential and proprietary information of
 * Jasper Systems, Inc. ("Confidential Information"). Any unauthorized
 * review, use, copy, disclosure or distribution of such Confidential
 * Information is strictly prohibited.
 */
package com.sb.services.common.search;

import org.hibernate.criterion.Criterion;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * SearchExpression is used to pass user specified search conditions to the backend.
 * It will be mapped to Hibernate Criterion or a Solr query string in order to construct a query.
 * Currently is only supports single value expression. <p>
 * <p/>
 * Necessary TestCase is required to make sure the propertyName and type of SearchExpression
 * matches the one in the data object, since compiler won't catch that inconsistency. <p>
 *
 * @author prinaray
 * @version $Id$
 */
public abstract class SearchExpression {
    private String propertyName;
    private Class propertyType;
    private Object value;

    public SearchExpression(String propertyName, Class propertyType) {
        if(propertyName==null) {
            throw new IllegalArgumentException("propertyName=null");
        }
        if(propertyType==null) {
            throw new IllegalArgumentException("propertyType=null");
        }
        this.propertyName = propertyName;
        this.propertyType = propertyType;
    }
    /**
     * @return a Hibernate Criterion to be used in the query
     */
    public abstract Criterion getCriterion(Map<String, String> aliases);
    public abstract void setStringValue(String value) throws IllegalArgumentException;

    /**
     * @return Each expression should provide a test value in String form
     */
    public abstract String getTestValue();
    /**
     * @return Property name matches the persistent entity's property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        if(propertyName==null) {
            throw new IllegalArgumentException("propertyName=null");
        }
        this.propertyName = propertyName;
    }

    public Class getPropertyType() {
        return propertyType;
    }

    /**
     * @return value object
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set value, do simple type check
     * @param value
     */
    public void setValue(Object value) throws IllegalArgumentException {
        Assert.notNull(value);
        if (! propertyType.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Requires property type=" + propertyType.getName()
                    + ", given=" + value.getClass().getName()
                    + ", value=" + value
                    + ", for " + this.toString());
        }
        this.value = value;
    }


    public String toString() {
        return getClass().getName() + "{" +
                "propertyName='" + propertyName + '\'' +
                ", propertyType=" + propertyType +
                ", value=" + value +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SearchExpression that = (SearchExpression) o;

        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) return false;
        if (propertyType != null ? !propertyType.equals(that.propertyType) : that.propertyType != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (propertyName != null ? propertyName.hashCode() : 0);
        result = 29 * result + (propertyType != null ? propertyType.hashCode() : 0);
        result = 29 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    // Base implementation does NOT support aliases usage
    public Criterion getCriterion() {
        return getCriterion(null);
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
