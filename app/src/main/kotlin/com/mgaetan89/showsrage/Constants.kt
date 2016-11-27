package com.mgaetan89.showsrage

import retrofit.RestAdapter
import java.util.*

object Constants {
    val COLOR_DARK_FACTOR = 0.8f
    val DATABASE_VERSION = 4L
    val NETWORK_LOG_LEVEL = if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL else RestAdapter.LogLevel.NONE
    val OMDB_URL = "http://www.omdbapi.com/"
    val SUPPORTED_LOCALES: List<Locale> = listOf(Locale.ENGLISH, Locale.FRENCH)

    object Bundle {
        val ANIME = "anime"
        val EPISODE_ID = "episode_id"
        val EPISODE_NUMBER = "episode_number"
        val EPISODES_COUNT = "episodes_count"
        val INDEXER_ID = "indexer_id"
        val LOGS_GROUPS = "logs_groups"
        val MENU_ID = "menu_id"
        val SCHEDULE_SECTION = "schedule_section"
        val SEARCH_QUERY = "search_query"
        val SEASON_NUMBER = "season_number"
        val UPDATE_MODEL = "update_model"
    }

    object Event {
        val CAST_EPISODE_VIDEO = "cast_episode_video"
        val PLAY_EPISODE_VIDEO = "play_episode_video"
    }

    object Intents {
        val ACTION_DISPLAY_HISTORY = BuildConfig.APPLICATION_ID + ".action.display_history"
        val ACTION_DISPLAY_SHOW = BuildConfig.APPLICATION_ID + ".action.display_show"
        val ACTION_EPISODE_ACTION_SELECTED = BuildConfig.APPLICATION_ID + ".action.episode_action_selected"
        val ACTION_EPISODE_SELECTED = BuildConfig.APPLICATION_ID + ".action.episode_selected"
        val ACTION_FILTER_SHOWS = BuildConfig.APPLICATION_ID + ".action.filter_shows"
        val ACTION_REFRESH_WIDGET = BuildConfig.APPLICATION_ID + ".action.refresh_widget"
        val ACTION_SEARCH_RESULT_SELECTED = BuildConfig.APPLICATION_ID + ".action.search_result_selected"
        val ACTION_SHOW_SELECTED = BuildConfig.APPLICATION_ID + ".action.show_selected"
    }

    enum class Payloads {
        SHOWS_STATS
    }
}
