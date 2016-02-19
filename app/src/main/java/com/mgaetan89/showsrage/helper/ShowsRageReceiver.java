package com.mgaetan89.showsrage.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.MainActivity;
import com.mgaetan89.showsrage.fragment.AddShowOptionsFragment;
import com.mgaetan89.showsrage.fragment.EpisodeFragment;
import com.mgaetan89.showsrage.fragment.ShowFragment;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.lang.ref.WeakReference;

import retrofit.Callback;

public class ShowsRageReceiver extends BroadcastReceiver {
	@NonNull
	private final WeakReference<MainActivity> activityReference;

	public ShowsRageReceiver(MainActivity activity) {
		this.activityReference = new WeakReference<>(activity);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}

		String action = intent.getAction();

		if (action == null || action.isEmpty()) {
			return;
		}

		if (action.equals(Constants.Intents.INSTANCE.getACTION_EPISODE_ACTION_SELECTED())) {
			this.handleEpisodeActionSelected(intent);
		} else if (action.equals(Constants.Intents.INSTANCE.getACTION_EPISODE_SELECTED())) {
			this.handleEpisodeSelected(intent);
		} else if (action.equals(Constants.Intents.INSTANCE.getACTION_SEARCH_RESULT_SELECTED())) {
			this.handleSearchResultSelected(intent);
		} else if (action.equals(Constants.Intents.INSTANCE.getACTION_SHOW_SELECTED())) {
			this.handleShowSelected(intent);
		}
	}

	private void handleEpisodeActionSelected(@NonNull Intent intent) {
		int episodeNumber = intent.getIntExtra(Constants.Bundle.INSTANCE.getEPISODE_NUMBER(), 0);
		int indexerId = intent.getIntExtra(Constants.Bundle.INSTANCE.getINDEXER_ID(), 0);
		int menuId = intent.getIntExtra(Constants.Bundle.INSTANCE.getMENU_ID(), 0);
		int seasonNumber = intent.getIntExtra(Constants.Bundle.INSTANCE.getSEASON_NUMBER(), 0);

		switch (menuId) {
			case R.id.menu_episode_search:
				this.searchEpisode(seasonNumber, episodeNumber, indexerId);

				break;

			case R.id.menu_episode_set_status_failed:
			case R.id.menu_episode_set_status_ignored:
			case R.id.menu_episode_set_status_skipped:
			case R.id.menu_episode_set_status_wanted:
				this.setEpisodeStatus(seasonNumber, episodeNumber, indexerId, Episode.getStatusForMenuId(menuId));

				break;
		}
	}

	private void handleEpisodeSelected(@NonNull Intent intent) {
		MainActivity activity = this.activityReference.get();

		if (activity == null) {
			return;
		}

		Bundle arguments = new Bundle();
		arguments.putParcelable(Constants.Bundle.INSTANCE.getSHOW_MODEL(), intent.getParcelableExtra(Constants.Bundle.INSTANCE.getEPISODE_MODEL()));
		arguments.putInt(Constants.Bundle.INSTANCE.getEPISODE_NUMBER(), intent.getIntExtra(Constants.Bundle.INSTANCE.getEPISODE_NUMBER(), 0));
		arguments.putInt(Constants.Bundle.INSTANCE.getEPISODES_COUNT(), intent.getIntExtra(Constants.Bundle.INSTANCE.getEPISODES_COUNT(), 0));
		arguments.putInt(Constants.Bundle.INSTANCE.getSEASON_NUMBER(), intent.getIntExtra(Constants.Bundle.INSTANCE.getSEASON_NUMBER(), 0));
		arguments.putParcelable(Constants.Bundle.INSTANCE.getSHOW_MODEL(), intent.getParcelableExtra(Constants.Bundle.INSTANCE.getSHOW_MODEL()));

		EpisodeFragment fragment = new EpisodeFragment();
		fragment.setArguments(arguments);

		activity.getSupportFragmentManager().beginTransaction()
				.addToBackStack("episode")
				.replace(R.id.content, fragment)
				.commit();
	}

	private void handleSearchResultSelected(@NonNull Intent intent) {
		MainActivity activity = this.activityReference.get();

		if (activity == null) {
			return;
		}

		Bundle arguments = new Bundle();
		arguments.putInt(Constants.Bundle.INSTANCE.getINDEXER_ID(), intent.getIntExtra(Constants.Bundle.INSTANCE.getINDEXER_ID(), 0));

		AddShowOptionsFragment fragment = new AddShowOptionsFragment();
		fragment.setArguments(arguments);
		fragment.show(activity.getSupportFragmentManager(), "add_show");
	}

	private void handleShowSelected(@NonNull Intent intent) {
		MainActivity activity = this.activityReference.get();

		if (activity == null) {
			return;
		}

		Bundle arguments = new Bundle();
		arguments.putParcelable(Constants.Bundle.INSTANCE.getSHOW_MODEL(), intent.getParcelableExtra(Constants.Bundle.INSTANCE.getSHOW_MODEL()));

		ShowFragment fragment = new ShowFragment();
		fragment.setArguments(arguments);

		activity.getSupportFragmentManager().beginTransaction()
				.addToBackStack("show")
				.replace(R.id.content, fragment)
				.commit();
	}

	private void searchEpisode(int seasonNumber, int episodeNumber, int indexerId) {
		MainActivity activity = this.activityReference.get();

		if (activity == null) {
			return;
		}

		Toast.makeText(activity, activity.getString(R.string.episode_search, episodeNumber, seasonNumber), Toast.LENGTH_SHORT).show();

		SickRageApi.Companion.getInstance().getServices().searchEpisode(indexerId, seasonNumber, episodeNumber, activity);
	}

	private void setEpisodeStatus(final int seasonNumber, final int episodeNumber, final int indexerId, final String status) {
		MainActivity activity = this.activityReference.get();

		if (activity == null) {
			return;
		}

		final Callback<GenericResponse> callback = activity;

		new AlertDialog.Builder(activity)
				.setMessage(R.string.replace_existing_episode)
				.setPositiveButton(R.string.replace, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SickRageApi.Companion.getInstance().getServices().setEpisodeStatus(indexerId, seasonNumber, episodeNumber, 1, status, callback);
					}
				})
				.setNegativeButton(R.string.keep, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SickRageApi.Companion.getInstance().getServices().setEpisodeStatus(indexerId, seasonNumber, episodeNumber, 0, status, callback);
					}
				})
				.show();
	}
}
