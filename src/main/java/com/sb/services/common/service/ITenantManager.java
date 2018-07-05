package com.sb.services.common.service;

import com.sb.services.common.entity.Tenant;
import com.sb.services.common.entity.filter.TenantFilter;
import com.sb.services.common.entity.model.SupplyByteException;

/**
 * @author: prinaray
 * @version $Id$
 */

public interface ITenantManager {

	public void addOrUpdate(Tenant tenant) throws SupplyByteException;

	public Tenant get(String uid) throws SupplyByteException;

	public Tenant getTenant(TenantFilter tenantFilter) throws SupplyByteException;

	public Tenant getTenantFromVsom(String vsomUid) throws SupplyByteException;
	
	public Tenant getTenantFromName(String tenantName) throws SupplyByteException;
}
