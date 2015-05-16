package com.mgaetan89.showsrage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.EpisodesAdapter;
import com.mgaetan89.showsrage.fragment.ShowFragment;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.Show;

/**
 * Requires a {@link com.mgaetan89.showsrage.Constants.Bundle#SHOW_MODEL Constants.Bundle#SHOW_MODEL} associated with a non-{@code null} {@link com.mgaetan89.showsrage.model.Show Show} in its {@link android.content.Intent Intent}.
 */
public class ShowActivity extends BaseActivity implements EpisodesAdapter.OnEpisodeSelectedListener {
	@Nullable
	private Show show = null;

	@Override
	public void onEpisodeSelected(int seasonNumber, int episodeNumber, @NonNull Episode episode, int episodesCount) {
		Intent intent = new Intent(this, EpisodeActivity.class);
		intent.putExtra(Constants.Bundle.EPISODE_MODEL, episode);
		intent.putExtra(Constants.Bundle.EPISODE_NUMBER, episodeNumber);
		intent.putExtra(Constants.Bundle.EPISODES_COUNT, episodesCount);
		intent.putExtra(Constants.Bundle.SEASON_NUMBER, seasonNumber);
		intent.putExtra(Constants.Bundle.SHOW_MODEL, this.show);

		this.startActivity(intent);
	}

	@Override
	protected int getSelectedMenuItemIndex() {
		return -1;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.show;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.show = (Show) this.getIntent().getSerializableExtra(Constants.Bundle.SHOW_MODEL);

		if (savedInstanceState == null) {
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new ShowFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(true);
	}
}
