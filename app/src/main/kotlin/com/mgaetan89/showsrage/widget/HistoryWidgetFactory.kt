package com.mgaetan89.showsrage.widget

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.saveHistory
import com.mgaetan89.showsrage.extension.toLocale
import com.mgaetan89.showsrage.extension.toRelativeDate
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.presenter.HistoryPresenter
import io.realm.Realm

class HistoryWidgetFactory(context: Context) : ListWidgetFactory<History>(context) {
	override fun getViewAt(position: Int): RemoteViews {
		val history = this.getItem(position)
		val presenter = HistoryPresenter(history)
		val logoUrl = presenter.getPosterUrl()

		val views = RemoteViews(this.context.packageName, this.itemLayout)
		views.setTextViewText(R.id.episode_date, if (history != null) this.getEpisodeDate(history) else "")
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

	override fun getItems(): List<History> {
		SickRageApi.instance.services?.getHistory()?.data?.let { histories ->
			Realm.getDefaultInstance().use {
				it.saveHistory(histories)
			}

			return histories
		}

		return emptyList()
	}

	private fun getEpisodeDate(history: History): String {
		val status = history.getStatusTranslationResource()
		val statusString = if (status != 0) {
			this.context.getString(status)
		} else {
			history.status
		}

		var text = this.context.getString(R.string.spaced_texts, statusString, history.date.toRelativeDate("yyyy-MM-dd hh:mm", 0).toString().toLowerCase())

		if ("subtitled".equals(history.status, true)) {
			val language = history.resource?.toLocale()?.displayLanguage

			if (!language.isNullOrEmpty()) {
				text += " [$language]"
			}
		}

		return text
	}

	internal fun getEpisodeTitle(presenter: HistoryPresenter)
			= this.context.getString(R.string.show_name_episode, presenter.getShowName(), presenter.getSeason(), presenter.getEpisode())
}
