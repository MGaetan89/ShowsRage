package com.mgaetan89.showsrage

import com.mgaetan89.showsrage.model.LogLevel
import com.mgaetan89.showsrage.model.ShowsFilters
import retrofit.RestAdapter

object Constants {
    val NETWORK_LOG_LEVEL = if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL else RestAdapter.LogLevel.NONE
    val OMDB_URL = "http://www.omdbapi.com/"

    object Bundle {
        val ANIME = "anime"
        val EPISODE_ID = "episode_id"
        val EPISODE_NUMBER = "episode_number"
        val EPISODES_COUNT = "episodes_count"
        val INDEXER_ID = "indexer_id"
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
            val SHOW_FILTER_STATE = "show_filter_state"
            val SHOW_FILTER_STATUS = "show_filter_status"
        }

        object Defaults {
            val LOGS_LEVEL = LogLevel.ERROR
            val SHOW_FILTER_STATE = ShowsFilters.State.ALL.name
            val SHOW_FILTER_STATUS = ShowsFilters.Status.ALL.status
        }
    }
}
