package com.mgaetan89.showsrage.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {
	@NonNull
	private static final Pattern logPattern = Pattern.compile("^([0-9]{4}-[0-9]{2}-[0-9]{2}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2})\\s+([A-Z]+)\\s+(.*)$");

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
		String log = this.logs.get(position);
		Matcher matcher = logPattern.matcher(log);

		if (matcher.matches()) {
			String dateTime = matcher.group(1);
			String errorType = matcher.group(2);
			String message = matcher.group(3);

			if (holder.dateTime != null) {
				holder.dateTime.setText(DateTimeHelper.getRelativeDate(dateTime, "yyyy-MM-dd hh:mm:ss", 0));
			}

			if (holder.errorType != null) {
				int errorTypeColor = 0;

				switch (errorType.toLowerCase()) {
					case "debug":
						errorTypeColor = R.color.debug;

						break;

					case "error":
						errorTypeColor = R.color.error;

						break;

					case "info":
						errorTypeColor = R.color.info;

						break;

					case "warning":
						errorTypeColor = R.color.warning;

						break;
				}

				holder.errorType.setText(errorType);
				holder.errorType.setTextColor(holder.errorType.getResources().getColor(errorTypeColor));
			}

			if (holder.message != null) {
				holder.message.setText(message.trim());
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_logs_list, parent, false);

		return new ViewHolder(view);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@Nullable
		public TextView dateTime;

		@Nullable
		public TextView errorType;

		@Nullable
		public TextView message;

		public ViewHolder(View view) {
			super(view);

			this.dateTime = (TextView) view.findViewById(R.id.log_date_time);
			this.errorType = (TextView) view.findViewById(R.id.log_error_type);
			this.message = (TextView) view.findViewById(R.id.log_message);
		}
	}
}
