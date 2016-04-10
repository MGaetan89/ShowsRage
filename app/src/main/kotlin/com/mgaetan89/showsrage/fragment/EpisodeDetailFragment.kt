package com.mgaetan89.showsrage.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.MediaRouteActionProvider
import android.support.v7.app.MediaRouteDiscoveryFragment
import android.support.v7.media.MediaControlIntent
import android.support.v7.media.MediaRouteSelector
import android.support.v7.media.MediaRouter
import android.support.v7.widget.CardView
import android.text.format.DateUtils
import android.view.*
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.ShowsRageApplication
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.GenericCallback
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.*
import com.mgaetan89.showsrage.network.OmDbApi
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.view.ColoredMediaRouteActionProvider
import io.realm.RealmChangeListener
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.regex.Pattern

class EpisodeDetailFragment : MediaRouteDiscoveryFragment(), Callback<SingleEpisode>, View.OnClickListener, RealmChangeListener {
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
    private var playVideoMenu: MenuItem? = null
    private var plot: TextView? = null
    private var plotLayout: CardView? = null
    private var quality: TextView? = null
    private var rated: TextView? = null
    private var rating: TextView? = null
    private var ratingStars: RatingBar? = null
    private var runtime: TextView? = null
    private var seasonNumber = 0
    private var show: Show? = null
    private var status: TextView? = null
    private var year: TextView? = null

    init {
        this.setHasOptionsMenu(true)

        this.routeSelector = MediaRouteSelector.Builder()
                .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
                .build()
    }

    override fun failure(error: RetrofitError?) {
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

            if (!imdbId.isNullOrEmpty()) {
                // We might now have the IMDB id yet
                omDbApi.getEpisodeByImDbId(imdbId, this.seasonNumber, this.episodeNumber, OmdbEpisodeCallback(this))
            } else {
                // So we try to get the data by using the show name
                omDbApi.getEpisodeByTitle(this.show!!.showName, this.seasonNumber, this.episodeNumber, OmdbEpisodeCallback(this))
            }
        }
    }

    override fun onChange() {
        this.displayEpisode(this.episode)
        this.displayStreamingMenus(this.episode)
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
        this.episodeNumber = arguments.getInt(Constants.Bundle.EPISODE_NUMBER, 0)
        this.seasonNumber = arguments.getInt(Constants.Bundle.SEASON_NUMBER, 0)
        this.show = arguments.getParcelable(Constants.Bundle.SHOW_MODEL)

        if (episodeId != null) {
            this.episode = RealmManager.getEpisode(episodeId, this)
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

        this.displayStreamingMenus(this.episode)
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
            this.quality = view.findViewById(R.id.episode_quality) as TextView?
            this.rated = view.findViewById(R.id.episode_rated) as TextView?
            this.rating = view.findViewById(R.id.episode_rating) as TextView?
            this.ratingStars = view.findViewById(R.id.episode_rating_stars) as RatingBar?
            this.runtime = view.findViewById(R.id.episode_runtime) as TextView?
            this.status = view.findViewById(R.id.episode_status) as TextView?
            this.year = view.findViewById(R.id.episode_year) as TextView?

            val searchEpisode = view.findViewById(R.id.search_episode) as FloatingActionButton?

            if (searchEpisode != null) {
                val activity = this.activity

                if (activity is MainActivity ) {
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

    override fun onDestroy() {
        this.episode?.removeChangeListeners()

        super.onDestroy()
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
        this.quality = null
        this.rated = null
        this.rating = null
        this.ratingStars = null
        this.runtime = null
        this.status = null
        this.year = null

        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_episode_set_status_failed,
            R.id.menu_episode_set_status_ignored,
            R.id.menu_episode_set_status_skipped,
            R.id.menu_episode_set_status_wanted -> {
                this.setEpisodeStatus(this.seasonNumber, this.episodeNumber, Episode.getStatusForMenuId(item?.itemId))

                return true
            }

            R.id.menu_play_video -> {
                this.clickPlayVideo()

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        if (this.show != null) {
            SickRageApi.instance.services?.getEpisode(this.show!!.indexerId, this.seasonNumber, this.episodeNumber, this)
        }
    }

    override fun success(singleEpisode: SingleEpisode?, response: Response?) {
        val episode = singleEpisode?.data

        if (episode != null && this.show != null) {
            RealmManager.saveEpisode(episode, this.show!!.indexerId, this.seasonNumber, this.episodeNumber)
        }
    }

    private fun clickPlayVideo() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(this.getEpisodeVideoUrl(), "video/*")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        this.startActivity(intent)
    }

    private fun displayEpisode(episode: Episode?) {
        if (episode == null) {
            return
        }

        this.episode = episode

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

            if (description.isEmpty()) {
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
            val status = episode.statusTranslationResource
            val statusString = if (status != 0) {
                this.getString(status)
            } else {
                episode.status
            }

            this.status!!.text = this.getString(R.string.status_value, statusString)
            this.status!!.visibility = View.VISIBLE
        }
    }

    private fun displayStreamingMenus(episode: Episode?) {
        this.castMenu?.isVisible = this.isCastMenuVisible(episode)
        this.playVideoMenu?.isVisible = this.isPlayMenuVisible(episode)
    }

    private fun getEpisodeVideoUrl(): Uri {
        var episodeUrl = SickRageApi.instance.videosUrl

        if (this.episode != null) {
            var location = this.episode!!.location

            RealmManager.getRootDirs().forEach {
                if (it != null) {
                    val currentLocation = it.location

                    if (location.startsWith(currentLocation)) {
                        location = location.replaceFirst(Pattern.quote(currentLocation), "")

                        return@forEach
                    }
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

    private fun isCastMenuVisible(episode: Episode?): Boolean {
        val activity = this.activity ?: return false
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val episodeDownloaded = this.isEpisodeDownloaded(episode)
        val streamInChromecast = preferences.getBoolean("stream_in_chromecast", false)

        return episodeDownloaded && streamInChromecast
    }

    private fun isEpisodeDownloaded(episode: Episode?): Boolean {
        return episode != null && episode.isLoaded && "Downloaded".equals(episode.status, true)
    }

    private fun isPlayMenuVisible(episode: Episode?): Boolean {
        val activity = this.activity ?: return false
        val prefences = PreferenceManager.getDefaultSharedPreferences(activity)
        val episodeDownloaded = this.isEpisodeDownloaded(episode)
        val viewInExternalVideoPlayer = prefences.getBoolean("view_in_external_video_player", false)

        return episodeDownloaded && viewInExternalVideoPlayer
    }

    private fun setEpisodeStatus(seasonNumber: Int, episodeNumber: Int, status: String?) {
        if (status == null || this.show == null) {
            return
        }

        val callback = GenericCallback(this.activity)
        val indexerId = this.show!!.indexerId

        AlertDialog.Builder(this.context)
                .setMessage(R.string.replace_existing_episode)
                .setPositiveButton(R.string.replace, { dialog, which ->
                    SickRageApi.instance.services?.setEpisodeStatus(indexerId, seasonNumber, episodeNumber, 1, status, callback)
                })
                .setNegativeButton(R.string.keep, { dialog, which ->
                    SickRageApi.instance.services?.setEpisodeStatus(indexerId, seasonNumber, episodeNumber, 0, status, callback)
                })
                .show()
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

            if (activity is MainActivity) {
                activity.updateRemoteControlVisibility()
            }

            if (application is ShowsRageApplication) {
                val playingVideo = PlayingVideoData()
                playingVideo.episode = fragment.episode
                playingVideo.route = route
                playingVideo.show = fragment.show
                playingVideo.videoUri = fragment.getEpisodeVideoUrl()

                application.playingVideo = playingVideo
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
            val fragment = this.fragmentReference.get() ?: return

            if (fragment.awards != null) {
                setText(fragment, fragment.awards!!, episode?.awards, 0, fragment.awardsLayout)
            }

            val actors = episode?.actors
            val director = episode?.director
            val writer = episode?.writer

            if (hasText(actors) || hasText(director) || hasText(writer)) {
                if (fragment.castingActors != null) {
                    setText(fragment, fragment.castingActors!!, actors, R.string.actors, null)
                }

                if (fragment.castingDirectors != null) {
                    setText(fragment, fragment.castingDirectors!!, director, R.string.directors, null)
                }

                if (fragment.castingLayout != null) {
                    fragment.castingLayout!!.visibility = View.VISIBLE
                }

                if (fragment.castingWriters != null) {
                    setText(fragment, fragment.castingWriters!!, writer, R.string.writers, null)
                }
            } else {
                if (fragment.castingLayout != null) {
                    fragment.castingLayout!!.visibility = View.GONE
                }
            }

            if (fragment.genre != null) {
                setText(fragment, fragment.genre!!, episode?.genre, R.string.genre, null)
            }

            if (fragment.languageCountry != null) {
                val country = episode?.country
                val language = episode?.language

                if (hasText(language)) {
                    if (hasText(country)) {
                        fragment.languageCountry!!.text = fragment.getString(R.string.language_county, language, country)
                    } else {
                        fragment.languageCountry!!.text = fragment.getString(R.string.language_value, language)
                    }

                    fragment.languageCountry!!.visibility = View.VISIBLE
                } else {
                    fragment.languageCountry!!.visibility = View.GONE
                }
            }

            if (fragment.rated != null) {
                setText(fragment, fragment.rated!!, episode?.rated, R.string.rated, null)
            }

            if (fragment.rating != null) {
                val imdbRating = episode?.imdbRating
                val imdbVotes = episode?.imdbVotes

                if (hasText(imdbRating) && hasText(imdbVotes)) {
                    fragment.rating!!.text = fragment.getString(R.string.rating, imdbRating, imdbVotes)
                    fragment.rating!!.visibility = View.VISIBLE
                } else {
                    fragment.rating!!.visibility = View.GONE
                }
            }

            if (fragment.ratingStars != null) {
                try {
                    fragment.ratingStars!!.rating = episode?.imdbRating?.toFloat() ?: 0f
                    fragment.ratingStars!!.visibility = View.VISIBLE
                } catch (exception: Exception) {
                    fragment.ratingStars!!.visibility = View.GONE
                }

            }

            if (fragment.runtime != null) {
                setText(fragment, fragment.runtime!!, episode?.runtime, R.string.runtime, null)
            }

            if (fragment.year != null) {
                setText(fragment, fragment.year!!, episode?.year, R.string.year, null)
            }
        }

        companion object {
            private fun hasText(text: String?): Boolean {
                return !text.isNullOrEmpty() && !"N/A".equals(text, true)
            }

            private fun setText(fragment: Fragment, textView: TextView, text: String?, label: Int, layout: View?) {
                if (hasText(text)) {
                    if (layout == null) {
                        textView.text = fragment.getString(label, text)
                        textView.visibility = View.VISIBLE
                    } else {
                        layout.visibility = View.VISIBLE
                        textView.text = text
                    }
                } else {
                    layout?.visibility = View.GONE
                    textView.visibility = View.GONE
                }
            }
        }
    }
}
