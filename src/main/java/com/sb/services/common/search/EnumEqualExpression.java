/**
 * Copyright 2005 Jasper Systems, Inc. All rights reserved.
 *
 * This software code is the confidential and proprietary information of
 * Jasper Systems, Inc. ("Confidential Information"). Any unauthorized
 * review, use, copy, disclosure or distribution of such Confidential
 * Information is strictly prohibited.
 */
package com.sb.services.common.search;

import org.springframework.util.Assert;

/**
 * Works with JDK 1.5 Enum class
 *
 * @author prinaray
 * @version $Id:$
 */
public class EnumEqualExpression extends EqualExpression {
    public EnumEqualExpression(String propertyName, Class propertyType) {
        super(propertyName, propertyType);
    }

    public EnumEqualExpression(String propertyName, Enum value) {
        this(propertyName, value!=null?value.getClass():null);
        setValue(value);
    }

    public void setStringValue(String value) throws IllegalArgumentException {
        Assert.notNull(value);
        setValue(Enum.valueOf(getPropertyType(), value));
    }

    public String getTestValue() {
        return "NA";    // make sure every Enum type has "NA" 
    }
}
