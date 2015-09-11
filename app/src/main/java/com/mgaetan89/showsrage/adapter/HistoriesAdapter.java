package com.mgaetan89.showsrage.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.helper.ImageLoader;
import com.mgaetan89.showsrage.model.History;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.network.SickRageApi;

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

		if (holder.date != null) {
			int status = history.getStatusTranslationResource();
			String statusString = history.getStatus();

			if (status != 0) {
				statusString = holder.date.getResources().getString(status);
			}

			holder.date.setText(holder.date.getResources().getString(R.string.spaced_texts, statusString, DateTimeHelper.getRelativeDate(history.getDate(), "yyyy-MM-dd hh:mm", 0).toString().toLowerCase()));
		}

		if (holder.logo != null) {
			holder.logo.setContentDescription(history.getShowName());

			ImageLoader.load(
					holder.logo,
					SickRageApi.getInstance().getPosterUrl(history.getTvDbId(), Indexer.TVDB),
					true
			);
		}

		if (holder.name != null) {
			holder.name.setText(holder.name.getResources().getString(R.string.show_name_episode, history.getShowName(), history.getSeason(), history.getEpisode()));
		}

		if (holder.providerQuality != null) {
			String provider = history.getProvider();

			if ("-1".equals(provider)) {
				holder.providerQuality.setText(history.getQuality());
			} else {
				Resources resources = holder.providerQuality.getResources();

				holder.providerQuality.setText(resources.getString(R.string.provider_quality, provider, history.getQuality()));
			}
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

		@Nullable
		public final ImageView logo;

		@Nullable
		public final TextView name;

		@Nullable
		public final TextView providerQuality;

		public ViewHolder(View view) {
			super(view);

			this.date = (TextView) view.findViewById(R.id.episode_date);
			this.logo = (ImageView) view.findViewById(R.id.episode_logo);
			this.name = (TextView) view.findViewById(R.id.episode_name);
			this.providerQuality = (TextView) view.findViewById(R.id.episode_provider_quality);
		}
	}
}
