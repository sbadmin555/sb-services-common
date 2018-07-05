package com.sb.services.common.search;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import java.util.Map;

public class LikeExpression extends SearchExpression {

    public static final String WILD_CARD_TOKEN = "*";

    public LikeExpression(String propertyName) {
        super(propertyName, String.class);
    }

    public LikeExpression(String propertyName, String value) {
        super(propertyName, String.class);
        if(value.contains("%")) {
    		value = value.replaceAll("%", "\\\\%");
    	}
        setValue(value);
    }

    public Criterion getCriterion(Map<String,String> aliases) {
        return Restrictions.ilike(resolvePropertyName(aliases), (String) getValue(), MatchMode.ANYWHERE);
    }

    public void setStringValue(String value) throws IllegalArgumentException {
        Assert.notNull(value);
        setValue(value.toString());
    }

    public String getTestValue() {
        return "likestring";
    }

}
