package com.sb.services.common.generator;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.AbstractUUIDGenerator;

public class StringKeyGenerator extends AbstractUUIDGenerator implements Generator {
	public Object key() {
		return UUID.randomUUID().toString();
	}

    @Override
    public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
    	return UUID.randomUUID().toString();
    }
}
