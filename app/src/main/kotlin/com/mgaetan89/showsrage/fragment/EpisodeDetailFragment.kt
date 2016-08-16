package com.mgaetan89.showsrage.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.MediaRouteActionProvider
import android.support.v7.app.MediaRouteDiscoveryFragment
import android.support.v7.media.MediaControlIntent
import android.support.v7.media.MediaRouteSelector
import android.support.v7.media.MediaRouter
import android.support.v7.widget.CardView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.ShowsRageApplication
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.extension.getEpisode
import com.mgaetan89.showsrage.extension.getEpisodes
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getRootDirs
import com.mgaetan89.showsrage.extension.streamInChromecast
import com.mgaetan89.showsrage.extension.streamInVideoPlayer
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.GenericCallback
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.helper.hasText
import com.mgaetan89.showsrage.helper.setText
import com.mgaetan89.showsrage.helper.toLocale
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.OmDbEpisode
import com.mgaetan89.showsrage.model.PlayingVideoData
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.SingleEpisode
import com.mgaetan89.showsrage.network.OmDbApi
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.view.ColoredMediaRouteActionProvider
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.*

class EpisodeDetailFragment : MediaRouteDiscoveryFragment(), Callback<SingleEpisode>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RealmChangeListener<Episode> {
    private var airs: TextView? = null
    private var awards: TextView? = null
    private var awardsLayout: CardView? = null
    private var castingActors: TextView? = null
    private var castingDirectors: TextView? = null
    private var castingLayout: CardView? = null
    private var castingWriters: TextView? = null
    private var castMenu: MenuItem? = null
    private var episode: Episode? = null
    private var episodeNumber = 0
    private var fileSize: TextView? = null
    private var genre: TextView? = null
    private var languageCountry: TextView? = null
    private var location: TextView? = null
    private var moreInformationLayout: CardView? = null
    private var name: TextView? = null
    private var omdbEpisodes: RealmResults<OmDbEpisode>? = null
    private val omdbEpisodesListener = RealmChangeListener<RealmResults<OmDbEpisode>> { episodes ->
        val episode = episodes.firstOrNull() ?: return@RealmChangeListener

        if (this.awards != null) {
            setText(this, this.awards!!, episode.awards, 0, this.awardsLayout)
        }

        val actors = episode.actors
        val director = episode.director
        val writer = episode.writer

        if (actors.hasText() || director.hasText() || writer.hasText()) {
            if (this.castingActors != null) {
                setText(this, this.castingActors!!, actors, R.string.actors, null)
            }

            if (this.castingDirectors != null) {
                setText(this, this.castingDirectors!!, director, R.string.directors, null)
            }

            if (this.castingLayout != null) {
                this.castingLayout!!.visibility = View.VISIBLE
            }

            if (this.castingWriters != null) {
                setText(this, this.castingWriters!!, writer, R.string.writers, null)
            }
        } else {
            if (this.castingLayout != null) {
                this.castingLayout!!.visibility = View.GONE
            }
        }

        if (this.genre != null) {
            setText(this, this.genre!!, episode.genre, R.string.genre, null)
        }

        if (this.languageCountry != null) {
            val country = episode.country
            val language = episode.language

            if (language.hasText()) {
                if (country.hasText()) {
                    this.languageCountry!!.text = this.getString(R.string.language_county, language, country)
                } else {
                    this.languageCountry!!.text = this.getString(R.string.language_value, language)
                }

                this.languageCountry!!.visibility = View.VISIBLE
            } else {
                this.languageCountry!!.visibility = View.GONE
            }
        }

        if (this.poster != null) {
            if (episode.poster.isNullOrEmpty()) {
                this.poster!!.visibility = View.GONE
            } else {
                ImageLoader.load(this.poster, episode.poster, false)

                this.poster!!.contentDescription = episode.title
                this.poster!!.visibility = View.VISIBLE
            }
        }

        if (this.rated != null) {
            setText(this, this.rated!!, episode.rated, R.string.rated, null)
        }

        if (this.rating != null) {
            val imdbRating = episode.imdbRating
            val imdbVotes = episode.imdbVotes

            if (imdbRating.hasText() && imdbVotes.hasText()) {
                this.rating!!.text = this.getString(R.string.rating, imdbRating, imdbVotes)
                this.rating!!.visibility = View.VISIBLE
            } else {
                this.rating!!.visibility = View.GONE
            }
        }

        if (this.ratingStars != null) {
            try {
                this.ratingStars!!.rating = episode.imdbRating?.toFloat() ?: 0f
                this.ratingStars!!.visibility = View.VISIBLE
            } catch (exception: Exception) {
                this.ratingStars!!.visibility = View.GONE
            }
        }

        if (this.runtime != null) {
            setText(this, this.runtime!!, episode.runtime, R.string.runtime, null)
        }

        if (this.year != null) {
            setText(this, this.year!!, episode.year, R.string.year, null)
        }
    }

    private var playVideoMenu: MenuItem? = null
    private var plot: TextView? = null
    private var plotLayout: CardView? = null
    private var poster: ImageView? = null
    private var quality: TextView? = null
    private var rated: TextView? = null
    private var rating: TextView? = null
    private var ratingStars: RatingBar? = null
    private var realm: Realm? = null
    private var runtime: TextView? = null
    private var seasonNumber = 0
    private var show: Show? = null
    private var status: TextView? = null
    private var subtitles: TextView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var year: TextView? = null

    init {
        this.setHasOptionsMenu(true)

        this.routeSelector = MediaRouteSelector.Builder()
                .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
                .build()
    }

    override fun failure(error: RetrofitError?) {
        this.swipeRefreshLayout?.isRefreshing = false

        error?.printStackTrace()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val restAdapter = RestAdapter.Builder()
                .setEndpoint(Constants.OMDB_URL)
                .setLogLevel(Constants.NETWORK_LOG_LEVEL)
                .build()
        val omDbApi = restAdapter.create(OmDbApi::class.java)

        this.activity?.title = if (this.seasonNumber <= 0) {
            this.getString(R.string.specials)
        } else {
            this.getString(R.string.season_number, this.seasonNumber)
        }

        if (this.show != null) {
            val imdbId = this.show!!.imdbId

            // We might not have the IMDB id yet
            if (imdbId.isNullOrEmpty()) {
                // So we try to get the data by using the show name
                val showName = this.show!!.showName

                if (!showName.isNullOrEmpty()) {
                    omDbApi.getEpisodeByTitle(showName!!, this.seasonNumber, this.episodeNumber, OmdbEpisodeCallback(this))
                }
            } else {
                this.omdbEpisodes = this.realm?.getEpisodes(OmDbEpisode.buildId(imdbId!!, this.seasonNumber.toString(), this.episodeNumber.toString()), this.omdbEpisodesListener)

                omDbApi.getEpisodeByImDbId(imdbId!!, this.seasonNumber, this.episodeNumber, OmdbEpisodeCallback(this))
            }
        }
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

        val arguments = this.arguments
        val episodeId = arguments.getString(Constants.Bundle.EPISODE_ID)
        val indexerId = arguments.getInt(Constants.Bundle.INDEXER_ID)
        this.episodeNumber = arguments.getInt(Constants.Bundle.EPISODE_NUMBER)
        this.seasonNumber = arguments.getInt(Constants.Bundle.SEASON_NUMBER)
        this.show = RealmManager.getShow(indexerId)

        if (episodeId != null) {
            this.episode = this.realm?.getEpisode(episodeId, this)
        }
    }

    override fun onCreateCallback(): MediaRouter.Callback? {
        return MediaRouterCallback(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.episode, menu)

        this.castMenu = menu?.findItem(R.id.menu_cast)
        this.playVideoMenu = menu?.findItem(R.id.menu_play_video)

        val activity = this.activity
        val mediaRouteActionProvider = MenuItemCompat.getActionProvider(this.castMenu) as MediaRouteActionProvider?

        mediaRouteActionProvider?.routeSelector = this.routeSelector

        if (activity is MainActivity && mediaRouteActionProvider is ColoredMediaRouteActionProvider) {
            val colors = activity.getThemColors()

            if (colors != null) {
                val colorPrimary = colors.primary

                if (colorPrimary != 0) {
                    mediaRouteActionProvider.buttonColor = Utils.getContrastColor(colorPrimary)
                }
            }
        }

        if (this.episode != null) {
            this.displayStreamingMenus(this.episode!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_episode_detail, container, false)

        if (view != null) {
            this.airs = view.findViewById(R.id.episode_airs) as TextView?
            this.awards = view.findViewById(R.id.episode_awards) as TextView?
            this.awardsLayout = view.findViewById(R.id.episode_awards_layout) as CardView?
            this.castingActors = view.findViewById(R.id.episode_casting_actors) as TextView?
            this.castingDirectors = view.findViewById(R.id.episode_casting_directors) as TextView?
            this.castingLayout = view.findViewById(R.id.episode_casting_layout) as CardView?
            this.castingWriters = view.findViewById(R.id.episode_casting_writers) as TextView?
            this.fileSize = view.findViewById(R.id.episode_file_size) as TextView?
            this.genre = view.findViewById(R.id.episode_genre) as TextView?
            this.languageCountry = view.findViewById(R.id.episode_language_country) as TextView?
            this.location = view.findViewById(R.id.episode_location) as TextView?
            this.moreInformationLayout = view.findViewById(R.id.episode_more_information_layout) as CardView?
            this.name = view.findViewById(R.id.episode_name) as TextView?
            this.plot = view.findViewById(R.id.episode_plot) as TextView?
            this.plotLayout = view.findViewById(R.id.episode_plot_layout) as CardView?
            this.poster = view.findViewById(R.id.episode_poster) as ImageView?
            this.quality = view.findViewById(R.id.episode_quality) as TextView?
            this.rated = view.findViewById(R.id.episode_rated) as TextView?
            this.rating = view.findViewById(R.id.episode_rating) as TextView?
            this.ratingStars = view.findViewById(R.id.episode_rating_stars) as RatingBar?
            this.runtime = view.findViewById(R.id.episode_runtime) as TextView?
            this.status = view.findViewById(R.id.episode_status) as TextView?
            this.subtitles = view.findViewById(R.id.episode_subtitles) as TextView?
            this.swipeRefreshLayout = view.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout?
            this.year = view.findViewById(R.id.episode_year) as TextView?

            this.swipeRefreshLayout?.setColorSchemeResources(R.color.accent)
            this.swipeRefreshLayout?.setOnRefreshListener(this)

            val searchEpisode = view.findViewById(R.id.search_episode) as FloatingActionButton?

            if (searchEpisode != null) {
                val activity = this.activity

                if (activity is MainActivity) {
                    val colors = activity.getThemColors()

                    if (colors != null) {
                        val colorPrimary = colors.primary

                        if (colorPrimary != 0) {
                            searchEpisode.backgroundTintList = ColorStateList.valueOf(colorPrimary)
                            DrawableCompat.setTint(DrawableCompat.wrap(searchEpisode.drawable), Utils.getContrastColor(colorPrimary))
                        }
                    }
                }

                searchEpisode.setOnClickListener(this)
            }
        }

        return view
    }

    override fun onDestroyView() {
        this.airs = null
        this.awards = null
        this.awardsLayout = null
        this.castingActors = null
        this.castingDirectors = null
        this.castingLayout = null
        this.castingWriters = null
        this.fileSize = null
        this.genre = null
        this.languageCountry = null
        this.location = null
        this.moreInformationLayout = null
        this.name = null
        this.plot = null
        this.plotLayout = null
        this.poster = null
        this.quality = null
        this.rated = null
        this.rating = null
        this.ratingStars = null
        this.runtime = null
        this.status = null
        this.subtitles = null
        this.swipeRefreshLayout = null
        this.year = null

        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_episode_set_status_failed,
            R.id.menu_episode_set_status_ignored,
            R.id.menu_episode_set_status_skipped,
            R.id.menu_episode_set_status_wanted -> {
                this.setEpisodeStatus(this.seasonNumber, this.episodeNumber, this.show?.indexerId ?: 0, item!!.itemId)

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

    override fun onRefresh() {
        this.swipeRefreshLayout?.isRefreshing = true

        if (this.show != null) {
            SickRageApi.instance.services?.getEpisode(this.show!!.indexerId, this.seasonNumber, this.episodeNumber, this)
        }
    }

    override fun onResume() {
        super.onResume()

        this.onRefresh()
    }

    override fun onStart() {
        super.onStart()

        this.realm = Realm.getDefaultInstance()
    }

    override fun onStop() {
        this.episode?.removeChangeListeners()
        this.omdbEpisodes?.removeChangeListeners()
        this.realm?.close()

        super.onStop()
    }

    override fun success(singleEpisode: SingleEpisode?, response: Response?) {
        this.swipeRefreshLayout?.isRefreshing = false

        val episode = singleEpisode?.data

        if (episode != null && this.show?.isValid ?: false) {
            RealmManager.saveEpisode(episode, this.show!!.indexerId, this.seasonNumber, this.episodeNumber)
        }
    }

    private fun clickPlayVideo() {
        val activity = this.activity

        if (activity is MainActivity) {
            activity.firebaseAnalytics?.logEvent(Constants.Event.PLAY_EPISODE_VIDEO, null)
        }

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(this.getEpisodeVideoUrl(), "video/*")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        this.startActivity(intent)
    }

    private fun displayEpisode(episode: Episode) {
        if (!episode.isLoaded) {
            return
        }

        this.airs?.text = this.getString(R.string.airs, DateTimeHelper.getRelativeDate(episode.airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS))
        this.airs?.visibility = View.VISIBLE

        if (episode.fileSize == 0L) {
            this.moreInformationLayout?.visibility = View.GONE
        } else {
            this.fileSize?.text = this.getString(R.string.file_size, episode.fileSizeHuman)
            this.location?.text = this.getString(R.string.location, episode.location)
            this.moreInformationLayout?.visibility = View.VISIBLE
        }

        this.name?.text = episode.name
        this.name?.visibility = View.VISIBLE

        if (this.plot != null) {
            val description = episode.description

            if (description.isNullOrEmpty()) {
                this.plotLayout?.visibility = View.GONE
            } else {
                this.plot?.text = description
                this.plotLayout?.visibility = View.VISIBLE
            }
        }

        if (this.quality != null) {
            val quality = episode.quality

            if ("N/A".equals(quality, ignoreCase = true)) {
                this.quality!!.visibility = View.GONE
            } else {
                this.quality!!.text = this.getString(R.string.quality, quality)
                this.quality!!.visibility = View.VISIBLE
            }
        }

        if (this.status != null) {
            val status = episode.getStatusTranslationResource()
            val statusString = if (status != 0) {
                this.getString(status)
            } else {
                episode.status
            }

            this.status!!.text = this.getString(R.string.status_value, statusString)
            this.status!!.visibility = View.VISIBLE
        }

        this.subtitles?.let {
            if (episode.subtitles.isNullOrEmpty()) {
                it.visibility = View.GONE
            } else {
                it.text = this.getString(R.string.subtitles_value, getDisplayableSubtitlesLanguages(episode.subtitles))
                it.visibility = View.VISIBLE
            }
        }
    }

    private fun displayStreamingMenus(episode: Episode) {
        this.castMenu?.isVisible = this.isCastMenuVisible(episode)
        this.playVideoMenu?.isVisible = this.isPlayMenuVisible(episode)
    }

    private fun getEpisodeVideoUrl(): Uri {
        var episodeUrl = SickRageApi.instance.videosUrl

        if (this.episode != null) {
            var location = this.episode!!.location

            if (!location.isNullOrEmpty()) {
                this.realm?.getRootDirs()?.filterNotNull()?.forEach {
                    val currentLocation = it.location

                    if (location!!.startsWith(currentLocation)) {
                        location = location!!.substring(currentLocation.length)

                        return@forEach
                    }
                }

                episodeUrl += location
            }
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
        return episode.isLoaded && "Downloaded".equals(episode.status, true)
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
        internal fun getDisplayableSubtitlesLanguages(subtitles: String): String {
            val subtitlesNames = subtitles.split(",").filter { !it.isNullOrEmpty() }.map(String::toLocale).filterNotNull()

            return subtitlesNames.map(Locale::getDisplayLanguage).filter { !it.isNullOrEmpty() }.joinToString()
        }
    }

    private class MediaRouterCallback(fragment: EpisodeDetailFragment) : MediaRouter.Callback() {
        private val fragmentReference: WeakReference<EpisodeDetailFragment>

        init {
            this.fragmentReference = WeakReference(fragment)
        }

        override fun onRouteSelected(router: MediaRouter?, route: MediaRouter.RouteInfo?) {
            this.updateRemotePlayer(route)
        }

        override fun onRouteUnselected(router: MediaRouter?, route: MediaRouter.RouteInfo?) {
            this.updateRemotePlayer(route)
        }

        private fun updateRemotePlayer(route: MediaRouter.RouteInfo?) {
            val fragment = this.fragmentReference.get() ?: return

            if (route == null || !fragment.userVisibleHint) {
                return
            }

            val activity = fragment.activity
            val application = activity.application

            if (application is ShowsRageApplication) {
                val playingVideo = PlayingVideoData()
                playingVideo.episode = fragment.episode
                playingVideo.route = route
                playingVideo.show = fragment.show
                playingVideo.videoUri = fragment.getEpisodeVideoUrl()

                application.playingVideo = playingVideo
            }

            if (activity is MainActivity) {
                activity.updateRemoteControlVisibility()

                activity.firebaseAnalytics?.logEvent(Constants.Event.CAST_EPISODE_VIDEO, null)
            }
        }
    }

    private class OmdbEpisodeCallback(fragment: EpisodeDetailFragment) : Callback<OmDbEpisode> {
        private val fragmentReference: WeakReference<EpisodeDetailFragment>

        init {
            this.fragmentReference = WeakReference(fragment)
        }

        override fun failure(error: RetrofitError?) {
            error?.printStackTrace()
        }

        override fun success(episode: OmDbEpisode?, response: Response?) {
            if (episode != null) {
                RealmManager.saveEpisode(episode)
            }
        }
    }
}
