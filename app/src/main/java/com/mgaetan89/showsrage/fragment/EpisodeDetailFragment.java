package com.mgaetan89.showsrage.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.SingleEpisode;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EpisodeDetailFragment extends Fragment implements Callback<SingleEpisode>, View.OnClickListener {
	@Nullable
	private TextView airs = null;

	@Nullable
	private Episode episode = null;

	private int episodeNumber = 0;

	@Nullable
	private TextView fileSize = null;

	@Nullable
	private TextView location = null;

	@Nullable
	private CardView moreInformationLayout = null;

	@Nullable
	private TextView name = null;

	@Nullable
	private MenuItem playVideoMenu = null;

	@Nullable
	private TextView plot = null;

	@Nullable
	private CardView plotLayout = null;

	@Nullable
	private TextView quality = null;

	private int seasonNumber = 0;

	@Nullable
	private Show show = null;

	@Nullable
	private TextView status = null;

	public EpisodeDetailFragment() {
		this.setHasOptionsMenu(true);
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ActionBar actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
		Bundle arguments = this.getArguments();
		Episode episode = (Episode) arguments.getSerializable(Constants.Bundle.EPISODE_MODEL);
		this.episodeNumber = arguments.getInt(Constants.Bundle.EPISODE_NUMBER, 0);
		this.seasonNumber = arguments.getInt(Constants.Bundle.SEASON_NUMBER, 0);

		if (actionBar != null) {
			actionBar.setTitle(this.getString(R.string.season_number, this.seasonNumber));
		}

		this.show = (Show) arguments.getSerializable(Constants.Bundle.SHOW_MODEL);

		this.displayEpisode(episode);
		SickRageApi.getInstance().getServices().getEpisode(this.show.getIndexerId(), this.seasonNumber, this.episodeNumber, this);
	}

	@Override
	public void onClick(View view) {
		if (this.show == null) {
			return;
		}

		Toast.makeText(this.getActivity(), this.getString(R.string.episode_search, this.episodeNumber, this.seasonNumber), Toast.LENGTH_SHORT).show();

		SickRageApi.getInstance().getServices().searchEpisode(this.show.getIndexerId(), this.seasonNumber, this.episodeNumber, new Callback<GenericResponse>() {
			@Override
			public void failure(RetrofitError error) {
				error.printStackTrace();
			}

			@Override
			public void success(GenericResponse genericResponse, Response response) {
				Toast.makeText(getActivity(), genericResponse.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.episode, menu);

		this.playVideoMenu = menu.findItem(R.id.menu_play_video);

		this.displayPlayVideoMenu(this.episode);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_episode_detail, container, false);

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

			FloatingActionButton searchEpisode = (FloatingActionButton) view.findViewById(R.id.search_episode);

			if (searchEpisode != null) {
				searchEpisode.setOnClickListener(this);
			}
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_episode_set_status_archived:
				this.setEpisodeStatus(seasonNumber, episodeNumber, "archived");

				break;

			case R.id.menu_episode_set_status_failed:
				this.setEpisodeStatus(seasonNumber, episodeNumber, "failed");

				break;

			case R.id.menu_episode_set_status_ignored:
				this.setEpisodeStatus(seasonNumber, episodeNumber, "ignored");

				break;

			case R.id.menu_episode_set_status_skipped:
				this.setEpisodeStatus(seasonNumber, episodeNumber, "skipped");

				break;

			case R.id.menu_episode_set_status_wanted:
				this.setEpisodeStatus(seasonNumber, episodeNumber, "wanted");

				break;

			case R.id.menu_play_video:
				this.clickPlayVideo();

				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void success(SingleEpisode singleEpisode, Response response) {
		this.displayEpisode(singleEpisode.getData());
		this.displayPlayVideoMenu(singleEpisode.getData());
	}

	private void clickPlayVideo() {
		String episodeUrl = SickRageApi.getInstance().getVideosUrl();

		if (this.show != null) {
			episodeUrl += this.show.getShowName().replace(" ", "%20") + "/";
		}

		if (this.episode != null) {
			episodeUrl += this.episode.getLocation().replace(" ", "%20");
		}

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(episodeUrl));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName("org.videolan.vlc", "org.videolan.vlc.gui.video.VideoPlayerActivity");

		this.startActivity(intent);
	}

	private void displayEpisode(@Nullable Episode episode) {
		if (episode == null) {
			return;
		}

		this.episode = episode;

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

	private void displayPlayVideoMenu(Episode episode) {
		if (episode == null) {
			return;
		}

		if (this.playVideoMenu != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
			boolean episodeDownloaded = "Downloaded".equalsIgnoreCase(episode.getStatus());
			boolean viewInVlc = preferences.getBoolean("view_in_vlc", false);

			this.playVideoMenu.setVisible(episodeDownloaded && viewInVlc);
		}
	}

	private void setEpisodeStatus(final int seasonNumber, final int episodeNumber, final String status) {
		if (this.show == null) {
			return;
		}

		final Callback<GenericResponse> callback = new Callback<GenericResponse>() {
			@Override
			public void failure(RetrofitError error) {
				error.printStackTrace();
			}

			@Override
			public void success(GenericResponse genericResponse, Response response) {
				Toast.makeText(getActivity(), genericResponse.getMessage(), Toast.LENGTH_SHORT).show();
			}
		};

		new AlertDialog.Builder(this.getActivity())
				.setMessage(R.string.replace_existing_episode)
				.setPositiveButton(R.string.replace, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SickRageApi.getInstance().getServices().setEpisodeStatus(show.getIndexerId(), seasonNumber, episodeNumber, 1, status, callback);
					}
				})
				.setNegativeButton(R.string.keep, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SickRageApi.getInstance().getServices().setEpisodeStatus(show.getIndexerId(), seasonNumber, episodeNumber, 0, status, callback);
					}
				})
				.show();
	}
}
