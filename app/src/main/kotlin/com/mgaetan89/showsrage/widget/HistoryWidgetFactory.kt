package com.mgaetan89.showsrage.widget

import android.content.Context
import android.preference.PreferenceManager
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.helper.Migration
import com.mgaetan89.showsrage.helper.toLocale
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import io.realm.RealmConfiguration

class HistoryWidgetFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private var itemLayout = R.layout.widget_adapter_histories_list_dark
    private var histories = mutableListOf<History>()
    private var loadingLayout = R.layout.widget_adapter_loading_dark

    init {
        SickRageApi.instance.init(PreferenceManager.getDefaultSharedPreferences(this.context))
    }

    override fun getCount() = this.histories.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getLoadingView() = RemoteViews(this.context.packageName, this.loadingLayout)

    override fun getViewAt(position: Int): RemoteViews {
        val history = this.histories[position]
        val logoUrl = SickRageApi.instance.getPosterUrl(history.tvDbId, Indexer.TVDB)

        val views = RemoteViews(this.context.packageName, this.itemLayout)
        views.setTextViewText(R.id.episode_date, this.getEpisodeDate(history))
        views.setContentDescription(R.id.episode_logo, history.showName ?: "")
        views.setTextViewText(R.id.episode_title, this.getEpisodeTitle(history))

        if (logoUrl.isEmpty()) {
            views.setViewVisibility(R.id.episode_logo, View.INVISIBLE)
        } else {
            ImageLoader.load(this.context, views, R.id.episode_logo, logoUrl, true)

            views.setViewVisibility(R.id.episode_logo, View.VISIBLE)
        }

        return views
    }

    override fun getViewTypeCount() = 1

    override fun hasStableIds() = true

    override fun onCreate() = Unit

    override fun onDataSetChanged() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)

        if (preferences.getBoolean("display_theme", true)) {
            this.itemLayout = R.layout.widget_adapter_histories_list_dark
            this.loadingLayout = R.layout.widget_adapter_loading_dark
        } else {
            this.itemLayout = R.layout.widget_adapter_histories_list_light
            this.loadingLayout = R.layout.widget_adapter_loading_light
        }

        SickRageApi.instance.services?.getHistory()?.data?.let {
            val histories = it.filterNotNull()
            val configuration = RealmConfiguration.Builder(this.context)
                    .schemaVersion(2)
                    .migration(Migration())
                    .build()

            Realm.getInstance(configuration).let {
                it.executeTransaction {
                    it.copyToRealmOrUpdate(histories)
                }
                it.close()
            }

            this.histories.clear()
            this.histories.addAll(histories)
        }
    }

    override fun onDestroy() = Unit

    private fun getEpisodeDate(history: History): String {
        val status = history.getStatusTranslationResource()
        val statusString = if (status != 0) {
            this.context.getString(status)
        } else {
            history.status
        }

        var text = this.context.getString(R.string.spaced_texts, statusString, DateTimeHelper.getRelativeDate(history.date, "yyyy-MM-dd hh:mm", 0)?.toString()?.toLowerCase())

        if ("subtitled".equals(history.status, true)) {
            val language = history.resource?.toLocale()?.displayLanguage

            if (!language.isNullOrEmpty()) {
                text += " [$language]"
            }
        }

        return text
    }

    private fun getEpisodeTitle(history: History): String {
        return this.context.getString(R.string.show_name_episode, history.showName ?: "", history.season, history.episode)
    }
}
