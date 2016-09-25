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
import com.mgaetan89.showsrage.extension.deleteShowWidgets
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.presenter.ShowPresenter
import io.realm.Realm

class ShowWidgetProvider : AppWidgetProvider() {
    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)

        if (appWidgetIds == null || appWidgetIds.isEmpty()) {
            return
        }

        Realm.getDefaultInstance().let {
            it.deleteShowWidgets(appWidgetIds.toTypedArray())
            it.close()
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

        context?.getPreferences()?.let {
            SickRageApi.instance.init(it)
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds?.forEach { appWidgetId ->
            val showWidget = RealmManager.getShowWidget(appWidgetId)

            showWidget?.let {
                val presenter = ShowPresenter(it.show)
                val views = RemoteViews(context?.packageName, R.layout.widget_show)
                views.setOnClickPendingIntent(R.id.widget, this.getWidgetPendingIntent(context, appWidgetId, presenter.getTvDbId()))
                views.setTextViewText(R.id.show_name, presenter.getShowName())

                context?.let {
                    ImageLoader.load(it, views, R.id.show_logo, presenter.getPosterUrl(), true, appWidgetId)
                }

                appWidgetManager?.updateAppWidget(appWidgetId, views)
            }
        }
    }

    private fun getWidgetPendingIntent(context: Context?, appWidgetId: Int, tvDbId: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Constants.Intents.ACTION_DISPLAY_SHOW
        intent.putExtra(Constants.Bundle.INDEXER_ID, tvDbId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        return PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }
}
