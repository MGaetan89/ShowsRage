package com.mgaetan89.showsrage.model;

public class ServerResponse<DATA> {
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
