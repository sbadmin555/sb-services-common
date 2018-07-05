package com.sb.services.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum CountryEnum {
    INDIA("India", 0),
    US("United States", 1)
    ;

    private final String value;
    private int ordinalNo = 0;

    CountryEnum(String v, int ordinalNo) {
        value = v;
        this.ordinalNo = ordinalNo;
    }

    public String value() {
        return value;
    }

    public int getordinalNo() {
        return ordinalNo;
    }

    private static Map<Integer, CountryEnum> ordinalMap = null;
    static {
        ordinalMap = new HashMap<>();
        for (CountryEnum e: CountryEnum.values()) {
            ordinalMap.put(e.getordinalNo(), e);
        }
    }

    public static CountryEnum fromOridnalNo(int ordinalNo) {
        CountryEnum retVal =  ordinalMap.get(ordinalNo);
        if(null == retVal) {
            throw new IllegalArgumentException(String.valueOf(ordinalNo));
        } else {
            return retVal;
        }
    }

    public static CountryEnum fromValue(String v) {
        for (CountryEnum c: CountryEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
