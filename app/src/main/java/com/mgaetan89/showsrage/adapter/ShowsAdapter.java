package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.databinding.AdapterShowsListBinding;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.presenter.ShowPresenter;

import java.util.Collections;
import java.util.List;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {
	@NonNull
	private List<Show> shows = Collections.emptyList();

	public interface OnShowSelectedListener {
		void onShowSelected(@NonNull Show show);
	}

	public ShowsAdapter(@Nullable List<Show> shows) {
		if (shows == null) {
			this.shows = Collections.emptyList();
		} else {
			this.shows = shows;
		}
	}

	@Override
	public int getItemCount() {
		return this.shows.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Show show = this.shows.get(position);

		holder.bind(new ShowPresenter(show));

		if (holder.nextEpisodeDate != null) {
			String nextEpisodeAirDate = show.getNextEpisodeAirDate();

			if (TextUtils.isEmpty(nextEpisodeAirDate)) {
				int status = show.getStatusTranslationResource();
				String statusString = show.getStatus();

				if (status != 0) {
					statusString = holder.nextEpisodeDate.getResources().getString(status);
				}

				holder.nextEpisodeDate.setText(statusString);
			} else {
				holder.nextEpisodeDate.setText(DateTimeHelper.getRelativeDate(nextEpisodeAirDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS));
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shows_list, parent, false);

		return new ViewHolder(view);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Nullable
		public final TextView nextEpisodeDate;

		private final AdapterShowsListBinding binding;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.binding = DataBindingUtil.bind(view);

			this.nextEpisodeDate = this.binding.includeContent.showNextEpisodeDate;
		}

		public void bind(ShowPresenter show) {
			this.binding.setShow(show);
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();
			Show show = ShowsAdapter.this.shows.get(this.getAdapterPosition());

			if (context instanceof OnShowSelectedListener && show != null) {
				((OnShowSelectedListener) context).onShowSelected(show);
			}
		}
	}
}
