package com.mgaetan89.showsrage.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.ComingEpisodesAdapter;
import com.mgaetan89.showsrage.fragment.ComingEpisodesFragment;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComingEpisodesActivity extends BaseActivity implements Callback<GenericResponse>, ComingEpisodesAdapter.OnEpisodeActionSelectedListener {
	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onEpisodeActionSelected(int seasonNumber, int episodeNumber, int indexerId, MenuItem action) {
		switch (action.getItemId()) {
			case R.id.menu_episode_search:
				this.searchEpisode(seasonNumber, episodeNumber, indexerId);

				break;

			case R.id.menu_episode_set_status_archived:
				this.setEpisodeStatus(seasonNumber, episodeNumber, indexerId, "archived");

				break;

			case R.id.menu_episode_set_status_failed:
				this.setEpisodeStatus(seasonNumber, episodeNumber, indexerId, "failed");

				break;

			case R.id.menu_episode_set_status_ignored:
				this.setEpisodeStatus(seasonNumber, episodeNumber, indexerId, "ignored");

				break;

			case R.id.menu_episode_set_status_skipped:
				this.setEpisodeStatus(seasonNumber, episodeNumber, indexerId, "skipped");

				break;

			case R.id.menu_episode_set_status_wanted:
				this.setEpisodeStatus(seasonNumber, episodeNumber, indexerId, "wanted");

				break;
		}
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		Toast.makeText(this, genericResponse.getMessage(), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected int getSelectedMenuItemIndex() {
		return MenuItems.COMING_EPISODES.ordinal();
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.coming_episodes;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new ComingEpisodesFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(false);
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
