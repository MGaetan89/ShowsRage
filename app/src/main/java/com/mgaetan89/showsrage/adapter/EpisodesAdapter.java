package com.mgaetan89.showsrage.adapter;

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
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Episode;

import java.util.Collections;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {
	@NonNull
	private List<Episode> episodes = Collections.emptyList();

	public EpisodesAdapter(@Nullable List<Episode> episodes) {
		if (episodes == null) {
			this.episodes = Collections.emptyList();
		} else {
			this.episodes = episodes;
		}
	}

	@Override
	public int getItemCount() {
		return this.episodes.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Episode episode = this.episodes.get(position);

		if (holder.date != null) {
			String airDate = episode.getAirDate();

			if (TextUtils.isEmpty(airDate)) {
				holder.date.setText(R.string.never);
			} else {
				holder.date.setText(DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS));
			}
		}

		if (holder.name != null) {
			holder.name.setText(holder.name.getResources().getString(R.string.episode_name, this.getItemCount() - position, episode.getName()));
		}

		if (holder.qualityOrStatus != null) {
			String quality = episode.getQuality();

			if ("N/A".equalsIgnoreCase(quality)) {
				holder.qualityOrStatus.setText(episode.getStatus());
			} else {
				holder.qualityOrStatus.setText(quality);
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_episodes_list, parent, false);

		return new ViewHolder(view);
	}


	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Nullable
		public TextView date;

		@Nullable
		public TextView name;

		@Nullable
		public TextView qualityOrStatus;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.date = (TextView) view.findViewById(R.id.episode_date);
			this.name = (TextView) view.findViewById(R.id.episode_name);
			this.qualityOrStatus = (TextView) view.findViewById(R.id.episode_quality_or_status);
		}

		@Override
		public void onClick(View view) {
			/*
			Context context = view.getContext();
			Show show = shows.get(this.getAdapterPosition());

			if (context != null && show != null) {
				Intent intent = new Intent(context, ShowActivity.class);
				intent.putExtra(Constants.Bundle.SHOW_MODEL, show);

				context.startActivity(intent);
			}
			*/
		}
	}
}
