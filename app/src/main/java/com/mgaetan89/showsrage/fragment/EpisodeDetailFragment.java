package com.mgaetan89.showsrage.fragment;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.app.MediaRouteDiscoveryFragment;
import android.support.v7.media.MediaControlIntent;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.databinding.FragmentEpisodeDetailBinding;
import com.mgaetan89.showsrage.helper.GenericCallback;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.model.PlayingVideoData;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.SingleEpisode;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mgaetan89.showsrage.presenter.EpisodePresenter;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EpisodeDetailFragment extends MediaRouteDiscoveryFragment implements Callback<SingleEpisode> {
	@Nullable
	private FragmentEpisodeDetailBinding binding = null;

	@Nullable
	private MenuItem castMenu = null;

	@Nullable
	private Episode episode = null;

	private int episodeNumber = 0;

	@Nullable
	private MenuItem playVideoMenu = null;

	private int seasonNumber = 0;

	@Nullable
	private Show show = null;

	public EpisodeDetailFragment() {
		this.setHasOptionsMenu(true);

		this.setRouteSelector(new MediaRouteSelector.Builder()
				.addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
				.build());
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
			if (this.seasonNumber <= 0) {
				actionBar.setTitle(R.string.specials);
			} else {
				actionBar.setTitle(this.getString(R.string.season_number, this.seasonNumber));
			}
		}

		this.show = (Show) arguments.getSerializable(Constants.Bundle.SHOW_MODEL);

		this.displayEpisode(episode);

		if (this.show != null) {
			SickRageApi.getInstance().getServices().getEpisode(this.show.getIndexerId(), this.seasonNumber, this.episodeNumber, this);
		}
	}

	@Override
	public MediaRouter.Callback onCreateCallback() {
		return new MediaRouterCallback(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.episode, menu);

		this.castMenu = menu.findItem(R.id.menu_cast);
		MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat.getActionProvider(this.castMenu);
		mediaRouteActionProvider.setRouteSelector(this.getRouteSelector());

		this.playVideoMenu = menu.findItem(R.id.menu_play_video);

		this.displayStreamingMenus(this.episode);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false);

		return this.binding.getRoot();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_episode_set_status_archived:
			case R.id.menu_episode_set_status_failed:
			case R.id.menu_episode_set_status_ignored:
			case R.id.menu_episode_set_status_skipped:
			case R.id.menu_episode_set_status_wanted:
				this.setEpisodeStatus(this.seasonNumber, this.episodeNumber, Episode.getStatusForMenuId(item.getItemId()));

				break;

			case R.id.menu_play_video:
				this.clickPlayVideo();

				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void searchEpisode() {
		if (this.show == null) {
			return;
		}

		Toast.makeText(this.getActivity(), this.getString(R.string.episode_search, this.episodeNumber, this.seasonNumber), Toast.LENGTH_SHORT).show();

		SickRageApi.getInstance().getServices().searchEpisode(this.show.getIndexerId(), this.seasonNumber, this.episodeNumber, new GenericCallback(this.getActivity()));
	}

	@Override
	public void success(SingleEpisode singleEpisode, Response response) {
		this.displayEpisode(singleEpisode.getData());
		this.displayStreamingMenus(singleEpisode.getData());
	}

	private boolean areStreamingMenusVisible(@Nullable Episode episode) {
		FragmentActivity activity = this.getActivity();

		if (activity == null || episode == null) {
			return false;
		}

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean episodeDownloaded = "Downloaded".equalsIgnoreCase(episode.getStatus());
		boolean viewInVlc = preferences.getBoolean("view_in_vlc", false);

		return episodeDownloaded && viewInVlc;
	}

	private void clickPlayVideo() {
		Intent intent = new Intent(Intent.ACTION_VIEW, this.getEpisodeVideoUrl());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName("org.videolan.vlc", "org.videolan.vlc.gui.video.VideoPlayerActivity");

		this.startActivity(intent);
	}

	private void displayEpisode(@Nullable Episode episode) {
		if (episode == null) {
			return;
		}

		this.episode = episode;

		if (this.binding != null) {
			EpisodePresenter episodePresenter = new EpisodePresenter(this.episode, this.getActivity());
			episodePresenter.setSearchListener(this);

			this.binding.setEpisode(episodePresenter);
		}
	}

	private void displayStreamingMenus(Episode episode) {
		boolean displayStreamingMenu = this.areStreamingMenusVisible(episode);

		if (this.castMenu != null) {
			this.castMenu.setVisible(displayStreamingMenu);
		}

		if (this.playVideoMenu != null) {
			this.playVideoMenu.setVisible(displayStreamingMenu);
		}
	}

	@NonNull
	private Uri getEpisodeVideoUrl() {
		String episodeUrl = SickRageApi.getInstance().getVideosUrl();

		if (this.show != null) {
			episodeUrl += this.show.getShowName() + "/";
		}

		if (this.episode != null) {
			episodeUrl += this.episode.getLocation();
		}

		try {
			URL url = new URL(episodeUrl);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

			return Uri.parse(uri.toString());
		} catch (MalformedURLException | URISyntaxException exception) {
			exception.printStackTrace();
		}

		return Uri.parse(episodeUrl);
	}

	private void setEpisodeStatus(final int seasonNumber, final int episodeNumber, final String status) {
		if (this.show == null) {
			return;
		}

		final Callback<GenericResponse> callback = new GenericCallback(this.getActivity());
		final int indexerId = this.show.getIndexerId();

		new AlertDialog.Builder(this.getActivity())
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

	private static final class MediaRouterCallback extends MediaRouter.Callback {
		@NonNull
		private WeakReference<EpisodeDetailFragment> fragmentReference = new WeakReference<>(null);

		private MediaRouterCallback(EpisodeDetailFragment fragment) {
			this.fragmentReference = new WeakReference<>(fragment);
		}

		@Override
		public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo route) {
			this.updateRemotePlayer(route);
		}

		@Override
		public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo route) {
			this.updateRemotePlayer(route);
		}

		private void updateRemotePlayer(MediaRouter.RouteInfo route) {
			EpisodeDetailFragment fragment = this.fragmentReference.get();

			if (fragment == null) {
				return;
			}

			Application application = fragment.getActivity().getApplication();

			if (application instanceof ShowsRageApplication) {
				PlayingVideoData playingVideo = new PlayingVideoData();
				playingVideo.setEpisode(fragment.episode);
				playingVideo.setRoute(route);
				playingVideo.setShow(fragment.show);
				playingVideo.setVideoUri(fragment.getEpisodeVideoUrl());

				((ShowsRageApplication) application).setPlayingVideo(playingVideo);
			}
		}
	}
}
