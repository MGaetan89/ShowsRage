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
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.graphics.Palette
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.extension.deleteShow
import com.mgaetan89.showsrage.extension.getSeries
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.saveSerie
import com.mgaetan89.showsrage.extension.saveShow
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.GenericCallback
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.helper.hasText
import com.mgaetan89.showsrage.helper.setText
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.model.RealmString
import com.mgaetan89.showsrage.model.Serie
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.SingleShow
import com.mgaetan89.showsrage.network.OmDbApi
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_show_overview.show_airs
import kotlinx.android.synthetic.main.fragment_show_overview.show_awards
import kotlinx.android.synthetic.main.fragment_show_overview.show_awards_layout
import kotlinx.android.synthetic.main.fragment_show_overview.show_banner
import kotlinx.android.synthetic.main.fragment_show_overview.show_casting_actors
import kotlinx.android.synthetic.main.fragment_show_overview.show_casting_directors
import kotlinx.android.synthetic.main.fragment_show_overview.show_casting_layout
import kotlinx.android.synthetic.main.fragment_show_overview.show_casting_writers
import kotlinx.android.synthetic.main.fragment_show_overview.show_fan_art
import kotlinx.android.synthetic.main.fragment_show_overview.show_genre
import kotlinx.android.synthetic.main.fragment_show_overview.show_imdb
import kotlinx.android.synthetic.main.fragment_show_overview.show_language_country
import kotlinx.android.synthetic.main.fragment_show_overview.show_location
import kotlinx.android.synthetic.main.fragment_show_overview.show_name
import kotlinx.android.synthetic.main.fragment_show_overview.show_network
import kotlinx.android.synthetic.main.fragment_show_overview.show_next_episode_date
import kotlinx.android.synthetic.main.fragment_show_overview.show_plot
import kotlinx.android.synthetic.main.fragment_show_overview.show_plot_layout
import kotlinx.android.synthetic.main.fragment_show_overview.show_poster
import kotlinx.android.synthetic.main.fragment_show_overview.show_quality
import kotlinx.android.synthetic.main.fragment_show_overview.show_rated
import kotlinx.android.synthetic.main.fragment_show_overview.show_rating
import kotlinx.android.synthetic.main.fragment_show_overview.show_rating_stars
import kotlinx.android.synthetic.main.fragment_show_overview.show_runtime
import kotlinx.android.synthetic.main.fragment_show_overview.show_status
import kotlinx.android.synthetic.main.fragment_show_overview.show_subtitles
import kotlinx.android.synthetic.main.fragment_show_overview.show_the_tvdb
import kotlinx.android.synthetic.main.fragment_show_overview.show_web_search
import kotlinx.android.synthetic.main.fragment_show_overview.show_year
import kotlinx.android.synthetic.main.fragment_show_overview.swipe_refresh
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

class ShowOverviewFragment : Fragment(), Callback<SingleShow>, View.OnClickListener, ImageLoader.OnImageResult, SwipeRefreshLayout.OnRefreshListener, Palette.PaletteAsyncListener, RealmChangeListener<Show> {
	private val indexerId by lazy { this.arguments.getInt(Constants.Bundle.INDEXER_ID) }
	private var omDbApi: OmDbApi? = null
	private var pauseMenu: MenuItem? = null
	private lateinit var realm: Realm
	private var resumeMenu: MenuItem? = null
	private var series: RealmResults<Serie>? = null
	private val seriesListener = RealmChangeListener<RealmResults<Serie>> { series ->
		val serie = series.firstOrNull() ?: return@RealmChangeListener

		setText(this, this.show_awards, serie.awards, 0, this.show_awards_layout)

		val actors = serie.actors
		val director = serie.director
		val writer = serie.writer

		if (actors.hasText() || director.hasText() || writer.hasText()) {
			setText(this, this.show_casting_actors, actors, R.string.actors, null)
			setText(this, this.show_casting_directors, director, R.string.directors, null)
			setText(this, this.show_casting_writers, writer, R.string.writers, null)

			this.show_casting_layout.visibility = View.VISIBLE
		} else {
			this.show_casting_layout.visibility = View.GONE
		}

		val country = serie.country
		val language = serie.language

		if (language.hasText()) {
			this.show_language_country.text = if (country.hasText()) {
				this.getString(R.string.language_county, language, country)
			} else {
				this.getString(R.string.language_value, language)
			}
			this.show_language_country.visibility = View.VISIBLE
		} else {
			this.show_language_country.visibility = View.GONE
		}

		setText(this, this.show_plot, serie.plot, 0, this.show_plot_layout)
		setText(this, this.show_rated, serie.rated, R.string.rated, null)

		val imdbRating = serie.imdbRating
		val imdbVotes = serie.imdbVotes

		if (imdbRating.hasText() && imdbVotes.hasText()) {
			this.show_rating.text = this.getString(R.string.rating, imdbRating, imdbVotes)
			this.show_rating.visibility = View.VISIBLE
		} else {
			this.show_rating.visibility = View.GONE
		}

		try {
			this.show_rating_stars.rating = serie.imdbRating?.toFloat() ?: 0f
			this.show_rating_stars.visibility = View.VISIBLE
		} catch (exception: Exception) {
			this.show_rating_stars.visibility = View.GONE
		}

		setText(this, this.show_runtime, serie.runtime, R.string.runtime, null)
		setText(this, this.show_year, serie.year, R.string.year, null)
	}
	private var serviceConnection: ServiceConnection? = null
	private lateinit var show: Show
	private var tabSession: CustomTabsSession? = null

	init {
		this.setHasOptionsMenu(true)
	}

	override fun failure(error: RetrofitError?) {
		this.swipe_refresh.isRefreshing = false

		error?.printStackTrace()
	}

	fun getSetShowQualityCallback() = GenericCallback(this.activity)

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val restAdapter = RestAdapter.Builder()
				.setEndpoint(Constants.OMDB_URL)
				.setLogLevel(Constants.NETWORK_LOG_LEVEL)
				.build()

		this.omDbApi = restAdapter.create(OmDbApi::class.java)
	}

	override fun onChange(show: Show) {
		if (!show.isLoaded || !show.isValid) {
			return
		}

		if (this.serviceConnection == null) {
			this.context?.let {
				this.serviceConnection = ServiceConnection(this)

				CustomTabsClient.bindCustomTabsService(it, "com.android.chrome", this.serviceConnection)
			}
		}

		this.activity?.title = show.showName

		val imdbId = show.imdbId
		val nextEpisodeAirDate = show.nextEpisodeAirDate

		this.showHidePauseResumeMenus(show.paused == 0)

		if (imdbId != null) {
			if (this.series == null) {
				this.series = this.realm.getSeries(imdbId, this.seriesListener)
			}

			this.omDbApi?.getShow(imdbId, OmdbShowCallback())
		}

		val airs = show.airs

		this.show_airs.text = this.getString(R.string.airs, if (airs.isNullOrEmpty()) "N/A" else airs)
		this.show_airs.visibility = View.VISIBLE

		ImageLoader.load(this.show_banner, SickRageApi.instance.getBannerUrl(show.tvDbId, Indexer.TVDB), false, null, this)
		this.show_banner.contentDescription = show.showName

		ImageLoader.load(this.show_fan_art, SickRageApi.instance.getFanArtUrl(show.tvDbId, Indexer.TVDB), false, null, this)
		this.show_fan_art.contentDescription = show.showName

		val genresList = show.genre

		if (genresList?.isNotEmpty() ?: false) {
			val genres = genresList!!.joinToString { it.value }

			this.show_genre.text = this.getString(R.string.genre, genres)
			this.show_genre.visibility = View.VISIBLE
		} else {
			this.show_genre.visibility = View.GONE
		}

		this.show_imdb.visibility = if (imdbId.isNullOrEmpty()) View.GONE else View.VISIBLE

		this.show_language_country.text = this.getString(R.string.language_value, show.language)
		this.show_language_country.visibility = View.VISIBLE

		val location = show.location

		this.show_location.text = this.getString(R.string.location, if (location.isNullOrEmpty()) "N/A" else location)
		this.show_location.visibility = View.VISIBLE

		this.show_name.text = show.showName
		this.show_name.visibility = View.VISIBLE

		if (nextEpisodeAirDate.isNullOrEmpty()) {
			this.show_next_episode_date.visibility = View.GONE
		} else {
			this.show_next_episode_date.text = this.getString(R.string.next_episode, DateTimeHelper.getRelativeDate(nextEpisodeAirDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS))
			this.show_next_episode_date.visibility = View.VISIBLE
		}

		this.show_network.text = this.getString(R.string.network, show.network)
		this.show_network.visibility = View.VISIBLE

		ImageLoader.load(this.show_poster, SickRageApi.instance.getPosterUrl(show.tvDbId, Indexer.TVDB), false, this, null)
		this.show_poster.contentDescription = show.showName

		val quality = show.quality

		if ("custom".equals(quality, true)) {
			val qualityDetails = show.qualityDetails
			val allowed = listToString(this.getTranslatedQualities(qualityDetails?.initial, true))
			val preferred = listToString(this.getTranslatedQualities(qualityDetails?.archive, false))

			this.show_quality.text = this.getString(R.string.quality_custom, allowed, preferred)
		} else {
			this.show_quality.text = this.getString(R.string.quality, quality)
		}

		this.show_quality.visibility = View.VISIBLE

		if (nextEpisodeAirDate.isNullOrEmpty()) {
			val status = show.getStatusTranslationResource()

			this.show_status.text = if (status != 0) this.getString(status) else show.status
			this.show_status.visibility = View.VISIBLE
		} else {
			this.show_status.visibility = View.GONE
		}

		this.show_subtitles.text = this.getString(R.string.subtitles_value, this.getString(if (show.subtitles == 0) R.string.no else R.string.yes))
		this.show_subtitles.visibility = View.VISIBLE
	}

	override fun onClick(view: View?) {
		if (view == null || !this.show.isLoaded) {
			return
		}

		val activity = this.activity
		var color = ContextCompat.getColor(activity, R.color.primary)
		val url = when (view.id) {
			R.id.show_imdb -> "http://www.imdb.com/title/${this.show.imdbId}"
			R.id.show_the_tvdb -> "http://thetvdb.com/?tab=series&id=${this.show.tvDbId}"
			R.id.show_web_search -> {
				val intent = Intent(Intent.ACTION_WEB_SEARCH)
				intent.putExtra(SearchManager.QUERY, this.show.showName)

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

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.show_overview, menu)

		this.pauseMenu = menu?.findItem(R.id.menu_pause_show)
		this.pauseMenu?.isVisible = false
		this.resumeMenu = menu?.findItem(R.id.menu_resume_show)
		this.resumeMenu?.isVisible = false
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater?.inflate(R.layout.fragment_show_overview, container, false)
	}

	override fun onDestroy() {
		val activity = this.activity

		if (activity is MainActivity) {
			activity.resetThemeColors()
		}

		if (this.serviceConnection != null) {
			this.context.unbindService(this.serviceConnection)
		}

		super.onDestroy()
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

		ViewCompat.setBackgroundTintList(this.show_imdb, colorStateList)
		this.show_imdb.setTextColor(textColor)

		ViewCompat.setBackgroundTintList(this.show_the_tvdb, colorStateList)
		this.show_the_tvdb.setTextColor(textColor)

		ViewCompat.setBackgroundTintList(this.show_web_search, colorStateList)
		this.show_web_search.setTextColor(textColor)
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

	override fun onRefresh() {
		this.swipe_refresh.isRefreshing = true

		val indexerId = this.arguments.getInt(Constants.Bundle.INDEXER_ID)

		SickRageApi.instance.services?.getShow(indexerId, this)
	}

	override fun onResume() {
		super.onResume()

		this.onRefresh()
	}

	override fun onStart() {
		super.onStart()

		this.realm = Realm.getDefaultInstance()
		this.show = this.realm.getShow(this.indexerId, this)
	}

	override fun onStop() {
		if (this.series?.isValid ?: false) {
			this.series?.removeAllChangeListeners()
		}

		if (this.show.isValid) {
			this.show.removeAllChangeListeners()
		}

		this.realm.close()

		super.onStop()
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		this.show_imdb.setOnClickListener(this)
		this.show_the_tvdb.setOnClickListener(this)
		this.show_web_search.setOnClickListener(this)

		this.swipe_refresh.setColorSchemeResources(R.color.accent)
		this.swipe_refresh.setOnRefreshListener(this)

		this.checkSupportWebSearch()
	}

	override fun success(singleShow: SingleShow?, response: Response?) {
		this.swipe_refresh.isRefreshing = false

		val show = singleShow?.data ?: return

		Realm.getDefaultInstance().let {
			it.saveShow(show)
			it.close()
		}
	}

	private fun changeQuality() {
		val arguments = Bundle()
		arguments.putInt(Constants.Bundle.INDEXER_ID, this.indexerId)

		val fragment = ChangeQualityFragment()
		fragment.arguments = arguments
		fragment.show(this.childFragmentManager, "change_quality")
	}

	private fun checkSupportWebSearch() {
		val webSearchIntent = Intent(Intent.ACTION_WEB_SEARCH)
		val manager = this.context.packageManager
		val activities = manager.queryIntentActivities(webSearchIntent, 0)

		this.show_web_search.visibility = if (activities.size > 0) View.VISIBLE else View.GONE
	}

	private fun deleteShow() {
		if (!this.show.isLoaded) {
			return
		}

		val indexerId = this.indexerId
		val callback = DeleteShowCallback(this.activity, indexerId)

		AlertDialog.Builder(this.context)
				.setTitle(this.getString(R.string.delete_show_title, this.show.showName))
				.setMessage(R.string.delete_show_message)
				.setPositiveButton(R.string.keep) { _, _ ->
					SickRageApi.instance.services?.deleteShow(indexerId, 0, callback)
				}
				.setNegativeButton(R.string.delete) { _, _ ->
					SickRageApi.instance.services?.deleteShow(indexerId, 1, callback)
				}
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
		this.showHidePauseResumeMenus(!pause)

		SickRageApi.instance.services?.pauseShow(this.indexerId, if (pause) 1 else 0, object : GenericCallback(this.activity) {
			override fun failure(error: RetrofitError?) {
				super.failure(error)

				showHidePauseResumeMenus(pause)
			}
		})
	}

	private fun rescanShow() {
		SickRageApi.instance.services?.rescanShow(this.indexerId, GenericCallback(this.activity))
	}

	private fun showHidePauseResumeMenus(isPause: Boolean) {
		this.pauseMenu?.isVisible = isPause
		this.resumeMenu?.isVisible = !isPause
	}

	private fun updateShow() {
		SickRageApi.instance.services?.updateShow(this.indexerId, GenericCallback(this.activity))
	}

	private class DeleteShowCallback(activity: FragmentActivity, val indexerId: Int) : GenericCallback(activity) {
		override fun success(genericResponse: GenericResponse?, response: Response?) {
			super.success(genericResponse, response)

			Realm.getDefaultInstance().let {
				it.deleteShow(this.indexerId)
				it.close()
			}

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
				Realm.getDefaultInstance().let {
					it.saveSerie(serie)
					it.close()
				}
			}
		}
	}

	private class ServiceConnection(fragment: ShowOverviewFragment) : CustomTabsServiceConnection() {
		private val fragmentReference = WeakReference(fragment)

		override fun onCustomTabsServiceConnected(componentName: ComponentName?, customTabsClient: CustomTabsClient?) {
			customTabsClient?.warmup(0L)

			val fragment = this.fragmentReference.get() ?: return
			fragment.tabSession = customTabsClient?.newSession(null)

			if (fragment.show.isValid) {
				fragment.tabSession?.mayLaunchUrl(Uri.parse("http://www.imdb.com/title/${fragment.show.imdbId}"), null, null)
				fragment.tabSession?.mayLaunchUrl(Uri.parse("http://thetvdb.com/?tab=series&id=${fragment.show.tvDbId}"), null, null)
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
