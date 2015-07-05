package com.mgaetan89.showsrage;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class ShowsRageApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		LeakCanary.install(this);
	}
}
