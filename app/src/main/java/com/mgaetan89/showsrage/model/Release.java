package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Release implements Serializable {
	private static final long serialVersionUID = 5327291604057056600L;

	private String body;
	private String htmlBody;
	private String name;
	@SerializedName("prerelease")
	private boolean preRelease;
	@SerializedName("published_at")
	private String publishedAt;

	public String getBody() {
		return this.body;
	}

	public String getHtmlBody() {
		if (this.htmlBody == null) {
			this.htmlBody = this.body;
		}

		return this.htmlBody;
	}

	public String getName() {
		return this.name;
	}

	public String getPublishedAt() {
		return this.publishedAt;
	}

	public boolean isPreRelease() {
		return this.preRelease;
	}

	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}
}
