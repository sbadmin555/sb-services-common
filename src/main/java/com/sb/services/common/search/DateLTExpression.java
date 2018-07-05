package com.sb.services.common.search;

import java.util.Date;

public class DateLTExpression extends LTExpression{
    public DateLTExpression(String propertyName, Class propertyType) {
        super(propertyName, propertyType);
    }

    public DateLTExpression(String propertyName, Date value) {
        this(propertyName, value != null ? value.getClass() : null);
        setValue(value);
    }

    @Override
    public void setStringValue(String value) {
    }

    @Override
    public String getTestValue() {
        return null;
    }
}
