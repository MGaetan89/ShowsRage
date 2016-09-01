package com.mgaetan89.showsrage.extension

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent

fun AppWidgetManager.updateAllWidgets(context: Context, widgetProvider: Class<out AppWidgetProvider>) {
    val widgetIds = this.getAppWidgetIds(ComponentName(context, widgetProvider))
    val intent = Intent(context, widgetProvider)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)

    context.sendBroadcast(intent)
}
