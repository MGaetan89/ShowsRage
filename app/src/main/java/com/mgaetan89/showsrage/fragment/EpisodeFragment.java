package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.Show;

public class EpisodeFragment extends Fragment {
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Intent intent = this.getActivity().getIntent();
		Episode episode = (Episode) intent.getSerializableExtra(Constants.Bundle.EPISODE_MODEL);
		int episodeNumber = intent.getIntExtra(Constants.Bundle.EPISODE_NUMBER, 0);
		int seasonNumber = intent.getIntExtra(Constants.Bundle.SEASON_NUMBER, 0);
		Show show = (Show) intent.getSerializableExtra(Constants.Bundle.SHOW_MODEL);

		Toast.makeText(this.getActivity(), String.format("%s - S%02dE%02d", show.getShowName(), seasonNumber, episodeNumber), Toast.LENGTH_SHORT).show();
	}
}
