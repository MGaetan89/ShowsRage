package com.mgaetan89.showsrage.model;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.RemotePlaybackClient;

public class PlayingVideoData {
	@Nullable
	private Episode episode = null;

	@Nullable
	private String itemId = null;

	@Nullable
	private RemotePlaybackClient remotePlaybackClient = null;

	@Nullable
	private MediaRouter.RouteInfo route = null;

	@Nullable
	private Show show = null;

	@Nullable
	private Uri videoUri = null;

	@Nullable
	public Episode getEpisode() {
		return this.episode;
	}

	@Nullable
	public String getItemId() {
		return this.itemId;
	}

	@Nullable
	public RemotePlaybackClient getRemotePlaybackClient() {
		return this.remotePlaybackClient;
	}

	@Nullable
	public MediaRouter.RouteInfo getRoute() {
		return this.route;
	}

	@Nullable
	public Show getShow() {
		return this.show;
	}

	@Nullable
	public Uri getVideoUri() {
		return this.videoUri;
	}

	public void setEpisode(@Nullable Episode episode) {
		this.episode = episode;
	}

	public void setItemId(@Nullable String itemId) {
		this.itemId = itemId;
	}

	public void setRemotePlaybackClient(@Nullable RemotePlaybackClient remotePlaybackClient) {
		this.remotePlaybackClient = remotePlaybackClient;
	}

	public void setRoute(@Nullable MediaRouter.RouteInfo route) {
		if (this.remotePlaybackClient != null && this.route != null) {
			this.remotePlaybackClient.release();
			this.remotePlaybackClient = null;
		}

		this.route = route;
	}

	public void setShow(@Nullable Show show) {
		this.show = show;
	}

	public void setVideoUri(@Nullable Uri videoUri) {
		this.videoUri = videoUri;
	}
}
