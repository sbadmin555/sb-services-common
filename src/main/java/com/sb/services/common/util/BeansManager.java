package com.sb.services.common.util;

import java.util.HashSet;
import java.util.Set;

import com.sb.services.common.service.dao.ITenantDao;

//@Component
public class BeansManager {

//	@Autowired
	private ITenantDao tenantDao;

	// This line will guarantee the BeansManager class will be injected last
//	@Autowired
	private Set<Injectable> injectables = new HashSet<>();

	// This method will make sure all the injectable classes will get the
	// BeansManager in its steady state,
	// where it's class members are ready to be set
//	@PostConstruct
	private void inject() {
		for (Injectable injectableItem : injectables) {
			injectableItem.inject(this);
		}
	}

	public ITenantDao getTenantDao() {
		return tenantDao;
	}

	public void setTenantDao(ITenantDao tenantDao) {
		this.tenantDao = tenantDao;
	}
}
