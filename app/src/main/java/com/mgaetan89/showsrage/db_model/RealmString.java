package com.mgaetan89.showsrage.db_model;

import io.realm.RealmObject;

// Temp class until the following issue is addressed: https://github.com/realm/realm-java/issues/575
public class RealmString extends RealmObject {
	private String name = "";

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
