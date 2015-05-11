package com.mgaetan89.showsrage.network;

import android.content.Context;

import com.mgaetan89.showsrage.fragment.ComingEpisodesFragment;
import com.mgaetan89.showsrage.fragment.LogsFragment;
import com.mgaetan89.showsrage.fragment.SeasonFragment;
import com.mgaetan89.showsrage.fragment.ShowFragment;
import com.mgaetan89.showsrage.fragment.ShowOverviewFragment;
import com.mgaetan89.showsrage.fragment.ShowsFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
		complete = true,
		injects = {
				ComingEpisodesFragment.class,
				LogsFragment.class,
				SeasonFragment.class,
				ShowFragment.class,
				ShowOverviewFragment.class,
				ShowsFragment.class
		}
)
public class SickRageModule {
	private Context context = null;

	public SickRageModule(Context context) {
		this.context = context;
	}

	@Provides
	@Singleton
	public SickRageApi provideSickRageApi() {
		return new SickRageApi(this.context);
	}
}
