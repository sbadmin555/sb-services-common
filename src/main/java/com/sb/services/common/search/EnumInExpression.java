package com.sb.services.common.search;

import java.util.List;

/**
 * Expression for performing an IN with a collection of enum values.
 * @version $Id: //depot/jasper_dev/module/CorePrime/src/com/jasperwireless/core/search/EnumInExpression.java#1 $
 */
public class EnumInExpression extends StringInExpression {
    // For now, this is the same as StringInExpression.
    public EnumInExpression(String propertyName) {
        super(propertyName);
    }

    public EnumInExpression(String propertyName, List values) {
        this(propertyName);
        setValue(values);
    }
}
