package com.mgaetan89.showsrage

import android.app.Application
import com.mgaetan89.showsrage.helper.Utils

class ShowsRageApplication : Application() {
	override fun onCreate() {
		super.onCreate()

		Utils.initRealm(this)
	}
}
