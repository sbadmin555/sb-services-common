package com.sb.services.common.search;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.Map;

public class NullOnlyExpression extends SearchExpression{

    public NullOnlyExpression (String propertyName) {
        super(propertyName, Object.class);
    }

    public Criterion getCriterion(Map<String,String> aliases) {
        return Restrictions.isNull(resolvePropertyName(aliases));
    }

    public void setStringValue(String value) {
        // do nothing
    }

    public String getTestValue() {
        // do nothing
        return "\"\"";
    }
}
