package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewCompat
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.inflate
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.presenter.EpisodePresenter
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import kotlinx.android.synthetic.main.adapter_episodes_list_content.view.episode_actions
import kotlinx.android.synthetic.main.adapter_episodes_list_content.view.episode_date
import kotlinx.android.synthetic.main.adapter_episodes_list_content.view.episode_name
import kotlinx.android.synthetic.main.adapter_episodes_list_content.view.episode_quality
import kotlinx.android.synthetic.main.adapter_episodes_list_content.view.episode_status

class EpisodesAdapter(episodes: RealmResults<Episode>, val seasonNumber: Int, val indexerId: Int, val reversed: Boolean) : RealmRecyclerViewAdapter<Episode, EpisodesAdapter.ViewHolder>(episodes, true) {
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val episode = this.getItem(position)?.takeIf { it.isValid } ?: return

		holder.bind(episode)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_episodes_list)

		return ViewHolder(view)
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		private val actions = view.episode_actions
		private val date = view.episode_date
		private val name = view.episode_name
		private val quality = view.episode_quality
		private val status = view.episode_status

		init {
			this.actions.setOnClickListener(this)
			this.name.isSelected = true

			view.setOnClickListener(this)
		}

		fun bind(episode: Episode) {
			val context = this.itemView.context
			val presenter = EpisodePresenter(episode)

			this.date.text = presenter.getAirDate() ?: context.getString(R.string.never)
			this.name.text = context.getString(R.string.episode_name, this.getEpisodeNumber(this.adapterPosition), episode.name)
			this.quality.text = presenter.getQuality()

			val backgroundColor = ContextCompat.getColor(context, presenter.getStatusColor())
			val status = episode.getStatusTranslationResource()
			ViewCompat.setBackgroundTintList(this.status, ColorStateList.valueOf(backgroundColor))
			this.status.text = if (status != 0) context.getString(status) else episode.status
		}

		override fun onClick(view: View) {
			when (view.id) {
				R.id.episode_actions -> this.showActions()
				else -> this.notifyEpisodeSelected()
			}
		}

		override fun onMenuItemClick(item: MenuItem): Boolean {
			this.notifyEpisodeActionSelected(item)

			return true
		}

		private fun getEpisodeNumber(position: Int) = if (reversed) itemCount - position else position + 1

		private fun notifyEpisodeActionSelected(item: MenuItem) {
			Intent(Constants.Intents.ACTION_EPISODE_ACTION_SELECTED).also {
				it.putExtra(Constants.Bundle.EPISODE_NUMBER, this.getEpisodeNumber(this.adapterPosition))
				it.putExtra(Constants.Bundle.INDEXER_ID, indexerId)
				it.putExtra(Constants.Bundle.MENU_ID, item.itemId)
				it.putExtra(Constants.Bundle.SEASON_NUMBER, seasonNumber)

				LocalBroadcastManager.getInstance(this.itemView.context).sendBroadcast(it)
			}
		}

		private fun notifyEpisodeSelected() {
			if (this.adapterPosition in (0 until itemCount)) {
				val episode = getItem(this.adapterPosition)?.takeIf { it.isValid } ?: return

				Intent(Constants.Intents.ACTION_EPISODE_SELECTED).also {
					it.putExtra(Constants.Bundle.EPISODE_ID, episode.id)
					it.putExtra(Constants.Bundle.EPISODE_NUMBER, this.getEpisodeNumber(this.adapterPosition))
					it.putExtra(Constants.Bundle.EPISODES_COUNT, itemCount)
					it.putExtra(Constants.Bundle.INDEXER_ID, indexerId)
					it.putExtra(Constants.Bundle.SEASON_NUMBER, seasonNumber)

					LocalBroadcastManager.getInstance(this.itemView.context).sendBroadcast(it)
				}
			}
		}

		private fun showActions() {
			PopupMenu(this.actions.context, this.actions).also {
				it.inflate(R.menu.episode_action)
				it.setOnMenuItemClickListener(this)
				it.show()
			}
		}
	}
}
