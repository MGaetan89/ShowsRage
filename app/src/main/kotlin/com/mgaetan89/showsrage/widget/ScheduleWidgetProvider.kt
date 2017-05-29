package com.mgaetan89.showsrage.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity

class ScheduleWidgetProvider : ListWidgetProvider() {
	override fun getListAdapterIntent(context: Context?, widgetId: Int): Intent {
		if (context == null) {
			return Intent()
		}

		val intent = Intent(context, ScheduleWidgetService::class.java)
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
		intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

		return intent
	}

	override fun getTitlePendingIntent(context: Context?, widgetId: Int): PendingIntent {
		val intent = Intent(context, MainActivity::class.java)
		intent.action = Constants.Intents.ACTION_DISPLAY_SCHEDULE
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

		return PendingIntent.getActivity(context, widgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
	}

	override fun getWidgetEmptyText(context: Context?) = context?.getString(R.string.no_coming_episodes)

	override fun getWidgetTitle(context: Context?) = context?.getString(R.string.schedule)
}
