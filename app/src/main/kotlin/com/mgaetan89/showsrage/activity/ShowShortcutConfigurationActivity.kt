package com.mgaetan89.showsrage.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.adapter.ShowsAdapter
import com.mgaetan89.showsrage.extension.changeLocale
import com.mgaetan89.showsrage.extension.getLocale
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.extension.getShowsListLayout
import com.mgaetan89.showsrage.extension.ignoreArticles
import com.mgaetan89.showsrage.extension.useDarkTheme
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.presenter.ShowPresenter
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_shows_section.empty
import kotlinx.android.synthetic.main.fragment_shows_section.fastscroll
import kotlinx.android.synthetic.main.fragment_shows_section.list
import java.util.Comparator

class ShowShortcutConfigurationActivity : AppCompatActivity() {
	private lateinit var realm: Realm
	private val receiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent?) {
			if (Constants.Intents.ACTION_SHOW_SELECTED == intent?.action) {
				addShortcut(intent.getIntExtra(Constants.Bundle.INDEXER_ID, 0))
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.realm = Realm.getDefaultInstance()

		SickRageApi.instance.init(this.getPreferences())

		this.setResult(RESULT_CANCELED)

		this.setContentView(R.layout.activity_show_shortcut_configuration)

		if (savedInstanceState == null) {
			val preferences = this.getPreferences()

			// Set the correct language
			this.resources.changeLocale(preferences.getLocale())

			// Set the correct theme
			if (preferences.useDarkTheme()) {
				this.delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			} else {
				this.delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			}
		}

		LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver, IntentFilter(Constants.Intents.ACTION_SHOW_SELECTED))

		this.configureRecyclerView()
	}

	override fun onDestroy() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver)

		this.realm.close()

		super.onDestroy()
	}

	private fun addShortcut(indexerId: Int) {
		BitmapLoaderTask(this).execute(indexerId)
	}

	private fun configureRecyclerView() {
		val recyclerView = this.list
		val ignoreArticles = this.getPreferences().ignoreArticles()
		val showsListLayout = this.getPreferences().getShowsListLayout()
		val shows = (this.realm.getShows(null) ?: emptyList<Show>())
				.sortedWith(Comparator { first, second ->
					val firstProperty = Utils.getSortableShowName(first, ignoreArticles)
					val secondProperty = Utils.getSortableShowName(second, ignoreArticles)

					firstProperty.compareTo(secondProperty)
				})

		this.fastscroll.setRecyclerView(recyclerView)

		recyclerView.adapter = ShowsAdapter(shows, showsListLayout, ignoreArticles)
		recyclerView.layoutManager = LinearLayoutManager(this)
		recyclerView.setPadding(0, 0, 0, 0)

		if (shows.isEmpty()) {
			this.empty?.visibility = View.VISIBLE
			recyclerView.visibility = View.GONE
		} else {
			this.empty?.visibility = View.GONE
			recyclerView.visibility = View.VISIBLE
		}
	}

	private fun sendResult(show: Show?, icon: Bitmap) {
		show?.let {
			val shortcutIntent = Intent(this, MainActivity::class.java)
			shortcutIntent.action = Constants.Intents.ACTION_DISPLAY_SHOW
			shortcutIntent.putExtra(Constants.Bundle.INDEXER_ID, show.tvDbId)
			shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

			val addIntent = Intent(Intent.ACTION_CREATE_SHORTCUT)
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, show.showName)
			addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon)
			addIntent.putExtra("duplicate", false)

			this.setResult(RESULT_OK, addIntent)
		}

		this.finish()
	}

	private class BitmapLoaderTask(private val activity: ShowShortcutConfigurationActivity) : AsyncTask<Int, Void, Bitmap>() {
		private var indexerId = 0

		override fun doInBackground(vararg indexerIds: Int?): Bitmap {
			this.indexerId = indexerIds.first() ?: 0

			val realm = Realm.getDefaultInstance()
			val show = realm.getShow(this.indexerId)
			val url = ShowPresenter(show).getPosterUrl()
			val default = BitmapFactory.decodeResource(this.activity.resources, R.mipmap.ic_launcher)
			realm.close()

			val futureBitmap = ImageLoader.getBitmap(this.activity, url, true) ?: return default
			val bitmap = try {
				futureBitmap.get()
			} catch(exception: Exception) {
				default
			}

			Glide.with(this.activity.applicationContext).clear(futureBitmap)

			val size = this.activity.resources.getDimensionPixelSize(R.dimen.shortcut_icon_size)

			return Bitmap.createScaledBitmap(bitmap, size, size, true)
		}

		override fun onPostExecute(bitmap: Bitmap) {
			super.onPostExecute(bitmap)

			val show = this.activity.realm.getShow(this.indexerId)

			this.activity.sendResult(show, bitmap)
		}
	}
}
