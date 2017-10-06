package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.presenter.ShowPresenter
import kotlinx.android.synthetic.main.adapter_shows_list.view.stub

class ShowsAdapter(val shows: List<Show>, val itemLayoutResource: Int, private val ignoreArticles: Boolean) : RecyclerView.Adapter<ShowsAdapter.ViewHolder>(), SectionTitleProvider {
	override fun getItemCount() = this.shows.size

	override fun getSectionTitle(position: Int): String {
		if (position !in (0 until this.shows.size)) {
			return ""
		}

		val showName = Utils.getSortableShowName(this.shows[position], this.ignoreArticles)

		return showName.firstOrNull()?.toUpperCase()?.toString() ?: ""
	}

	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		val show = this.shows[position].takeIf { it.isValid } ?: return

		holder?.bind(show)
	}

	override fun onBindViewHolder(holder: ViewHolder?, position: Int, payloads: MutableList<Any>?) {
		val localPayloads = payloads.orEmpty()
		if (localPayloads.contains(Constants.Payloads.SHOWS_STATS)) {
			val show = this.shows[position]

			holder?.bind(ShowPresenter(show).getShowStat())
		} else {
			this.onBindViewHolder(holder, position)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
		val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_shows_list, parent, false)

		return ViewHolder(view)
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
		private val logo: ImageView
		private val name: TextView?
		private val networkQuality: TextView?
		private val nextEpisodeAirDate: TextView?
		private val paused: View
		private val stats: ProgressBar

		init {
			view.stub?.let {
				it.layoutResource = itemLayoutResource
				it.inflate()
			}

			this.logo = view.findViewById(R.id.show_logo) as ImageView
			this.name = view.findViewById(R.id.show_name) as TextView?
			this.networkQuality = view.findViewById(R.id.show_network_quality) as TextView?
			this.nextEpisodeAirDate = view.findViewById(R.id.show_next_episode_date) as TextView?
			this.paused = view.findViewById(R.id.show_paused)
			this.stats = view.findViewById(R.id.show_stats) as ProgressBar

			this.name?.isSelected = true

			view.setOnClickListener(this)
		}

		fun bind(show: Show) {
			val context = this.itemView.context
			val presenter = ShowPresenter(show)

			this.logo.contentDescription = presenter.getShowName()

			when (itemLayoutResource) {
				R.layout.adapter_shows_list_content_banner -> ImageLoader.load(this.logo, presenter.getBannerUrl(), false)
				R.layout.adapter_shows_list_content_poster -> ImageLoader.load(this.logo, presenter.getPosterUrl(), true)
			}

			this.name?.let {
				it.text = presenter.getShowName()
			}

			this.networkQuality?.text = context.getString(R.string.separated_texts, presenter.getNetwork(), presenter.getQuality())

			this.nextEpisodeAirDate?.let {
				val nextEpisodeAirDate = show.nextEpisodeAirDate

				if (nextEpisodeAirDate.isEmpty()) {
					val status = show.getStatusTranslationResource()

					it.text = if (status != 0) context.getString(status) else show.status
				} else {
					it.text = DateTimeHelper.getRelativeDate(nextEpisodeAirDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)
				}
			}

			this.paused.visibility = if (presenter.isPaused()) View.VISIBLE else View.GONE

			this.bind(presenter.getShowStat())
		}

		fun bind(showStat: RealmShowStat?) {
			showStat?.let {
				this.stats.max = it.episodesCount
				this.stats.progress = it.downloaded
				this.stats.secondaryProgress = it.downloaded + it.snatched
			}
		}

		override fun onClick(view: View?) {
			this.notifyShowSelected()
		}

		private fun notifyShowSelected() {
			val show = shows.getOrNull(this.adapterPosition).takeIf { it != null && it.isValid } ?: return

			Intent(Constants.Intents.ACTION_SHOW_SELECTED).also {
				it.putExtra(Constants.Bundle.INDEXER_ID, show.indexerId)

				LocalBroadcastManager.getInstance(this.itemView.context).sendBroadcast(it)
			}
		}
	}
}
