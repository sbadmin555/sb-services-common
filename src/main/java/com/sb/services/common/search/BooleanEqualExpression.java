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
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author prinaray
 * @version $Id:$
 */
public class BooleanEqualExpression extends EqualExpression {

    /**
     * If true, we will search the database for both nulls and false values when the expression has a value of false.
     * Otherwise, we will search for just false values.
     * This is true by default.
     */
    private boolean treatNullAsFalse = true;

    public BooleanEqualExpression(String propertyName) {
        super(propertyName, Boolean.class);
    }

    public void setStringValue(String value) throws IllegalArgumentException {
        Assert.notNull(value);
        setValue(Boolean.valueOf(value.toString()));
    }

    public BooleanEqualExpression(String propertyName, Boolean value) {
        super(propertyName, Boolean.class);
        Assert.notNull(value);
        setValue(value);
    }

    public String getTestValue() {
        return "true";
    }

    public Criterion getCriterion(Map<String, String> aliases) {
        String propertyName = resolvePropertyName(aliases);
        Boolean value = (Boolean) getValue();
        if (treatNullAsFalse && !Boolean.TRUE.equals(value)) {
            return Restrictions.or(Restrictions.isNull(propertyName), Restrictions.eq(propertyName, Boolean.FALSE));
        } else {
            return Restrictions.eq(propertyName, value);
        }
    }

    public void setTreatNullAsFalse(boolean treatNullAsFalse) {
        this.treatNullAsFalse = treatNullAsFalse;
    }
}
