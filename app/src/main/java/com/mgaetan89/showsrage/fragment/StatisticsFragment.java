package com.mgaetan89.showsrage.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.ShowsStat;
import com.mgaetan89.showsrage.model.ShowsStats;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.text.NumberFormat;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StatisticsFragment extends DialogFragment implements Callback<ShowsStats> {
	@Nullable
	private TextView episodesDownloaded = null;

	@Nullable
	private View episodesDownloadedBar = null;

	@Nullable
	private TextView episodesMissing = null;

	@Nullable
	private View episodesMissingBar = null;

	@Nullable
	private TextView episodesSnatched = null;

	@Nullable
	private View episodesSnatchedBar = null;

	@Nullable
	private TextView episodesTotal = null;

	@Nullable
	private LinearLayout progressLayout = null;

	@Nullable
	private TextView showsActive = null;

	@Nullable
	private TextView showsTotal = null;

	@Nullable
	private LinearLayout statisticsLayout = null;

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SickRageApi.getInstance().getServices().getShowsStats(this);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(this.getActivity());
		View view = inflater.inflate(R.layout.fragment_statistics, null);

		if (view != null) {
			this.episodesDownloaded = (TextView) view.findViewById(R.id.episodes_downloaded);
			this.episodesDownloadedBar = view.findViewById(R.id.episodes_downloaded_bar);
			this.episodesMissing = (TextView) view.findViewById(R.id.episodes_missing);
			this.episodesMissingBar = view.findViewById(R.id.episodes_missing_bar);
			this.episodesSnatched = (TextView) view.findViewById(R.id.episodes_snatched);
			this.episodesSnatchedBar = view.findViewById(R.id.episodes_snatched_bar);
			this.episodesTotal = (TextView) view.findViewById(R.id.episodes_total);
			this.progressLayout = (LinearLayout) view.findViewById(R.id.progress_layout);
			this.showsActive = (TextView) view.findViewById(R.id.shows_active);
			this.showsTotal = (TextView) view.findViewById(R.id.shows_total);
			this.statisticsLayout = (LinearLayout) view.findViewById(R.id.statistics_layout);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setTitle(R.string.statistics);
		builder.setView(view);
		builder.setPositiveButton(android.R.string.ok, null);

		return builder.create();
	}

	@Override
	public void onDestroyView() {
		this.episodesDownloaded = null;
		this.episodesDownloadedBar = null;
		this.episodesMissing = null;
		this.episodesMissingBar = null;
		this.episodesSnatched = null;
		this.episodesSnatchedBar = null;
		this.episodesTotal = null;
		this.progressLayout = null;
		this.showsActive = null;
		this.showsTotal = null;
		this.statisticsLayout = null;

		super.onDestroyView();
	}

	@Override
	public void success(ShowsStats showsStats, Response response) {
		ShowsStat showsStat = showsStats.getData();
		int episodesDownloaded = showsStat.getEpisodesDownloaded();
		int episodesMissing = showsStat.getEpisodesMissing();
		int episodesSnatched = showsStat.getEpisodesSnatched();
		int episodesTotal = showsStat.getEpisodesTotal();
		float weightDownloaded = (float) episodesDownloaded / episodesTotal;
		float weightMissing = (float) episodesMissing / episodesTotal;
		float weightSnatched = (float) episodesSnatched / episodesTotal;
		NumberFormat numberFormat = NumberFormat.getInstance();

		if (this.episodesDownloaded != null) {
			this.episodesDownloaded.setText(this.getString(R.string.downloaded_count, numberFormat.format(episodesDownloaded)));
		}

		if (this.episodesDownloadedBar != null) {
			ViewGroup.LayoutParams layoutParams = this.episodesDownloadedBar.getLayoutParams();

			if (layoutParams instanceof LinearLayout.LayoutParams) {
				((LinearLayout.LayoutParams) layoutParams).weight = weightDownloaded;
			}
		}

		if (this.episodesMissing != null) {
			this.episodesMissing.setText(this.getString(R.string.missing, numberFormat.format(episodesMissing)));
		}

		if (this.episodesMissingBar != null) {
			ViewGroup.LayoutParams layoutParams = this.episodesMissingBar.getLayoutParams();

			if (layoutParams instanceof LinearLayout.LayoutParams) {
				((LinearLayout.LayoutParams) layoutParams).weight = weightMissing;
			}
		}

		if (this.episodesSnatched != null) {
			this.episodesSnatched.setText(this.getString(R.string.snatched_count, numberFormat.format(episodesSnatched)));
		}

		if (this.episodesSnatchedBar != null) {
			ViewGroup.LayoutParams layoutParams = this.episodesSnatchedBar.getLayoutParams();

			if (layoutParams instanceof LinearLayout.LayoutParams) {
				((LinearLayout.LayoutParams) layoutParams).weight = weightSnatched;
			}
		}

		if (this.episodesTotal != null) {
			this.episodesTotal.setText(this.getString(R.string.total, numberFormat.format(episodesTotal)));
		}

		if (this.progressLayout != null) {
			this.progressLayout.setVisibility(View.GONE);
		}

		if (this.showsActive != null) {
			this.showsActive.setText(this.getString(R.string.active, numberFormat.format(showsStat.getShowsActive())));
		}

		if (this.showsTotal != null) {
			this.showsTotal.setText(this.getString(R.string.total, numberFormat.format(showsStat.getShowsTotal())));
		}

		if (this.statisticsLayout != null) {
			this.statisticsLayout.setVisibility(View.VISIBLE);
		}
	}
}
