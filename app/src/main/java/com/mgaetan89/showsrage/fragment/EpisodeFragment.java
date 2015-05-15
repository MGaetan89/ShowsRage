package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.SingleEpisode;
import com.mgaetan89.showsrage.network.SickRageApi;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EpisodeFragment extends Fragment implements Callback<SingleEpisode> {
	@Inject
	public SickRageApi api;

	@Nullable
	private TextView airs = null;

	@Nullable
	private TextView fileSize = null;

	@Nullable
	private TextView location = null;

	@Nullable
	private CardView moreInformationLayout = null;

	@Nullable
	private TextView name = null;

	@Nullable
	private TextView plot = null;

	@Nullable
	private CardView plotLayout = null;

	@Nullable
	private TextView quality = null;

	@Nullable
	private TextView status = null;

	public EpisodeFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		AppCompatActivity activity = (AppCompatActivity) this.getActivity();
		ActionBar actionBar = activity.getSupportActionBar();
		Intent intent = activity.getIntent();
		Episode episode = (Episode) intent.getSerializableExtra(Constants.Bundle.EPISODE_MODEL);
		int episodeNumber = intent.getIntExtra(Constants.Bundle.EPISODE_NUMBER, 0);
		int seasonNumber = intent.getIntExtra(Constants.Bundle.SEASON_NUMBER, 0);
		Show show = (Show) intent.getSerializableExtra(Constants.Bundle.SHOW_MODEL);

		if (actionBar != null) {
			actionBar.setTitle(this.getString(R.string.season_number, seasonNumber));
		}

		this.displayEpisode(episode);
		this.api.getServices().getEpisode(show.getIndexerId(), seasonNumber, episodeNumber, this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((ShowsRageApplication) this.getActivity().getApplication()).inject(this);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_episode, container, false);

		if (view != null) {
			this.airs = (TextView) view.findViewById(R.id.episode_airs);
			this.fileSize = (TextView) view.findViewById(R.id.episode_file_size);
			this.location = (TextView) view.findViewById(R.id.episode_location);
			this.moreInformationLayout = (CardView) view.findViewById(R.id.episode_more_information_layout);
			this.name = (TextView) view.findViewById(R.id.episode_name);
			this.plot = (TextView) view.findViewById(R.id.episode_plot);
			this.plotLayout = (CardView) view.findViewById(R.id.episode_plot_layout);
			this.quality = (TextView) view.findViewById(R.id.episode_quality);
			this.status = (TextView) view.findViewById(R.id.episode_status);
		}

		return view;
	}

	@Override
	public void onDestroyView() {
		this.airs = null;
		this.fileSize = null;
		this.location = null;
		this.moreInformationLayout = null;
		this.name = null;
		this.plot = null;
		this.plotLayout = null;
		this.quality = null;
		this.status = null;

		super.onDestroyView();
	}

	@Override
	public void success(SingleEpisode singleEpisode, Response response) {
		this.displayEpisode(singleEpisode.getData());
	}

	private void displayEpisode(@Nullable Episode episode) {
		if (episode == null) {
			return;
		}

		if (this.airs != null) {
			this.airs.setText(this.getString(R.string.airs, DateTimeHelper.getRelativeDate(episode.getAirDate(), "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)));
			this.airs.setVisibility(View.VISIBLE);
		}

		if (episode.getFileSize() == 0L) {
			if (this.moreInformationLayout != null) {
				this.moreInformationLayout.setVisibility(View.GONE);
			}
		} else {
			if (this.fileSize != null) {
				this.fileSize.setText(this.getString(R.string.file_size, episode.getFileSizeHuman()));
			}

			if (this.location != null) {
				this.location.setText(this.getString(R.string.location, episode.getLocation()));
			}

			if (this.moreInformationLayout != null) {
				this.moreInformationLayout.setVisibility(View.VISIBLE);
			}
		}

		if (this.name != null) {
			this.name.setText(episode.getName());
			this.name.setVisibility(View.VISIBLE);
		}

		if (this.plot != null) {
			String description = episode.getDescription();

			if (TextUtils.isEmpty(description)) {
				if (this.plotLayout != null) {
					this.plotLayout.setVisibility(View.GONE);
				}
			} else {
				this.plot.setText(description);

				if (this.plotLayout != null) {
					this.plotLayout.setVisibility(View.VISIBLE);
				}
			}
		}

		if (this.quality != null) {
			String quality = episode.getQuality();

			if ("N/A".equalsIgnoreCase(quality)) {
				this.quality.setVisibility(View.GONE);
			} else {
				this.quality.setText(this.getString(R.string.quality, quality));
				this.quality.setVisibility(View.VISIBLE);
			}
		}

		if (this.status != null) {
			this.status.setText(this.getString(R.string.status, episode.getStatus()));
			this.status.setVisibility(View.VISIBLE);
		}
	}
}
