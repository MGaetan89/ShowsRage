package com.mgaetan89.showsrage.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.databinding.AdapterHistoriesListBinding
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.toLocale
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.presenter.HistoryPresenter

class HistoriesAdapter(val histories: List<History>) : RecyclerView.Adapter<HistoriesAdapter.ViewHolder>() {
    override fun getItemCount() = this.histories.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val history = this.histories[position]

        holder?.bind(HistoryPresenter(history))

        if (holder?.date != null) {
            val status = history.getStatusTranslationResource()
            val statusString = if (status != 0) {
                holder?.date.resources.getString(status)
            } else {
                history.status
            }

            holder?.date.text = holder?.date.resources.getString(R.string.spaced_texts, statusString, DateTimeHelper.getRelativeDate(history.date, "yyyy-MM-dd hh:mm", 0)?.toString()?.toLowerCase())

            if ("subtitled".equals(history.status, true)) {
                val language = history.resource?.toLocale()?.displayLanguage

                if (!language.isNullOrEmpty()) {
                    holder?.date.append(" [$language]")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_histories_list, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView?
        private val binding: AdapterHistoriesListBinding

        init {
            this.binding = DataBindingUtil.bind(view)
            this.date = this.binding.includeContent.episodeDate
        }

        fun bind(history: HistoryPresenter?) {
            this.binding.setHistory(history)
        }
    }
}
