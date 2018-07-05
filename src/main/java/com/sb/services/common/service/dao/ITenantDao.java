package com.sb.services.common.service.dao;


import com.sb.services.common.data.GenericDao;
import com.sb.services.common.entity.Tenant;
import com.sb.services.common.entity.model.SupplyByteException;

/**
 * @author: Priti
 * @version $Id$
 */

public interface ITenantDao extends GenericDao<Tenant,String>{
	
	public void addOrUpdate(Tenant tenant) throws SupplyByteException;
}
