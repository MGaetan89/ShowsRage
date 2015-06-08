package com.mgaetan89.showsrage.model;

import java.io.Serializable;

public class ServerResponse<DATA> implements Serializable {
	private static final long serialVersionUID = -3290908098594350721L;

	private DATA data;
	private String message;
	private String result;

	public DATA getData() {
		return this.data;
	}

	public String getMessage() {
		return this.message;
	}

	public String getResult() {
		return this.result;
	}
}
