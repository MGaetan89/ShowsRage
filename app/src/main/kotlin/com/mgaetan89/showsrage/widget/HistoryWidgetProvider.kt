package com.mgaetan89.showsrage.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.extension.changeLocale
import com.mgaetan89.showsrage.extension.getLocale
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.useDarkTheme
import com.mgaetan89.showsrage.network.SickRageApi

class HistoryWidgetProvider : AppWidgetProvider() {
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

        context?.getPreferences()?.let {
            SickRageApi.instance.init(it)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val defaultWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
        val widgetId = intent?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, defaultWidgetId) ?: defaultWidgetId

        when (intent?.action) {
            Constants.Intents.ACTION_REFRESH_WIDGET -> this.refreshWidget(context, widgetId)
            else -> super.onReceive(context, intent)
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val preferences = context?.getPreferences()

        context?.resources?.changeLocale(preferences.getLocale())

        val widgetLayout = if (preferences.useDarkTheme()) {
            R.layout.widget_history_dark
        } else {
            R.layout.widget_history_light
        }

        appWidgetIds?.forEach {
            this.refreshWidget(context, it)

            val views = RemoteViews(context?.packageName, widgetLayout)
            views.setEmptyView(R.id.list, R.id.empty)
            views.setImageViewResource(R.id.refresh, R.drawable.ic_refresh_white_24dp)
            views.setRemoteAdapter(R.id.list, this.getListAdapterIntent(context, it))
            views.setTextViewText(R.id.title, context?.getString(R.string.history))

            views.setOnClickPendingIntent(R.id.logo, this.getApplicationPendingIntent(context))
            views.setOnClickPendingIntent(R.id.refresh, this.getRefreshPendingIntent(context, it))
            views.setOnClickPendingIntent(R.id.title, this.getHistoryPendingIntent(context))

            appWidgetManager?.updateAppWidget(it, views)
        }
    }

    private fun getApplicationPendingIntent(context: Context?): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    private fun getHistoryPendingIntent(context: Context?): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = Constants.Intents.ACTION_DISPLAY_HISTORY
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    private fun getListAdapterIntent(context: Context?, appWidgetId: Int): Intent {
        val intent = Intent(context, HistoryWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

        return intent
    }

    private fun getRefreshPendingIntent(context: Context?, widgetId: Int): PendingIntent {
        val intent = Intent(Constants.Intents.ACTION_REFRESH_WIDGET)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)

        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    private fun refreshWidget(context: Context?, widgetId: Int) {
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(widgetId, R.id.list)
    }
}
