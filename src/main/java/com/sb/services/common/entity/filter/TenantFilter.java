package com.sb.services.common.entity.filter;

public class TenantFilter extends BaseFilter {
	private String byTenantId;
	private String byTenantName;

	public String getByTenantId() {
		return byTenantId;
	}

	public void setByTenantId(String byTenantId) {
		this.byTenantId = byTenantId;
	}

	public String getByTenantName() {
		return byTenantName;
	}

	public void setByTenantName(String byTenantName) {
		this.byTenantName = byTenantName;
	}

	@Override
	public String toString() {
		return "TenantFilter [byTenantId=" + byTenantId + ", byTenantName=" + byTenantName + "]";
	}

}
