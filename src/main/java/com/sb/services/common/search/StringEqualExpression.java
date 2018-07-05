/**
 * Copyright 2005 Jasper Systems, Inc. All rights reserved.
 *
 * This software code is the confidential and proprietary information of
 * Jasper Systems, Inc. ("Confidential Information"). Any unauthorized
 * review, use, copy, disclosure or distribution of such Confidential
 * Information is strictly prohibited.
 */
package com.sb.services.common.search;

import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * String equal is always case insensitive for a search expression.
 * 
 * @author prinaray
 * @version $Id:$
 */
public class StringEqualExpression extends EqualExpression {

	public StringEqualExpression(String propertyName) {
		super(propertyName, String.class);
	}

	public StringEqualExpression(String propertyName, String value) {
		super(propertyName, String.class);
		if(value.contains("%")) {
    		value = value.replaceAll("%", "\\\\%");
    	}
		setStringValue(value);
	}

	public void setStringValue(String value) {
		setValue(value);
	}

	@Override
	public Criterion getCriterion(Map<String, String> aliases) {
		return Restrictions.ilike(resolvePropertyName(aliases), (String) getValue(), MatchMode.EXACT);
	}

	public String getTestValue() {
		return "teststring";
	}

}
