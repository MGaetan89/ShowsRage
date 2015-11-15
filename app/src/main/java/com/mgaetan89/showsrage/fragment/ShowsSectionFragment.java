package com.mgaetan89.showsrage.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.AddShowActivity;
import com.mgaetan89.showsrage.adapter.ShowsAdapter;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.ShowStat;
import com.mgaetan89.showsrage.model.ShowStatWrapper;
import com.mgaetan89.showsrage.model.ShowStats;
import com.mgaetan89.showsrage.model.ShowStatsWrapper;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowsSectionFragment extends Fragment implements View.OnClickListener {
	@Nullable
	private ShowsAdapter adapter = null;

	@Nullable
	private TextView emptyView = null;

	@NonNull
	private final List<Show> filteredShows = new ArrayList<>();

	@NonNull
	private final FilterReceiver receiver = new FilterReceiver(this);

	@Nullable
	private RecyclerView recyclerView = null;

	@NonNull
	private final Collection<Show> shows = new ArrayList<>();

	public ShowsSectionFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle arguments = this.getArguments();

		if (arguments != null) {
			@SuppressWarnings("unchecked")
			Collection<Show> shows = (Collection<Show>) arguments.getSerializable(Constants.Bundle.SHOWS);

			if (shows != null) {
				this.filteredShows.addAll(shows);
				this.shows.addAll(shows);
			}
		}

		if (!this.shows.isEmpty()) {
			String command = getCommand(this.shows);
			Map<String, Integer> parameters = getCommandParameters(this.shows);

			SickRageApi.getInstance().getServices().getShowStats(command, parameters, new ShowStatsCallback(this));
		}

		this.updateLayout();
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
		View view = inflater.inflate(R.layout.fragment_shows_section, container, false);

		if (view != null) {
			this.emptyView = (TextView) view.findViewById(android.R.id.empty);
			this.recyclerView = (RecyclerView) view.findViewById(android.R.id.list);

			FloatingActionButton addShow = (FloatingActionButton) view.findViewById(R.id.add_show);

			if (addShow != null) {
				addShow.setOnClickListener(this);
			}

			if (this.recyclerView != null) {
				String showsListLayout = PreferenceManager.getDefaultSharedPreferences(this.getContext()).getString("display_shows_list_layout", "poster");
				int columnCount = this.getResources().getInteger(R.integer.shows_column_count);
				this.adapter = new ShowsAdapter(this.filteredShows, getAdapterLayoutResource(showsListLayout));

				this.recyclerView.setAdapter(this.adapter);
				this.recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), columnCount));
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.filteredShows.clear();
		this.shows.clear();

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		this.emptyView = null;
		this.recyclerView = null;

		super.onDestroyView();
	}

	@Override
	public void onPause() {
		LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.receiver);

		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

		IntentFilter intentFilter = new IntentFilter(Constants.Intents.ACTION_FILTER_SHOWS);

		LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.receiver, intentFilter);
	}

	@LayoutRes
	/* package */ static int getAdapterLayoutResource(String preferedLayout) {
		if ("banner".equals(preferedLayout)) {
			return R.layout.adapter_shows_list_content_banner;
		}

		return R.layout.adapter_shows_list_content_poster;
	}

	@NonNull
	/* package */ static String getCommand(@Nullable Iterable<Show> shows) {
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
	/* package */ static Map<String, Integer> getCommandParameters(@Nullable Iterable<Show> shows) {
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

	private void updateLayout() {
		if (this.filteredShows.isEmpty()) {
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

	/* package */ static final class FilterReceiver extends BroadcastReceiver {
		private final WeakReference<ShowsSectionFragment> fragmentReference;

		FilterReceiver(ShowsSectionFragment fragment) {
			this.fragmentReference = new WeakReference<>(fragment);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			ShowsSectionFragment fragment = this.fragmentReference.get();

			if (fragment == null) {
				return;
			}

			int filterMode = intent.getIntExtra(Constants.Bundle.FILTER_MODE, ShowsFragment.FILTER_PAUSED_ACTIVE_BOTH);
			int filterStatus = intent.getIntExtra(Constants.Bundle.FILTER_STATUS, ShowsFragment.FILTER_STATUS_ALL);
			String searchQuery = intent.getStringExtra(Constants.Bundle.SEARCH_QUERY);

			Collection<Show> filteredShows = new ArrayList<>();
			Collection<Show> shows = fragment.shows;

			for (Show show : shows) {
				if (match(show, filterMode, filterStatus, searchQuery)) {
					filteredShows.add(show);
				}
			}

			fragment.filteredShows.clear();
			fragment.filteredShows.addAll(filteredShows);
			fragment.updateLayout();
		}

		/* package */
		static boolean match(@Nullable Show show, int filterMode, int filterStatus, String searchQuery) {
			return show != null &&
					matchFilterMode(show, filterMode) &&
					matchFilterStatus(show, filterStatus) &&
					matchSearchQuery(show, searchQuery);
		}

		/* package */
		static boolean matchFilterMode(@NonNull Show show, int filterMode) {
			switch (filterMode) {
				case ShowsFragment.FILTER_PAUSED_ACTIVE_ACTIVE:
					return show.getPaused() == 0;

				case ShowsFragment.FILTER_PAUSED_ACTIVE_BOTH:
					return true;

				case ShowsFragment.FILTER_PAUSED_ACTIVE_PAUSED:
					return show.getPaused() == 1;
			}

			return false;
		}

		/* package */
		static boolean matchFilterStatus(@NonNull Show show, int filterStatus) {
			switch (filterStatus) {
				case ShowsFragment.FILTER_STATUS_ALL:
					return true;

				case ShowsFragment.FILTER_STATUS_CONTINUING:
					return "continuing".equalsIgnoreCase(show.getStatus());

				case ShowsFragment.FILTER_STATUS_ENDED:
					return "ended".equalsIgnoreCase(show.getStatus());

				case ShowsFragment.FILTER_STATUS_UNKNOWN:
					return "unknown".equalsIgnoreCase(show.getStatus());
			}

			return false;
		}

		/* package */
		static boolean matchSearchQuery(@NonNull Show show, @Nullable String searchQuery) {
			if (searchQuery == null || searchQuery.isEmpty()) {
				return true;
			}

			searchQuery = searchQuery.trim();

			if (searchQuery.isEmpty()) {
				return true;
			}

			String showName = show.getShowName().toLowerCase();
			searchQuery = searchQuery.toLowerCase();

			return showName.contains(searchQuery);
		}
	}

	private static final class ShowStatsCallback implements Callback<ShowStatsWrapper> {
		private final WeakReference<ShowsSectionFragment> fragmentReference;

		ShowStatsCallback(ShowsSectionFragment fragment) {
			this.fragmentReference = new WeakReference<>(fragment);
		}

		@Override
		public void failure(RetrofitError error) {
			error.printStackTrace();
		}

		@Override
		public void success(ShowStatsWrapper showStatsWrapper, Response response) {
			ShowsSectionFragment fragment = this.fragmentReference.get();

			if (fragment == null) {
				return;
			}

			ShowStatWrapper data = showStatsWrapper.getData();
			Map<Integer, ShowStats> showStats = data.getShowStats();

			if (showStats != null) {
				for (Map.Entry<Integer, ShowStats> entry : showStats.entrySet()) {
					ShowStat showStatsData = entry.getValue().getData();
					int indexerId = entry.getKey();

					for (Show show : fragment.filteredShows) {
						if (show.getIndexerId() == indexerId) {
							show.setEpisodesCount(showStatsData.getTotal());
							show.setDownloaded(showStatsData.getTotalDone());
							show.setSnatched(showStatsData.getTotalPending());

							break;
						}
					}

					for (Show show : fragment.shows) {
						if (show.getIndexerId() == indexerId) {
							show.setEpisodesCount(showStatsData.getTotal());
							show.setDownloaded(showStatsData.getTotalDone());
							show.setSnatched(showStatsData.getTotalPending());

							break;
						}
					}
				}
			}

			fragment.updateLayout();
		}
	}
}
