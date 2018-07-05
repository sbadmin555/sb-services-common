package com.sb.services.common.service.dao.impl;


import com.sb.services.common.data.GenericHibernateDaoImpl;
import com.sb.services.common.entity.Tenant;
import com.sb.services.common.entity.model.SupplyByteException;
import com.sb.services.common.service.dao.ITenantDao;

import org.springframework.stereotype.Service;

/**
 * @author: prinaray
 * @version $Id$
 */
@Service
public class TenantDaoImpl extends GenericHibernateDaoImpl<Tenant, String> implements ITenantDao {
    
    @Override
    	public void addOrUpdate(Tenant tenant) throws SupplyByteException {
    		super.addOrUpdate(tenant);
    }
}
