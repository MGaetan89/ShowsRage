package com.mgaetan89.showsrage.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.inflate
import com.mgaetan89.showsrage.extension.toLocale
import com.mgaetan89.showsrage.extension.toRelativeDate
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.presenter.HistoryPresenter
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import kotlinx.android.synthetic.main.adapter_histories_list_content.view.episode_date
import kotlinx.android.synthetic.main.adapter_histories_list_content.view.episode_logo
import kotlinx.android.synthetic.main.adapter_histories_list_content.view.episode_name
import kotlinx.android.synthetic.main.adapter_histories_list_content.view.episode_provider_quality

class HistoriesAdapter(histories: RealmResults<History>) : RealmRecyclerViewAdapter<History, HistoriesAdapter.ViewHolder>(histories, true) {
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val history = this.getItem(position)?.takeIf { it.isValid } ?: return

		holder.bind(history)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_histories_list)

		return ViewHolder(view)
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val date = view.episode_date
		private val logo = view.episode_logo
		private val name = view.episode_name
		private val providerQuality = view.episode_provider_quality

		init {
			this.name.isSelected = true
		}

		fun bind(history: History) {
			val context = this.itemView.context
			val presenter = HistoryPresenter(history)

			val status = history.getStatusTranslationResource()
			val statusString = if (status != 0) context.getString(status) else history.status

			this.date.text = context.getString(R.string.spaced_texts, statusString, history.date.toRelativeDate("yyyy-MM-dd hh:mm", 0).toString().toLowerCase())

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
