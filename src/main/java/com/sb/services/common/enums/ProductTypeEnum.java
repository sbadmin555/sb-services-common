package com.sb.services.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ProductTypeEnum {
    ELECTRONICS("Electronics", 0),
    BOOK("Books", 1)
    ;

    private final String value;
    private int ordinalNo = 0;

    ProductTypeEnum(String v, int ordinalNo) {
        value = v;
        this.ordinalNo = ordinalNo;
    }

    public String value() {
        return value;
    }

    public int getordinalNo() {
        return ordinalNo;
    }

    private static Map<Integer, ProductTypeEnum> ordinalMap = null;
    static {
        ordinalMap = new HashMap<>();
        for (ProductTypeEnum e: ProductTypeEnum.values()) {
            ordinalMap.put(e.getordinalNo(), e);
        }
    }

    public static ProductTypeEnum fromOridnalNo(int ordinalNo) {
        ProductTypeEnum retVal =  ordinalMap.get(ordinalNo);
        if(null == retVal) {
            throw new IllegalArgumentException(String.valueOf(ordinalNo));
        } else {
            return retVal;
        }
    }

    public static ProductTypeEnum fromValue(String v) {
        for (ProductTypeEnum c: ProductTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
