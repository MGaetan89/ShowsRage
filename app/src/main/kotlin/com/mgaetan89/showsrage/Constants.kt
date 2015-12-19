package com.mgaetan89.showsrage

import com.mgaetan89.showsrage.model.LogLevel

import retrofit.RestAdapter

object Constants {
    val NETWORK_LOG_LEVEL: RestAdapter.LogLevel = if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL else RestAdapter.LogLevel.NONE
    val OMDB_URL = "http://www.omdbapi.com/"

    object Bundle {
        val COLOR_ACCENT = "color_accent"
        val COLOR_PRIMARY = "color_primary"
        val EPISODE_MODEL = "episode_model"
        val EPISODE_NUMBER = "episode_number"
        val EPISODES_COUNT = "episodes_count"
        val FILTER_MODE = "filter_mode"
        val FILTER_STATUS = "filter_status"
        val INDEXER_ID = "indexer_id"
        val MENU_ID = "menu_id"
        val SCHEDULES = "schedules"
        val SEARCH_QUERY = "search_query"
        val SEASON_NUMBER = "season_number"
        val SHOW_MODEL = "show_model"
        val SHOWS = "shows"
        val UPDATE_MODEL = "update_model"
    }

    object Intents {
        val ACTION_EPISODE_ACTION_SELECTED = BuildConfig.APPLICATION_ID + ".action.episode_action_selected"
        val ACTION_EPISODE_SELECTED = BuildConfig.APPLICATION_ID + ".action.episode_selected"
        val ACTION_FILTER_SHOWS = BuildConfig.APPLICATION_ID + ".action.filter_shows"
        val ACTION_SEARCH_RESULT_SELECTED = BuildConfig.APPLICATION_ID + ".action.search_result_selected"
        val ACTION_SHOW_SELECTED = BuildConfig.APPLICATION_ID + ".action.show_selected"
    }

    object Preferences {
        object Fields {
            val LAST_VERSION_CHECK_TIME = "last_version_check_time"
            val LOGS_LEVEL = "logs_level"
            val ROOT_DIRS = "root_dirs"
        }

        object Defaults {
            val LOGS_LEVEL = LogLevel.ERROR
        }
    }
}
