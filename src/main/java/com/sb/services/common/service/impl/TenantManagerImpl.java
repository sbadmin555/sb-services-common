package com.sb.services.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import com.sb.services.common.entity.Tenant;
import com.sb.services.common.entity.filter.TenantFilter;
import com.sb.services.common.entity.model.SupplyByteException;
import com.sb.services.common.service.ITenantManager;
import com.sb.services.common.service.dao.ITenantDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sb.services.common.util.CommonMessages;

/**
 * @author: Priti
 * @version $Id$
 */

@Repository
@Transactional
public class TenantManagerImpl implements ITenantManager {

	public static final Logger LOG = LoggerFactory.getLogger(TenantManagerImpl.class);

	@Autowired
	private ITenantDao tenantDao;

	private Map<String, String> tenantMap = new HashMap<>();

	@Override
	public void addOrUpdate(Tenant tenant) throws SupplyByteException {
		tenantDao.addOrUpdate(tenant);
	}

	public Tenant get(String uid) throws SupplyByteException {
		return tenantDao.get(uid);
	}

	public Tenant getTenantFromName(String tenantName) throws SupplyByteException {

		throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.FAILURE, tenantName);
	}

	public Tenant getTenant(TenantFilter tenantFilter) throws SupplyByteException {

		return null;
	}

	public Tenant getTenantFromVsom(String vsomUid) throws SupplyByteException {
		return new Tenant();
	}
}
