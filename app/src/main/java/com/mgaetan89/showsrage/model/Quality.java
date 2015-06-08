package com.mgaetan89.showsrage.model;

import java.io.Serializable;
import java.util.List;

public class Quality implements Serializable {
	private static final long serialVersionUID = 512591388605584556L;

	private List<String> archive;
	private List<String> initial;

	public List<String> getArchive() {
		return this.archive;
	}

	public List<String> getInitial() {
		return this.initial;
	}
}
