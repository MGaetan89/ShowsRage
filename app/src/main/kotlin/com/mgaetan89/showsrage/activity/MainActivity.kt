package com.mgaetan89.showsrage.activity

import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.graphics.ColorUtils
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.ShowsRageApplication
import com.mgaetan89.showsrage.extension.changeLocale
import com.mgaetan89.showsrage.extension.getLastVersionCheckTime
import com.mgaetan89.showsrage.extension.getLocale
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getVersionCheckInterval
import com.mgaetan89.showsrage.extension.saveLastVersionCheckTime
import com.mgaetan89.showsrage.extension.saveRootDirs
import com.mgaetan89.showsrage.extension.updateAllWidgets
import com.mgaetan89.showsrage.extension.useDarkTheme
import com.mgaetan89.showsrage.fragment.HistoryFragment
import com.mgaetan89.showsrage.fragment.LogsFragment
import com.mgaetan89.showsrage.fragment.PostProcessingFragment
import com.mgaetan89.showsrage.fragment.RemoteControlFragment
import com.mgaetan89.showsrage.fragment.ScheduleFragment
import com.mgaetan89.showsrage.fragment.SettingsFragment
import com.mgaetan89.showsrage.fragment.ShowsFragment
import com.mgaetan89.showsrage.fragment.StatisticsFragment
import com.mgaetan89.showsrage.helper.ShowsRageReceiver
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.model.RootDirs
import com.mgaetan89.showsrage.model.ThemeColors
import com.mgaetan89.showsrage.model.UpdateResponse
import com.mgaetan89.showsrage.model.UpdateResponseWrapper
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.widget.HistoryWidgetProvider
import com.mgaetan89.showsrage.widget.ScheduleWidgetProvider
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.app_bar
import kotlinx.android.synthetic.main.activity_main.drawer_content
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.tabs
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.drawer_header.view.app_logo
import kotlinx.android.synthetic.main.drawer_header.view.app_name
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), Callback<GenericResponse>, NavigationView.OnNavigationItemSelectedListener {
	var firebaseAnalytics: FirebaseAnalytics? = null
		private set

	var themeColors: ThemeColors? = null
		set(value) {
			field = value ?: return

			val (colorPrimary, colorAccent) = value
			val textColor = Utils.getContrastColor(colorPrimary)

			this.app_bar?.setBackgroundColor(colorPrimary)

			this.drawerHeader?.let {
				it.setBackgroundColor(colorPrimary)

				val drawable = DrawableCompat.wrap(it.app_logo.drawable)
				DrawableCompat.setTint(drawable, textColor)

				it.app_name?.setTextColor(textColor)
			}

			val colorsIcon = intArrayOf(colorPrimary, this.drawer_content?.itemIconTintList?.defaultColor ?: Color.WHITE)
			val colorsText = intArrayOf(colorPrimary, this.drawer_content?.itemTextColor?.defaultColor ?: Color.WHITE)
			val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())

			this.drawer_content?.itemIconTintList = ColorStateList(states, colorsIcon)
			this.drawer_content?.itemTextColor = ColorStateList(states, colorsText)

			val selectedTextColor = ColorUtils.setAlphaComponent(textColor, (0.7f * 255f).toInt())

			this.tabs?.setSelectedTabIndicatorColor(colorAccent)
			this.tabs?.setTabTextColors(selectedTextColor, textColor)

			this.toolbar?.setItemColor(textColor)

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				val colorPrimaryDark = floatArrayOf(0f, 0f, 0f)
				ColorUtils.colorToHSL(colorPrimary, colorPrimaryDark)
				colorPrimaryDark[2] *= COLOR_DARK_FACTOR

				this.window.statusBarColor = ColorUtils.HSLToColor(colorPrimaryDark)

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (textColor == Color.BLACK) {
						this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
					}
				}
			}
		}

	private var drawerHeader: ConstraintLayout? = null
	private var drawerToggle: ActionBarDrawerToggle? = null
	private val receiver: ShowsRageReceiver by lazy { ShowsRageReceiver(this) }

	fun displayHomeAsUp(displayHomeAsUp: Boolean) {
		if (displayHomeAsUp) {
			this.drawerToggle?.isDrawerIndicatorEnabled = false
			this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
		} else {
			this.supportActionBar?.setDisplayHomeAsUpEnabled(false)
			this.drawerToggle?.isDrawerIndicatorEnabled = true
		}
	}

	override fun failure(error: RetrofitError?) {
		error?.printStackTrace()
	}

	override fun onBackPressed() {
		if (this.drawer_layout?.isDrawerOpen(this.drawer_content) == true) {
			this.drawer_layout?.closeDrawers()
		} else {
			super.onBackPressed()
		}
	}

	override fun onConfigurationChanged(newConfig: Configuration?) {
		super.onConfigurationChanged(newConfig)

		this.drawerToggle?.onConfigurationChanged(newConfig)
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		var backStackName: String? = null
		var eventHandled = true
		var fragment: Fragment? = null
		val id = item.itemId

		when (id) {
			R.id.menu_check_update -> {
				eventHandled = false

				this.checkForUpdate(true)
			}

			R.id.menu_history -> fragment = HistoryFragment.newInstance()

			R.id.menu_logs -> fragment = LogsFragment.newInstance()

			R.id.menu_post_processing -> {
				eventHandled = false

				PostProcessingFragment.newInstance()
						.show(this.supportFragmentManager, "post_processing")
			}

			R.id.menu_remote_control -> {
				eventHandled = false

				RemoteControlFragment.newInstance()
						.show(this.supportFragmentManager, "remote_control")
			}

			R.id.menu_restart -> {
				eventHandled = false

				AlertDialog.Builder(this)
						.setMessage(R.string.restart_confirm)
						.setPositiveButton(R.string.restart) { _, _ ->
							SickRageApi.instance.services?.restart(this)
						}
						.setNegativeButton(android.R.string.cancel, null)
						.show()
			}

			R.id.menu_schedule -> fragment = ScheduleFragment.newInstance()

			R.id.menu_settings -> {
				backStackName = "settings"
				fragment = SettingsFragment.newInstance()
			}

			R.id.menu_shows -> fragment = ShowsFragment.newInstance()

			R.id.menu_statistics -> {
				eventHandled = false

				StatisticsFragment.newInstance()
						.show(this.supportFragmentManager, "statistics")
			}
		}

		this.drawer_layout?.closeDrawer(this.drawer_content)

		if (eventHandled) {
			item.isChecked = true

			this.tabs?.removeAllTabs()
			this.tabs?.visibility = View.GONE
		}

		if (fragment != null) {
			this.resetThemeColors()

			this.toolbar?.menu?.clear()

			val fragmentTransaction = this.supportFragmentManager.beginTransaction()
			fragmentTransaction.replace(R.id.content, fragment)

			if (!backStackName.isNullOrEmpty()) {
				fragmentTransaction.addToBackStack(backStackName)
			}

			fragmentTransaction.commitAllowingStateLoss()
		}

		return eventHandled
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (this.drawerToggle?.onOptionsItemSelected(item) == true) {
			return true
		}

		if (item?.itemId == android.R.id.home) {
			this.onBackPressed()

			return true
		}

		return super.onOptionsItemSelected(item)
	}

	fun resetThemeColors() {
		val colorAccent = ContextCompat.getColor(this, R.color.accent)
		val colorPrimary = ContextCompat.getColor(this, R.color.primary)

		this.themeColors = ThemeColors(colorPrimary, colorAccent)
	}

	override fun success(genericResponse: GenericResponse?, response: Response?) {
		val message = genericResponse?.message ?: return
		if (message.isNotBlank()) {
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
		}
	}

	fun updateRemoteControlVisibility() {
		val application = this.application
		if (application is ShowsRageApplication) {
			this.drawer_content?.menu?.findItem(R.id.menu_remote_control)?.isVisible = application.hasPlayingVideo()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.setContentView(R.layout.activity_main)

		this.firebaseAnalytics = FirebaseAnalytics.getInstance(this)

		val preferences = this.getPreferences()

		if (savedInstanceState == null) {
			// Set the correct language
			this.resources.changeLocale(preferences.getLocale())

			// Set the correct theme
			if (preferences.useDarkTheme()) {
				this.delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			} else {
				this.delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			}
		}

		SickRageApi.instance.init(preferences)
		SickRageApi.instance.services?.getRootDirs(RootDirsCallback())

		// Refresh existing widgets
		AppWidgetManager.getInstance(this).let {
			it.updateAllWidgets(this, HistoryWidgetProvider::class.java)
			it.updateAllWidgets(this, ScheduleWidgetProvider::class.java)
		}

		this.drawerToggle = ActionBarDrawerToggle(this, this.drawer_layout, this.toolbar, R.string.open_menu, R.string.close_menu)

		this.drawer_layout?.addDrawerListener(this.drawerToggle!!)
		this.drawer_layout?.post {
			drawerToggle?.syncState()
		}

		this.drawerHeader = this.drawer_content?.inflateHeaderView(R.layout.drawer_header) as ConstraintLayout

		this.drawer_content?.setNavigationItemSelectedListener(this)

		this.setSupportActionBar(this.toolbar)

		if (savedInstanceState == null) {
			this.drawer_content?.menu?.performIdentifierAction(getInitialMenuId(this.intent?.action), 0)
		}
	}

	override fun onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver)

		super.onPause()
	}

	override fun onResume() {
		super.onResume()

		val intentFilter = IntentFilter()
		intentFilter.addAction(Constants.Intents.ACTION_EPISODE_ACTION_SELECTED)
		intentFilter.addAction(Constants.Intents.ACTION_EPISODE_SELECTED)
		intentFilter.addAction(Constants.Intents.ACTION_SEARCH_RESULT_SELECTED)
		intentFilter.addAction(Constants.Intents.ACTION_SHOW_SELECTED)

		LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver, intentFilter)

		this.updateRemoteControlVisibility()
		this.checkForUpdate(false)
		this.handleIntentAction()
	}

	private fun checkForUpdate(manualCheck: Boolean) {
		val preferences = this.getPreferences()
		val lastVersionCheckTime = preferences.getLastVersionCheckTime()
		val checkInterval = preferences.getVersionCheckInterval()

		if (shouldCheckForUpdate(checkInterval, manualCheck, lastVersionCheckTime)) {
			SickRageApi.instance.services?.checkForUpdate(CheckForUpdateCallback(this, manualCheck))
		}
	}

	private fun handleIntentAction() {
		when (this.intent.action) {
			Constants.Intents.ACTION_DISPLAY_SHOW -> {
				with(Intent(Constants.Intents.ACTION_SHOW_SELECTED)) {
					putExtra(Constants.Bundle.INDEXER_ID, intent.getIntExtra(Constants.Bundle.INDEXER_ID, 0))

					receiver.onReceive(this@MainActivity, this)
				}
			}
		}
	}

	companion object {
		private const val COLOR_DARK_FACTOR = 0.8f

		@IdRes
		internal fun getInitialMenuId(action: String?) = when (action) {
			Constants.Intents.ACTION_DISPLAY_HISTORY -> R.id.menu_history
			Constants.Intents.ACTION_DISPLAY_SCHEDULE -> R.id.menu_schedule
			else -> R.id.menu_shows
		}

		internal fun shouldCheckForUpdate(checkInterval: Long, manualCheck: Boolean, lastCheckTime: Long): Boolean {
			// Always check for new version if the user triggered the version check himself
			if (manualCheck) {
				return true
			}

			// The automatic version check is disabled
			if (checkInterval == 0L) {
				return false
			}

			// Check if we need to look for new update, depending on the user preferences
			return System.currentTimeMillis() - lastCheckTime >= checkInterval
		}
	}

	private class CheckForUpdateCallback(activity: AppCompatActivity, val manualCheck: Boolean) : Callback<UpdateResponseWrapper> {
		private val activityReference = WeakReference(activity)

		override fun failure(error: RetrofitError?) {
			// SickRage may not support this request
			// SickRage version 4.0.30 is required
			if (this.manualCheck) {
				this.activityReference.get()?.let {
					Toast.makeText(it, R.string.sickrage_4030_required, Toast.LENGTH_SHORT).show()
				}
			}

			error?.printStackTrace()
		}

		override fun success(updateResponseWrapper: UpdateResponseWrapper?, response: Response?) {
			this.handleCheckForUpdateResponse(updateResponseWrapper?.data, this.manualCheck)
		}

		private fun handleCheckForUpdateResponse(update: UpdateResponse?, manualCheck: Boolean) {
			val activity = this.activityReference.get() ?: return
			if (update == null) {
				return
			}

			activity.getPreferences().saveLastVersionCheckTime(System.currentTimeMillis())

			if (!update.needsUpdate) {
				if (manualCheck) {
					Toast.makeText(activity, R.string.no_update, Toast.LENGTH_SHORT).show()
				}

				return
			}

			if (manualCheck) {
				Toast.makeText(activity, R.string.new_update, Toast.LENGTH_SHORT).show()
			}

			val intent = Intent(activity, UpdateActivity::class.java)
			intent.putExtra(Constants.Bundle.UPDATE_MODEL, update)

			val channel = activity.getString(R.string.app_name)
			val pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

			val notification = NotificationCompat.Builder(activity, channel)
					.setAutoCancel(true)
					.setColor(ContextCompat.getColor(activity, R.color.primary))
					.setContentIntent(pendingIntent)
					.setContentTitle(activity.getString(R.string.app_name))
					.setContentText(activity.getString(R.string.update_available))
					.setLocalOnly(true)
					.setSmallIcon(R.drawable.ic_notification)
					.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
					.setStyle(NotificationCompat.BigTextStyle().bigText(
							activity.getString(R.string.update_available_detailed, update.currentVersion?.version, update.latestVersion?.version, update.commitsOffset)
					))
					.build()

			with(activity.getSystemService(NOTIFICATION_SERVICE) as NotificationManager) {
				notify(0, notification)
			}
		}
	}

	private class RootDirsCallback : Callback<RootDirs> {
		override fun failure(error: RetrofitError?) {
			error?.printStackTrace()
		}

		override fun success(rootDirs: RootDirs?, response: Response?) {
			Realm.getDefaultInstance().use {
				it.saveRootDirs(rootDirs?.data.orEmpty())
			}
		}
	}
}
