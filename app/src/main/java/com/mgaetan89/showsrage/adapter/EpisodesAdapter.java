package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.databinding.AdapterEpisodesListBinding;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.presenter.EpisodePresenter;

import java.util.Collections;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {
	@NonNull
	private List<Episode> episodes = Collections.emptyList();

	private final boolean reversed;

	private final int seasonNumber;

	private final Show show;

	public EpisodesAdapter(@Nullable List<Episode> episodes, int seasonNumber, Show show, boolean reversed) {
		if (episodes == null) {
			this.episodes = Collections.emptyList();
		} else {
			this.episodes = episodes;
		}

		this.reversed = reversed;
		this.seasonNumber = seasonNumber;
		this.show = show;
	}

	@Override
	public int getItemCount() {
		return this.episodes.size();
	}

	public boolean isReversed() {
		return this.reversed;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Episode episode = this.episodes.get(position);

		holder.bind(new EpisodePresenter(episode));

		if (holder.name != null) {
			holder.name.setText(holder.name.getResources().getString(R.string.episode_name, this.getEpisodeNumber(position), episode.getName()));
		}

		if (holder.status != null) {
			int status = episode.getStatusTranslationResource();
			String statusString = episode.getStatus();

			if (status != 0) {
				statusString = holder.status.getResources().getString(status);
			}

			holder.status.setText(statusString);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_episodes_list, parent, false);

		return new ViewHolder(view);
	}

	private int getEpisodeNumber(int position) {
		if (this.reversed) {
			return this.getItemCount() - position;
		}

		return position + 1;
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		@Nullable
		public final TextView name;

		@Nullable
		public final TextView status;

		@Nullable
		private final ImageView actions;

		private final AdapterEpisodesListBinding binding;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.binding = DataBindingUtil.bind(view);

			this.actions = this.binding.includeContent.episodeActions;
			this.name = this.binding.includeContent.episodeName;
			this.status = this.binding.includeContent.episodeStatus;

			if (this.actions != null) {
				this.actions.setOnClickListener(this);
			}
		}

		public void bind(EpisodePresenter episode) {
			this.binding.setEpisode(episode);
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();

			if (view.getId() == R.id.episode_actions) {
				PopupMenu popupMenu = new PopupMenu(context, this.actions);
				popupMenu.inflate(R.menu.episode_action);
				popupMenu.setOnMenuItemClickListener(this);
				popupMenu.show();
			} else {
				EpisodesAdapter adapter = EpisodesAdapter.this;
				Episode episode = adapter.episodes.get(this.getAdapterPosition());

				if (context != null) {
					Intent intent = new Intent(Constants.Intents.ACTION_EPISODE_SELECTED);
					intent.putExtra(Constants.Bundle.EPISODE_MODEL, episode);
					intent.putExtra(Constants.Bundle.EPISODE_NUMBER, adapter.getEpisodeNumber(this.getAdapterPosition()));
					intent.putExtra(Constants.Bundle.EPISODES_COUNT, adapter.getItemCount());
					intent.putExtra(Constants.Bundle.SEASON_NUMBER, adapter.seasonNumber);
					intent.putExtra(Constants.Bundle.SHOW_MODEL, adapter.show);

					LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
				}
			}
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			if (this.actions != null) {
				Context context = this.actions.getContext();

				if (context != null) {
					EpisodesAdapter adapter = EpisodesAdapter.this;
					Intent intent = new Intent(Constants.Intents.ACTION_EPISODE_ACTION_SELECTED);
					intent.putExtra(Constants.Bundle.EPISODE_NUMBER, adapter.getEpisodeNumber(this.getAdapterPosition()));
					intent.putExtra(Constants.Bundle.INDEXER_ID, adapter.show.getIndexerId());
					intent.putExtra(Constants.Bundle.MENU_ID, item.getItemId());
					intent.putExtra(Constants.Bundle.SEASON_NUMBER, adapter.seasonNumber);

					LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

					return true;
				}
			}

			return false;
		}
	}
}
