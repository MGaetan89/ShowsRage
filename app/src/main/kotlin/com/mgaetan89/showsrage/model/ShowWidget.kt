package com.mgaetan89.showsrage.model

import android.appwidget.AppWidgetManager
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ShowWidget(
        @PrimaryKey open var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID,
        open var show: Show? = null
) : RealmObject() {
}
