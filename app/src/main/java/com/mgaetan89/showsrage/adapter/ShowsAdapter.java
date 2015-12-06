package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.databinding.AdapterShowsListBinding;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.presenter.ShowPresenter;

import java.util.Collections;
import java.util.List;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {
	@LayoutRes
	private int itemLayoutResource = R.layout.adapter_shows_list_content_poster;

	@NonNull
	private List<Show> shows = Collections.emptyList();

	public ShowsAdapter(@Nullable List<Show> shows, int itemLayoutResource) {
		this.itemLayoutResource = itemLayoutResource;

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

			if (!this.binding.stub.isInflated()) {
				ViewStub viewStub = this.binding.stub.getViewStub();
				viewStub.setLayoutResource(ShowsAdapter.this.itemLayoutResource);
				viewStub.inflate();
			}

			this.nextEpisodeDate = (TextView) this.binding.stub.getRoot().findViewById(R.id.show_next_episode_date);
		}

		public void bind(ShowPresenter show) {
			this.binding.setShow(show);
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();

			if (context != null) {
				Show show = ShowsAdapter.this.shows.get(this.getAdapterPosition());
				Intent intent = new Intent(Constants.Intents.ACTION_SHOW_SELECTED);
				intent.putExtra(Constants.Bundle.SHOW_MODEL, show);

				LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
			}
		}
	}
}
