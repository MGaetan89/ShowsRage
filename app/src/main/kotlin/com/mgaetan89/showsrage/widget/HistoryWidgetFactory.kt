package com.mgaetan89.showsrage.widget

import android.content.Context
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
import io.realm.Sort

class HistoryWidgetFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private var histories: List<History>? = null

    override fun getCount() = this.histories?.size ?: 0

    override fun getItemId(position: Int) = position.toLong()

    override fun getLoadingView() = RemoteViews(this.context.packageName, R.layout.widget_adapter_loading)

    override fun getViewAt(position: Int): RemoteViews {
        val history = this.histories?.get(position)
        val logoUrl = if (history != null) SickRageApi.instance.getPosterUrl(history.tvDbId, Indexer.TVDB) else ""

        val views = RemoteViews(this.context.packageName, R.layout.widget_adapter_histories_list)
        views.setTextViewText(R.id.episode_date, this.getEpisodeDate(history))
        views.setContentDescription(R.id.episode_logo, history?.showName ?: "")
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
        val configuration = RealmConfiguration.Builder(this.context).let {
            it.schemaVersion(2)
            it.migration(Migration())
            it.build()
        }

        Realm.getInstance(configuration).let {
            this.histories = it.copyFromRealm(it.where(History::class.java).findAllSorted("date", Sort.DESCENDING))

            it.close()
        }
    }

    override fun onDestroy() = Unit

    private fun getEpisodeDate(history: History?): String {
        if (history == null) {
            return ""
        }

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

    private fun getEpisodeTitle(history: History?): String {
        return this.context.getString(R.string.show_name_episode, history?.showName ?: "", history?.season ?: 0, history?.episode ?: 0)
    }
}
