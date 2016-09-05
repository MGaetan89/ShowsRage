package com.mgaetan89.showsrage.activity

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.futuremind.recyclerviewfastscroll.FastScroller
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.adapter.ShowsAdapter
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getShowsListLayout
import com.mgaetan89.showsrage.extension.updateAllWidgets
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowWidget
import com.mgaetan89.showsrage.widget.ShowWidgetProvider

class ShowWidgetConfigurationActivity : AppCompatActivity() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (Constants.Intents.ACTION_SHOW_SELECTED.equals(intent?.action)) {
                addWidget(intent?.getIntExtra(Constants.Bundle.INDEXER_ID, 0) ?: 0)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setResult(RESULT_CANCELED)

        this.setContentView(R.layout.activity_show_widget_configuration)

        RealmManager.init()

        LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver, IntentFilter(Constants.Intents.ACTION_SHOW_SELECTED))

        (this.findViewById(android.R.id.list) as RecyclerView?)?.let {
            val empty = this.findViewById(android.R.id.empty) as TextView?
            val shows = RealmManager.getShows(null, null) ?: emptyList<Show>()

            (this.findViewById(R.id.fastscroll) as FastScroller?)?.setRecyclerView(it)

            it.adapter = ShowsAdapter(shows, this.getPreferences().getShowsListLayout(), false)
            it.layoutManager = LinearLayoutManager(this)
            it.setPadding(0, 0, 0, 0)

            if (shows.isEmpty()) {
                empty?.visibility = View.VISIBLE
                it.visibility = View.GONE
            } else {
                empty?.visibility = View.GONE
                it.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver)

        super.onDestroy()
    }

    private fun addWidget(indexerId: Int) {
        val appWidgetId = this.intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        val show: Show? = RealmManager.getShow(indexerId, null)

        if (appWidgetId == null || appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID || show == null) {
            this.finish()

            return
        }

        this.saveShowWidget(appWidgetId, show)
        this.updateWidget()
        this.sendResult(appWidgetId)
        this.finish()
    }

    private fun saveShowWidget(appWidgetId: Int, show: Show) {
        val showWidget = ShowWidget()
        showWidget.widgetId = appWidgetId
        showWidget.show = show

        RealmManager.saveShowWidget(showWidget)
    }

    private fun sendResult(appWidgetId: Int) {
        val result = Intent()
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        this.setResult(RESULT_OK, result)
    }

    private fun updateWidget() {
        AppWidgetManager.getInstance(this).updateAllWidgets(this, ShowWidgetProvider::class.java)
    }
}
