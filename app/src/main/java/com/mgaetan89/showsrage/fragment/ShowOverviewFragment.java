package com.mgaetan89.showsrage.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.BaseActivity;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.helper.GenericCallback;
import com.mgaetan89.showsrage.helper.ImageLoader;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.model.Quality;
import com.mgaetan89.showsrage.model.Serie;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.SingleShow;
import com.mgaetan89.showsrage.network.OmDbApi;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowOverviewFragment extends Fragment implements Callback<SingleShow>, View.OnClickListener, ImageLoader.OnImageResult, Palette.PaletteAsyncListener {
	@Nullable
	private TextView airs = null;

	@Nullable
	private TextView awards = null;

	@Nullable
	private CardView awardsLayout = null;

	@Nullable
	private ImageView banner = null;

	@Nullable
	private TextView castingActors = null;

	@Nullable
	private TextView castingDirectors = null;

	@Nullable
	private CardView castingLayout = null;

	@Nullable
	private TextView castingWriters = null;

	@Nullable
	private ImageView fanArt = null;

	@Nullable
	private TextView genre = null;

	@Nullable
	private Button imdb = null;

	@Nullable
	private TextView languageCountry = null;

	@Nullable
	private TextView location = null;

	@Nullable
	private TextView name = null;

	@Nullable
	private TextView network = null;

	@Nullable
	private TextView nextEpisodeDate = null;

	private OmDbApi omDbApi = null;

	@Nullable
	private MenuItem pauseMenu = null;

	@Nullable
	private TextView plot = null;

	@Nullable
	private CardView plotLayout = null;

	@Nullable
	private ImageView poster = null;

	@Nullable
	private TextView quality = null;

	@Nullable
	private TextView rated = null;

	@Nullable
	private TextView rating = null;

	@Nullable
	private MenuItem resumeMenu = null;

	@Nullable
	private TextView runtime = null;

	private Show show = null;

	@Nullable
	private TextView status = null;

	@Nullable
	private Button theTvDb = null;

	@Nullable
	private Button tvRage = null;

	@Nullable
	private TextView year = null;

	public ShowOverviewFragment() {
		this.setHasOptionsMenu(true);
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@NonNull
	public Callback<GenericResponse> getSetShowQualityCallback() {
		return new GenericCallback(this.getActivity());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint("http://www.omdbapi.com/")
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.build();

		this.omDbApi = restAdapter.create(OmDbApi.class);

		AppCompatActivity activity = (AppCompatActivity) this.getActivity();
		ActionBar actionBar = activity.getSupportActionBar();
		Intent intent = activity.getIntent();
		this.show = (Show) intent.getSerializableExtra(Constants.Bundle.SHOW_MODEL);

		if (actionBar != null) {
			actionBar.setTitle(this.show.getShowName());

			SickRageApi.getInstance().getServices().getShow(this.show.getIndexerId(), this);
		}
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		switch (view.getId()) {
			case R.id.show_imdb: {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.imdb.com/title/" + this.show.getImdbId()));

				this.startActivity(intent);

				break;
			}

			case R.id.show_the_tvdb: {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://thetvdb.com/?tab=series&id=" + this.show.getTvDbId()));

				this.startActivity(intent);

				break;
			}

			case R.id.show_tvrage: {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.tvrage.com/show/id-" + this.show.getTvRageId()));

				this.startActivity(intent);

				break;
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.show_overview, menu);

		this.pauseMenu = menu.findItem(R.id.menu_pause_show);
		this.pauseMenu.setVisible(false);
		this.resumeMenu = menu.findItem(R.id.menu_resume_show);
		this.resumeMenu.setVisible(false);

		if (this.show != null) {
			this.showHidePauseResumeMenus(this.show.getPaused() == 0);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_overview, container, false);

		if (view != null) {
			this.airs = (TextView) view.findViewById(R.id.show_airs);
			this.awards = (TextView) view.findViewById(R.id.show_awards);
			this.awardsLayout = (CardView) view.findViewById(R.id.show_awards_layout);
			this.banner = (ImageView) view.findViewById(R.id.show_banner);
			this.castingActors = (TextView) view.findViewById(R.id.show_casting_actors);
			this.castingDirectors = (TextView) view.findViewById(R.id.show_casting_directors);
			this.castingLayout = (CardView) view.findViewById(R.id.show_casting_layout);
			this.castingWriters = (TextView) view.findViewById(R.id.show_casting_writers);
			this.fanArt = (ImageView) view.findViewById(R.id.show_fan_art);
			this.genre = (TextView) view.findViewById(R.id.show_genre);
			this.imdb = (Button) view.findViewById(R.id.show_imdb);
			this.languageCountry = (TextView) view.findViewById(R.id.show_language_country);
			this.location = (TextView) view.findViewById(R.id.show_location);
			this.name = (TextView) view.findViewById(R.id.show_name);
			this.network = (TextView) view.findViewById(R.id.show_network);
			this.nextEpisodeDate = (TextView) view.findViewById(R.id.show_next_episode_date);
			this.plot = (TextView) view.findViewById(R.id.show_plot);
			this.plotLayout = (CardView) view.findViewById(R.id.show_plot_layout);
			this.poster = (ImageView) view.findViewById(R.id.show_poster);
			this.quality = (TextView) view.findViewById(R.id.show_quality);
			this.rated = (TextView) view.findViewById(R.id.show_rated);
			this.rating = (TextView) view.findViewById(R.id.show_rating);
			this.runtime = (TextView) view.findViewById(R.id.show_runtime);
			this.status = (TextView) view.findViewById(R.id.show_status);
			this.theTvDb = (Button) view.findViewById(R.id.show_the_tvdb);
			this.tvRage = (Button) view.findViewById(R.id.show_tvrage);
			this.year = (TextView) view.findViewById(R.id.show_year);


			if (this.imdb != null) {
				this.imdb.setOnClickListener(this);
			}

			if (this.theTvDb != null) {
				this.theTvDb.setOnClickListener(this);
			}

			if (this.tvRage != null) {
				this.tvRage.setOnClickListener(this);
			}
		}

		return view;
	}

	@Override
	public void onDestroyView() {
		this.airs = null;
		this.awards = null;
		this.awardsLayout = null;
		this.banner = null;
		this.castingActors = null;
		this.castingDirectors = null;
		this.castingLayout = null;
		this.castingWriters = null;
		this.fanArt = null;
		this.genre = null;
		this.imdb = null;
		this.languageCountry = null;
		this.location = null;
		this.name = null;
		this.nextEpisodeDate = null;
		this.network = null;
		this.plot = null;
		this.plotLayout = null;
		this.poster = null;
		this.quality = null;
		this.rated = null;
		this.rating = null;
		this.runtime = null;
		this.status = null;
		this.theTvDb = null;
		this.tvRage = null;
		this.year = null;

		super.onDestroyView();
	}

	@Override
	public void onGenerated(Palette palette) {
		FragmentActivity activity = this.getActivity();

		if (!(activity instanceof BaseActivity)) {
			return;
		}

		((BaseActivity) activity).setPalette(palette);

		int tintColor = this.getActivity().getIntent().getIntExtra(Constants.Bundle.COLOR_PRIMARY, 0);

		if (tintColor == 0) {
			return;
		}

		ColorStateList colorStateList = ColorStateList.valueOf(tintColor);

		if (this.imdb != null) {
			ViewCompat.setBackgroundTintList(this.imdb, colorStateList);
		}

		if (this.theTvDb != null) {
			ViewCompat.setBackgroundTintList(this.theTvDb, colorStateList);
		}

		if (this.tvRage != null) {
			ViewCompat.setBackgroundTintList(this.tvRage, colorStateList);
		}
	}

	@Override
	public void onImageError(ImageView imageView, @Nullable Exception exception, @Nullable Drawable errorDrawable) {
		if (imageView != null) {
			ViewParent parent = imageView.getParent();

			if (parent instanceof View) {
				((View) parent).setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onImageReady(ImageView imageView, Bitmap resource) {
		if (imageView != null) {
			ViewParent parent = imageView.getParent();

			if (parent instanceof View) {
				((View) parent).setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_change_quality:
				this.changeQuality();

				return true;

			case R.id.menu_delete_show:
				this.deleteShow();

				return true;

			case R.id.menu_pause_show:
				this.pauseOrResumeShow(true);

				return true;

			case R.id.menu_resume_show:
				this.pauseOrResumeShow(false);

				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void success(SingleShow singleShow, Response response) {
		this.show = singleShow.getData();
		String nextEpisodeAirDate = this.show.getNextEpisodeAirDate();

		this.showHidePauseResumeMenus(this.show.getPaused() == 0);

		this.omDbApi.getShow(this.show.getImdbId(), new OmdbShowCallback(this));

		if (this.airs != null) {
			String airs = this.show.getAirs();

			if (TextUtils.isEmpty(airs)) {
				this.airs.setText(this.getString(R.string.airs, "N/A"));
			} else {
				this.airs.setText(this.getString(R.string.airs, airs));
			}

			this.airs.setVisibility(View.VISIBLE);
		}

		if (this.banner != null) {
			ImageLoader.load(
					this.banner,
					SickRageApi.getInstance().getBannerUrl(this.show.getTvDbId(), Indexer.TVDB),
					false,
					null,
					this
			);

			this.banner.setContentDescription(this.show.getShowName());
		}

		if (this.fanArt != null) {
			ImageLoader.load(
					this.fanArt,
					SickRageApi.getInstance().getFanArtUrl(this.show.getTvDbId(), Indexer.TVDB),
					false,
					null,
					this
			);

			this.fanArt.setContentDescription(this.show.getShowName());
		}

		if (this.genre != null) {
			String genres = "";
			List<String> genresList = this.show.getGenre();

			for (int i = 0; i < genresList.size(); i++) {
				genres += genresList.get(i);

				if (i + 1 < genresList.size()) {
					genres += ", ";
				}
			}

			this.genre.setText(this.getString(R.string.genre, genres));
			this.genre.setVisibility(View.VISIBLE);
		}

		if (this.imdb != null) {
			if (TextUtils.isEmpty(this.show.getImdbId())) {
				this.imdb.setVisibility(View.GONE);
			} else {
				this.imdb.setVisibility(View.VISIBLE);
			}
		}

		if (this.languageCountry != null) {
			this.languageCountry.setText(this.getString(R.string.language_value, this.show.getLanguage()));
			this.languageCountry.setVisibility(View.VISIBLE);
		}

		if (this.location != null) {
			String location = this.show.getLocation();

			if (TextUtils.isEmpty(location)) {
				this.location.setText(this.getString(R.string.location, "N/A"));
			} else {
				this.location.setText(this.getString(R.string.location, location));
			}

			this.location.setVisibility(View.VISIBLE);
		}

		if (this.name != null) {
			this.name.setText(this.show.getShowName());
			this.name.setVisibility(View.VISIBLE);
		}

		if (this.nextEpisodeDate != null) {
			if (TextUtils.isEmpty(nextEpisodeAirDate)) {
				this.nextEpisodeDate.setVisibility(View.GONE);
			} else {
				this.nextEpisodeDate.setText(this.getString(R.string.next_episode, DateTimeHelper.getRelativeDate(nextEpisodeAirDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)));
				this.nextEpisodeDate.setVisibility(View.VISIBLE);
			}
		}

		if (this.network != null) {
			this.network.setText(this.getString(R.string.network, this.show.getNetwork()));
			this.network.setVisibility(View.VISIBLE);
		}

		if (this.poster != null) {
			ImageLoader.load(
					this.poster,
					SickRageApi.getInstance().getPosterUrl(this.show.getTvDbId(), Indexer.TVDB),
					false,
					this,
					null
			);

			this.poster.setContentDescription(this.show.getShowName());
		}

		if (this.quality != null) {
			if ("custom".equalsIgnoreCase(this.show.getQuality())) {
				Quality qualityDetails = this.show.getQualityDetails();
				String allowed = listToString(this.getTranslatedQualities(qualityDetails.getInitial(), true));
				String preferred = listToString(this.getTranslatedQualities(qualityDetails.getArchive(), false));

				this.quality.setText(this.getString(R.string.quality_custom, allowed, preferred));
			} else {
				this.quality.setText(this.getString(R.string.quality, this.show.getQuality()));
			}

			this.quality.setVisibility(View.VISIBLE);
		}

		if (this.status != null) {
			if (TextUtils.isEmpty(nextEpisodeAirDate)) {
				this.status.setText(this.getString(R.string.status_value, this.show.getStatus()));
				this.status.setVisibility(View.VISIBLE);
			} else {
				this.status.setVisibility(View.GONE);
			}
		}
	}

	private void changeQuality() {
		Bundle arguments = new Bundle();
		arguments.putInt(Constants.Bundle.INDEXER_ID, this.show.getIndexerId());

		ChangeQualityFragment fragment = new ChangeQualityFragment();
		fragment.setArguments(arguments);
		fragment.show(this.getChildFragmentManager(), "change_quality");
	}

	private void deleteShow() {
		final int indexerId = this.show.getIndexerId();
		final Callback<GenericResponse> callback = new DeleteShowCallback(this.getActivity());

		new AlertDialog.Builder(this.getActivity())//
				.setTitle(this.getString(R.string.delete_show_title, this.show.getShowName()))//
				.setMessage(R.string.delete_show_message)//
				.setPositiveButton(R.string.keep, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SickRageApi.getInstance().getServices().deleteShow(indexerId, 0, callback);
					}
				})//
				.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SickRageApi.getInstance().getServices().deleteShow(indexerId, 1, callback);
					}
				})//
				.setNeutralButton(R.string.cancel, null)//
				.show();
	}

	@NonNull
	private List<String> getTranslatedQualities(@Nullable Collection<String> qualities, boolean allowed) {
		List<String> translatedQualities = new ArrayList<>();

		if (qualities == null || qualities.isEmpty()) {
			return translatedQualities;
		}

		List<String> keys;
		List<String> values;
		Resources resources = this.getResources();

		if (allowed) {
			keys = Arrays.asList(resources.getStringArray(R.array.allowed_qualities_keys));
			values = Arrays.asList(resources.getStringArray(R.array.allowed_qualities_values));
		} else {
			keys = Arrays.asList(resources.getStringArray(R.array.allowed_qualities_keys));
			values = Arrays.asList(resources.getStringArray(R.array.allowed_qualities_values));
		}

		for (String quality : qualities) {
			int position = keys.indexOf(quality);

			if (position != -1) {
				// Skip the "Ignore" first item
				translatedQualities.add(values.get(position + 1));
			}
		}

		return translatedQualities;
	}

	@NonNull
	private static String listToString(@Nullable List<String> list) {
		if (list == null || list.isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder();

		for (int i = 0, n = list.size(); i < n; i++) {
			builder.append(list.get(i));

			if (i < n - 1) {
				builder.append(", ");
			}
		}

		return builder.toString();
	}

	private void pauseOrResumeShow(final boolean pause) {
		this.showHidePauseResumeMenus(!pause);

		SickRageApi.getInstance().getServices().pauseShow(this.show.getIndexerId(), pause ? 1 : 0, new GenericCallback(this.getActivity()) {
			@Override
			public void failure(RetrofitError error) {
				super.failure(error);

				ShowOverviewFragment.this.showHidePauseResumeMenus(pause);
			}
		});
	}

	private void showHidePauseResumeMenus(boolean isPause) {
		if (this.pauseMenu != null) {
			this.pauseMenu.setVisible(isPause);
		}

		if (this.resumeMenu != null) {
			this.resumeMenu.setVisible(!isPause);
		}
	}

	private static final class DeleteShowCallback extends GenericCallback {
		private DeleteShowCallback(FragmentActivity activity) {
			super(activity);
		}

		@Override
		public void success(GenericResponse genericResponse, Response response) {
			super.success(genericResponse, response);

			NavUtils.navigateUpFromSameTask(this.getActivity());
		}
	}

	private static final class OmdbShowCallback implements Callback<Serie> {
		private WeakReference<ShowOverviewFragment> fragmentReference = null;

		private OmdbShowCallback(ShowOverviewFragment fragment) {
			this.fragmentReference = new WeakReference<>(fragment);
		}

		@Override
		public void failure(RetrofitError error) {
			error.printStackTrace();
		}

		@Override
		public void success(Serie serie, Response response) {
			ShowOverviewFragment fragment = this.fragmentReference.get();

			if (fragment == null) {
				return;
			}

			String actors = serie.getActors();
			String director = serie.getDirector();
			String writer = serie.getWriter();
			boolean hasActors = !"N/A".equalsIgnoreCase(actors);
			boolean hasDirectors = !"N/A".equalsIgnoreCase(director);
			boolean hasWriters = !"N/A".equalsIgnoreCase(writer);

			if (hasActors || hasDirectors || hasWriters) {
				if (fragment.awards != null) {
					String awardsText = serie.getAwards();

					if ("N/A".equalsIgnoreCase(awardsText)) {
						if (fragment.awardsLayout != null) {
							fragment.awardsLayout.setVisibility(View.GONE);
						}
					} else {
						fragment.awards.setText(awardsText);

						if (fragment.awardsLayout != null) {
							fragment.awardsLayout.setVisibility(View.VISIBLE);
						}
					}
				}

				if (fragment.castingActors != null) {
					if (hasActors) {
						fragment.castingActors.setText(fragment.getString(R.string.actors, actors));
						fragment.castingActors.setVisibility(View.VISIBLE);
					} else {
						fragment.castingActors.setVisibility(View.GONE);
					}
				}

				if (fragment.castingDirectors != null) {
					if (hasDirectors) {
						fragment.castingDirectors.setText(fragment.getString(R.string.directors, director));
						fragment.castingDirectors.setVisibility(View.VISIBLE);
					} else {
						fragment.castingDirectors.setVisibility(View.GONE);
					}
				}

				if (fragment.castingLayout != null) {
					fragment.castingLayout.setVisibility(View.VISIBLE);
				}

				if (fragment.castingWriters != null) {
					if (hasWriters) {
						fragment.castingWriters.setText(fragment.getString(R.string.writers, writer));
						fragment.castingWriters.setVisibility(View.VISIBLE);
					} else {
						fragment.castingWriters.setVisibility(View.GONE);
					}
				}
			} else {
				if (fragment.castingLayout != null) {
					fragment.castingLayout.setVisibility(View.GONE);
				}
			}

			if (fragment.languageCountry != null) {
				fragment.languageCountry.setText(fragment.getString(R.string.language_county, serie.getLanguage(), serie.getCountry()));
				fragment.languageCountry.setVisibility(View.VISIBLE);
			}

			if (fragment.plot != null) {
				String plotText = serie.getPlot();

				if ("N/A".equalsIgnoreCase(plotText)) {
					if (fragment.plotLayout != null) {
						fragment.plotLayout.setVisibility(View.GONE);
					}
				} else {
					fragment.plot.setText(plotText);

					if (fragment.plotLayout != null) {
						fragment.plotLayout.setVisibility(View.VISIBLE);
					}
				}
			}

			if (fragment.rated != null) {
				fragment.rated.setText(fragment.getString(R.string.rated, serie.getRated()));
				fragment.rated.setVisibility(View.VISIBLE);
			}

			if (fragment.rating != null) {
				fragment.rating.setText(fragment.getString(R.string.rating, serie.getImdbRating(), serie.getImdbVotes()));
				fragment.rating.setVisibility(View.VISIBLE);
			}

			if (fragment.runtime != null) {
				fragment.runtime.setText(fragment.getString(R.string.runtime, serie.getRuntime()));
				fragment.runtime.setVisibility(View.VISIBLE);
			}

			if (fragment.year != null) {
				fragment.year.setText(fragment.getString(R.string.year, serie.getYear()));
				fragment.year.setVisibility(View.VISIBLE);
			}
		}
	}
}
