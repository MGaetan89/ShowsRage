package com.mgaetan89.showsrage.activity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.design.widget.AppBarLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.graphics.ColorUtils
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.ShowsRageApplication
import com.mgaetan89.showsrage.extension.changeLocale
import com.mgaetan89.showsrage.fragment.HistoryFragment
import com.mgaetan89.showsrage.fragment.LogsFragment
import com.mgaetan89.showsrage.fragment.PostProcessingFragment
import com.mgaetan89.showsrage.fragment.RemoteControlFragment
import com.mgaetan89.showsrage.fragment.ScheduleFragment
import com.mgaetan89.showsrage.fragment.SettingsAboutFragment
import com.mgaetan89.showsrage.fragment.SettingsAboutLicensesFragment
import com.mgaetan89.showsrage.fragment.SettingsAboutShowsRageFragment
import com.mgaetan89.showsrage.fragment.SettingsBehaviorFragment
import com.mgaetan89.showsrage.fragment.SettingsDisplayFragment
import com.mgaetan89.showsrage.fragment.SettingsExperimentalFeaturesFragment
import com.mgaetan89.showsrage.fragment.SettingsFragment
import com.mgaetan89.showsrage.fragment.SettingsServerApiKeyFragment
import com.mgaetan89.showsrage.fragment.SettingsServerFragment
import com.mgaetan89.showsrage.fragment.ShowsFragment
import com.mgaetan89.showsrage.fragment.StatisticsFragment
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.helper.ShowsArchitect
import com.mgaetan89.showsrage.helper.ShowsRageReceiver
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.model.OmDbEpisode
import com.mgaetan89.showsrage.model.Quality
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.RealmString
import com.mgaetan89.showsrage.model.RootDir
import com.mgaetan89.showsrage.model.RootDirs
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.model.Serie
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowsStat
import com.mgaetan89.showsrage.model.ThemeColors
import com.mgaetan89.showsrage.model.UpdateResponse
import com.mgaetan89.showsrage.model.UpdateResponseWrapper
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.view.ColoredToolbar
import io.kolumbus.Kolumbus
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), Callback<GenericResponse>, NavigationView.OnNavigationItemSelectedListener {
    var firebaseAnalytics: FirebaseAnalytics? = null
        private set
    private var appBarLayout: AppBarLayout? = null
    private var drawerHeader: LinearLayout? = null
    private var drawerLayout: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null
    private val receiver = ShowsRageReceiver(this)
    private var tabLayout: TabLayout? = null
    private var themeColors: ThemeColors? = null
    private var toolbar: ColoredToolbar? = null

    fun displayHomeAsUp(displayHomeAsUp: Boolean) {
        val actionBar = this.supportActionBar

        if (displayHomeAsUp) {
            this.drawerToggle?.isDrawerIndicatorEnabled = false
            actionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            actionBar?.setDisplayHomeAsUpEnabled(false)
            this.drawerToggle?.isDrawerIndicatorEnabled = true
        }
    }

    override fun failure(error: RetrofitError?) {
        error?.printStackTrace()
    }

    fun getThemColors() = this.themeColors

    override fun onBackPressed() {
        if (this.navigationView != null && this.drawerLayout?.isDrawerOpen(this.navigationView) ?: false) {
            this.drawerLayout?.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        this.drawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        var eventHandled = true
        var fragment: Fragment? = null
        val id = item?.itemId

        when (id) {
            R.id.menu_check_update -> {
                eventHandled = false

                this.checkForUpdate(true)
            }

            R.id.menu_history -> fragment = HistoryFragment()

            R.id.menu_kolumbus -> {
                eventHandled = false

                Kolumbus.explore(Episode::class.java)
                        .explore(History::class.java)
                        .explore(LogEntry::class.java)
                        .explore(OmDbEpisode::class.java)
                        .explore(Quality::class.java)
                        .explore(RealmShowStat::class.java)
                        .explore(RealmString::class.java)
                        .explore(RootDir::class.java)
                        .explore(Schedule::class.java)
                        .explore(Serie::class.java)
                        .explore(Show::class.java)
                        .explore(ShowsStat::class.java)
                        .withArchitect(ShowsArchitect())
                        .navigate(this)
            }

            R.id.menu_logs -> fragment = LogsFragment()

            R.id.menu_post_processing -> {
                eventHandled = false

                PostProcessingFragment().show(this.supportFragmentManager, "post_processing")
            }

            R.id.menu_remote_control -> {
                eventHandled = false

                RemoteControlFragment().show(this.supportFragmentManager, "remote_control")
            }

            R.id.menu_restart -> {
                eventHandled = false

                AlertDialog.Builder(this)
                        .setMessage(R.string.restart_confirm)
                        .setPositiveButton(R.string.restart, { dialog, which ->
                            SickRageApi.instance.services?.restart(this)
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()
            }

            R.id.menu_schedule -> fragment = ScheduleFragment()

            R.id.menu_settings -> fragment = SettingsFragment()

            R.id.menu_shows -> fragment = ShowsFragment()

            R.id.menu_statistics -> {
                eventHandled = false

                StatisticsFragment().show(this.supportFragmentManager, "statistics")
            }
        }

        if (this.navigationView != null) {
            this.drawerLayout?.closeDrawer(this.navigationView)
        }

        if (eventHandled) {
            item?.isChecked = true

            this.tabLayout?.removeAllTabs()
            this.tabLayout?.visibility = View.GONE
        }

        if (fragment != null) {
            this.resetThemeColors()

            this.toolbar?.menu?.clear()

            this.supportFragmentManager.beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit()
        }

        return eventHandled
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (this.drawerToggle?.onOptionsItemSelected(item) ?: false) {
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

        this.setThemeColors(ThemeColors(colorPrimary, colorAccent))
    }

    fun setThemeColors(colors: ThemeColors) {
        this.themeColors = colors

        val (colorPrimary, colorAccent) = this.themeColors!!
        val textColor = Utils.getContrastColor(colorPrimary)

        this.appBarLayout?.setBackgroundColor(colorPrimary)

        if (this.drawerHeader != null) {
            this.drawerHeader!!.setBackgroundColor(colorPrimary)

            val logo = this.drawerHeader!!.findViewById(R.id.app_logo) as ImageView?
            val name = this.drawerHeader!!.findViewById(R.id.app_name) as TextView?

            if (logo != null) {
                val drawable = DrawableCompat.wrap(logo.drawable)
                DrawableCompat.setTint(drawable, textColor)
            }

            name?.setTextColor(textColor)
        }

        if (this.navigationView != null) {
            val colorsIcon = intArrayOf(colorPrimary, this.navigationView!!.itemIconTintList?.defaultColor ?: Color.WHITE)
            val colorsText = intArrayOf(colorPrimary, this.navigationView!!.itemTextColor?.defaultColor ?: Color.WHITE)
            val states = arrayOf(
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf()
            )

            this.navigationView!!.itemIconTintList = ColorStateList(states, colorsIcon)
            this.navigationView!!.itemTextColor = ColorStateList(states, colorsText)
        }

        if (this.tabLayout != null) {
            val selectedTextColor = ColorUtils.setAlphaComponent(textColor, (0.7f * 255f).toInt())

            this.tabLayout!!.setSelectedTabIndicatorColor(colorAccent)
            this.tabLayout!!.setTabTextColors(selectedTextColor, textColor)
        }

        this.toolbar?.setItemColor(textColor)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val colorPrimaryDark = floatArrayOf(0f, 0f, 0f)
            ColorUtils.colorToHSL(colorPrimary, colorPrimaryDark)
            colorPrimaryDark[2] *= COLOR_DARK_FACTOR

            this.window.statusBarColor = ColorUtils.HSLToColor(colorPrimaryDark)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (textColor == Color.BLACK) {
                this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    override fun success(genericResponse: GenericResponse?, response: Response?) {
        if (genericResponse?.message?.isNotBlank() ?: false) {
            Toast.makeText(this, genericResponse!!.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateRemoteControlVisibility() {
        if (this.navigationView != null) {
            val hasRemotePlaybackClient = (this.application as ShowsRageApplication?)?.hasPlayingVideo() ?: false

            this.navigationView!!.menu.findItem(R.id.menu_remote_control).isVisible = hasRemotePlaybackClient
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setContentView(R.layout.activity_main)

        this.firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        RealmManager.init(this, null)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        if (savedInstanceState == null) {
            // Set the correct language
            val newLocale = SettingsFragment.getPreferredLocale(preferences.getString("display_language", ""))

            this.resources.changeLocale(newLocale)

            // Set the correct theme
            if (preferences.getBoolean("display_theme", true)) {
                this.delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                this.delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            this.recreate()
        }

        SickRageApi.instance.init(preferences)
        SickRageApi.instance.services?.getRootDirs(RootDirsCallback(this))

        this.appBarLayout = this.findViewById(R.id.app_bar) as AppBarLayout?
        this.drawerLayout = this.findViewById(R.id.drawer_layout) as DrawerLayout?
        this.navigationView = this.findViewById(R.id.drawer_content) as NavigationView?
        this.tabLayout = this.findViewById(R.id.tabs) as TabLayout?
        this.toolbar = this.findViewById(R.id.toolbar) as ColoredToolbar?

        if (this.drawerLayout != null) {
            this.drawerToggle = ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar, R.string.open_menu, R.string.close_menu)

            this.drawerLayout!!.addDrawerListener(this.drawerToggle!!)
            this.drawerLayout!!.post {
                drawerToggle?.syncState()
            }
        }

        if (this.navigationView != null) {
            this.drawerHeader = this.navigationView!!.inflateHeaderView(R.layout.drawer_header) as LinearLayout?

            this.navigationView!!.setNavigationItemSelectedListener(this)
        }

        this.setSupportActionBar(this.toolbar)

        this.displayStartFragment()
    }

    override fun onDestroy() {
        super.onDestroy()

        RealmManager.close()
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
    }

    private fun checkForUpdate(manualCheck: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val lastVersionCheckTime = preferences.getLong(Constants.Preferences.Fields.LAST_VERSION_CHECK_TIME, 0L)
        val checkInterval = preferences.getString("behavior_version_check", "0").toLong()

        if (shouldCheckForUpdate(checkInterval, manualCheck, lastVersionCheckTime)) {
            SickRageApi.instance.services?.checkForUpdate(CheckForUpdateCallback(this, manualCheck))
        }
    }

    private fun displayStartFragment() {
        // Start the correct Setting Fragment, if necessary
        val settingFragment = getSettingFragmentForPath(this.intent.data?.path)

        if (settingFragment != null) {
            this.supportFragmentManager.beginTransaction()
                    .replace(R.id.content, settingFragment)
                    .commit()

            return
        }

        // Display the list of shows
        this.navigationView?.menu?.performIdentifierAction(R.id.menu_shows, 0)
    }

    companion object {
        private const val COLOR_DARK_FACTOR = 0.8f

        fun getSettingFragmentForPath(path: String?): SettingsFragment? {
            return when (path) {
                "/" -> SettingsFragment()
                "/about" -> SettingsAboutFragment()
                "/about/licenses" -> SettingsAboutLicensesFragment()
                "/about/showsrage" -> SettingsAboutShowsRageFragment()
                "/behavior" -> SettingsBehaviorFragment()
                "/display" -> SettingsDisplayFragment()
                "/experimental_features" -> SettingsExperimentalFeaturesFragment()
                "/server" -> SettingsServerFragment()
                "/server/api_key" -> SettingsServerApiKeyFragment()
                else -> null
            }
        }

        fun shouldCheckForUpdate(checkInterval: Long, manualCheck: Boolean, lastCheckTime: Long): Boolean {
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
        private val activityReference: WeakReference<AppCompatActivity>

        init {
            this.activityReference = WeakReference(activity)
        }

        override fun failure(error: RetrofitError?) {
            // SickRage may not support this request
            // SickRage version 4.0.30 is required
            if (this.manualCheck) {
                val activity = this.activityReference.get()

                if (activity != null) {
                    Toast.makeText(activity, R.string.sickrage_4030_required, Toast.LENGTH_SHORT).show()
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

            with(PreferenceManager.getDefaultSharedPreferences(activity).edit()) {
                putLong(Constants.Preferences.Fields.LAST_VERSION_CHECK_TIME, System.currentTimeMillis())
                apply()
            }

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

            val pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = NotificationCompat.Builder(activity)
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

    private class RootDirsCallback(activity: AppCompatActivity) : Callback<RootDirs> {
        private val activityReference: WeakReference<AppCompatActivity>;

        init {
            this.activityReference = WeakReference(activity)
        }

        override fun failure(error: RetrofitError?) {
            error?.printStackTrace()
        }

        override fun success(rootDirs: RootDirs?, response: Response?) {
            RealmManager.saveRootDirs(rootDirs?.data ?: emptyList())
        }
    }
}
