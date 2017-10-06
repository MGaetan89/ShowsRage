package com.mgaetan89.showsrage.extension

import android.content.SharedPreferences
import android.support.annotation.LayoutRes
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.LogLevel
import com.mgaetan89.showsrage.model.ShowsFilters
import com.mgaetan89.showsrage.model.Sort
import java.util.Locale

fun SharedPreferences.getApiKey() = this.getString(Fields.API_KEY.field, "") ?: ""

fun SharedPreferences.getEpisodeSort() = if (this.getBoolean(Fields.EPISODE_SORT.field, false)) Sort.ASCENDING else Sort.DESCENDING

fun SharedPreferences?.getLanguage() = this?.getString(Fields.DISPLAY_LANGUAGE.field, "") ?: ""

fun SharedPreferences.getLastVersionCheckTime() = this.getLong(Fields.LAST_VERSION_CHECK_TIME.field, 0L)

fun SharedPreferences?.getLocale(): Locale {
	val default = Locale.getDefault()
	val language = this.getLanguage()

	return when (language) {
		"en" -> Locale.ENGLISH
		"fr" -> Locale.FRENCH
		else -> if (Constants.SUPPORTED_LOCALES.contains(default)) default else Locale.ENGLISH
	}
}

fun SharedPreferences.getLogLevel(): LogLevel {
	val default = Constants.Defaults.LOG_LEVEL
	val logsLevelString = this.getString(Fields.LOGS_LEVEL.field, default.name)

	return try {
		LogLevel.valueOf(logsLevelString)
	} catch (exception: IllegalArgumentException) {
		default
	}
}

fun SharedPreferences?.getLogsAutoUpdateInterval(): Int {
	val logsAutoUpdateInterval = this?.getString(Fields.LOGS_AUTO_UPDATE_INTERVAL.field, "0") ?: "0"

	return logsAutoUpdateInterval.toIntOrNull() ?: 0
}

fun SharedPreferences.getPortNumber() = this.getString(Fields.PORT_NUMBER.field, "") ?: ""

fun SharedPreferences.getSeasonSort() = if (this.getBoolean(Fields.SEASON_SORT.field, false)) Sort.ASCENDING else Sort.DESCENDING

fun SharedPreferences.getServerAddress() = this.getString(Fields.SERVER_ADDRESS.field, "") ?: ""

fun SharedPreferences.getServerPassword(): String? = this.getString(Fields.SERVER_PASSWORD.field, null)

fun SharedPreferences.getServerPath() = this.getString(Fields.SERVER_PATH.field, "")?.trim('/') ?: ""

fun SharedPreferences.getServerUsername(): String? = this.getString(Fields.SERVER_USERNAME.field, null)

fun SharedPreferences.getShowsFilterState(): ShowsFilters.State {
	val default = ShowsFilters.State.ALL
	val stateString = this.getString(Fields.SHOW_FILTER_STATE.field, default.name)

	return try {
		ShowsFilters.State.valueOf(stateString)
	} catch (exception: IllegalArgumentException) {
		default
	}
}

fun SharedPreferences.getShowsFilterStatus() = this.getInt(Fields.SHOW_FILTER_STATUS.field, ShowsFilters.Status.ALL.status)

@LayoutRes
fun SharedPreferences.getShowsListLayout(): Int {
	val preferredLayout = this.getString(Fields.SHOWS_LIST_LAYOUT.field, "poster")

	return when (preferredLayout) {
		"banner" -> R.layout.adapter_shows_list_content_banner
		else -> R.layout.adapter_shows_list_content_poster
	}
}

fun SharedPreferences.getVersionCheckInterval(): Long {
	val versionCheckInterval = this.getString(Fields.VERSION_CHECK_INTERVAL.field, "0")

	return versionCheckInterval.toLongOrNull() ?: 0L
}

fun SharedPreferences.ignoreArticles() = this.getBoolean(Fields.IGNORE_ARTICLES.field, false)

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

fun SharedPreferences.splitShowsAnimes() = this.getBoolean(Fields.SPLIT_SHOWS_ANIMES.field, false)

fun SharedPreferences.streamInChromecast() = this.getBoolean(Fields.STREAM_IN_CHROMECAST.field, false)

fun SharedPreferences.streamInVideoPlayer() = this.getBoolean(Fields.STREAM_IN_VIDEO_PLAYER.field, false)

fun SharedPreferences.useBasicAuth() = this.getBoolean(Fields.BASIC_AUTH.field, false)

fun SharedPreferences?.useDarkTheme() = this?.getBoolean(Fields.THEME.field, true) ?: true

fun SharedPreferences.useHttps() = this.getBoolean(Fields.HTTPS.field, false)

fun SharedPreferences?.useSelfSignedCertificate() = this?.getBoolean(Fields.SELF_SIGNED_CERTIFICATE.field, false) ?: false

enum class Fields(val field: String) {
	API_KEY("api_key"),
	BASIC_AUTH("basic_auth"),
	DISPLAY_LANGUAGE("display_language"),
	EPISODE_SORT("display_episodes_sort"),
	HTTPS("use_https"),
	IGNORE_ARTICLES("display_ignore_articles"),
	LAST_VERSION_CHECK_TIME("last_version_check_time"),
	LOGS_LEVEL("logs_level"),
	LOGS_AUTO_UPDATE_INTERVAL("behavior_logs_auto_update"),
	PORT_NUMBER("server_port_number"),
	SEASON_SORT("display_seasons_sort"),
	SELF_SIGNED_CERTIFICATE("self_signed_certificate"),
	SERVER_ADDRESS("server_address"),
	SERVER_PASSWORD("server_password"),
	SERVER_PATH("server_path"),
	SERVER_USERNAME("server_username"),
	SHOW_FILTER_STATE("show_filter_state"),
	SHOW_FILTER_STATUS("show_filter_status"),
	SHOWS_LIST_LAYOUT("display_shows_list_layout"),
	SPLIT_SHOWS_ANIMES("display_split_shows_animes"),
	STREAM_IN_CHROMECAST("stream_in_chromecast"),
	STREAM_IN_VIDEO_PLAYER("view_in_external_video_player"),
	THEME("display_theme"),
	VERSION_CHECK_INTERVAL("behavior_version_check")
}
