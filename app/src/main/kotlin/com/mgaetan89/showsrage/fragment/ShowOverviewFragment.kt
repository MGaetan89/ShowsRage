package com.mgaetan89.showsrage.fragment

import android.app.SearchManager
import android.content.ComponentName
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsIntent
import android.support.customtabs.CustomTabsServiceConnection
import android.support.customtabs.CustomTabsSession
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.support.v7.graphics.Palette
import android.support.v7.widget.CardView
import android.text.format.DateUtils
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.helper.*
import com.mgaetan89.showsrage.model.*
import com.mgaetan89.showsrage.network.OmDbApi
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.RealmResults
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

class ShowOverviewFragment : Fragment(), Callback<SingleShow>, View.OnClickListener, ImageLoader.OnImageResult, Palette.PaletteAsyncListener, RealmChangeListener<Show> {
    private var airs: TextView? = null
    private var awards: TextView? = null
    private var awardsLayout: CardView? = null
    private var banner: ImageView? = null
    private var castingActors: TextView? = null
    private var castingDirectors: TextView? = null
    private var castingLayout: CardView? = null
    private var castingWriters: TextView? = null
    private var fanArt: ImageView? = null
    private var genre: TextView? = null
    private var imdb: Button? = null
    private var languageCountry: TextView? = null
    private var location: TextView? = null
    private var name: TextView? = null
    private var network: TextView? = null
    private var nextEpisodeDate: TextView? = null
    private var omDbApi: OmDbApi? = null
    private var pauseMenu: MenuItem? = null
    private var plot: TextView? = null
    private var plotLayout: CardView? = null
    private var poster: ImageView? = null
    private var quality: TextView? = null
    private var rated: TextView? = null
    private var rating: TextView? = null
    private var ratingStars: RatingBar? = null
    private var resumeMenu: MenuItem? = null
    private var runtime: TextView? = null
    private var series: RealmResults<Serie>? = null
    private val seriesListener = RealmChangeListener<RealmResults<Serie>> { series ->
        val serie = series.firstOrNull() ?: return@RealmChangeListener

        if (this.awards != null) {
            setText(this, this.awards!!, serie.awards, 0, this.awardsLayout)
        }

        val actors = serie.actors
        val director = serie.director
        val writer = serie.writer

        if (actors.hasText() || director.hasText() || writer.hasText()) {
            if (this.castingActors != null) {
                setText(this, this.castingActors!!, actors, R.string.actors, null)
            }

            if (this.castingDirectors != null) {
                setText(this, this.castingDirectors!!, director, R.string.directors, null)
            }

            this.castingLayout?.visibility = View.VISIBLE

            if (this.castingWriters != null) {
                setText(this, this.castingWriters!!, writer, R.string.writers, null)
            }
        } else {
            this.castingLayout?.visibility = View.GONE
        }

        if (this.languageCountry != null) {
            val country = serie.country
            val language = serie.language

            if (language.hasText()) {
                this.languageCountry!!.text = if (country.hasText()) {
                    this.getString(R.string.language_county, language, country)
                } else {
                    this.getString(R.string.language_value, language)
                }
                this.languageCountry!!.visibility = View.VISIBLE
            } else {
                this.languageCountry!!.visibility = View.GONE
            }
        }

        if (this.plot != null) {
            setText(this, this.plot!!, serie.plot, 0, this.plotLayout)
        }

        if (this.rated != null) {
            setText(this, this.rated!!, serie.rated, R.string.rated, null)
        }

        if (this.rating != null) {
            val imdbRating = serie.imdbRating
            val imdbVotes = serie.imdbVotes

            if (imdbRating.hasText() && imdbVotes.hasText()) {
                this.rating!!.text = this.getString(R.string.rating, imdbRating, imdbVotes)
                this.rating!!.visibility = View.VISIBLE
            } else {
                this.rating!!.visibility = View.GONE
            }
        }

        if (this.ratingStars != null) {
            try {
                this.ratingStars!!.rating = serie.imdbRating?.toFloat() ?: 0f
                this.ratingStars!!.visibility = View.VISIBLE
            } catch(exception: Exception) {
                this.ratingStars!!.visibility = View.GONE
            }
        }

        if (this.runtime != null) {
            setText(this, this.runtime!!, serie.runtime, R.string.runtime, null)
        }

        if (this.year != null) {
            setText(this, this.year!!, serie.year, R.string.year, null)
        }
    }
    private var serviceConnection: ServiceConnection? = null
    private var show: Show? = null
    private var status: TextView? = null
    private var tabSession: CustomTabsSession? = null
    private var theTvDb: Button? = null
    private var webSearch: Button? = null
    private var year: TextView? = null

    init {
        this.setHasOptionsMenu(true)
    }

    override fun failure(error: RetrofitError?) {
        error?.printStackTrace()
    }

    fun getSetShowQualityCallback(): Callback<GenericResponse> {
        return GenericCallback(this.activity);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val restAdapter = RestAdapter.Builder()
                .setEndpoint(Constants.OMDB_URL)
                .setLogLevel(Constants.NETWORK_LOG_LEVEL)
                .build()

        this.omDbApi = restAdapter.create(OmDbApi::class.java)
    }

    override fun onChange(show: Show) {
        if (!show.isLoaded) {
            return
        }

        if (this.serviceConnection == null) {
            this.serviceConnection = ServiceConnection(this)

            CustomTabsClient.bindCustomTabsService(this.context, "com.android.chrome", this.serviceConnection)
        }

        this.activity?.title = show.showName

        val imdbId = show.imdbId
        val nextEpisodeAirDate = show.nextEpisodeAirDate

        this.showHidePauseResumeMenus(show.paused == 0)

        if (imdbId != null) {
            if (this.series == null) {
                this.series = RealmManager.getSeries(imdbId, this.seriesListener)
            }

            this.omDbApi?.getShow(imdbId, OmdbShowCallback())
        }

        if (this.airs != null) {
            val airs = show.airs

            this.airs!!.text = if (airs.isNullOrEmpty()) {
                this.getString(R.string.airs, "N/A")
            } else {
                this.getString(R.string.airs, airs)
            }

            this.airs!!.visibility = View.VISIBLE
        }

        if (this.banner != null) {
            ImageLoader.load(
                    this.banner,
                    SickRageApi.instance.getBannerUrl(show.tvDbId, Indexer.TVDB),
                    false, null, this
            )

            this.banner!!.contentDescription = show.showName
        }

        if (this.fanArt != null) {
            ImageLoader.load(
                    this.fanArt,
                    SickRageApi.instance.getFanArtUrl(show.tvDbId, Indexer.TVDB),
                    false, null, this
            )

            this.fanArt!!.contentDescription = show.showName
        }

        if (this.genre != null) {
            val genresList = show.genre

            if (genresList?.isNotEmpty() ?: false) {
                val genres = genresList!!.joinToString { it.value }

                this.genre!!.text = this.getString(R.string.genre, genres)
                this.genre!!.visibility = View.VISIBLE
            } else {
                this.genre!!.visibility = View.GONE
            }
        }

        if (this.imdb != null) {
            this.imdb!!.visibility = if (imdbId.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        if (this.languageCountry != null) {
            this.languageCountry!!.text = this.getString(R.string.language_value, show.language)
            this.languageCountry!!.visibility = View.VISIBLE
        }

        if (this.location != null) {
            val location = show.location

            this.location!!.text = if (location.isNullOrEmpty()) {
                this.getString(R.string.location, "N/A")
            } else {
                this.getString(R.string.location, location)
            }
            this.location!!.visibility = View.VISIBLE
        }

        if (this.name != null) {
            this.name!!.text = show.showName
            this.name!!.visibility = View.VISIBLE
        }

        if (this.nextEpisodeDate != null) {
            if (nextEpisodeAirDate.isNullOrEmpty()) {
                this.nextEpisodeDate!!.visibility = View.GONE
            } else {
                this.nextEpisodeDate!!.text = this.getString(R.string.next_episode, DateTimeHelper.getRelativeDate(nextEpisodeAirDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS))
                this.nextEpisodeDate!!.visibility = View.VISIBLE
            }
        }

        if (this.network != null) {
            this.network!!.text = this.getString(R.string.network, show.network)
            this.network!!.visibility = View.VISIBLE
        }

        if (this.poster != null) {
            ImageLoader.load(
                    this.poster,
                    SickRageApi.instance.getPosterUrl(show.tvDbId, Indexer.TVDB),
                    false, this, null
            )

            this.poster!!.contentDescription = show.showName
        }

        if (this.quality != null) {
            val quality = show.quality

            if ("custom".equals(quality, true)) {
                val qualityDetails = show.qualityDetails
                val allowed = listToString(this.getTranslatedQualities(qualityDetails?.initial, true))
                val preferred = listToString(this.getTranslatedQualities(qualityDetails?.archive, false))

                this.quality!!.text = this.getString(R.string.quality_custom, allowed, preferred)
            } else {
                this.quality!!.text = this.getString(R.string.quality, quality)
            }

            this.quality!!.visibility = View.VISIBLE
        }

        if (this.status != null) {
            if (nextEpisodeAirDate.isNullOrEmpty()) {
                val status = show.getStatusTranslationResource()

                this.status!!.text = if (status != 0) {
                    this.getString(status)
                } else {
                    show.status
                }
                this.status!!.visibility = View.VISIBLE
            } else {
                this.status!!.visibility = View.GONE
            }
        }
    }

    override fun onClick(view: View?) {
        if (view == null || this.show == null) {
            return
        }

        val activity = this.activity
        var color = ContextCompat.getColor(activity, R.color.primary)
        val url = when (view.id) {
            R.id.show_imdb -> "http://www.imdb.com/title/${this.show!!.imdbId}"
            R.id.show_the_tvdb -> "http://thetvdb.com/?tab=series&id=${this.show!!.tvDbId}"
            R.id.show_web_search -> {
                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                intent.putExtra(SearchManager.QUERY, this.show!!.showName)

                this.startActivity(intent)

                return
            }
            else -> return
        }

        if (activity is MainActivity) {
            val colors = activity.getThemColors()

            if (colors != null) {
                color = colors.primary
            }
        }

        val tabIntent = CustomTabsIntent.Builder(this.tabSession)
                .addDefaultShareMenuItem()
                .enableUrlBarHiding()
                .setCloseButtonIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_arrow_back_white_24dp))
                .setShowTitle(true)
                .setToolbarColor(color)
                .build()
        tabIntent.launchUrl(this.activity, Uri.parse(url))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val indexerId = this.arguments.getInt(Constants.Bundle.INDEXER_ID)

        this.show = RealmManager.getShow(indexerId, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.show_overview, menu)

        this.pauseMenu = menu?.findItem(R.id.menu_pause_show)
        this.pauseMenu?.isVisible = false
        this.resumeMenu = menu?.findItem(R.id.menu_resume_show)
        this.resumeMenu?.isVisible = false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_show_overview, container, false)

        if (view != null) {
            this.airs = view.findViewById(R.id.show_airs) as TextView?
            this.awards = view.findViewById(R.id.show_awards) as TextView?
            this.awardsLayout = view.findViewById(R.id.show_awards_layout) as CardView?
            this.banner = view.findViewById(R.id.show_banner) as ImageView?
            this.castingActors = view.findViewById(R.id.show_casting_actors) as TextView?
            this.castingDirectors = view.findViewById(R.id.show_casting_directors) as TextView?
            this.castingLayout = view.findViewById(R.id.show_casting_layout) as CardView?
            this.castingWriters = view.findViewById(R.id.show_casting_writers) as TextView?
            this.fanArt = view.findViewById(R.id.show_fan_art) as ImageView?
            this.genre = view.findViewById(R.id.show_genre) as TextView?
            this.imdb = view.findViewById(R.id.show_imdb) as Button?
            this.languageCountry = view.findViewById(R.id.show_language_country) as TextView?
            this.location = view.findViewById(R.id.show_location) as TextView?
            this.name = view.findViewById(R.id.show_name) as TextView?
            this.network = view.findViewById(R.id.show_network) as TextView?
            this.nextEpisodeDate = view.findViewById(R.id.show_next_episode_date) as TextView?
            this.plot = view.findViewById(R.id.show_plot) as TextView?
            this.plotLayout = view.findViewById(R.id.show_plot_layout) as CardView?
            this.poster = view.findViewById(R.id.show_poster) as ImageView?
            this.quality = view.findViewById(R.id.show_quality) as TextView?
            this.rated = view.findViewById(R.id.show_rated) as TextView?
            this.rating = view.findViewById(R.id.show_rating) as TextView?
            this.ratingStars = view.findViewById(R.id.show_rating_stars) as RatingBar?
            this.runtime = view.findViewById(R.id.show_runtime) as TextView?
            this.status = view.findViewById(R.id.show_status) as TextView?
            this.theTvDb = view.findViewById(R.id.show_the_tvdb) as Button?
            this.webSearch = view.findViewById(R.id.show_web_search) as Button?
            this.year = view.findViewById(R.id.show_year) as TextView?

            this.imdb?.setOnClickListener(this)
            this.theTvDb?.setOnClickListener(this)
            this.webSearch?.setOnClickListener(this)

            this.checkSupportWebSearch()
        }

        return view
    }

    override fun onDestroy() {
        val activity = this.activity

        if (activity is MainActivity) {
            activity.resetThemeColors()
        }

        if (this.serviceConnection != null) {
            this.context.unbindService(this.serviceConnection)
        }

        this.series?.removeChangeListeners()
        this.show?.removeChangeListeners()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.airs = null
        this.awards = null
        this.awardsLayout = null
        this.banner = null
        this.castingActors = null
        this.castingDirectors = null
        this.castingLayout = null
        this.castingWriters = null
        this.fanArt = null
        this.genre = null
        this.imdb = null
        this.languageCountry = null
        this.location = null
        this.name = null
        this.nextEpisodeDate = null
        this.network = null
        this.plot = null
        this.plotLayout = null
        this.poster = null
        this.quality = null
        this.rated = null
        this.rating = null
        this.ratingStars = null
        this.runtime = null
        this.status = null
        this.theTvDb = null
        this.webSearch = null
        this.year = null

        super.onDestroyView()
    }

    override fun onGenerated(palette: Palette?) {
        if (palette == null || this.context == null) {
            return
        }

        val activity = this.activity
        val colors = Utils.getThemeColors(this.context, palette)
        val colorPrimary = colors.primary

        if (activity is MainActivity) {
            activity.setThemeColors(colors)
        }

        if (colorPrimary == 0) {
            return
        }

        val colorStateList = ColorStateList.valueOf(colorPrimary)
        val textColor = Utils.getContrastColor(colorPrimary)

        if (this.imdb != null) {
            ViewCompat.setBackgroundTintList(this.imdb, colorStateList)
            this.imdb!!.setTextColor(textColor)
        }

        if (this.theTvDb != null) {
            ViewCompat.setBackgroundTintList(this.theTvDb, colorStateList)
            this.theTvDb!!.setTextColor(textColor)
        }

        if (this.webSearch != null) {
            ViewCompat.setBackgroundTintList(this.webSearch, colorStateList)
            this.webSearch!!.setTextColor(textColor)
        }
    }

    override fun onImageError(imageView: ImageView, exception: Exception?, errorDrawable: Drawable?) {
        val parent = imageView.parent

        if (parent is View) {
            parent.visibility = View.GONE
        }
    }

    override fun onImageReady(imageView: ImageView, resource: Bitmap?) {
        val parent = imageView.parent

        if (parent is View) {
            parent.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_change_quality -> {
                this.changeQuality()

                true
            }

            R.id.menu_delete_show -> {
                this.deleteShow()

                true
            }

            R.id.menu_pause_show -> {
                this.pauseOrResumeShow(true)

                true
            }

            R.id.menu_rescan_show -> {
                this.rescanShow()

                true
            }

            R.id.menu_resume_show -> {
                this.pauseOrResumeShow(false)

                true
            }

            R.id.menu_update_show -> {
                this.updateShow()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        val indexerId = this.arguments.getInt(Constants.Bundle.INDEXER_ID)

        SickRageApi.instance.services?.getShow(indexerId, this)
    }

    override fun success(singleShow: SingleShow?, response: Response?) {
        val show = singleShow?.data ?: return

        RealmManager.saveShow(show)
    }

    private fun changeQuality() {
        if (this.show == null) {
            return
        }

        val arguments = Bundle()
        arguments.putInt(Constants.Bundle.INDEXER_ID, this.show!!.indexerId)

        val fragment = ChangeQualityFragment()
        fragment.arguments = arguments
        fragment.show(this.childFragmentManager, "change_quality")
    }

    private fun checkSupportWebSearch() {
        val webSearchIntent = Intent(Intent.ACTION_WEB_SEARCH)
        val manager = this.context.packageManager
        val activities = manager.queryIntentActivities(webSearchIntent, 0)

        this.webSearch?.visibility = if (activities.size > 0) View.VISIBLE else View.GONE
    }

    private fun deleteShow() {
        if (this.show == null) {
            return
        }

        val indexerId = this.show!!.indexerId
        val callback = DeleteShowCallback(this.activity, indexerId)

        AlertDialog.Builder(this.context)
                .setTitle(this.getString(R.string.delete_show_title, this.show!!.showName))
                .setMessage(R.string.delete_show_message)
                .setPositiveButton(R.string.keep, { dialog, which ->
                    SickRageApi.instance.services?.deleteShow(indexerId, 0, callback)
                })
                .setNegativeButton(R.string.delete, { dialog, which ->
                    SickRageApi.instance.services?.deleteShow(indexerId, 1, callback)
                })
                .setNeutralButton(R.string.cancel, null)
                .show()
    }

    private fun getTranslatedQualities(qualities: RealmList<RealmString>?, allowed: Boolean): List<String> {
        val translatedQualities = mutableListOf<String>()

        if (qualities == null || qualities.isEmpty()) {
            return translatedQualities
        }

        val keys = if (allowed) {
            resources.getStringArray(R.array.allowed_qualities_keys).toList()
        } else {
            resources.getStringArray(R.array.allowed_qualities_keys).toList()
        }
        val values = if (allowed) {
            resources.getStringArray(R.array.allowed_qualities_values).toList()
        } else {
            resources.getStringArray(R.array.allowed_qualities_values).toList()
        }

        qualities.forEach {
            val position = keys.indexOf(it.value)

            if (position != -1) {
                // Skip the "Ignore" first item
                translatedQualities.add(values[position + 1])
            }
        }

        return translatedQualities
    }

    private fun pauseOrResumeShow(pause: Boolean) {
        if (this.show == null) {
            return
        }

        this.showHidePauseResumeMenus(!pause)

        SickRageApi.instance.services?.pauseShow(this.show!!.indexerId, if (pause) 1 else 0, object : GenericCallback(this.activity) {
            override fun failure(error: RetrofitError?) {
                super.failure(error)

                showHidePauseResumeMenus(pause)
            }
        })
    }

    private fun rescanShow() {
        if (this.show != null) {
            SickRageApi.instance.services?.rescanShow(this.show!!.indexerId, GenericCallback(this.activity))
        }
    }

    private fun showHidePauseResumeMenus(isPause: Boolean) {
        this.pauseMenu?.isVisible = isPause
        this.resumeMenu?.isVisible = !isPause
    }

    private fun updateShow() {
        if (this.show != null) {
            SickRageApi.instance.services?.updateShow(this.show!!.indexerId, GenericCallback(this.activity))
        }
    }

    private class DeleteShowCallback(activity: FragmentActivity, val indexerId: Int) : GenericCallback(activity) {
        override fun success(genericResponse: GenericResponse?, response: Response?) {
            super.success(genericResponse, response)

            RealmManager.deleteShow(this.indexerId)

            val activity = this.getActivity() ?: return
            val intent = Intent(activity, MainActivity::class.java)

            activity.startActivity(intent)
        }
    }

    private class OmdbShowCallback : Callback<Serie> {
        override fun failure(error: RetrofitError?) {
            error?.printStackTrace()
        }

        override fun success(serie: Serie?, response: Response?) {
            if (serie != null) {
                RealmManager.saveSerie(serie)
            }
        }
    }

    private class ServiceConnection(fragment: ShowOverviewFragment) : CustomTabsServiceConnection() {
        private val fragmentReference: WeakReference<ShowOverviewFragment>

        init {
            this.fragmentReference = WeakReference(fragment)
        }

        override fun onCustomTabsServiceConnected(componentName: ComponentName?, customTabsClient: CustomTabsClient?) {
            customTabsClient?.warmup(0L)

            val fragment = this.fragmentReference.get() ?: return
            fragment.tabSession = customTabsClient?.newSession(null)

            if (fragment.show?.isValid ?: false) {
                fragment.tabSession?.mayLaunchUrl(Uri.parse("http://www.imdb.com/title/${fragment.show!!.imdbId}"), null, null)
                fragment.tabSession?.mayLaunchUrl(Uri.parse("http://thetvdb.com/?tab=series&id=${fragment.show!!.tvDbId}"), null, null)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            this.fragmentReference.clear()
        }
    }

    companion object {
        private fun listToString(list: List<String>?): String {
            if (list == null || list.isEmpty()) {
                return ""
            }

            val builder = StringBuilder()

            list.forEachIndexed { i, string ->
                builder.append(string)

                if (i < list.size - 1) {
                    builder.append(", ")
                }
            }

            return builder.toString()
        }
    }
}
