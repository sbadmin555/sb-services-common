package com.sb.services.common.search;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author prinaray
 * @version $Id:$
 */
public class StringNotInExpression extends SearchExpression {
    public StringNotInExpression(String propertyName) {
        super(propertyName, Collection.class);
    }

    public StringNotInExpression(String propertyName, Object value) {
        super(propertyName, Collection.class);
        Assert.notNull(value);
        setValue(value);
    }

    public Criterion getCriterion(Map<String, String> aliases) {
    	System.err.println("not inside");
        return Restrictions.not(Restrictions.in(resolvePropertyName(aliases), (Collection) getValue()));
    }

    public void setStringValue(String value) throws IllegalArgumentException {
        List values = new ArrayList();
        values.add(value);
        setValue(values);
    }

    public String getTestValue() {
        return "100";
    }
}
