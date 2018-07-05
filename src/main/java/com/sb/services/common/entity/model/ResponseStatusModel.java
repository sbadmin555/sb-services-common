package com.sb.services.common.entity.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseStatusModel {

	private ErrorStatus status;
	private Object data;

	public ErrorStatus getStatus() {
		return status;
	}

	public void setStatus(ErrorStatus status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResponseStatusModel [status=" + status + ", data=" + data + "]";
	}

}
