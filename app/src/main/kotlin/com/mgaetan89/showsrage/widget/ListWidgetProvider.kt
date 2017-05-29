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
import com.mgaetan89.showsrage.extension.changeLocale
import com.mgaetan89.showsrage.extension.getLocale
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.useDarkTheme
import com.mgaetan89.showsrage.network.SickRageApi

abstract class ListWidgetProvider : AppWidgetProvider() {
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
		val widgetLayout = if (preferences.useDarkTheme()) R.layout.widget_list_dark else R.layout.widget_list_light

		context?.resources?.changeLocale(preferences.getLocale())

		appWidgetIds?.forEach {
			this.refreshWidget(context, it)

			val views = RemoteViews(context?.packageName, widgetLayout)
			views.setEmptyView(R.id.list, R.id.empty)
			views.setImageViewResource(R.id.refresh, R.drawable.ic_refresh_white_24dp)
			views.setRemoteAdapter(R.id.list, this.getListAdapterIntent(context, it))
			views.setTextViewText(R.id.empty, this.getWidgetEmptyText(context))
			views.setTextViewText(R.id.title, this.getWidgetTitle(context))

			views.setOnClickPendingIntent(R.id.logo, this.getApplicationPendingIntent(context, it))
			views.setOnClickPendingIntent(R.id.refresh, this.getRefreshPendingIntent(context, it))
			views.setOnClickPendingIntent(R.id.title, this.getTitlePendingIntent(context, it))

			appWidgetManager?.updateAppWidget(it, views)
		}
	}

	internal abstract fun getListAdapterIntent(context: Context?, widgetId: Int): Intent

	protected abstract fun getTitlePendingIntent(context: Context?, widgetId: Int): PendingIntent

	internal abstract fun getWidgetEmptyText(context: Context?): String?

	internal abstract fun getWidgetTitle(context: Context?): String?

	private fun getApplicationPendingIntent(context: Context?, widgetId: Int): PendingIntent {
		val intent = Intent(context, MainActivity::class.java)
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

		return PendingIntent.getActivity(context, widgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
	}


	private fun getRefreshPendingIntent(context: Context?, widgetId: Int): PendingIntent {
		val intent = Intent(Constants.Intents.ACTION_REFRESH_WIDGET)
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)

		return PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
	}

	private fun refreshWidget(context: Context?, widgetId: Int) {
		AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(widgetId, R.id.list)
	}
}
