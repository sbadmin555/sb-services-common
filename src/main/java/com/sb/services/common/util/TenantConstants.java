package com.sb.services.common.util;

public abstract class TenantConstants {
	private TenantConstants() {
		throw new IllegalStateException("Utility class");
	}

	public static final String TENANT_NOTEXIST = "The tenant {} doesn't exist";
	public static final String TENANT_NULL = "The tenant is null";
	public static final String BYTENANTNAME = "byTenantName";
}
