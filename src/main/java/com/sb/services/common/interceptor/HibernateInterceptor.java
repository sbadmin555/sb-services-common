package com.sb.services.common.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;

public class HibernateInterceptor extends EmptyInterceptor implements Serializable {
//    private static final Logger log = LogManager.getLogger();

    public boolean onSave(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) {
        return super.onSave(entity,id,state,propertyNames,types);
    }

    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onLoad(entity, id, state, propertyNames, types);
    }

    public boolean onFlushDirty(Object entity,
                                Serializable id,
                                Object[] currentState,
                                Object[] previousState,
                                String[] propertyNames,
                                Type[] types) {
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        super.onDelete(entity, id, state, propertyNames, types);
    }
}

