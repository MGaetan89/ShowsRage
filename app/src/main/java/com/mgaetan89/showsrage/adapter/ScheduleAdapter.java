package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.helper.ImageLoader;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.model.Schedule;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.Collections;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
	@NonNull
	private List<Schedule> schedules = Collections.emptyList();

	public interface OnEpisodeActionSelectedListener {
		void onEpisodeActionSelected(int seasonNumber, int episodeNumber, int indexerId, MenuItem action);
	}

	public ScheduleAdapter(@Nullable List<Schedule> schedules) {
		if (schedules == null) {
			this.schedules = Collections.emptyList();
		} else {
			this.schedules = schedules;
		}
	}

	@Override
	public int getItemCount() {
		return this.schedules.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Schedule schedule = this.schedules.get(position);

		if (holder.date != null) {
			String airDate = schedule.getAirDate();

			if (TextUtils.isEmpty(airDate)) {
				holder.date.setText(R.string.never);
			} else {
				holder.date.setText(DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS));
			}
		}

		if (holder.logo != null) {
			holder.logo.setContentDescription(schedule.getShowName());

			ImageLoader.load(
					holder.logo,
					SickRageApi.getInstance().getPosterUrl(schedule.getTvDbId(), Indexer.TVDB),
					true
			);
		}

		if (holder.name != null) {
			holder.name.setText(holder.name.getResources().getString(R.string.show_name_episode, schedule.getShowName(), schedule.getSeason(), schedule.getEpisode()));
		}

		if (holder.networkQuality != null) {
			holder.networkQuality.setText(holder.networkQuality.getResources().getString(R.string.separated_texts, schedule.getNetwork(), schedule.getQuality()));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_schedule_list, parent, false);

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

			view.setOnClickListener(this);

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
			Context context = view.getContext();

			if (view.getId() == R.id.episode_actions) {
				PopupMenu popupMenu = new PopupMenu(context, this.actions);
				popupMenu.inflate(R.menu.episode_action);
				popupMenu.setOnMenuItemClickListener(this);
				popupMenu.show();
			} else {
				Schedule schedule = ScheduleAdapter.this.schedules.get(this.getAdapterPosition());
				String plot = schedule.getEpisodePlot();

				if (plot != null) {
					String message = context.getString(R.string.season_episode_name, schedule.getSeason(), schedule.getEpisode(), schedule.getEpisodeName());
					message += "\n\n";
					message += plot;

					AlertDialog dialog = new AlertDialog.Builder(context)
							.setTitle(schedule.getShowName())
							.setMessage(message)
							.setPositiveButton(R.string.close, null)
							.show();

					try {
						TextView textView = (TextView) dialog.getWindow().getDecorView().findViewById(android.R.id.message);
						textView.setTextIsSelectable(true);
					} catch (Exception exception) {
						exception.printStackTrace();
						// The TextView was not found
					}
				} else {
					Toast.makeText(context, R.string.no_plot, Toast.LENGTH_SHORT).show();
				}
			}
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			if (this.actions != null) {
				Context context = this.actions.getContext();

				if (context instanceof OnEpisodeActionSelectedListener) {
					Schedule schedule = ScheduleAdapter.this.schedules.get(this.getAdapterPosition());

					if (schedule != null) {
						((OnEpisodeActionSelectedListener) context).onEpisodeActionSelected(schedule.getSeason(), schedule.getEpisode(), schedule.getIndexerId(), item);
					}

					return true;
				}
			}

			return false;
		}
	}
}
