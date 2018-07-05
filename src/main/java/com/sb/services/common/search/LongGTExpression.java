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
 * Works with JDK 1.5 Enum class
 *
 * @author prinaray
 * @version $Id:$
 */
public class LongGTExpression extends GTExpression {
    public LongGTExpression(String propertyName, Class propertyType) {
        super(propertyName, propertyType);
    }

    public LongGTExpression(String propertyName, Long value) {
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
