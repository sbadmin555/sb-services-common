/**
 * Copyright 2006 Jasper Wireless, Inc. All rights reserved.
 *
 * This software code is the confidential and proprietary information of
 * Jasper Wireless, Inc. ("Confidential Information"). Any unauthorized
 * review, use, copy, disclosure or distribution of such Confidential
 * Information is strictly prohibited.
 */

package com.sb.services.common.search;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;

import java.util.Map;

/**
 * @author prinaray
 * @version $Id: $
 */
public class ContainsExpression extends SearchExpression {
    private static final Logger log = Logger.getLogger(ContainsExpression.class);

    private String within;

    public ContainsExpression(String propertyName) {
        super(propertyName, String.class);
    }

    public Criterion getCriterion(Map<String,String> aliases) {
        return new ContainsCriterion(resolvePropertyName(aliases), getValue().toString());
    }

    public void setStringValue(String value) throws IllegalArgumentException {
        setValue(value);
    }

    public void setWithin(String within) {
        this.within = within;
    }

    public String getWithin() {
        return within;
    }

    public String getTestValue() {
        return "teststring";
    }

    /**
     * Escapes all non-wildcard characters so that Oracle Text will treat
     * the value as a literal. This is necessary because words like
     * "minus", "about", etc. have special meaning to Oracle Text.
     * <p>
     * We also convert the "*" wildcard character to "%" and "," to an "OR".
     *
     * @param value
     * @return the escaped value
     */
    static String escapeValue(String value) {
        // 1. Escape any special characters like "\&=?" and surround the non-wildcard
        // sequences with {}.
        // 2. Convert "*" wildcards to "%".
        // 3. Convert commas to an "OR" expression.
        String res = value.replaceAll("([\\\\\\&\\=\\?\\{\\}\\(\\)\\[\\]\\-\\;\\~\\|\\$\\!\\>\\%\\_])", "\\\\$1");
        res = res.replaceAll("(?<=\\s|\\,|^)([^\\*\\,\\s]+)(?=\\s|\\,|$)", "{$1}");
        res = res.replaceAll("\\*", "%");
        res = res.replaceAll("(?<=[^\\s])\\s*\\,\\s*(?=[^\\s]+)", " OR ");
        res = res.replaceAll("\\s*\\,\\s*$", "");
        res = res.replaceAll("^\\s*\\,\\s*", "");
        return res;
    }

    class ContainsCriterion implements Criterion {
        private final String propertyName;
        private final String value;

        protected ContainsCriterion(String propertyName, String value) {
            this.propertyName = propertyName;
            this.value = value;
        }

        public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
            String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
            StringBuffer fragment = new StringBuffer();
            if (columns.length>1) fragment.append('(');
            for ( int i=0; i<columns.length; i++ ) {
                fragment.append("contains(");
                fragment.append( columns[i] );
                fragment.append( ", ?) > 0");
                if ( i<columns.length-1 ) fragment.append(" and ");
            }
            if (columns.length>1) fragment.append(')');
            return fragment.toString();
        }

        public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
            StringBuffer sqlExpr = new StringBuffer(escapeValue(value));
            if (within != null) {
                sqlExpr.append(" WITHIN ");
                sqlExpr.append(within);
            }
            if (log.isDebugEnabled()) {
                log.debug("Search for value: "+value+"; sql expression: "+sqlExpr);
            }
            return new TypedValue[] {criteriaQuery.getTypedValue(criteria,
                    propertyName, sqlExpr.toString()) };
        }
    }

}
