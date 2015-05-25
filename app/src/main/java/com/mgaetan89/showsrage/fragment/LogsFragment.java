package com.mgaetan89.showsrage.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.adapter.LogsAdapter;
import com.mgaetan89.showsrage.model.LogLevel;
import com.mgaetan89.showsrage.model.Logs;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogsFragment extends Fragment implements Callback<Logs>, SwipeRefreshLayout.OnRefreshListener {
	@Inject
	public SickRageApi api;

	@Nullable
	private LogsAdapter adapter = null;

	@Nullable
	private TextView emptyView = null;

	@NonNull
	private final List<String> logs = new ArrayList<>();

	@Nullable
	private RecyclerView recyclerView = null;

	@Nullable
	private SwipeRefreshLayout swipeRefreshLayout = null;

	public LogsFragment() {
		this.setHasOptionsMenu(true);
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((ShowsRageApplication) this.getActivity().getApplication()).inject(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.logs, menu);

		switch (this.getPreferredLogsLevel()) {
			case DEBUG:
				menu.findItem(R.id.menu_debug).setChecked(true);

				break;

			case ERROR:
				menu.findItem(R.id.menu_error).setChecked(true);

				break;

			case INFO:
				menu.findItem(R.id.menu_info).setChecked(true);

				break;

			case WARNING:
				menu.findItem(R.id.menu_warning).setChecked(true);

				break;
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_logs, container, false);

		if (view != null) {
			this.emptyView = (TextView) view.findViewById(android.R.id.empty);
			this.recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
			this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

			if (this.recyclerView != null) {
				this.adapter = new LogsAdapter(this.logs);

				this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
					@Override
					public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
						super.onScrolled(recyclerView, dx, dy);

						if (swipeRefreshLayout != null) {
							int topRowVerticalPosition =
									(recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();

							swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
						}
					}

					@Override
					public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
						super.onScrollStateChanged(recyclerView, newState);
					}
				});
				this.recyclerView.setAdapter(this.adapter);
				this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
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
		this.logs.clear();

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
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getGroupId() == R.id.menu_logs_level) {
			return this.handleLogsLevelSelection(item);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefresh() {
		if (this.swipeRefreshLayout != null) {
			this.swipeRefreshLayout.setRefreshing(true);
		}

		this.api.getServices().getLogs(this.getPreferredLogsLevel(), this);
	}

	@Override
	public void success(Logs logs, Response response) {
		if (this.swipeRefreshLayout != null) {
			this.swipeRefreshLayout.setRefreshing(false);
		}

		this.logs.clear();

		if (logs != null) {
			this.logs.addAll(logs.getData());
		}

		if (this.logs.isEmpty()) {
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
	private LogLevel getPreferredLogsLevel() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		String logsLevelString = preferences.getString(Constants.Preferences.Fields.LOGS_LEVEL, Constants.Preferences.Defaults.LOGS_LEVEL.name());

		try {
			return LogLevel.valueOf(logsLevelString);
		} catch (IllegalArgumentException ignored) {
			return Constants.Preferences.Defaults.LOGS_LEVEL;
		}
	}

	private boolean handleLogsLevelSelection(MenuItem item) {
		LogLevel logLevel = null;

		switch (item.getItemId()) {
			case R.id.menu_debug:
				logLevel = LogLevel.DEBUG;

				break;

			case R.id.menu_error:
				logLevel = LogLevel.ERROR;

				break;

			case R.id.menu_info:
				logLevel = LogLevel.INFO;

				break;

			case R.id.menu_warning:
				logLevel = LogLevel.WARNING;

				break;
		}

		if (logLevel != null) {
			// Check the selected menu item
			item.setChecked(true);

			// Save the selected logs level
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).edit();
			editor.putString(Constants.Preferences.Fields.LOGS_LEVEL, logLevel.name());
			editor.apply();

			// Refresh the list of logs
			this.onRefresh();

			return true;
		}

		return false;
	}
}
