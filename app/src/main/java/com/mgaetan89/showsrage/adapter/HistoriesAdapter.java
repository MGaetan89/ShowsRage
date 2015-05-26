package com.mgaetan89.showsrage.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.History;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mgaetan89.showsrage.picasso.CircleTransformation;
import com.squareup.picasso.Picasso;

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
			holder.date.setText(DateTimeHelper.getRelativeDate(history.getDate(), "yyyy-MM-dd hh:mm", 0));
		}

		if (holder.logo != null) {
			holder.logo.setContentDescription(history.getShowName());

			Picasso.with(holder.logo.getContext())//
					.load(SickRageApi.getInstance().getApiUrl() + "?cmd=show.getposter&tvdbid=" + history.getTvDbId())//
					.transform(new CircleTransformation())//
					.into(holder.logo);
		}

		if (holder.name != null) {
			holder.name.setText(holder.name.getResources().getString(R.string.show_name_episode, history.getShowName(), history.getSeason(), history.getEpisode()));
		}

		if (holder.statusProvider != null) {
			Resources resources = holder.statusProvider.getResources();
			String provider = history.getProvider();
			String status = this.getTranslatedStatus(resources, history.getStatus());

			if ("-1".equals(provider)) {
				holder.statusProvider.setText(status);
			} else {
				holder.statusProvider.setText(resources.getString(R.string.status_from, status, provider));
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
		public TextView date;

		@Nullable
		public ImageView logo;

		@Nullable
		public TextView name;

		@Nullable
		public TextView statusProvider;

		public ViewHolder(View view) {
			super(view);

			this.date = (TextView) view.findViewById(R.id.episode_date);
			this.logo = (ImageView) view.findViewById(R.id.episode_logo);
			this.name = (TextView) view.findViewById(R.id.episode_name);
			this.statusProvider = (TextView) view.findViewById(R.id.episode_status_provider);
		}
	}

	private String getTranslatedStatus(Resources resources, String originalStatus) {
		if (TextUtils.isEmpty(originalStatus)) {
			return originalStatus;
		}

		switch (originalStatus) {
			case "Downloaded":
				return resources.getString(R.string.downloaded);

			case "Snatched":
				return resources.getString(R.string.snatched);

			default:
				return originalStatus;
		}
	}
}
