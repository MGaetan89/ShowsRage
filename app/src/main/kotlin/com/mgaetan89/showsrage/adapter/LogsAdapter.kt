package com.mgaetan89.showsrage.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.databinding.AdapterLogsListBinding
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.presenter.LogPresenter
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class LogsAdapter(context: Context, logs: RealmResults<LogEntry>) : RealmRecyclerViewAdapter<LogEntry, LogsAdapter.ViewHolder>(context, logs, true) {
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(LogPresenter(this.getItem(position)))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_logs_list, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: AdapterLogsListBinding

        init {
            this.binding = DataBindingUtil.bind(view)
        }

        fun bind(logEntry: LogPresenter) {
            this.binding.log = logEntry
        }
    }
}
