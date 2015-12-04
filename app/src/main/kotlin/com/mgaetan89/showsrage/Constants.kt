package com.mgaetan89.showsrage

import com.mgaetan89.showsrage.model.LogLevel

import retrofit.RestAdapter

object Constants {
    val NETWORK_LOG_LEVEL = if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL else RestAdapter.LogLevel.NONE

    object Bundle {
        const val COLOR_ACCENT = "color_accent"
        const val COLOR_PRIMARY = "color_primary"
        const val EPISODE_MODEL = "episode_model"
        const val EPISODE_NUMBER = "episode_number"
        const val EPISODES_COUNT = "episodes_count"
        const val FILTER_MODE = "filter_mode"
        const val FILTER_STATUS = "filter_status"
        const val INDEXER_ID = "indexer_id"
        const val SCHEDULES = "schedules"
        const val SEARCH_QUERY = "search_query"
        const val SEASON_NUMBER = "season_number"
        const val SHOW_MODEL = "show_model"
        const val SHOWS = "shows"
        const val UPDATE_MODEL = "update_model"
    }

    object Intents {
        const val ACTION_FILTER_SHOWS = BuildConfig.APPLICATION_ID + ".action.filter_shows"
    }

    object Preferences {
        object Fields {
            const val LAST_VERSION_CHECK_TIME = "last_version_check_time"
            const val LOGS_LEVEL = "logs_level"
            const val ROOT_DIRS = "root_dirs"
        }

        object Defaults {
            val LOGS_LEVEL = LogLevel.ERROR
        }
    }
}
