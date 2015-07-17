package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.AddShowActivity;
import com.mgaetan89.showsrage.adapter.ShowsAdapter;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.ShowStat;
import com.mgaetan89.showsrage.model.ShowStatWrapper;
import com.mgaetan89.showsrage.model.ShowStats;
import com.mgaetan89.showsrage.model.ShowStatsWrapper;
import com.mgaetan89.showsrage.model.Shows;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowsFragment extends Fragment implements Callback<Shows>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
	@Nullable
	private ShowsAdapter adapter = null;

	@Nullable
	private TextView emptyView = null;

	@Nullable
	private RecyclerView recyclerView = null;

	@NonNull
	private final List<Show> shows = new ArrayList<>();

	@Nullable
	private SwipeRefreshLayout swipeRefreshLayout = null;

	public ShowsFragment() {
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

		this.onRefresh();
	}

	@Override
	public void onClick(View view) {
		if (view == null) {
			return;
		}

		if (view.getId() == R.id.add_show) {
			Intent intent = new Intent(this.getActivity(), AddShowActivity.class);

			this.startActivity(intent);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shows, container, false);

		if (view != null) {
			this.emptyView = (TextView) view.findViewById(android.R.id.empty);
			this.recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
			this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

			FloatingActionButton addShow = (FloatingActionButton) view.findViewById(R.id.add_show);

			if (addShow != null) {
				addShow.setOnClickListener(this);
			}

			if (this.recyclerView != null) {
				int columnCount = this.getResources().getInteger(R.integer.shows_column_count);
				this.adapter = new ShowsAdapter(this.shows);

				this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
					@Override
					public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
						super.onScrolled(recyclerView, dx, dy);

						if (swipeRefreshLayout != null) {
							swipeRefreshLayout.setEnabled(!recyclerView.canScrollVertically(-1));
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
		this.shows.clear();

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

		SickRageApi.getInstance().getServices().getShows(this);
	}

	@Override
	public void success(Shows shows, Response response) {
		if (this.swipeRefreshLayout != null) {
			this.swipeRefreshLayout.setRefreshing(false);
		}

		this.shows.clear();

		if (shows != null) {
			this.shows.addAll(shows.getData().values());

			String command = getCommand(this.shows);
			Map<String, Integer> parameters = getCommandParameters(this.shows);

			SickRageApi.getInstance().getServices().getShowStats(command, parameters, new Callback<ShowStatsWrapper>() {
				@Override
				public void failure(RetrofitError error) {
					error.printStackTrace();
				}

				@Override
				public void success(ShowStatsWrapper showStatsWrapper, Response response) {
					ShowStatWrapper data = showStatsWrapper.getData();
					Map<Integer, ShowStats> showStats = data.getShowStats();

					if (showStats != null) {
						for (Map.Entry<Integer, ShowStats> entry : showStats.entrySet()) {
							ShowStat showStatsData = entry.getValue().getData();
							int indexerId = entry.getKey();

							for (Show show : ShowsFragment.this.shows) {
								if (show.getIndexerId() == indexerId) {
									show.setEpisodesCount(showStatsData.getTotal());
									show.setDownloaded(showStatsData.getTotalDone());
									show.setSnatched(showStatsData.getTotalPending());

									break;
								}
							}
						}
					}

					if (adapter != null) {
						adapter.notifyDataSetChanged();
					}
				}
			});
		}

		if (this.shows.isEmpty()) {
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

	@NonNull
	/* package */ static String getCommand(@Nullable List<Show> shows) {
		StringBuilder command = new StringBuilder();

		if (shows != null) {
			for (Show show : shows) {
				if (!isShowValid(show)) {
					continue;
				}

				if (command.length() > 0) {
					command.append("|");
				}

				command.append("show.stats_").append(show.getIndexerId());
			}
		}

		return command.toString();
	}

	@NonNull
	/* package */ static Map<String, Integer> getCommandParameters(@Nullable List<Show> shows) {
		Map<String, Integer> parameters = new HashMap<>();

		if (shows != null) {
			for (Show show : shows) {
				if (!isShowValid(show)) {
					continue;
				}

				int indexerId = show.getIndexerId();

				parameters.put("show.stats_" + indexerId + ".indexerid", indexerId);
			}
		}

		return parameters;
	}

	/* package */
	static boolean isShowValid(@Nullable Show show) {
		return show != null && show.getIndexerId() > 0;
	}
}
