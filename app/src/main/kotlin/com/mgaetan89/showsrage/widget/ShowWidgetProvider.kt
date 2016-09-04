package com.mgaetan89.showsrage.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.network.SickRageApi

class ShowWidgetProvider : AppWidgetProvider() {
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

        context?.getPreferences()?.let {
            SickRageApi.instance.init(it)
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds?.forEach { appWidgetId ->
            val views = RemoteViews(context?.packageName, R.layout.widget_show)
            views.setOnClickPendingIntent(R.id.widget, this.getWidgetPendingIntent(context))
            views.setTextViewText(R.id.show_name, "2 Broke Girls")

            context?.let {
                val imageUrl = SickRageApi.instance.getPosterUrl(248741, Indexer.TVDB)

                ImageLoader.load(it, views, R.id.show_logo, imageUrl, true, appWidgetId)
            }

            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }

    private fun getWidgetPendingIntent(context: Context?): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Constants.Intents.ACTION_DISPLAY_SHOW
        intent.putExtra(Constants.Bundle.INDEXER_ID, 248741)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }
}
