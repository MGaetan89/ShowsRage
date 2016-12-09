package com.mgaetan89.showsrage.widget

import android.content.Intent
import android.os.Binder
import android.widget.RemoteViewsService

class ScheduleWidgetService : RemoteViewsService() {
    private val binder by lazy { ServiceBinder() }

    override fun onBind(intent: Intent?) = this.binder

    override fun onGetViewFactory(intent: Intent?) = ScheduleWidgetFactory(this)

    inner class ServiceBinder : Binder() {
        val service: ScheduleWidgetService
            get() = this@ScheduleWidgetService
    }
}
