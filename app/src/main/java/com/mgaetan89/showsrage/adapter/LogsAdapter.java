package com.mgaetan89.showsrage.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.BR;
import com.mgaetan89.showsrage.databinding.AdapterLogsListBinding;
import com.mgaetan89.showsrage.model.LogEntry;
import com.mgaetan89.showsrage.presenter.LogEntryPresenter;

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
		LogEntryPresenter presenter = new LogEntryPresenter(logEntry, holder.binding.getRoot());

		holder.binding.setVariable(BR.log, presenter);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterLogsListBinding binding = AdapterLogsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

		return new ViewHolder(binding);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@NonNull
		public final AdapterLogsListBinding binding;

		public ViewHolder(@NonNull AdapterLogsListBinding binding) {
			super(binding.getRoot());

			this.binding = binding;
		}
	}
}
