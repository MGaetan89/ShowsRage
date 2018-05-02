package com.mgaetan89.showsrage

import com.mgaetan89.showsrage.model.LogLevel
import java.util.Locale

object Constants {
	val SUPPORTED_LOCALES = listOf(Locale.ENGLISH, Locale.FRENCH)

	object Bundle {
		const val ANIME = "anime"
		const val EPISODE_ID = "episode_id"
		const val EPISODE_NUMBER = "episode_number"
		const val EPISODES_COUNT = "episodes_count"
		const val INDEXER_ID = "indexer_id"
		const val LOGS_GROUPS = "logs_groups"
		const val MENU_ID = "menu_id"
		const val SCHEDULE_SECTION = "schedule_section"
		const val SEARCH_QUERY = "search_query"
		const val SEASON_NUMBER = "season_number"
		const val UPDATE_MODEL = "update_model"
	}

	object Defaults {
		val LOG_LEVEL = LogLevel.ERROR
	}

	object Intents {
		const val ACTION_DISPLAY_HISTORY = BuildConfig.APPLICATION_ID + ".action.display_history"
		const val ACTION_DISPLAY_SCHEDULE = BuildConfig.APPLICATION_ID + ".action.display_schedule"
		const val ACTION_DISPLAY_SHOW = BuildConfig.APPLICATION_ID + ".action.display_show"
		const val ACTION_EPISODE_ACTION_SELECTED = BuildConfig.APPLICATION_ID + ".action.episode_action_selected"
		const val ACTION_EPISODE_SELECTED = BuildConfig.APPLICATION_ID + ".action.episode_selected"
		const val ACTION_FILTER_SHOWS = BuildConfig.APPLICATION_ID + ".action.filter_shows"
		const val ACTION_REFRESH_WIDGET = BuildConfig.APPLICATION_ID + ".action.refresh_widget"
		const val ACTION_SEARCH_RESULT_SELECTED = BuildConfig.APPLICATION_ID + ".action.search_result_selected"
		const val ACTION_SHOW_SELECTED = BuildConfig.APPLICATION_ID + ".action.show_selected"
	}

	enum class Payloads {
		SHOWS_STATS
	}
}
