package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AlertDialog
import android.support.v7.media.MediaControlIntent
import android.support.v7.media.MediaItemStatus
import android.support.v7.media.MediaSessionStatus
import android.support.v7.media.RemotePlaybackClient
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.ShowsRageApplication
import com.mgaetan89.showsrage.model.PlayingVideoData
import java.lang.ref.WeakReference

class RemoteControlFragment : DialogFragment(), View.OnClickListener, SeekBar.OnSeekBarChangeListener, Runnable {
    private var episodeCurrentTime: TextView? = null
    private var episodeDuration: TextView? = null
    private var episodeSeekBar: SeekBar? = null
    private val handler = Handler()
    private var play: ImageView? = null
    private var playing = false
    private var playPauseStopCallback: PlayPauseStopCallback? = null
    private var position = 0L
    private var statusCallback: StatusCallback? = null
    private var volume = 0
    private var volumeMute: ImageView? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.playPauseStopCallback = PlayPauseStopCallback(this)
        this.statusCallback = StatusCallback(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.remote_fast_forward -> this.fastForward()
            R.id.remote_fast_rewind -> this.fastRewind()
            R.id.remote_play_pause -> this.playPause()
            R.id.remote_volume_down -> this.volumeDown()
            R.id.remote_volume_mute -> this.volumeMute()
            R.id.remote_volume_up -> this.volumeUp()
            R.id.remote_stop -> this.stop()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playingVideo = this.getPlayingVideo()
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_remote_control, null)

        if (view != null) {
            this.episodeCurrentTime = view.findViewById(R.id.episode_current_time) as TextView?
            this.episodeDuration = view.findViewById(R.id.episode_duration) as TextView?
            this.episodeSeekBar = view.findViewById(R.id.episode_seek_bar) as SeekBar?
            this.play = view.findViewById(R.id.remote_play_pause) as ImageView?
            this.volumeMute = view.findViewById(R.id.remote_volume_mute) as ImageView?

            this.episodeSeekBar?.setOnSeekBarChangeListener(this)
            this.play?.setOnClickListener(this)
            this.volumeMute?.setOnClickListener(this)

            val episodeName = view.findViewById(R.id.episode_name) as TextView?
            val fastForward = view.findViewById(R.id.remote_fast_forward) as ImageView?
            val fastRewind = view.findViewById(R.id.remote_fast_rewind) as ImageView?
            val volumeDown = view.findViewById(R.id.remote_volume_down) as ImageView?
            val volumeUp = view.findViewById(R.id.remote_volume_up) as ImageView?
            val stop = view.findViewById(R.id.remote_stop) as ImageView?

            if (episodeName != null) {
                if (playingVideo != null) {
                    val episode = playingVideo.episode

                    if (episode != null) {
                        episodeName.text = episode.name
                        episodeName.visibility = View.VISIBLE
                    } else {
                        episodeName.visibility = View.GONE
                    }
                } else {
                    episodeName.visibility = View.GONE
                }
            }

            fastForward?.setOnClickListener(this)
            fastRewind?.setOnClickListener(this)
            volumeDown?.setOnClickListener(this)
            volumeUp?.setOnClickListener(this)
            stop?.setOnClickListener(this)
        }

        val builder = AlertDialog.Builder(this.context)

        if (playingVideo != null) {
            val route = playingVideo.route
            val show = playingVideo.show

            if (show != null) {
                builder.setTitle(show.showName)
            }

            if (route != null) {
                this.volume = route.volume
            }
        }

        builder.setView(view)
        builder.setPositiveButton(R.string.close, null)

        return builder.show()
    }

    override fun onDestroyView() {
        this.episodeCurrentTime = null
        this.episodeDuration = null
        this.episodeSeekBar = null
        this.play = null
        this.volumeMute = null

        super.onDestroyView()
    }

    override fun onPause() {
        this.handler.removeCallbacks(this)

        super.onPause()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (!fromUser || !this.playing) {
            return
        }

        val playingVideo = this.getPlayingVideo() ?: return
        val itemId = playingVideo.itemId
        val remotePlaybackClient = playingVideo.remotePlaybackClient

        if (!itemId.isNullOrEmpty() && remotePlaybackClient != null) {
            this.position = progress.toLong()

            remotePlaybackClient.seek(itemId, this.position, null, object : RemotePlaybackClient.ItemActionCallback() {})
        }
    }

    override fun onResume() {
        super.onResume()

        this.handler.post(this)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun run() {
        val playingVideo = this.getPlayingVideo() ?: return
        val itemId = playingVideo.itemId
        val remotePlaybackClient = playingVideo.remotePlaybackClient

        if (!itemId.isNullOrEmpty() && remotePlaybackClient != null) {
            remotePlaybackClient.getStatus(itemId, null, this.statusCallback)

            this.handler.postDelayed(this, ONE_SECOND_IN_MS)
        }
    }

    private fun fastForward() {
        val playingVideo = this.getPlayingVideo() ?: return
        val itemId = playingVideo.itemId
        val remotePlaybackClient = playingVideo.remotePlaybackClient

        if (!itemId.isNullOrEmpty() && remotePlaybackClient != null) {
            this.position += SEEK_OFFSET_IN_MS

            remotePlaybackClient.seek(itemId, this.position, null, object : RemotePlaybackClient.ItemActionCallback() {})
        }
    }

    private fun fastRewind() {
        val playingVideo = this.getPlayingVideo() ?: return
        val itemId = playingVideo.itemId
        val remotePlaybackClient = playingVideo.remotePlaybackClient

        if (!itemId.isNullOrEmpty() && remotePlaybackClient != null) {
            this.position = Math.max(this.position - SEEK_OFFSET_IN_MS, 0L)

            remotePlaybackClient.seek(itemId, this.position, null, object : RemotePlaybackClient.ItemActionCallback() {})
        }
    }

    private fun getPlayingVideo(): PlayingVideoData? {
        val activity = this.activity ?: return null
        val application = activity.application

        if (application is ShowsRageApplication) {
            return application.playingVideo
        }

        return null
    }

    private fun playPause() {
        val playingVideo = this.getPlayingVideo() ?: return
        val remotePlaybackClient = playingVideo.remotePlaybackClient ?: return

        if (this.playing) {
            remotePlaybackClient.pause(null, this.playPauseStopCallback)
        } else {
            remotePlaybackClient.resume(null, this.playPauseStopCallback)
        }
    }

    private fun setVolumeUpIconColor(iconColor: Int) {
        if (this.volumeMute != null) {
            val drawable = DrawableCompat.wrap(this.volumeMute!!.drawable)
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this.context, iconColor))
        }
    }

    private fun stop() {
        val playingVideo = this.getPlayingVideo() ?: return
        val remotePlaybackClient = playingVideo.remotePlaybackClient

        remotePlaybackClient?.stop(null, this.playPauseStopCallback)
    }

    private fun updatePlayPauseIcon() {
        this.play?.setImageResource(if (this.playing) {
            R.drawable.ic_pause_white_24dp
        } else {
            R.drawable.ic_play_arrow_white_24dp
        })
    }

    private fun volumeDown() {
        val playingVideo = this.getPlayingVideo() ?: return
        val route = playingVideo.route ?: return

        this.volume -= route.volume / 10
        this.volume = Math.max(this.volume, 0)

        route.requestSetVolume(this.volume)

        this.setVolumeUpIconColor(android.R.color.white)
    }

    private fun volumeMute() {
        val playingVideo = this.getPlayingVideo() ?: return
        val route = playingVideo.route ?: return
        val currentVolume = route.volume
        val iconColor: Int

        if (currentVolume > 0) {
            iconColor = R.color.accent

            route.requestSetVolume(0)
        } else {
            iconColor = android.R.color.white

            route.requestSetVolume(this.volume)
        }

        this.volume = currentVolume

        this.setVolumeUpIconColor(iconColor)
    }

    private fun volumeUp() {
        val playingVideo = this.getPlayingVideo() ?: return
        val route = playingVideo.route ?: return

        this.volume += route.volumeMax / 10
        this.volume = Math.min(this.volume, route.volumeMax)

        route.requestSetVolume(this.volume)

        this.setVolumeUpIconColor(android.R.color.white)
    }

    private class PlayPauseStopCallback(fragment: RemoteControlFragment) : RemotePlaybackClient.SessionActionCallback() {
        private val fragmentReference: WeakReference<RemoteControlFragment>

        init {
            this.fragmentReference = WeakReference(fragment)
        }

        override fun onResult(data: Bundle?, sessionId: String?, sessionStatus: MediaSessionStatus?) {
            super.onResult(data, sessionId, sessionStatus)

            val fragment = this.fragmentReference.get() ?: return
            fragment.playing = !fragment.playing
            fragment.updatePlayPauseIcon()
        }
    }

    private class StatusCallback(fragment: RemoteControlFragment) : RemotePlaybackClient.ItemActionCallback() {
        private val fragmentReference: WeakReference<RemoteControlFragment>

        init {
            this.fragmentReference = WeakReference(fragment)
        }

        override fun onResult(data: Bundle?, sessionId: String?, sessionStatus: MediaSessionStatus?, itemId: String?, itemStatus: MediaItemStatus?) {
            super.onResult(data, sessionId, sessionStatus, itemId, itemStatus)

            val fragment = this.fragmentReference.get() ?: return
            val status = MediaItemStatus.fromBundle(data?.getBundle(MediaControlIntent.EXTRA_ITEM_STATUS))

            fragment.playing = status.playbackState == MediaItemStatus.PLAYBACK_STATE_PLAYING
            fragment.position = status.contentPosition
            fragment.updatePlayPauseIcon()

            fragment.episodeCurrentTime?.text = formatTime(status.contentPosition)
            fragment.episodeCurrentTime?.visibility = View.VISIBLE

            fragment.episodeDuration?.text = formatTime(status.contentDuration)
            fragment.episodeDuration?.visibility = View.VISIBLE

            fragment.episodeSeekBar?.max = status.contentDuration.toInt()
            fragment.episodeSeekBar?.progress = status.contentPosition.toInt()
            fragment.episodeSeekBar?.visibility = View.VISIBLE
        }
    }

    companion object {
        const val ONE_SECOND_IN_MS = 1000L
        const val ONE_MINUTE_IN_SECONDS = 60L
        const val ONE_HOUR_IN_SECONDS = 60L * ONE_MINUTE_IN_SECONDS
        const val SEEK_OFFSET_IN_MS = 60L * ONE_SECOND_IN_MS

        protected fun formatTime(time: Long): String {
            var localTime = Math.max(time, 0L)
            localTime /= ONE_SECOND_IN_MS
            val hours = localTime / ONE_HOUR_IN_SECONDS

            localTime %= ONE_HOUR_IN_SECONDS
            val minutes = localTime / ONE_MINUTE_IN_SECONDS

            localTime %= ONE_MINUTE_IN_SECONDS

            var formattedTime = ""

            if (hours > 0L) {
                formattedTime += "%02d:".format(hours)
            }

            return formattedTime + "%02d:".format(minutes) + "%02d".format(localTime)
        }
    }
}
