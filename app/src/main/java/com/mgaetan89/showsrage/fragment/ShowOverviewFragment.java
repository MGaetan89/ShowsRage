package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Serie;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.SingleShow;
import com.mgaetan89.showsrage.network.OmDbApi;
import com.mgaetan89.showsrage.network.SickRageApi;

import me.gujun.android.taggroup.TagGroup;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowOverviewFragment extends Fragment implements Callback<SingleShow>, View.OnClickListener {
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

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_overview, container, false);

		if (view != null) {
			this.airs = (TextView) view.findViewById(R.id.show_airs);
			this.awards = (TextView) view.findViewById(R.id.show_awards);
			this.awardsLayout = (CardView) view.findViewById(R.id.show_awards_layout);
			this.castingActors = (TextView) view.findViewById(R.id.show_casting_actors);
			this.castingDirectors = (TextView) view.findViewById(R.id.show_casting_directors);
			this.castingLayout = (CardView) view.findViewById(R.id.show_casting_layout);
			this.castingWriters = (TextView) view.findViewById(R.id.show_casting_writers);
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
		this.awards = null;
		this.awardsLayout = null;
		this.castingActors = null;
		this.castingDirectors = null;
		this.castingLayout = null;
		this.castingWriters = null;
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
				String actors = serie.getActors();
				String director = serie.getDirector();
				String writer = serie.getWriter();
				boolean hasActors = !"N/A".equalsIgnoreCase(actors);
				boolean hasDirectors = !"N/A".equalsIgnoreCase(director);
				boolean hasWriters = !"N/A".equalsIgnoreCase(writer);

				if (hasActors || hasDirectors || hasWriters) {
					if (awards != null) {
						String awardsText = serie.getAwards();

						if ("N/A".equalsIgnoreCase(awardsText)) {
							if (awardsLayout != null) {
								awardsLayout.setVisibility(View.GONE);
							}
						} else {
							awards.setText(awardsText);

							if (awardsLayout != null) {
								awardsLayout.setVisibility(View.VISIBLE);
							}
						}
					}

					if (castingActors != null) {
						if (hasActors) {
							castingActors.setText(getString(R.string.actors, actors));
							castingActors.setVisibility(View.VISIBLE);
						} else {
							castingActors.setVisibility(View.GONE);
						}
					}

					if (castingDirectors != null) {
						if (hasDirectors) {
							castingDirectors.setText(getString(R.string.directors, director));
							castingDirectors.setVisibility(View.VISIBLE);
						} else {
							castingDirectors.setVisibility(View.GONE);
						}
					}

					if (castingLayout != null) {
						castingLayout.setVisibility(View.VISIBLE);
					}

					if (castingWriters != null) {
						if (hasWriters) {
							castingWriters.setText(getString(R.string.writers, writer));
							castingWriters.setVisibility(View.VISIBLE);
						} else {
							castingWriters.setVisibility(View.GONE);
						}
					}
				} else {
					if (castingLayout != null) {
						castingLayout.setVisibility(View.GONE);
					}
				}

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
			String airs = this.show.getAirs();

			if (TextUtils.isEmpty(airs)) {
				this.airs.setText(this.getString(R.string.airs, "N/A"));
			} else {
				this.airs.setText(this.getString(R.string.airs, airs));
			}

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
			Glide.with(this)//
					.load(SickRageApi.getInstance().getApiUrl() + "?cmd=show.getposter&tvdbid=" + this.show.getTvDbId())//
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
