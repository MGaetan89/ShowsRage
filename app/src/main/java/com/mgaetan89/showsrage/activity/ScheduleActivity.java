package com.mgaetan89.showsrage.activity;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.ScheduleAdapter;
import com.mgaetan89.showsrage.fragment.ScheduleFragment;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;

public class ScheduleActivity extends BaseActivity implements ScheduleAdapter.OnEpisodeActionSelectedListener {
	@Override
	public void onEpisodeActionSelected(int seasonNumber, int episodeNumber, int indexerId, MenuItem action) {
		switch (action.getItemId()) {
			case R.id.menu_episode_search:
				this.searchEpisode(seasonNumber, episodeNumber, indexerId);

				break;

			case R.id.menu_episode_set_status_failed:
			case R.id.menu_episode_set_status_ignored:
			case R.id.menu_episode_set_status_skipped:
			case R.id.menu_episode_set_status_wanted:
				this.setEpisodeStatus(seasonNumber, episodeNumber, indexerId, Episode.getStatusForMenuId(action.getItemId()));

				break;
		}
	}

	@Override
	protected boolean displayHomeAsUp() {
		return false;
	}

	@Override
	protected Fragment getFragment() {
		return new ScheduleFragment();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_schedule;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.schedule;
	}

	private void searchEpisode(int seasonNumber, int episodeNumber, int indexerId) {
		Toast.makeText(this, this.getString(R.string.episode_search, episodeNumber, seasonNumber), Toast.LENGTH_SHORT).show();

		SickRageApi.getInstance().getServices().searchEpisode(indexerId, seasonNumber, episodeNumber, this);
	}

	private void setEpisodeStatus(final int seasonNumber, final int episodeNumber, final int indexerId, final String status) {
		final Callback<GenericResponse> callback = this;

		new AlertDialog.Builder(this)
				.setMessage(R.string.replace_existing_episode)
				.setPositiveButton(R.string.replace, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SickRageApi.getInstance().getServices().setEpisodeStatus(indexerId, seasonNumber, episodeNumber, 1, status, callback);
					}
				})
				.setNegativeButton(R.string.keep, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SickRageApi.getInstance().getServices().setEpisodeStatus(indexerId, seasonNumber, episodeNumber, 0, status, callback);
					}
				})
				.show();
	}
}
