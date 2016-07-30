package com.mgaetan89.showsrage.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import android.widget.RemoteViews
import com.mgaetan89.showsrage.BuildConfig
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.model.Histories
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class HistoryWidgetProvider : AppWidgetProvider(), Callback<Histories> {
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

        SickRageApi.instance.init(PreferenceManager.getDefaultSharedPreferences(context))
    }

    override fun failure(error: RetrofitError?) {
        error?.printStackTrace()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            INTENT_REFRESH -> this.requestData()
            else -> super.onReceive(context, intent)
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val widgetLayout = if (preferences.getBoolean("display_theme", true)) {
            R.layout.widget_history_dark
        } else {
            R.layout.widget_history_light
        }

        this.requestData()

        appWidgetIds?.forEach {
            val views = RemoteViews(context?.packageName, widgetLayout)
            views.setEmptyView(R.id.list, R.id.empty)
            views.setImageViewResource(R.id.refresh, R.drawable.ic_refresh_white_24dp)
            views.setRemoteAdapter(R.id.list, this.getListAdapterIntent(context, it))

            views.setOnClickPendingIntent(R.id.logo, this.getApplicationPendingIntent(context))
            views.setOnClickPendingIntent(R.id.refresh, this.getRefreshPendingIntent(context))
            views.setOnClickPendingIntent(R.id.title, this.getHistoryPendingIntent(context))

            appWidgetManager?.updateAppWidget(it, views)
        }
    }

    override fun success(histories: Histories?, response: Response?) {
        RealmManager.saveHistory(histories?.data ?: emptyList())

        // TODO Notify widget
    }

    private fun getApplicationPendingIntent(context: Context?): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)

        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun getHistoryPendingIntent(context: Context?): PendingIntent {
        // TODO Open the application in the History section
        val intent = Intent(context, MainActivity::class.java)

        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun getListAdapterIntent(context: Context?, appWidgetId: Int): Intent {
        val intent = Intent(context, HistoryWidgetService::class.java)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        return intent
    }

    private fun getRefreshPendingIntent(context: Context?): PendingIntent {
        val intent = Intent(INTENT_REFRESH)

        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    private fun requestData() {
        SickRageApi.instance.services?.getHistory(this)
    }

    companion object {
        private val INTENT_REFRESH = BuildConfig.APPLICATION_ID + ".widget.REFRESH"
    }
}
