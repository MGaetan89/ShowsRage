package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.databinding.AdapterScheduleListBinding;
import com.mgaetan89.showsrage.model.Schedule;
import com.mgaetan89.showsrage.presenter.SchedulePresenter;

import java.util.Collections;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
	@NonNull
	private List<Schedule> schedules = Collections.emptyList();

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

		if (holder.actions == null) {
			holder.bind(new SchedulePresenter(schedule, null));
		} else {
			holder.bind(new SchedulePresenter(schedule, holder.actions.getContext()));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_schedule_list, parent, false);

		return new ViewHolder(view);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		@Nullable
		private final ImageView actions;

		private final AdapterScheduleListBinding binding;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.binding = DataBindingUtil.bind(view);

			this.actions = this.binding.includeContent.episodeActions;

			if (this.actions != null) {
				this.actions.setOnClickListener(this);
			}
		}

		public void bind(SchedulePresenter schedule) {
			this.binding.setSchedule(schedule);
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

				if (context != null) {
					Schedule schedule = ScheduleAdapter.this.schedules.get(this.getAdapterPosition());
					Intent intent = new Intent(Constants.Intents.INSTANCE.getACTION_EPISODE_ACTION_SELECTED());
					intent.putExtra(Constants.Bundle.INSTANCE.getEPISODE_NUMBER(), schedule.getEpisode());
					intent.putExtra(Constants.Bundle.INSTANCE.getINDEXER_ID(), schedule.getIndexerId());
					intent.putExtra(Constants.Bundle.INSTANCE.getMENU_ID(), item.getItemId());
					intent.putExtra(Constants.Bundle.INSTANCE.getSEASON_NUMBER(), schedule.getSeason());

					LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

					return true;
				}
			}

			return false;
		}
	}
}
