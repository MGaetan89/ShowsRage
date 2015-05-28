package com.mgaetan89.showsrage;

import android.app.Application;

public class ShowsRageApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		//LeakCanary.install(this);
	}
}
