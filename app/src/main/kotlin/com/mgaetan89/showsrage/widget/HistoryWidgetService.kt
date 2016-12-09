package com.mgaetan89.showsrage.widget

import android.content.Intent
import android.os.Binder
import android.widget.RemoteViewsService

class HistoryWidgetService : RemoteViewsService() {
    private val binder by lazy { ServiceBinder() }

    override fun onBind(intent: Intent?) = this.binder

    override fun onGetViewFactory(intent: Intent?) = HistoryWidgetFactory(this)

    inner class ServiceBinder : Binder() {
        val service: HistoryWidgetService
            get() = this@HistoryWidgetService
    }
}