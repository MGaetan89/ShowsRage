package com.mgaetan89.showsrage.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
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
import com.mgaetan89.showsrage.adapter.LogsAdapter;
import com.mgaetan89.showsrage.model.LogLevel;
import com.mgaetan89.showsrage.model.Logs;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogsFragment extends Fragment implements Callback<Logs>, SwipeRefreshLayout.OnRefreshListener {
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.logs, menu);

		int menuId = getMenuIdForLogLevel(this.getPreferredLogsLevel());

		if (menuId > 0) {
			menu.findItem(menuId).setChecked(true);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

						if (LogsFragment.this.swipeRefreshLayout != null) {
							LogsFragment.this.swipeRefreshLayout.setEnabled(!recyclerView.canScrollVertically(-1));
						}
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

		SickRageApi.getInstance().getServices().getLogs(this.getPreferredLogsLevel(), this);
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

	@Nullable
	/* package */ static LogLevel getLogLevelForMenuId(@IdRes int menuId) {
		switch (menuId) {
			case R.id.menu_debug:
				return LogLevel.DEBUG;

			case R.id.menu_error:
				return LogLevel.ERROR;

			case R.id.menu_info:
				return LogLevel.INFO;

			case R.id.menu_warning:
				return LogLevel.WARNING;
		}

		return null;
	}

	@IdRes
	/* package */ static int getMenuIdForLogLevel(@Nullable LogLevel logLevel) {
		if (logLevel != null) {
			switch (logLevel) {
				case DEBUG:
					return R.id.menu_debug;

				case ERROR:
					return R.id.menu_error;

				case INFO:
					return R.id.menu_info;

				case WARNING:
					return R.id.menu_warning;
			}
		}

		return 0;
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
		LogLevel logLevel = getLogLevelForMenuId(item.getItemId());

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
