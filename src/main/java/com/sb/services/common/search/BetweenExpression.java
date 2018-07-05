
package com.sb.services.common.search;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.Map;

/**
 * User: prinaray
 * Date: Mar 2, 2018
 * Time: 1:32:10 PM
 */
public class BetweenExpression<DataType> {
    private String propertyName;
    private DataType lowerValue;
    private DataType upperValue;

    public BetweenExpression(String propertyName) {
        this.propertyName = propertyName;
    }
    /**
     * @return a Hibernate Criterion to be used in the query
     */
    public Criterion getCriterion(Map<String, String> aliases) {
        return Restrictions.between(resolvePropertyName(aliases), lowerValue, upperValue);
    }

    /**
     * @return Each expression should provide a test value in String form
     */
    public DataType getTestLowerValue() {
        return (DataType)"1776";
    }

    public DataType getTestUpperValue() {
        return (DataType)"1777";
    }

    /**
     * @return Property name matches the persistent entity's property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public DataType getLowerValue() {
        return lowerValue;
    }

    public DataType getUpperValue() {
        return upperValue;
    }

    public void setLowerValue(DataType lowerValue) {
        this.lowerValue = lowerValue;
    }

    public void setUpperValue(DataType upperValue) {
        this.upperValue = upperValue;
    }

    public String getSolrQuery() {
        boolean minInclusive = true;
        boolean maxInclusive = true;
        return getPropertyName().toUpperCase()+":"+
                (minInclusive ? "[" : "{")+
                (lowerValue == null ? "*" : lowerValue)+
                " TO "+
                (upperValue == null ? "*" : upperValue)+
                (maxInclusive ? "]" : "}");
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

    public String toString() {
        return getClass().getName() + "{" +
                "propertyName='" + propertyName + '\'' +
                ", lowerValue=" + lowerValue +
                ", upperValue=" + upperValue +
                '}';
    }

}