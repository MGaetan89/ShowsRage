package com.mgaetan89.showsrage.fragment;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.app.MediaRouteDiscoveryFragment;
import android.support.v7.media.MediaControlIntent;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
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
import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.activity.BaseActivity;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.helper.GenericCallback;
import com.mgaetan89.showsrage.helper.Utils;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.model.OmDbEpisode;
import com.mgaetan89.showsrage.model.PlayingVideoData;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.SingleEpisode;
import com.mgaetan89.showsrage.network.OmDbApi;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mgaetan89.showsrage.view.ColoredMediaRouteActionProvider;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EpisodeDetailFragment extends MediaRouteDiscoveryFragment implements Callback<SingleEpisode>, View.OnClickListener {
	@Nullable
	private TextView airs = null;

	@Nullable
	private TextView awards = null;

	@Nullable
	private CardView awardsLayout = null;

	@Nullable
	private TextView castingActors = null;

	@Nullable
	private TextView castingDirectors = null;

	@Nullable
	private CardView castingLayout = null;

	@Nullable
	private TextView castingWriters = null;

	@Nullable
	private MenuItem castMenu = null;

	@Nullable
	private Episode episode = null;

	private int episodeNumber = 0;

	@Nullable
	private TextView fileSize = null;

	@Nullable
	private TextView genre = null;

	@Nullable
	private TextView languageCountry = null;

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

	@Nullable
	private TextView rated = null;

	@Nullable
	private TextView rating = null;

	@Nullable
	private TextView runtime = null;

	private int seasonNumber = 0;

	@Nullable
	private Show show = null;

	@Nullable
	private TextView status = null;

	@Nullable
	private TextView year = null;

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

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(Constants.OMDB_URL)
				.setLogLevel(Constants.NETWORK_LOG_LEVEL)
				.build();
		OmDbApi omDbApi = restAdapter.create(OmDbApi.class);

		ActionBar actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
		Bundle arguments = this.getArguments();
		Episode episode = arguments.getParcelable(Constants.Bundle.EPISODE_MODEL);
		this.episodeNumber = arguments.getInt(Constants.Bundle.EPISODE_NUMBER, 0);
		this.seasonNumber = arguments.getInt(Constants.Bundle.SEASON_NUMBER, 0);

		if (actionBar != null) {
			if (this.seasonNumber <= 0) {
				actionBar.setTitle(R.string.specials);
			} else {
				actionBar.setTitle(this.getString(R.string.season_number, this.seasonNumber));
			}
		}

		this.show = arguments.getParcelable(Constants.Bundle.SHOW_MODEL);

		this.displayEpisode(episode);

		if (this.show != null) {
			SickRageApi.getInstance().getServices().getEpisode(this.show.getIndexerId(), this.seasonNumber, this.episodeNumber, this);


			String imdbId = this.show.getImdbId();

			if (imdbId != null && !imdbId.isEmpty()) {
				// We might not have the IMDB yet
				omDbApi.getEpisodeByImDbId(imdbId, this.seasonNumber, this.episodeNumber, new OmdbEpisodeCallback(this));
			} else {
				// So we try to get the data by using the show name
				omDbApi.getEpisodeByTitle(this.show.getShowName(), this.seasonNumber, this.episodeNumber, new OmdbEpisodeCallback(this));
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (this.show == null) {
			return;
		}

		Toast.makeText(this.getActivity(), this.getString(R.string.episode_search, this.episodeNumber, this.seasonNumber), Toast.LENGTH_SHORT).show();

		SickRageApi.getInstance().getServices().searchEpisode(this.show.getIndexerId(), this.seasonNumber, this.episodeNumber, new GenericCallback(this.getActivity()));
	}

	@Override
	public MediaRouter.Callback onCreateCallback() {
		return new MediaRouterCallback(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.episode, menu);

		this.castMenu = menu.findItem(R.id.menu_cast);
		Intent intent = this.getActivity().getIntent();
		MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat.getActionProvider(this.castMenu);

		if (mediaRouteActionProvider != null) {
			mediaRouteActionProvider.setRouteSelector(this.getRouteSelector());
		}

		if (intent != null && mediaRouteActionProvider instanceof ColoredMediaRouteActionProvider) {
			int colorPrimary = intent.getIntExtra(Constants.Bundle.COLOR_PRIMARY, 0);

			if (colorPrimary != 0) {
				((ColoredMediaRouteActionProvider) mediaRouteActionProvider).setButtonColor(Utils.getContrastColor(colorPrimary));
			}
		}

		this.playVideoMenu = menu.findItem(R.id.menu_play_video);

		this.displayStreamingMenus(this.episode);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_episode_detail, container, false);

		if (view != null) {
			this.airs = (TextView) view.findViewById(R.id.episode_airs);
			this.awards = (TextView) view.findViewById(R.id.episode_awards);
			this.awardsLayout = (CardView) view.findViewById(R.id.episode_awards_layout);
			this.castingActors = (TextView) view.findViewById(R.id.episode_casting_actors);
			this.castingDirectors = (TextView) view.findViewById(R.id.episode_casting_directors);
			this.castingLayout = (CardView) view.findViewById(R.id.episode_casting_layout);
			this.castingWriters = (TextView) view.findViewById(R.id.episode_casting_writers);
			this.fileSize = (TextView) view.findViewById(R.id.episode_file_size);
			this.genre = (TextView) view.findViewById(R.id.episode_genre);
			this.languageCountry = (TextView) view.findViewById(R.id.episode_language_country);
			this.location = (TextView) view.findViewById(R.id.episode_location);
			this.moreInformationLayout = (CardView) view.findViewById(R.id.episode_more_information_layout);
			this.name = (TextView) view.findViewById(R.id.episode_name);
			this.plot = (TextView) view.findViewById(R.id.episode_plot);
			this.plotLayout = (CardView) view.findViewById(R.id.episode_plot_layout);
			this.quality = (TextView) view.findViewById(R.id.episode_quality);
			this.rated = (TextView) view.findViewById(R.id.episode_rated);
			this.rating = (TextView) view.findViewById(R.id.episode_rating);
			this.runtime = (TextView) view.findViewById(R.id.episode_runtime);
			this.status = (TextView) view.findViewById(R.id.episode_status);
			this.year = (TextView) view.findViewById(R.id.episode_year);

			FloatingActionButton searchEpisode = (FloatingActionButton) view.findViewById(R.id.search_episode);

			if (searchEpisode != null) {
				FragmentActivity activity = this.getActivity();

				if (activity != null) {
					Intent intent = activity.getIntent();

					if (intent != null) {
						int colorPrimary = intent.getIntExtra(Constants.Bundle.COLOR_PRIMARY, 0);

						if (colorPrimary != 0) {
							searchEpisode.setBackgroundTintList(ColorStateList.valueOf(colorPrimary));
							DrawableCompat.setTint(DrawableCompat.wrap(searchEpisode.getDrawable()), Utils.getContrastColor(colorPrimary));
						}
					}
				}

				searchEpisode.setOnClickListener(this);
			}
		}

		return view;
	}

	@Override
	public void onDestroyView() {
		this.airs = null;
		this.awards = null;
		this.awardsLayout = null;
		this.castingActors = null;
		this.castingDirectors = null;
		this.castingLayout = null;
		this.castingWriters = null;
		this.fileSize = null;
		this.genre = null;
		this.languageCountry = null;
		this.location = null;
		this.moreInformationLayout = null;
		this.name = null;
		this.plot = null;
		this.plotLayout = null;
		this.quality = null;
		this.rated = null;
		this.rating = null;
		this.runtime = null;
		this.status = null;
		this.year = null;

		super.onDestroyView();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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

	@Override
	public void success(SingleEpisode singleEpisode, Response response) {
		this.displayEpisode(singleEpisode.getData());
		this.displayStreamingMenus(singleEpisode.getData());
	}

	private void clickPlayVideo() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(this.getEpisodeVideoUrl(), "video/*");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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
			int status = episode.getStatusTranslationResource();
			String statusString = episode.getStatus();

			if (status != 0) {
				statusString = this.getString(status);
			}

			this.status.setText(this.getString(R.string.status_value, statusString));
			this.status.setVisibility(View.VISIBLE);
		}
	}

	private void displayStreamingMenus(Episode episode) {
		if (this.castMenu != null) {
			this.castMenu.setVisible(this.isCastMenuVisible(episode));
		}

		if (this.playVideoMenu != null) {
			this.playVideoMenu.setVisible(this.isPlayMenuVisible(episode));
		}
	}

	@NonNull
	private Uri getEpisodeVideoUrl() {
		String episodeUrl = SickRageApi.getInstance().getVideosUrl();

		if (this.episode != null) {
			String location = this.episode.getLocation();
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
			Set<String> rootDirs = preferences.getStringSet(Constants.Preferences.Fields.ROOT_DIRS, null);

			if (rootDirs != null) {
				for (String rootDir : rootDirs) {
					if (location.startsWith(rootDir)) {
						location = location.replaceFirst(Pattern.quote(rootDir), "");

						break;
					}
				}
			}

			episodeUrl += location;
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

	private boolean isCastMenuVisible(@Nullable Episode episode) {
		FragmentActivity activity = this.getActivity();

		if (activity == null) {
			return false;
		}

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean episodeDownloaded = this.isEpisodeDownloaded(episode);
		boolean viewInExternalVideoPlayer = preferences.getBoolean("stream_in_chromecast", false);

		return episodeDownloaded && viewInExternalVideoPlayer;
	}

	private boolean isEpisodeDownloaded(@Nullable Episode episode) {
		return episode != null && "Downloaded".equalsIgnoreCase(episode.getStatus());
	}

	private boolean isPlayMenuVisible(@Nullable Episode episode) {
		FragmentActivity activity = this.getActivity();

		if (activity == null) {
			return false;
		}

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean episodeDownloaded = this.isEpisodeDownloaded(episode);
		boolean viewInExternalVideoPlayer = preferences.getBoolean("view_in_external_video_player", false);

		return episodeDownloaded && viewInExternalVideoPlayer;
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

			if (fragment == null || !fragment.getUserVisibleHint()) {
				return;
			}

			FragmentActivity activity = fragment.getActivity();

			if (activity instanceof BaseActivity) {
				((BaseActivity) activity).updateRemoteControlVisibility();
			}

			Application application = activity.getApplication();

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

	private static final class OmdbEpisodeCallback implements Callback<OmDbEpisode> {
		private WeakReference<EpisodeDetailFragment> fragmentReference = null;

		private OmdbEpisodeCallback(EpisodeDetailFragment fragment) {
			this.fragmentReference = new WeakReference<>(fragment);
		}

		@Override
		public void failure(RetrofitError error) {
			error.printStackTrace();
		}

		@Override
		public void success(OmDbEpisode episode, Response response) {
			EpisodeDetailFragment fragment = this.fragmentReference.get();

			if (fragment == null) {
				return;
			}

			if (fragment.awards != null) {
				setText(fragment, fragment.awards, episode.getAwards(), 0, fragment.awardsLayout);
			}

			String actors = episode.getActors();
			String director = episode.getDirector();
			String writer = episode.getWriter();

			if (hasText(actors) || hasText(director) || hasText(writer)) {
				if (fragment.castingActors != null) {
					setText(fragment, fragment.castingActors, actors, R.string.actors, null);
				}

				if (fragment.castingDirectors != null) {
					setText(fragment, fragment.castingDirectors, director, R.string.directors, null);
				}

				if (fragment.castingLayout != null) {
					fragment.castingLayout.setVisibility(View.VISIBLE);
				}

				if (fragment.castingWriters != null) {
					setText(fragment, fragment.castingWriters, writer, R.string.writers, null);
				}
			} else {
				if (fragment.castingLayout != null) {
					fragment.castingLayout.setVisibility(View.GONE);
				}
			}

			if (fragment.genre != null) {
				setText(fragment, fragment.genre, episode.getGenre(), R.string.genre, null);
			}

			if (fragment.languageCountry != null) {
				String country = episode.getCountry();
				String language = episode.getLanguage();

				if (hasText(language)) {
					if (hasText(country)) {
						fragment.languageCountry.setText(fragment.getString(R.string.language_county, language, country));
					} else {
						fragment.languageCountry.setText(fragment.getString(R.string.language_value, language));
					}

					fragment.languageCountry.setVisibility(View.VISIBLE);
				} else {
					fragment.languageCountry.setVisibility(View.GONE);
				}
			}

			if (fragment.rated != null) {
				setText(fragment, fragment.rated, episode.getRated(), R.string.rated, null);
			}

			if (fragment.rating != null) {
				String imdbRating = episode.getImdbRating();
				String imdbVotes = episode.getImdbVotes();

				if (hasText(imdbRating) && hasText(imdbVotes)) {
					fragment.rating.setText(fragment.getString(R.string.rating, imdbRating, imdbVotes));
					fragment.rating.setVisibility(View.VISIBLE);
				} else {
					fragment.rating.setVisibility(View.GONE);
				}
			}

			if (fragment.runtime != null) {
				setText(fragment, fragment.runtime, episode.getRuntime(), R.string.runtime, null);
			}

			if (fragment.year != null) {
				setText(fragment, fragment.year, episode.getYear(), R.string.year, null);
			}
		}

		private static boolean hasText(String text) {
			return !TextUtils.isEmpty(text) && !"N/A".equalsIgnoreCase(text);
		}

		private static void setText(@NonNull Fragment fragment, @NonNull TextView textView, @Nullable String text, @StringRes int label, @Nullable View layout) {
			if (hasText(text)) {
				if (layout == null) {
					textView.setText(fragment.getString(label, text));
					textView.setVisibility(View.VISIBLE);
				} else {
					textView.setText(text);

					layout.setVisibility(View.VISIBLE);
				}
			} else {
				textView.setVisibility(View.GONE);

				if (layout != null) {
					layout.setVisibility(View.GONE);
				}
			}
		}
	}
}
