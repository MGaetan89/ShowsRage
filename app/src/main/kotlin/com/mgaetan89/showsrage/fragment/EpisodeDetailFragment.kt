package com.mgaetan89.showsrage.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.cast.framework.SessionManagerListener
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.extension.getEpisode
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getRootDirs
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.saveEpisode
import com.mgaetan89.showsrage.extension.streamInChromecast
import com.mgaetan89.showsrage.extension.streamInVideoPlayer
import com.mgaetan89.showsrage.extension.toLocale
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.GenericCallback
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.SingleEpisode
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import io.realm.RealmChangeListener
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_airs
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_file_size
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_location
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_more_information_layout
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_name
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_plot
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_plot_layout
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_quality
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_status
import kotlinx.android.synthetic.main.fragment_episode_detail.episode_subtitles
import kotlinx.android.synthetic.main.fragment_episode_detail.search_episode
import kotlinx.android.synthetic.main.fragment_episode_detail.swipe_refresh
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.Locale

class EpisodeDetailFragment : Fragment(), Callback<SingleEpisode>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RealmChangeListener<Episode> {
	private var castMenu: MenuItem? = null
	private var castSessionManager: SessionManager? = null
	private lateinit var episode: Episode
	private var episodeNumber = 0
	private var playVideoMenu: MenuItem? = null
	private lateinit var realm: Realm
	private var seasonNumber = 0
	private val sessionCallback = SessionCallback()
	private var show: Show? = null

	init {
		this.setHasOptionsMenu(true)
	}

	override fun failure(error: RetrofitError?) {
		this.swipe_refresh.isRefreshing = false

		error?.printStackTrace()
	}

	override fun onChange(episode: Episode) {
		if (!episode.isLoaded) {
			return
		}

		this.displayEpisode(episode)
		this.displayStreamingMenus(episode)
	}

	override fun onClick(view: View?) {
		if (this.show == null) {
			return
		}

		Toast.makeText(this.context, this.getString(R.string.episode_search, this.episodeNumber, this.seasonNumber), Toast.LENGTH_SHORT).show()

		SickRageApi.instance.services?.searchEpisode(this.show!!.indexerId, this.seasonNumber, this.episodeNumber, GenericCallback(this.activity))
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.castSessionManager = CastContext.getSharedInstance(this.context).sessionManager
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.episode, menu)

		this.castMenu = CastButtonFactory.setUpMediaRouteButton(this.activity.applicationContext, menu, R.id.menu_cast)
		this.playVideoMenu = menu?.findItem(R.id.menu_play_video)

		this.displayStreamingMenus(this.episode)
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater?.inflate(R.layout.fragment_episode_detail, container, false)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return when (item?.itemId) {
			R.id.menu_episode_set_status_failed,
			R.id.menu_episode_set_status_ignored,
			R.id.menu_episode_set_status_skipped,
			R.id.menu_episode_set_status_wanted -> {
				this.setEpisodeStatus(this.seasonNumber, this.episodeNumber, this.show?.indexerId ?: 0, item.itemId)

				return true
			}

			R.id.menu_play_video -> {
				this.clickPlayVideo()

				return true
			}

			R.id.menu_subtitles_search -> {
				this.searchSubtitles(this.seasonNumber, this.episodeNumber, this.show?.indexerId ?: 0)

				return true
			}

			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onPause() {
		this.castSessionManager?.removeSessionManagerListener(this.sessionCallback, CastSession::class.java)

		super.onPause()
	}

	override fun onRefresh() {
		this.swipe_refresh.isRefreshing = true

		if (this.show != null) {
			SickRageApi.instance.services?.getEpisode(this.show!!.indexerId, this.seasonNumber, this.episodeNumber, this)
		}
	}

	override fun onResume() {
		super.onResume()

		this.castSessionManager?.addSessionManagerListener(this.sessionCallback, CastSession::class.java)

		this.onRefresh()
	}

	override fun onStart() {
		super.onStart()

		this.realm = Realm.getDefaultInstance()

		val arguments = this.arguments
		val episodeId = arguments.getString(Constants.Bundle.EPISODE_ID)
		val indexerId = arguments.getInt(Constants.Bundle.INDEXER_ID)

		this.episode = this.realm.getEpisode(episodeId ?: "", this)
		this.episodeNumber = arguments.getInt(Constants.Bundle.EPISODE_NUMBER)
		this.seasonNumber = arguments.getInt(Constants.Bundle.SEASON_NUMBER)
		this.show = this.realm.getShow(indexerId)

		this.activity?.title = if (this.seasonNumber <= 0) {
			this.getString(R.string.specials)
		} else {
			this.getString(R.string.season_number, this.seasonNumber)
		}
	}

	override fun onStop() {
		if (this.episode.isValid) {
			this.episode.removeAllChangeListeners()
		}

		this.realm.close()

		super.onStop()
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		this.episode_name.isSelected = true

		this.swipe_refresh.setColorSchemeResources(R.color.accent)
		this.swipe_refresh.setOnRefreshListener(this)

		val activity = this.activity

		if (activity is MainActivity) {
			val colors = activity.getThemColors()

			if (colors != null) {
				val colorPrimary = colors.primary

				if (colorPrimary != 0) {
					this.search_episode.backgroundTintList = ColorStateList.valueOf(colorPrimary)
					DrawableCompat.setTint(DrawableCompat.wrap(this.search_episode.drawable), Utils.getContrastColor(colorPrimary))
				}
			}
		}

		this.search_episode.setOnClickListener(this)
	}

	override fun success(singleEpisode: SingleEpisode?, response: Response?) {
		this.swipe_refresh.isRefreshing = false

		val episode = singleEpisode?.data

		if (episode != null && this.show?.isValid ?: false) {
			Realm.getDefaultInstance().let {
				it.saveEpisode(episode, this.show!!.indexerId, this.seasonNumber, this.episodeNumber)
				it.close()
			}
		}
	}

	private fun clickPlayVideo() {
		val activity = this.activity

		if (activity is MainActivity) {
			activity.firebaseAnalytics?.logEvent(EVENT_PLAY_EPISODE_VIDEO, null)
		}

		val intent = Intent(Intent.ACTION_VIEW)
		intent.setDataAndType(this.getEpisodeVideoUrl(), "video/*")
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

		this.startActivity(intent)
	}

	private fun displayEpisode(episode: Episode) {
		if (!episode.isValid) {
			return
		}

		this.episode_airs.text = this.getString(R.string.airs, DateTimeHelper.getRelativeDate(episode.airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS))
		this.episode_airs.visibility = View.VISIBLE

		if (episode.fileSize == 0L) {
			this.episode_more_information_layout.visibility = View.GONE
		} else {
			this.episode_file_size.text = this.getString(R.string.file_size, episode.fileSizeHuman)
			this.episode_location.text = this.getString(R.string.location, episode.location)
			this.episode_more_information_layout.visibility = View.VISIBLE
		}

		this.episode_name.text = episode.name
		this.episode_name.visibility = View.VISIBLE

		val description = episode.description

		if (description.isNullOrEmpty()) {
			this.episode_plot_layout.visibility = View.GONE
		} else {
			this.episode_plot.text = description
			this.episode_plot_layout.visibility = View.VISIBLE
		}

		val quality = episode.quality

		if ("N/A".equals(quality, ignoreCase = true)) {
			this.episode_quality.visibility = View.GONE
		} else {
			this.episode_quality.text = this.getString(R.string.quality, quality)
			this.episode_quality.visibility = View.VISIBLE
		}

		val status = episode.getStatusTranslationResource()
		val statusString = if (status != 0) {
			this.getString(status)
		} else {
			episode.status
		}

		this.episode_status.text = this.getString(R.string.status_value, statusString)
		this.episode_status.visibility = View.VISIBLE

		if (episode.subtitles.isNullOrEmpty()) {
			this.episode_subtitles.visibility = View.GONE
		} else {
			this.episode_subtitles.text = this.getString(R.string.subtitles_value, getDisplayableSubtitlesLanguages(episode.subtitles))
			this.episode_subtitles.visibility = View.VISIBLE
		}
	}

	private fun displayStreamingMenus(episode: Episode) {
		this.castMenu?.isVisible = this.isCastMenuVisible(episode)
		this.playVideoMenu?.isVisible = this.isPlayMenuVisible(episode)
	}

	private fun getEpisodeVideoUrl(): Uri {
		var episodeUrl = SickRageApi.instance.videosUrl
		var location = this.episode.location

		if (!location.isNullOrEmpty()) {
			this.realm.getRootDirs().filterNotNull().forEach {
				val currentLocation = it.location

				if (location!!.startsWith(currentLocation)) {
					location = location!!.substring(currentLocation.length)

					return@forEach
				}
			}

			episodeUrl += location
		}

		try {
			val url = URL(episodeUrl)
			val uri = URI(url.protocol, url.userInfo, url.host, url.port, url.path, url.query, url.ref)

			return Uri.parse(uri.toString())
		} catch (exception: MalformedURLException) {
			exception.printStackTrace()
		} catch(exception: URISyntaxException) {
			exception.printStackTrace()
		}

		return Uri.parse(episodeUrl)
	}

	private fun isCastMenuVisible(episode: Episode): Boolean {
		val activity = this.activity ?: return false
		val episodeDownloaded = this.isEpisodeDownloaded(episode)
		val streamInChromecast = activity.getPreferences().streamInChromecast()

		return episodeDownloaded && streamInChromecast
	}

	private fun isEpisodeDownloaded(episode: Episode): Boolean {
		return episode.isValid && "Downloaded".equals(episode.status, true)
	}

	private fun isPlayMenuVisible(episode: Episode): Boolean {
		val activity = this.activity ?: return false
		val episodeDownloaded = this.isEpisodeDownloaded(episode)
		val viewInExternalVideoPlayer = activity.getPreferences().streamInVideoPlayer()

		return episodeDownloaded && viewInExternalVideoPlayer
	}

	private fun setEpisodeStatus(seasonNumber: Int, episodeNumber: Int, indexerId: Int, menuId: Int) {
		with(Intent(Constants.Intents.ACTION_EPISODE_ACTION_SELECTED)) {
			putExtra(Constants.Bundle.EPISODE_NUMBER, episodeNumber)
			putExtra(Constants.Bundle.INDEXER_ID, indexerId)
			putExtra(Constants.Bundle.MENU_ID, menuId)
			putExtra(Constants.Bundle.SEASON_NUMBER, seasonNumber)

			LocalBroadcastManager.getInstance(context).sendBroadcast(this)
		}
	}

	private fun searchSubtitles(seasonNumber: Int, episodeNumber: Int, indexerId: Int) {
		with(Intent(Constants.Intents.ACTION_EPISODE_ACTION_SELECTED)) {
			putExtra(Constants.Bundle.EPISODE_NUMBER, episodeNumber)
			putExtra(Constants.Bundle.INDEXER_ID, indexerId)
			putExtra(Constants.Bundle.MENU_ID, R.id.menu_subtitles_search)
			putExtra(Constants.Bundle.SEASON_NUMBER, seasonNumber)

			LocalBroadcastManager.getInstance(context).sendBroadcast(this)
		}
	}

	companion object {
		private const val EVENT_CAST_EPISODE_VIDEO = "cast_episode_video"
		private const val EVENT_PLAY_EPISODE_VIDEO = "play_episode_video"

		internal fun getDisplayableSubtitlesLanguages(subtitles: String): String {
			val subtitlesNames = subtitles.split(",").filter { !it.isNullOrEmpty() }.map(String::toLocale).filterNotNull()

			return subtitlesNames.map(Locale::getDisplayLanguage).filter { !it.isNullOrEmpty() }.joinToString()
		}
	}

	private inner class SessionCallback : SessionManagerListener<CastSession> {
		override fun onSessionEnded(session: CastSession, error: Int) {
			activity?.supportInvalidateOptionsMenu()
		}

		override fun onSessionEnding(session: CastSession) = Unit

		override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
			this.onApplicationConnected(session)

			activity?.supportInvalidateOptionsMenu()
		}

		override fun onSessionResumeFailed(session: CastSession, error: Int) = Unit

		override fun onSessionResuming(session: CastSession, sessionId: String?) = Unit

		override fun onSessionStarted(session: CastSession, sessionId: String?) {
			this.onApplicationConnected(session)

			activity?.supportInvalidateOptionsMenu()
		}

		override fun onSessionStartFailed(session: CastSession, error: Int) = Unit

		override fun onSessionStarting(session: CastSession) = Unit

		override fun onSessionSuspended(session: CastSession, error: Int) = Unit

		private fun buildMediaInfo(): MediaInfo {
			val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_TV_SHOW)
			movieMetadata.putInt(MediaMetadata.KEY_EPISODE_NUMBER, episode.number)
			movieMetadata.putInt(MediaMetadata.KEY_SEASON_NUMBER, episode.season)
			movieMetadata.putString(MediaMetadata.KEY_TITLE, episode.name)

			return MediaInfo.Builder(getEpisodeVideoUrl().toString())
					.setContentType("videos/*")
					.setMetadata(movieMetadata)
					.setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
					.build()
		}

		private fun onApplicationConnected(session: CastSession) {
			val activity = this@EpisodeDetailFragment.activity

			if (activity is MainActivity) {
				activity.firebaseAnalytics?.logEvent(EVENT_CAST_EPISODE_VIDEO, null)
			}

			session.remoteMediaClient.load(this.buildMediaInfo())
		}
	}
}
