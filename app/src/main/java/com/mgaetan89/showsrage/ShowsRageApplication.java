package com.mgaetan89.showsrage;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.media.MediaItemStatus;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaSessionStatus;
import android.support.v7.media.RemotePlaybackClient;

import com.mgaetan89.showsrage.model.PlayingVideoData;

public class ShowsRageApplication extends Application {
	@Nullable
	private PlayingVideoData playingVideo = null;

	@Nullable
	public PlayingVideoData getPlayingVideo() {
		return this.playingVideo;
	}

	public boolean hasPlayingVideo() {
		return this.playingVideo != null;
	}

	public void setPlayingVideo(@Nullable PlayingVideoData playingVideo) {
		this.playingVideo = playingVideo;

		this.startVideo();
	}

	private void startVideo() {
		if (this.playingVideo == null) {
			return;
		}

		MediaRouter.RouteInfo route = this.playingVideo.getRoute();

		if (route != null) {
			Uri videoUri = this.playingVideo.getVideoUri();

			RemotePlaybackClient remotePlaybackClient = new RemotePlaybackClient(this.getApplicationContext(), route);

			if (videoUri != null) {
				remotePlaybackClient.play(videoUri, "video/*", null, 0, null, new RemotePlaybackClient.ItemActionCallback() {
					@Override
					public void onResult(Bundle data, String sessionId, MediaSessionStatus sessionStatus, String itemId, MediaItemStatus itemStatus) {
						super.onResult(data, sessionId, sessionStatus, itemId, itemStatus);

						if (ShowsRageApplication.this.playingVideo != null) {
							ShowsRageApplication.this.playingVideo.setItemId(itemId);
						}
					}
				});
			}

			this.playingVideo.setRemotePlaybackClient(remotePlaybackClient);
		}
	}
}
