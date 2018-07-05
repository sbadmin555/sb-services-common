package com.sb.services.common.util;

/**
 * Common messages between VSOM & Federator
 * @author lkota
 *
 */
public enum CommonMessages implements IMessages {
	
	GLOBAL_FAILURE("System error"),
	PARTIAL_FAILURE("Operation is partially successful"),
	FAILURE("Operation failed"),
	SUCCESS("Operation succeeded"),
	INFO("Additional information"),
	WARNING("Warning"),

	system("System"),
	database_error("Database error"),

    ;


	private String msgStr;
	CommonMessages(String msgStr) {
		this.msgStr = msgStr;
	}

	public String getMessage() {
		return msgStr;
	}

	@Override
	public String getName() {
		return this.name();
	}

}
