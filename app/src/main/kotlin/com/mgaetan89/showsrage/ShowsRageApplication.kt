package com.mgaetan89.showsrage

import android.app.Application
import android.os.Bundle
import android.support.v7.media.MediaItemStatus
import android.support.v7.media.MediaSessionStatus
import android.support.v7.media.RemotePlaybackClient
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.PlayingVideoData

class ShowsRageApplication : Application() {
	var playingVideo: PlayingVideoData? = null
		set(value) {
			field = value

			this.startVideo()
		}

	fun hasPlayingVideo() = this.playingVideo != null

	override fun onCreate() {
		super.onCreate()

		Utils.initRealm(this)
	}

	private fun startVideo() {
		val route = this.playingVideo?.route ?: return
		val videoUri = this.playingVideo?.videoUri ?: return
		val removePlaybackClient = RemotePlaybackClient(this.applicationContext, route)

		removePlaybackClient.play(videoUri, "video/*", null, 0, null, object : RemotePlaybackClient.ItemActionCallback() {
			override fun onResult(data: Bundle?, sessionId: String?, sessionStatus: MediaSessionStatus?, itemId: String?, itemStatus: MediaItemStatus?) {
				super.onResult(data, sessionId, sessionStatus, itemId, itemStatus)

				playingVideo?.itemId = itemId
			}
		})
	}
}
