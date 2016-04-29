package com.mgaetan89.showsrage.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Quality extends RealmObject {
	private RealmList<RealmString> archive = null;
	@PrimaryKey
	private int indexerId = 0;
	private RealmList<RealmString> initial = null;

	public RealmList<RealmString> getArchive() {
		return this.archive;
	}

	public int getIndexerId() {
		return this.indexerId;
	}

	public RealmList<RealmString> getInitial() {
		return this.initial;
	}

	public void setArchive(RealmList<RealmString> archive) {
		this.archive = archive;
	}

	public void setIndexerId(int indexerId) {
		this.indexerId = indexerId;
	}

	public void setInitial(RealmList<RealmString> initial) {
		this.initial = initial;
	}
}
