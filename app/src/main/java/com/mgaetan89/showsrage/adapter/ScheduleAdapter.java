package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.databinding.AdapterScheduleListBinding;
import com.mgaetan89.showsrage.model.Schedule;
import com.mgaetan89.showsrage.presenter.SchedulePresenter;

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

		holder.bind(new SchedulePresenter(schedule));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_schedule_list, parent, false);

		return new ViewHolder(view);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		private final AdapterScheduleListBinding binding;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.binding = DataBindingUtil.bind(view);

			if (this.binding.includeContent.episodeActions != null) {
				this.binding.includeContent.episodeActions.setOnClickListener(this);
			}
		}

		public void bind(SchedulePresenter schedule) {
			this.binding.setSchedule(schedule);
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();

			if (view.getId() == R.id.episode_actions) {
				PopupMenu popupMenu = new PopupMenu(context, this.binding.includeContent.episodeActions);
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
			if (this.binding.includeContent.episodeActions != null) {
				Context context = this.binding.includeContent.episodeActions.getContext();

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
