package com.mgaetan89.showsrage.db_model;

import io.realm.RealmObject;

// Temp class until the following issue is addressed: https://github.com/realm/realm-java/issues/575
public class RealmString extends RealmObject {
	private String value = "";

	public RealmString() {
	}

	public RealmString(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
