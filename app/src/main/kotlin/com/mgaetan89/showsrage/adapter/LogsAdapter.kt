package com.mgaetan89.showsrage.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.presenter.LogPresenter
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import kotlinx.android.synthetic.main.adapter_logs_list.view.log_date_time
import kotlinx.android.synthetic.main.adapter_logs_list.view.log_error_type
import kotlinx.android.synthetic.main.adapter_logs_list.view.log_group
import kotlinx.android.synthetic.main.adapter_logs_list.view.log_message

class LogsAdapter(logs: RealmResults<LogEntry>) : RealmRecyclerViewAdapter<LogEntry, LogsAdapter.ViewHolder>(logs, true) {
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val logEntry = this.getItem(position)?.takeIf { it.isValid } ?: return

		holder.bind(logEntry)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_logs_list, parent, false)

		return ViewHolder(view)
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val dateTime = view.log_date_time
		private val errorType = view.log_error_type
		private val group = view.log_group
		private val message = view.log_message

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
