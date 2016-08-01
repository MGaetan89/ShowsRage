package com.mgaetan89.showsrage.extension

import android.content.SharedPreferences
import android.support.annotation.LayoutRes
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.LogLevel
import com.mgaetan89.showsrage.model.ShowsFilters
import com.mgaetan89.showsrage.model.Sort
import java.util.*

fun SharedPreferences.getApiKey(): String {
    return this.getString(Fields.API_KEY.field, "")
}

fun SharedPreferences.getEpisodeSort(): Sort {
    return if (this.getBoolean("display_episodes_sort", false)) Sort.ASCENDING else Sort.DESCENDING
}

fun SharedPreferences?.getLanguage(): String? {
    return this?.getString(Fields.DISPLAY_LANGUAGE.field, "")
}

fun SharedPreferences.getLastVersionCheckTime(): Long {
    return this.getLong(Fields.LAST_VERSION_CHECK_TIME.field, 0L)
}

fun SharedPreferences?.getLocale(): Locale {
    val language = this.getLanguage()

    return when (language) {
        "en" -> Locale.ENGLISH
        "fr" -> Locale.FRENCH
        else -> if (Constants.SUPPORTED_LOCALES.contains(Locale.getDefault())) {
            Locale.getDefault()
        } else {
            Locale.ENGLISH
        }
    }
}

fun SharedPreferences.getLogLevel(): LogLevel {
    val default = LogLevel.ERROR
    val logsLevelString = this.getString(Fields.LOGS_LEVEL.field, default.name)

    return try {
        LogLevel.valueOf(logsLevelString)
    } catch(exception: IllegalArgumentException) {
        default
    }
}

fun SharedPreferences.getPortNumber(): String {
    return this.getString("server_port_number", "")
}

fun SharedPreferences.getSeasonSort(): Sort {
    return if (this.getBoolean("display_seasons_sort", false)) Sort.ASCENDING else Sort.DESCENDING
}

fun SharedPreferences.getServerAddress(): String {
    return this.getString("server_address", "")
}

fun SharedPreferences.getServerPassword(): String {
    return this.getString(Fields.SERVER_PASSWORD.field, null)
}

fun SharedPreferences.getServerPath(): String {
    val path = this.getString("server_path", "")

    if (path.isNullOrEmpty()) {
        return ""
    }

    return path!!.replace("^/+|/$+".toRegex(), "")
}

fun SharedPreferences.getServerUsername(): String {
    return this.getString("server_username", null)
}

fun SharedPreferences.getShowsFilterState(): ShowsFilters.State {
    val default = ShowsFilters.State.ALL
    val stateString = this.getString(Fields.SHOW_FILTER_STATE.field, default.name)

    return try {
        ShowsFilters.State.valueOf(stateString)
    } catch(exception: IllegalArgumentException) {
        default
    }
}

fun SharedPreferences.getShowsFilterStatus(): Int {
    return this.getInt(Fields.SHOW_FILTER_STATUS.field, ShowsFilters.Status.ALL.status)
}

@LayoutRes
fun SharedPreferences.getShowsListLayout(): Int {
    val preferredLayout = this.getString("display_shows_list_layout", "poster")

    return when (preferredLayout) {
        "banner" -> R.layout.adapter_shows_list_content_banner
        else -> R.layout.adapter_shows_list_content_poster
    }
}

fun SharedPreferences.getVersionCheckInterval(): Long {
    return this.getString("behavior_version_check", "0").toLong()
}

fun SharedPreferences.ignoreArticles(): Boolean {
    return this.getBoolean("display_ignore_articles", false)
}

fun SharedPreferences.saveLastVersionCheckTime(lastVersionCheckTime: Long) {
    this.edit().putLong(Fields.LAST_VERSION_CHECK_TIME.field, lastVersionCheckTime).apply()
}

fun SharedPreferences.saveLogLevel(logLevel: LogLevel) {
    this.edit().putString(Fields.LOGS_LEVEL.field, logLevel.name).apply()
}

fun SharedPreferences.saveShowsFilter(state: ShowsFilters.State, status: Int) {
    this.edit()
            .putString(Fields.SHOW_FILTER_STATE.field, state.name)
            .putInt(Fields.SHOW_FILTER_STATUS.field, status)
            .apply()
}

fun SharedPreferences.splitShowsAnimes(): Boolean {
    return this.getBoolean("display_split_shows_animes", false)
}

fun SharedPreferences.streamInChromecast(): Boolean {
    return this.getBoolean("stream_in_chromecast", false)
}

fun SharedPreferences.streamInVideoPlayer(): Boolean {
    return this.getBoolean("view_in_external_video_player", false)
}

fun SharedPreferences.useBasicAuth(): Boolean {
    return this.getBoolean("basic_auth", false)
}

fun SharedPreferences?.useDarkTheme(): Boolean {
    return this?.getBoolean(Fields.DISPLAY_THEME.field, true) ?: true
}

fun SharedPreferences.useHttps(): Boolean {
    return this.getBoolean("use_https", false)
}

fun SharedPreferences?.useSelfSignedCertificate(): Boolean {
    return this?.getBoolean("self_signed_certificate", false) ?: false
}

enum class Fields(val field: String) {
    API_KEY("api_key"),
    DISPLAY_LANGUAGE("display_language"),
    DISPLAY_THEME("display_theme"),
    LAST_VERSION_CHECK_TIME("last_version_check_time"),
    LOGS_LEVEL("logs_level"),
    SERVER_PASSWORD("server_password"),
    SHOW_FILTER_STATE("show_filter_state"),
    SHOW_FILTER_STATUS("show_filter_status")
}
