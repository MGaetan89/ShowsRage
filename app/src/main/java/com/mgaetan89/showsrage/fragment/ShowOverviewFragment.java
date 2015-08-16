package com.mgaetan89.showsrage.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.BaseActivity;
import com.mgaetan89.showsrage.databinding.FragmentShowOverviewBinding;
import com.mgaetan89.showsrage.helper.GenericCallback;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.model.Serie;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.SingleShow;
import com.mgaetan89.showsrage.network.OmDbApi;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mgaetan89.showsrage.presenter.ShowPresenter;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowOverviewFragment extends Fragment implements Callback<SingleShow>, Palette.PaletteAsyncListener {
	@Nullable
	private TextView awards = null;

	@Nullable
	private CardView awardsLayout = null;

	private FragmentShowOverviewBinding binding = null;

	@Nullable
	private TextView castingActors = null;

	@Nullable
	private TextView castingDirectors = null;

	@Nullable
	private CardView castingLayout = null;

	@Nullable
	private TextView castingWriters = null;

	@Nullable
	private Button imdb = null;

	@Nullable
	private TextView languageCountry = null;

	private OmDbApi omDbApi = null;

	@Nullable
	private MenuItem pauseMenu = null;

	@Nullable
	private TextView plot = null;

	@Nullable
	private CardView plotLayout = null;

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
				.setLogLevel(Constants.NETWORK_LOG_LEVEL)
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
		this.binding = FragmentShowOverviewBinding.inflate(inflater, container, false);

		this.awards = this.binding.showAwards; // TODO
		this.awardsLayout = this.binding.showAwardsLayout; // TODO
		this.castingActors = this.binding.showCastingActors; // TODO
		this.castingDirectors = this.binding.showCastingDirectors; // TODO
		this.castingLayout = this.binding.showCastingLayout; // TODO
		this.castingWriters = this.binding.showCastingWriters; // TODO
		this.imdb = this.binding.showImdb;
		this.languageCountry = this.binding.showLanguageCountry; // TODO
		this.plot = this.binding.showPlot; // TODO
		this.plotLayout = this.binding.showPlotLayout; // TODO
		this.rated = this.binding.showRated; // TODO
		this.rating = this.binding.showRating; // TODO
		this.runtime = this.binding.showRuntime; // TODO
		this.theTvDb = this.binding.showTheTvdb;
		this.tvRage = this.binding.showTvrage;
		this.year = this.binding.showYear; // TODO

		return this.binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		this.awards = null;
		this.awardsLayout = null;
		this.castingActors = null;
		this.castingDirectors = null;
		this.castingLayout = null;
		this.castingWriters = null;
		this.imdb = null;
		this.languageCountry = null;
		this.plot = null;
		this.plotLayout = null;
		this.rated = null;
		this.rating = null;
		this.runtime = null;
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

			case R.id.menu_rescan_show:
				this.rescanShow();

				return true;

			case R.id.menu_resume_show:
				this.pauseOrResumeShow(false);

				return true;

			case R.id.menu_update_show:
				this.updateShow();

				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void success(SingleShow singleShow, Response response) {
		this.show = singleShow.getData();

		this.showHidePauseResumeMenus(this.show.getPaused() == 0);

		this.binding.setShow(new ShowPresenter(this.show, this.getActivity()));

		this.omDbApi.getShow(this.show.getImdbId(), new OmdbShowCallback(this));

		if (this.languageCountry != null) {
			this.languageCountry.setText(this.getString(R.string.language_value, this.show.getLanguage()));
			this.languageCountry.setVisibility(View.VISIBLE);
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

	private void rescanShow() {
		if (this.show != null) {
			SickRageApi.getInstance().getServices().rescanShow(this.show.getIndexerId(), new GenericCallback(this.getActivity()));
		}
	}

	private void showHidePauseResumeMenus(boolean isPause) {
		if (this.pauseMenu != null) {
			this.pauseMenu.setVisible(isPause);
		}

		if (this.resumeMenu != null) {
			this.resumeMenu.setVisible(!isPause);
		}
	}

	private void updateShow() {
		if (this.show != null) {
			SickRageApi.getInstance().getServices().updateShow(this.show.getIndexerId(), new GenericCallback(this.getActivity()));
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

			if (fragment.awards != null) {
				setText(fragment, fragment.awards, serie.getAwards(), 0, fragment.awardsLayout);
			}

			String actors = serie.getActors();
			String director = serie.getDirector();
			String writer = serie.getWriter();

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

			if (fragment.languageCountry != null) {
				String country = serie.getCountry();
				String language = serie.getLanguage();

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

			if (fragment.plot != null) {
				setText(fragment, fragment.plot, serie.getPlot(), 0, fragment.plotLayout);
			}

			if (fragment.rated != null) {
				setText(fragment, fragment.rated, serie.getRated(), R.string.rated, null);
			}

			if (fragment.rating != null) {
				String imdbRating = serie.getImdbRating();
				String imdbVotes = serie.getImdbVotes();

				if (hasText(imdbRating) && hasText(imdbVotes)) {
					fragment.rating.setText(fragment.getString(R.string.rating, imdbRating, imdbVotes));
					fragment.rating.setVisibility(View.VISIBLE);
				} else {
					fragment.rating.setVisibility(View.GONE);
				}
			}

			if (fragment.runtime != null) {
				setText(fragment, fragment.runtime, serie.getRuntime(), R.string.runtime, null);
			}

			if (fragment.year != null) {
				setText(fragment, fragment.year, serie.getYear(), R.string.year, null);
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
