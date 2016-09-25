package com.mgaetan89.showsrage.widget

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.saveHistory
import com.mgaetan89.showsrage.extension.useDarkTheme
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.helper.toLocale
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.presenter.HistoryPresenter
import io.realm.Realm

class HistoryWidgetFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private var itemLayout = R.layout.widget_adapter_histories_list_dark
    private var histories = mutableListOf<History>()
    private var loadingLayout = R.layout.widget_adapter_loading_dark

    init {
        SickRageApi.instance.init(this.context.getPreferences())

        this.setLayoutFiles()
    }

    override fun getCount() = this.histories.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getLoadingView() = RemoteViews(this.context.packageName, this.loadingLayout)

    override fun getViewAt(position: Int): RemoteViews {
        val history = this.histories[position]
        val presenter = HistoryPresenter(history)
        val logoUrl = presenter.getPosterUrl()

        val views = RemoteViews(this.context.packageName, this.itemLayout)
        views.setTextViewText(R.id.episode_date, this.getEpisodeDate(history))
        views.setContentDescription(R.id.episode_logo, presenter.getShowName())
        views.setTextViewText(R.id.episode_title, this.getEpisodeTitle(presenter))

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
        this.setLayoutFiles()
        this.getHistory()
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

    private fun getEpisodeTitle(presenter: HistoryPresenter): String {
        return this.context.getString(R.string.show_name_episode, presenter.getShowName(), presenter.getSeason(), presenter.getEpisode())
    }

    private fun getHistory() {
        SickRageApi.instance.services?.getHistory()?.data?.let {
            val histories = it.filterNotNull()

            Realm.getDefaultInstance().let {
                it.saveHistory(histories)
                it.close()
            }

            this.histories.clear()
            this.histories.addAll(histories)
        }
    }

    private fun setLayoutFiles() {
        if (this.context.getPreferences().useDarkTheme()) {
            this.itemLayout = R.layout.widget_adapter_histories_list_dark
            this.loadingLayout = R.layout.widget_adapter_loading_dark
        } else {
            this.itemLayout = R.layout.widget_adapter_histories_list_light
            this.loadingLayout = R.layout.widget_adapter_loading_light
        }
    }
}
