package com.mgaetan89.showsrage;

import android.app.Application;

import com.mgaetan89.showsrage.network.SickRageModule;
import com.squareup.leakcanary.LeakCanary;

import dagger.ObjectGraph;

public class ShowsRageApplication extends Application {
	private ObjectGraph graph = null;

	@Override
	public void onCreate() {
		super.onCreate();

		LeakCanary.install(this);

		this.graph = ObjectGraph.create(new SickRageModule(this));
	}

	public void inject(Object object) {
		this.graph.inject(object);
	}
}
