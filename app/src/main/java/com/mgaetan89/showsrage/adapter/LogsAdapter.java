package com.mgaetan89.showsrage.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.databinding.AdapterLogsListBinding;
import com.mgaetan89.showsrage.model.LogEntry;
import com.mgaetan89.showsrage.presenter.LogPresenter;

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

		holder.bind(new LogPresenter(logEntry));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_logs_list, parent, false);

		return new ViewHolder(view);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final AdapterLogsListBinding binding;

		public ViewHolder(View view) {
			super(view);

			this.binding = DataBindingUtil.bind(view);
		}

		public void bind(LogPresenter logEntry) {
			this.binding.setLog(logEntry);
		}
	}
}
