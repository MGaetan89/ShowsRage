package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.EpisodesAdapter;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.model.Episodes;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SeasonFragment extends Fragment implements Callback<Episodes>, SwipeRefreshLayout.OnRefreshListener {
	@Nullable
	private EpisodesAdapter adapter = null;

	@Nullable
	private TextView emptyView = null;

	@NonNull
	private final List<Episode> episodes = new ArrayList<>();

	private int indexerId = 0;

	@Nullable
	private RecyclerView recyclerView = null;

	private int seasonNumber = 0;

	@Nullable
	private SwipeRefreshLayout swipeRefreshLayout = null;

	public SeasonFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		if (this.swipeRefreshLayout != null) {
			this.swipeRefreshLayout.setRefreshing(false);
		}

		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Intent intent = this.getActivity().getIntent();
		Show show = (Show) intent.getSerializableExtra(Constants.Bundle.SHOW_MODEL);

		this.indexerId = show.getIndexerId();

		this.onRefresh();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_season, container, false);

		this.seasonNumber = this.getArguments().getInt(Constants.Bundle.SEASON_NUMBER, 0);

		if (view != null) {
			this.emptyView = (TextView) view.findViewById(android.R.id.empty);
			this.recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
			this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

			if (this.recyclerView != null) {
				int columnCount = this.getResources().getInteger(R.integer.shows_column_count);
				this.adapter = new EpisodesAdapter(this.episodes, this.seasonNumber);

				this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
					@Override
					public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
						super.onScrolled(recyclerView, dx, dy);

						if (SeasonFragment.this.swipeRefreshLayout != null) {
							SeasonFragment.this.swipeRefreshLayout.setEnabled(!recyclerView.canScrollVertically(-1));
						}
					}
				});
				this.recyclerView.setAdapter(this.adapter);
				this.recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), columnCount));
			}

			if (this.swipeRefreshLayout != null) {
				this.swipeRefreshLayout.setColorSchemeResources(R.color.accent);
				this.swipeRefreshLayout.setOnRefreshListener(this);
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.episodes.clear();

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		this.emptyView = null;
		this.recyclerView = null;
		this.swipeRefreshLayout = null;

		super.onDestroyView();
	}

	@Override
	public void onRefresh() {
		if (this.swipeRefreshLayout != null) {
			this.swipeRefreshLayout.setRefreshing(true);
		}

		SickRageApi.getInstance().getServices().getEpisodes(this.indexerId, this.seasonNumber, this);
	}

	@Override
	public void success(Episodes episodes, Response response) {
		if (this.swipeRefreshLayout != null) {
			this.swipeRefreshLayout.setRefreshing(false);
		}

		this.episodes.clear();

		if (episodes != null) {
			this.episodes.addAll(episodes.getData().values());

			Collections.reverse(this.episodes);
		}

		if (this.episodes.isEmpty()) {
			if (this.emptyView != null) {
				this.emptyView.setVisibility(View.VISIBLE);
			}

			if (this.recyclerView != null) {
				this.recyclerView.setVisibility(View.GONE);
			}
		} else {
			if (this.emptyView != null) {
				this.emptyView.setVisibility(View.GONE);
			}

			if (this.recyclerView != null) {
				this.recyclerView.setVisibility(View.VISIBLE);
			}
		}

		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged();
		}
	}
}
