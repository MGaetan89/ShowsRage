package com.mgaetan89.showsrage.widget

import android.content.Intent
import android.widget.RemoteViewsService

class HistoryWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?) = HistoryWidgetFactory(this)
}
