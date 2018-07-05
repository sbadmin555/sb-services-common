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

import java.util.Map;

/**
 * @author prinaray
 * @version $Id$
 */
public abstract class EqualExpression extends SearchExpression {
    public EqualExpression(String propertyName, Class propertyType) {
        super(propertyName, propertyType);
    }

    public Criterion getCriterion(Map<String, String> aliases) {
        return Restrictions.eq(resolvePropertyName(aliases), getValue());
    }
}
