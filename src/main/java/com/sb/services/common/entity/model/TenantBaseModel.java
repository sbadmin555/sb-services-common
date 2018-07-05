package com.sb.services.common.entity.model;

public class TenantBaseModel extends SBBaseModel {

	private TenantModel tenant;

	public TenantModel getTenant() {
		return tenant;
	}

	public void setTenant(TenantModel tenantModel) {
		this.tenant = tenantModel;
	}
}
