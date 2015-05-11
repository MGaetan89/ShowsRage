package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mgaetan89.showsrage.picasso.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {
	@Inject
	public SickRageApi api;

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

		if (holder.logo != null) {
			holder.logo.setContentDescription(show.getShowName());

			Picasso.with(holder.logo.getContext())//
					.load(this.api.getBaseUrl() + "?cmd=show.getposter&tvdbid=" + show.getTvDbId())//
					.transform(new CircleTransformation())//
					.into(holder.logo);
		}

		if (holder.name != null) {
			holder.name.setText(show.getShowName());
		}

		if (holder.networkQuality != null) {
			holder.networkQuality.setText(show.getNetwork() + " / " + show.getQuality());
		}

		if (holder.nextEpisodeDate != null) {
			String nextEpisodeAirDate = show.getNextEpisodeAirDate();

			if (TextUtils.isEmpty(nextEpisodeAirDate)) {
				holder.nextEpisodeDate.setText(show.getStatus());
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

	public void setApi(SickRageApi api) {
		this.api = api;
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Nullable
		public ImageView logo;

		@Nullable
		public TextView name;

		@Nullable
		public TextView networkQuality;

		@Nullable
		public TextView nextEpisodeDate;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.logo = (ImageView) view.findViewById(R.id.show_logo);
			this.name = (TextView) view.findViewById(R.id.show_name);
			this.networkQuality = (TextView) view.findViewById(R.id.show_network_quality);
			this.nextEpisodeDate = (TextView) view.findViewById(R.id.show_next_episode_date);
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();
			Show show = shows.get(this.getAdapterPosition());

			if (context instanceof OnShowSelectedListener && show != null) {
				((OnShowSelectedListener) context).onShowSelected(show);
			}
		}
	}
}
