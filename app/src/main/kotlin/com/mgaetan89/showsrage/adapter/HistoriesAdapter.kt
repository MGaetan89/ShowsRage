package com.mgaetan89.showsrage.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.helper.toLocale
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.presenter.HistoryPresenter
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class HistoriesAdapter(histories: RealmResults<History>) : RealmRecyclerViewAdapter<History, HistoriesAdapter.ViewHolder>(histories, true) {
	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		val history = this.getItem(position).takeIf { it != null && it.isValid } ?: return

		holder?.bind(history)
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
		val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_histories_list, parent, false)

		return ViewHolder(view)
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val date = view.findViewById(R.id.episode_date) as TextView
		private val logo = view.findViewById(R.id.episode_logo) as ImageView
		private val name = view.findViewById(R.id.episode_name) as TextView
		private val providerQuality = view.findViewById(R.id.episode_provider_quality) as TextView

		init {
			this.name.isSelected = true
		}

		fun bind(history: History) {
			val context = this.itemView.context
			val presenter = HistoryPresenter(history)

			val status = history.getStatusTranslationResource()
			val statusString = if (status != 0) context.getString(status) else history.status

			this.date.text = context.getString(R.string.spaced_texts, statusString, DateTimeHelper.getRelativeDate(history.date, "yyyy-MM-dd hh:mm", 0)?.toString()?.toLowerCase())

			if ("subtitled".equals(history.status, true)) {
				val language = history.resource?.toLocale()?.displayLanguage

				if (!language.isNullOrEmpty()) {
					this.date.append(" [$language]")
				}
			}

			this.logo.contentDescription = presenter.getShowName()
			ImageLoader.load(this.logo, presenter.getPosterUrl(), true)

			this.name.text = context.getString(R.string.show_name_episode, presenter.getShowName(), presenter.getSeason(), presenter.getEpisode())
			this.providerQuality.text = presenter.getProviderQuality() ?: context.getString(R.string.provider_quality, presenter.getProvider(), presenter.getQuality())
		}
	}
}
