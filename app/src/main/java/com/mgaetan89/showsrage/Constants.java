package com.mgaetan89.showsrage;

import com.mgaetan89.showsrage.model.LogLevel;

import retrofit.RestAdapter;

public abstract class Constants {
	public static final RestAdapter.LogLevel NETWORK_LOG_LEVEL = BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;
	public static final long VERSION_CHECK_INTERVAL = 4 * 60 * 60 * 1000;

	public abstract static class Bundle {
		public static final String COLOR_ACCENT = "color_accent";
		public static final String COLOR_PRIMARY = "color_primary";
		public static final String COMING_EPISODES = "coming_episodes";
		public static final String EPISODE_MODEL = "episode_model";
		public static final String EPISODE_NUMBER = "episode_number";
		public static final String EPISODES_COUNT = "episodes_count";
		public static final String INDEXER_ID = "indexer_id";
		public static final String SEASON_NUMBER = "season_number";
		public static final String SHOW_MODEL = "show_model";
		public static final String UPDATE_MODEL = "update_model";
	}

	public abstract static class Preferences {
		public abstract static class Fields {
			public static final String LAST_VERSION_CHECK_TIME = "last_version_check_time";
			public static final String LOGS_LEVEL = "logs_level";
		}

		public abstract static class Defaults {
			public static final LogLevel LOGS_LEVEL = LogLevel.ERROR;
		}
	}
}
