package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.databinding.AdapterEpisodesListBinding
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.presenter.EpisodePresenter

class EpisodesAdapter(val episodes: List<Episode>, val seasonNumber: Int, val show: Show?, val reversed: Boolean) : RecyclerView.Adapter<EpisodesAdapter.ViewHolder>() {
    override fun getItemCount() = this.episodes.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val episode = this.episodes[position]

        holder?.bind(EpisodePresenter(episode))

        holder?.name?.text = holder?.name?.resources?.getString(R.string.episode_name, this.getEpisodeNumber(position), episode.name)

        if (holder?.status != null) {
            val status = episode.statusTranslationResource

            holder?.status.text = if (status != 0) {
                holder?.status.resources?.getString(status)
            } else {
                episode.status
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_episodes_list, parent, false)

        return ViewHolder(view)
    }

    fun getEpisodeNumber(position: Int): Int {
        if (this.reversed) {
            return this.itemCount - position
        }

        return position + 1
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        val name: TextView?
        val status: TextView?
        private val actions: ImageView?
        private val binding: AdapterEpisodesListBinding

        init {
            view.setOnClickListener(this)

            this.binding = DataBindingUtil.bind(view)

            this.actions = this.binding.includeContent.episodeActions
            this.name = this.binding.includeContent.episodeName
            this.status = this.binding.includeContent.episodeStatus

            this.actions?.setOnClickListener(this)
        }

        fun bind(episode: EpisodePresenter) {
            this.binding.setEpisode(episode)
        }

        override fun onClick(view: View?) {
            val context = view?.context ?: return

            if (view?.id == R.id.episode_actions) {
                val popupMenu = PopupMenu(context, this.actions)
                popupMenu.inflate(R.menu.episode_action)
                popupMenu.setOnMenuItemClickListener(this)
                popupMenu.show()
            } else {
                val episode = episodes[this.adapterPosition]
                val intent = Intent(Constants.Intents.ACTION_EPISODE_SELECTED)
                intent.putExtra(Constants.Bundle.EPISODE_MODEL, episode)
                intent.putExtra(Constants.Bundle.EPISODE_NUMBER, getEpisodeNumber(this.adapterPosition))
                intent.putExtra(Constants.Bundle.EPISODES_COUNT, itemCount)
                intent.putExtra(Constants.Bundle.SEASON_NUMBER, seasonNumber)
                intent.putExtra(Constants.Bundle.SHOW_MODEL, show)

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            val context = this.actions?.context ?: return false
            val intent = Intent(Constants.Intents.ACTION_EPISODE_ACTION_SELECTED)
            intent.putExtra(Constants.Bundle.EPISODE_NUMBER, getEpisodeNumber(this.adapterPosition))
            intent.putExtra(Constants.Bundle.INDEXER_ID, show?.indexerId)
            intent.putExtra(Constants.Bundle.MENU_ID, item?.itemId)
            intent.putExtra(Constants.Bundle.SEASON_NUMBER, seasonNumber)

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

            return true
        }
    }
}
