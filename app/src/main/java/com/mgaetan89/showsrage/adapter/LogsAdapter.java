package com.mgaetan89.showsrage.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.LogEntry;

import java.util.Collections;
import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {
	@NonNull
	private List<String> logs = Collections.emptyList();

	public LogsAdapter(@Nullable List<String> logs) {
		if (logs == null) {
			this.logs = Collections.emptyList();
		} else {
			this.logs = logs;
		}
	}

	@Override
	public int getItemCount() {
		return this.logs.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		LogEntry logEntry = new LogEntry(this.logs.get(position));

		if (holder.dateTime != null) {
			holder.dateTime.setText(DateTimeHelper.getRelativeDate(logEntry.getDateTime(), "yyyy-MM-dd hh:mm:ss", 0));
		}

		if (holder.errorType != null) {
			int errorTypeColor = logEntry.getErrorColor();

			holder.errorType.setText(logEntry.getErrorType());
			holder.errorType.setTextColor(ContextCompat.getColor(holder.errorType.getContext(), errorTypeColor));
		}

		if (holder.message != null) {
			holder.message.setText(logEntry.getMessage().trim());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_logs_list, parent, false);

		return new ViewHolder(view);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@Nullable
		public final TextView dateTime;

		@Nullable
		public final TextView errorType;

		@Nullable
		public final TextView message;

		public ViewHolder(View view) {
			super(view);

			this.dateTime = (TextView) view.findViewById(R.id.log_date_time);
			this.errorType = (TextView) view.findViewById(R.id.log_error_type);
			this.message = (TextView) view.findViewById(R.id.log_message);
		}
	}
}
