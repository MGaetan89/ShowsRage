package com.mgaetan89.showsrage.model

import android.net.Uri
import android.support.v7.media.MediaRouter
import android.support.v7.media.RemotePlaybackClient

class PlayingVideoData {
    var episode: Episode? = null
    var itemId: String? = null
    var remotePlaybackClient: RemotePlaybackClient? = null
    var route: MediaRouter.RouteInfo? = null
        set(value) {
            if (this.remotePlaybackClient != null && this.route != null) {
                this.remotePlaybackClient!!.release()
                this.remotePlaybackClient = null
            }

            field = value
        }
    var show: Show? = null
    var videoUri: Uri? = null
}
