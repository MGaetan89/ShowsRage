package com.mgaetan89.showsrage;

import com.mgaetan89.showsrage.model.LogLevel;

public abstract class Constants {
	public abstract static class Bundle {
		public static final String EPISODE_MODEL = "episode_model";
		public static final String EPISODE_NUMBER = "episode_number";
		public static final String SEASON_NUMBER = "season_number";
		public static final String SHOW_MODEL = "show_model";
	}

	public abstract static class Preferences {
		public abstract static class Fields {
			public static final String LOGS_LEVEL = "logs_level";
		}

		public abstract static class Defaults {
			public static final LogLevel LOGS_LEVEL = LogLevel.ERROR;
		}
	}
}
