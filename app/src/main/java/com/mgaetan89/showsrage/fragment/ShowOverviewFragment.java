package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Serie;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.SingleShow;
import com.mgaetan89.showsrage.network.OmDbApi;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import me.gujun.android.taggroup.TagGroup;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowOverviewFragment extends Fragment implements Callback<SingleShow>, View.OnClickListener {
	@Inject
	public SickRageApi api;

	@Nullable
	private TextView airs = null;

	@Nullable
	private TagGroup genre = null;

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
	private TextView runtime = null;

	private Show show = null;

	@Nullable
	private TextView status = null;

	@Nullable
	private TextView year = null;

	public ShowOverviewFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
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
		final ActionBar actionBar = activity.getSupportActionBar();
		Intent intent = activity.getIntent();
		this.show = (Show) intent.getSerializableExtra(Constants.Bundle.SHOW_MODEL);

		if (actionBar != null) {
			actionBar.setTitle(this.show.getShowName());

			this.api.getServices().getShow(this.show.getIndexerId(), this);
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((ShowsRageApplication) this.getActivity().getApplication()).inject(this);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_overview, container, false);

		if (view != null) {
			this.airs = (TextView) view.findViewById(R.id.show_airs);
			this.genre = (TagGroup) view.findViewById(R.id.show_genre);
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
			this.year = (TextView) view.findViewById(R.id.show_year);

			ObservableScrollView scroll = (ObservableScrollView) view.findViewById(R.id.scroll);

			if (scroll != null) {
				MaterialViewPagerHelper.registerScrollView(getActivity(), scroll, null);
			}


			Button imdb = (Button) view.findViewById(R.id.show_imdb);
			Button theTvDb = (Button) view.findViewById(R.id.show_the_tvdb);
			Button tvRage = (Button) view.findViewById(R.id.show_tvrage);

			if (imdb != null) {
				imdb.setOnClickListener(this);
			}

			if (theTvDb != null) {
				theTvDb.setOnClickListener(this);
			}

			if (tvRage != null) {
				tvRage.setOnClickListener(this);
			}
		}

		return view;
	}

	@Override
	public void onDestroyView() {
		this.airs = null;
		this.genre = null;
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
		this.year = null;

		super.onDestroyView();
	}

	@Override
	public void success(SingleShow singleShow, Response response) {
		this.show = singleShow.getData();
		String nextEpisodeAirDate = this.show.getNextEpisodeAirDate();

		this.omDbApi.getShow(this.show.getImdbId(), new Callback<Serie>() {
			@Override
			public void failure(RetrofitError error) {
				error.printStackTrace();
			}

			@Override
			public void success(Serie serie, Response response) {
				if (languageCountry != null) {
					languageCountry.setText(getString(R.string.language_county, serie.getLanguage(), serie.getCountry()));
					languageCountry.setVisibility(View.VISIBLE);
				}

				if (plot != null) {
					String plotText = serie.getPlot();

					if ("N/A".equalsIgnoreCase(plotText)) {
						if (plotLayout != null) {
							plotLayout.setVisibility(View.GONE);
						}
					} else {
						plot.setText(plotText);

						if (plotLayout != null) {
							plotLayout.setVisibility(View.VISIBLE);
						}
					}
				}

				if (rated != null) {
					rated.setText(getString(R.string.rated, serie.getRated()));
					rated.setVisibility(View.VISIBLE);
				}

				if (rating != null) {
					rating.setText(getString(R.string.rating, serie.getImdbRating(), serie.getImdbVotes()));
					rating.setVisibility(View.VISIBLE);
				}

				if (runtime != null) {
					runtime.setText(getString(R.string.runtime, serie.getRuntime()));
					runtime.setVisibility(View.VISIBLE);
				}

				if (year != null) {
					year.setText(getString(R.string.year, serie.getYear()));
					year.setVisibility(View.VISIBLE);
				}
			}
		});

		if (this.airs != null) {
			this.airs.setText(this.getString(R.string.airs, this.show.getAirs()));
			this.airs.setVisibility(View.VISIBLE);
		}

		if (this.genre != null) {
			this.genre.setTags(this.show.getGenre());
			this.genre.setVisibility(View.VISIBLE);
		}

		if (this.languageCountry != null) {
			this.languageCountry.setText(this.getString(R.string.language, this.show.getLanguage()));
			this.languageCountry.setVisibility(View.VISIBLE);
		}

		if (this.location != null) {
			this.location.setText(this.getString(R.string.location, this.show.getLocation()));
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
			Picasso.with(this.getActivity())//
					.load(this.api.getBaseUrl() + "?cmd=show.getposter&tvdbid=" + this.show.getTvDbId())//
					.into(this.poster);

			this.poster.setContentDescription(this.show.getShowName());
		}

		if (this.status != null) {
			if (TextUtils.isEmpty(nextEpisodeAirDate)) {
				this.status.setText(this.getString(R.string.status, this.show.getStatus()));
				this.status.setVisibility(View.VISIBLE);
			} else {
				this.status.setVisibility(View.GONE);
			}
		}

		if (this.quality != null) {
			this.quality.setText(this.getString(R.string.quality, this.show.getQuality()));
			this.quality.setVisibility(View.VISIBLE);
		}
	}
}
