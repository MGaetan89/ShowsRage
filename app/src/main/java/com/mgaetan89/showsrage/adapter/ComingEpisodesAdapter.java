package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
import com.mgaetan89.showsrage.model.ComingEpisode;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mgaetan89.showsrage.picasso.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ComingEpisodesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int ITEM_TYPE_EPISODE = 0;
	private static final int ITEM_TYPE_SECTION = 1;
	private static final int ITEM_TYPE_UNKNOWN = 2;

	@NonNull
	private Map<String, List<ComingEpisode>> comingEpisodes = Collections.emptyMap();

	public interface OnEpisodeActionSelectedListener {
		void onEpisodeActionSelected(int seasonNumber, int episodeNumber, int indexerId, MenuItem action);
	}

	public ComingEpisodesAdapter(@Nullable Map<String, List<ComingEpisode>> comingEpisodes) {
		if (comingEpisodes == null) {
			this.comingEpisodes = Collections.emptyMap();
		} else {
			this.comingEpisodes = comingEpisodes;
		}
	}

	@Override
	public int getItemCount() {
		int itemCount = this.comingEpisodes.size();

		for (List<ComingEpisode> episodes : this.comingEpisodes.values()) {
			if (episodes != null) {
				itemCount += episodes.size();
			}
		}

		return itemCount;
	}

	@Override
	public int getItemViewType(int position) {
		return this.getViewTypeForPosition(position);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Object item = this.getItemAtPosition(position);

		if (item == null) {
			return;
		}

		if (holder instanceof EpisodeViewHolder) {
			this.onBindEpisodeViewHolder((EpisodeViewHolder) holder, (ComingEpisode) item);
		} else if (holder instanceof SectionViewHolder) {
			this.onBindSectionViewHolder((SectionViewHolder) holder, (String) item);
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == ITEM_TYPE_EPISODE) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_coming_episodes_episode, parent, false);

			return new EpisodeViewHolder(view);
		}

		if (viewType == ITEM_TYPE_SECTION) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_coming_episodes_section, parent, false);

			return new SectionViewHolder(view);
		}


		return null;
	}

	private int getViewTypeForPosition(int position) {
		int index = 0;

		for (Map.Entry<String, List<ComingEpisode>> entry : this.comingEpisodes.entrySet()) {
			if (index == position) {
				return ITEM_TYPE_SECTION;
			}

			for (ComingEpisode ignored : entry.getValue()) {
				index++;

				if (index == position) {
					return ITEM_TYPE_EPISODE;
				}
			}

			index++;
		}

		return ITEM_TYPE_UNKNOWN;
	}

	@Nullable
	private Object getItemAtPosition(int position) {
		int index = 0;

		for (Map.Entry<String, List<ComingEpisode>> entry : this.comingEpisodes.entrySet()) {
			if (index == position) {
				return entry.getKey();
			}

			for (ComingEpisode comingEpisode : entry.getValue()) {
				index++;

				if (index == position) {
					return comingEpisode;
				}
			}

			index++;
		}

		return null;
	}

	@StringRes
	private int getSectionName(String sectionId) {
		if (TextUtils.isEmpty(sectionId)) {
			return 0;
		}

		switch (sectionId) {
			case "later":
				return R.string.later;

			case "missed":
				return R.string.missed;

			case "soon":
				return R.string.soon;

			case "today":
				return R.string.today;
		}

		return 0;
	}

	private void onBindEpisodeViewHolder(EpisodeViewHolder holder, ComingEpisode comingEpisode) {
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

			Picasso.with(holder.logo.getContext())//
					.load(SickRageApi.getInstance().getApiUrl() + "?cmd=show.getposter&tvdbid=" + comingEpisode.getTvDbId())//
					.transform(new CircleTransformation())//
					.into(holder.logo);
		}

		if (holder.name != null) {
			holder.name.setText(holder.name.getResources().getString(R.string.season_episode_name, comingEpisode.getSeason(), comingEpisode.getEpisode(), comingEpisode.getEpisodeName()));
		}

		if (holder.networkQuality != null) {
			holder.networkQuality.setText(comingEpisode.getNetwork() + " / " + comingEpisode.getQuality());
		}
	}

	private void onBindSectionViewHolder(SectionViewHolder holder, String sectionId) {
		if (holder.name != null) {
			holder.name.setText(this.getSectionName(sectionId));
		}
	}

	public class EpisodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		@Nullable
		public ImageView actions;

		@Nullable
		public TextView date;

		@Nullable
		public ImageView logo;

		@Nullable
		public TextView name;

		@Nullable
		public TextView networkQuality;

		public EpisodeViewHolder(View view) {
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
					ComingEpisode comingEpisode = (ComingEpisode) getItemAtPosition(this.getAdapterPosition());

					if (comingEpisode != null) {
						((OnEpisodeActionSelectedListener) context).onEpisodeActionSelected(comingEpisode.getSeason(), comingEpisode.getEpisode(), comingEpisode.getIndexerId(), item);
					}

					return true;
				}
			}

			return false;
		}
	}

	public class SectionViewHolder extends RecyclerView.ViewHolder {
		@Nullable
		public TextView name;

		public SectionViewHolder(View view) {
			super(view);

			this.name = (TextView) view.findViewById(R.id.section_name);
		}
	}
}
