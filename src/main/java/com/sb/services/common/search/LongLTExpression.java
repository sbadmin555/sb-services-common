/**
 * Copyright 2005 Jasper Systems, Inc. All rights reserved.
 *
 * This software code is the confidential and proprietary information of
 * Jasper Systems, Inc. ("Confidential Information"). Any unauthorized
 * review, use, copy, disclosure or distribution of such Confidential
 * Information is strictly prohibited.
 */
package com.sb.services.common.search;

/**
 *
 * @author shurawat
 * @version $Id:$
 */
public class LongLTExpression extends LTExpression {
    public LongLTExpression(String propertyName, Class propertyType) {
        super(propertyName, propertyType);
    }

    public LongLTExpression(String propertyName, Long value) {
        this(propertyName, value!=null?value.getClass():null);
        setValue(value);
    }

	@Override
	public void setStringValue(String value) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTestValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
