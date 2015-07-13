package com.mgaetan89.showsrage.model;

import java.io.Serializable;

public class Version implements Serializable {
	private static final long serialVersionUID = -8762673751004986305L;

	private String branch;
	private String commit;
	private String version;

	public String getBranch() {
		return this.branch;
	}

	public String getCommit() {
		return this.commit;
	}

	public String getVersion() {
		return this.version;
	}
}
