package com.mgaetan89.showsrage.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.presenter.LogPresenter
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class LogsAdapter(logs: RealmResults<LogEntry>) : RealmRecyclerViewAdapter<LogEntry, LogsAdapter.ViewHolder>(logs, true) {
	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		val logEntry = this.getItem(position).takeIf { it != null && it.isValid } ?: return

		holder?.bind(logEntry)
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
		val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_logs_list, parent, false)

		return ViewHolder(view)
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val dateTime = view.findViewById(R.id.log_date_time) as TextView
		private val errorType = view.findViewById(R.id.log_error_type) as TextView
		private val group = view.findViewById(R.id.log_group) as TextView
		private val message = view.findViewById(R.id.log_message) as TextView

		init {
			this.group.isSelected = true
		}

		fun bind(logEntry: LogEntry) {
			val context = this.itemView.context
			val presenter = LogPresenter(logEntry)

			this.dateTime.text = presenter.getDateTime()
			this.errorType.text = presenter.getErrorType()
			this.errorType.setTextColor(ContextCompat.getColor(context, presenter.getErrorColor()))
			this.group.text = presenter.getGroup()
			this.message.text = presenter.getMessage()
		}
	}
}
