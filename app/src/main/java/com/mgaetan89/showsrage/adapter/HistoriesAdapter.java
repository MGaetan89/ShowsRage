package com.mgaetan89.showsrage.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.databinding.AdapterHistoriesListBinding;
import com.mgaetan89.showsrage.model.History;
import com.mgaetan89.showsrage.presenter.HistoryPresenter;

import java.util.Collections;
import java.util.List;

public class HistoriesAdapter extends RecyclerView.Adapter<HistoriesAdapter.ViewHolder> {
	@NonNull
	private List<History> histories = Collections.emptyList();

	public HistoriesAdapter(@Nullable List<History> histories) {
		if (histories == null) {
			this.histories = Collections.emptyList();
		} else {
			this.histories = histories;
		}
	}

	@Override
	public int getItemCount() {
		return this.histories.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		History history = this.histories.get(position);

		holder.binding.setHistory(new HistoryPresenter(history, holder.binding.getRoot().getContext()));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterHistoriesListBinding binding = AdapterHistoriesListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

		return new ViewHolder(binding);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@NonNull
		public final AdapterHistoriesListBinding binding;

		public ViewHolder(@NonNull AdapterHistoriesListBinding binding) {
			super(binding.getRoot());

			this.binding = binding;
		}
	}
}
