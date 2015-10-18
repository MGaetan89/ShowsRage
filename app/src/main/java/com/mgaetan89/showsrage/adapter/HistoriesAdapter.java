package com.mgaetan89.showsrage.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.databinding.AdapterHistoriesListBinding;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
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

		holder.bind(new HistoryPresenter(history));

		if (holder.date != null) {
			int status = history.getStatusTranslationResource();
			String statusString = history.getStatus();

			if (status != 0) {
				statusString = holder.date.getResources().getString(status);
			}

			holder.date.setText(holder.date.getResources().getString(R.string.spaced_texts, statusString, DateTimeHelper.getRelativeDate(history.getDate(), "yyyy-MM-dd hh:mm", 0).toString().toLowerCase()));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_histories_list, parent, false);

		return new ViewHolder(view);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@Nullable
		public final TextView date;

		private final AdapterHistoriesListBinding binding;

		public ViewHolder(View view) {
			super(view);

			this.binding = DataBindingUtil.bind(view);

			this.date = this.binding.includeContent.episodeDate;
		}

		public void bind(HistoryPresenter history) {
			this.binding.setHistory(history);
		}
	}
}
