package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.helper.ImageLoader;
import com.mgaetan89.showsrage.model.ComingEpisode;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.Collections;
import java.util.List;

public class ComingEpisodesAdapter extends RecyclerView.Adapter<ComingEpisodesAdapter.ViewHolder> {
	@NonNull
	private List<ComingEpisode> comingEpisodes = Collections.emptyList();

	public interface OnEpisodeActionSelectedListener {
		void onEpisodeActionSelected(int seasonNumber, int episodeNumber, int indexerId, MenuItem action);
	}

	public ComingEpisodesAdapter(@Nullable List<ComingEpisode> comingEpisodes) {
		if (comingEpisodes == null) {
			this.comingEpisodes = Collections.emptyList();
		} else {
			this.comingEpisodes = comingEpisodes;
		}
	}

	@Override
	public int getItemCount() {
		return this.comingEpisodes.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		ComingEpisode comingEpisode = this.comingEpisodes.get(position);

		if (holder.date != null) {
			String airDate = comingEpisode.getAirDate();

			if (TextUtils.isEmpty(airDate)) {
				holder.date.setText(R.string.never);
			} else {
				holder.date.setText(DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS));
			}
		}

		if (holder.logo != null) {
			holder.logo.setContentDescription(comingEpisode.getShowName());

			ImageLoader.load(
					holder.logo,
					SickRageApi.getInstance().getPosterUrl(comingEpisode.getTvDbId(), Indexer.TVDB),
					true
			);
		}

		if (holder.name != null) {
			holder.name.setText(holder.name.getResources().getString(R.string.season_episode_name, comingEpisode.getSeason(), comingEpisode.getEpisode(), comingEpisode.getEpisodeName()));
		}

		if (holder.networkQuality != null) {
			holder.networkQuality.setText(holder.networkQuality.getResources().getString(R.string.separated_texts, comingEpisode.getNetwork(), comingEpisode.getQuality()));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_coming_episodes_list, parent, false);

		return new ViewHolder(view);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		@Nullable
		public final ImageView actions;

		@Nullable
		public final TextView date;

		@Nullable
		public final ImageView logo;

		@Nullable
		public final TextView name;

		@Nullable
		public final TextView networkQuality;

		public ViewHolder(View view) {
			super(view);

			this.actions = (ImageView) view.findViewById(R.id.episode_actions);
			this.date = (TextView) view.findViewById(R.id.episode_date);
			this.logo = (ImageView) view.findViewById(R.id.episode_logo);
			this.name = (TextView) view.findViewById(R.id.episode_name);
			this.networkQuality = (TextView) view.findViewById(R.id.episode_network_quality);

			if (this.actions != null) {
				this.actions.setOnClickListener(this);
			}
		}

		@Override
		public void onClick(View view) {
			PopupMenu popupMenu = new PopupMenu(view.getContext(), this.actions);
			popupMenu.inflate(R.menu.episode_action);
			popupMenu.setOnMenuItemClickListener(this);
			popupMenu.show();
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			if (this.actions != null) {
				Context context = this.actions.getContext();

				if (context instanceof OnEpisodeActionSelectedListener) {
					ComingEpisode comingEpisode = ComingEpisodesAdapter.this.comingEpisodes.get(this.getAdapterPosition());

					if (comingEpisode != null) {
						((OnEpisodeActionSelectedListener) context).onEpisodeActionSelected(comingEpisode.getSeason(), comingEpisode.getEpisode(), comingEpisode.getIndexerId(), item);
					}

					return true;
				}
			}

			return false;
		}
	}
}
