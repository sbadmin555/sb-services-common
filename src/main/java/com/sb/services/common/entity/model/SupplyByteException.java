package com.sb.services.common.entity.model;

import java.text.MessageFormat;

import com.sb.services.common.util.CommonMessages;
import com.sb.services.common.util.IMessages;

@SuppressWarnings("serial")
public class SupplyByteException extends Exception {

	public static enum ErrorType {
		GLOBAL_FAILURE(-3), PARTIAL_FAILURE(-2), FAILURE(-1), SUCCESS(0), INFO(1), WARNING(2);

		private ErrorType(int code) {
			this.code = code;
		}

		public int getNumValue() {
			return code;
		}

		private int code;
	}

	protected ErrorType errorType = ErrorType.SUCCESS;
	protected String errorReason = null;
	protected String errorReasonCode = null;
	protected String errorMsg = null;

	public SupplyByteException(ErrorType errorType, String reason, String reasonCode) {
		super(reason);
		this.errorType = errorType;
		this.errorReason = reason;
		this.errorReasonCode = reasonCode;

	}

	public SupplyByteException(ErrorType errorType, IMessages reasonCode, Object... objects) {
		super(getErrorMessage(errorType, reasonCode, objects));
		objects = replaceQuotes(objects);
		this.errorType = errorType;
		this.errorMsg = getErrorMessage(errorType, reasonCode, objects);
		this.errorReasonCode = reasonCode.getName();
		this.errorReason = formatErrorReason(reasonCode.getMessage(), objects);

	}

	public SupplyByteException(String errorMsg, ErrorType errorType, IMessages reasonCode) {
		super(errorMsg);
		this.errorType = errorType;
		this.errorMsg = errorMsg;
		this.errorReasonCode = reasonCode.getName();
		this.errorReason = errorMsg.replace(CommonMessages.FAILURE.getMessage() + ": ", "");
	}

	public SupplyByteException() {
		super();
	}

	public SupplyByteException(Exception e) {
		if (e instanceof SupplyByteException) {
			SupplyByteException ex = (SupplyByteException) e;
			this.errorType = ex.errorType;
			this.errorReason = ex.errorReason;
			this.errorReasonCode = ex.errorReasonCode;
			this.errorMsg = ex.errorMsg;
		} else {
			this.errorType = ErrorType.FAILURE;
			this.errorReason = e.getMessage();
		}
	}

	public SupplyByteException(ErrorStatus error) {
		fromErrorStatus(error);
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	public String getErrorReasonCode() {
		return errorReasonCode;
	}

	public void setErrorReasonCode(String errorReasonCode) {
		this.errorReasonCode = errorReasonCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SupplyByteException:{ errorType = " + errorType.name());
		if (errorReason != null) {
			stringBuilder.append(", errorReason = " + errorReason);
		}
		if (errorMsg != null) {
			stringBuilder.append(", errorMsg = " + errorMsg);
		}
		if (errorReasonCode != null) {
			stringBuilder.append(", errorReasonCode = " + errorReasonCode);
		}
		if (errorReason != null) {
			stringBuilder.append(", errorReason = " + errorReason);
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	private void fromErrorStatus(ErrorStatus error) {
		setErrorType(error.getErrorType());
		setErrorReason(error.getErrorReason());
		setErrorReasonCode(error.getErrorReasonCode());
		setErrorMsg(error.getErrorMsg());

	}

	private static String formatErrorReason(final String errorReason, final Object... objects) {
		return MessageFormat.format(errorReason, objects);
	}

	private static String getErrorMessage(final ErrorType errorType, final IMessages reasonCode, final Object... objects) {
		final String error = CommonMessages.valueOf(errorType.name()).getMessage();
		final String reason = formatErrorReason(reasonCode.getMessage(), objects);
		return error + ": " + reason;
	}

	private Object[] replaceQuotes(final Object... objects) {
		final CharSequence c1 = "\"";
		final CharSequence c2 = "\\\"";
		for (int itt = 0; itt < objects.length; itt++) {
			if (objects[itt] == null) {
				objects[itt] = "";
			}
			objects[itt] = objects[itt].toString().replace(c1, c2);
		}
		return objects;
	}

}
