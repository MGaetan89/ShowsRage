package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import com.mgaetan89.showsrage.model.Episode;

import java.util.Collections;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {
	@NonNull
	private List<Episode> episodes = Collections.emptyList();

	private final int seasonNumber;

	public interface OnEpisodeActionSelectedListener {
		void onEpisodeActionSelected(int seasonNumber, int episodeNumber, MenuItem action);
	}

	public interface OnEpisodeSelectedListener {
		void onEpisodeSelected(int seasonNumber, int episodeNumber, @NonNull Episode episode, int episodesCount);
	}

	public EpisodesAdapter(@Nullable List<Episode> episodes, int seasonNumber) {
		if (episodes == null) {
			this.episodes = Collections.emptyList();
		} else {
			this.episodes = episodes;
		}

		this.seasonNumber = seasonNumber;
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
			holder.name.setText(holder.name.getResources().getString(R.string.episode_name, position + 1, episode.getName()));
		}

		if (holder.quality != null) {
			String quality = episode.getQuality();

			if ("N/A".equalsIgnoreCase(quality)) {
				holder.quality.setText("");
			} else {
				holder.quality.setText(quality);
			}
		}

		if (holder.status != null) {
			int status = episode.getStatusTranslationResource();
			String statusString = episode.getStatus();

			if (status != 0) {
				statusString = holder.status.getResources().getString(status);
			}

			holder.status.setText(statusString);

			Drawable background = DrawableCompat.wrap(holder.status.getBackground());
			DrawableCompat.setTint(background, ContextCompat.getColor(holder.status.getContext(), episode.getStatusBackgroundColor()));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_episodes_list, parent, false);

		return new ViewHolder(view);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		@Nullable
		public final ImageView actions;

		@Nullable
		public final TextView date;

		@Nullable
		public final TextView name;

		@Nullable
		public final TextView quality;

		@Nullable
		public final TextView status;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.actions = (ImageView) view.findViewById(R.id.episode_actions);
			this.date = (TextView) view.findViewById(R.id.episode_date);
			this.name = (TextView) view.findViewById(R.id.episode_name);
			this.quality = (TextView) view.findViewById(R.id.episode_quality);
			this.status = (TextView) view.findViewById(R.id.episode_status);

			if (this.actions != null) {
				this.actions.setOnClickListener(this);
			}
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

				if (context instanceof OnEpisodeSelectedListener && episode != null) {
					int itemCount = adapter.getItemCount();

					((OnEpisodeSelectedListener) context).onEpisodeSelected(adapter.seasonNumber, this.getAdapterPosition() + 1, episode, itemCount);
				}
			}
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			if (this.actions != null) {
				Context context = this.actions.getContext();

				if (context instanceof OnEpisodeActionSelectedListener) {
					EpisodesAdapter adapter = EpisodesAdapter.this;

					((OnEpisodeActionSelectedListener) context).onEpisodeActionSelected(adapter.seasonNumber, this.getAdapterPosition() + 1, item);

					return true;
				}
			}

			return false;
		}
	}
}
