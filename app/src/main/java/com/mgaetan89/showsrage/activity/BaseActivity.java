package com.mgaetan89.showsrage.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.StatisticsFragment;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class BaseActivity extends AppCompatActivity implements Callback<GenericResponse>, NavigationView.OnNavigationItemSelectedListener {
	@Nullable
	private DrawerLayout drawerLayout = null;

	@Nullable
	private ActionBarDrawerToggle drawerToggle = null;

	@Nullable
	private NavigationView navigationView = null;

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onBackPressed() {
		if (this.drawerLayout != null && this.navigationView != null && this.drawerLayout.isDrawerOpen(this.navigationView)) {
			this.drawerLayout.closeDrawers();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (this.drawerToggle != null) {
			this.drawerToggle.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		int id = menuItem.getItemId();

		switch (id) {
			case R.id.menu_coming_episodes: {
				Intent intent = new Intent(this, ComingEpisodesActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

				this.startActivity(intent);

				menuItem.setChecked(true);

				return true;
			}

			case R.id.menu_history: {
				Intent intent = new Intent(this, HistoryActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

				this.startActivity(intent);

				menuItem.setChecked(true);

				return true;
			}

			case R.id.menu_logs: {
				Intent intent = new Intent(this, LogsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

				this.startActivity(intent);

				menuItem.setChecked(true);

				return true;
			}

			case R.id.menu_restart: {
				new AlertDialog.Builder(this)
						.setMessage(R.string.restart_confirm)
						.setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								SickRageApi.getInstance().getServices().restart(BaseActivity.this);
							}
						})
						.setNegativeButton(android.R.string.cancel, null)
						.show();

				return true;
			}

			case R.id.menu_settings: {
				Intent intent = new Intent(this, SettingsActivity.class);

				this.startActivity(intent);

				menuItem.setChecked(true);

				return true;
			}

			case R.id.menu_shows: {
				Intent intent = new Intent(this, ShowsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

				this.startActivity(intent);

				menuItem.setChecked(true);

				return true;
			}

			case R.id.menu_statistics: {
				StatisticsFragment fragment = new StatisticsFragment();
				fragment.show(this.getSupportFragmentManager(), "statistics");

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (this.drawerToggle != null && this.drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		if (item.getItemId() == android.R.id.home) {
			this.onBackPressed();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		Toast.makeText(this, genericResponse.getMessage(), Toast.LENGTH_SHORT).show();
	}

	protected abstract boolean displayHomeAsUp();

	protected abstract Fragment getFragment();

	@IdRes
	protected abstract int getSelectedMenuId();

	@StringRes
	protected abstract int getTitleResourceId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);

		SickRageApi.getInstance().init(PreferenceManager.getDefaultSharedPreferences(this));

		this.setTitle(this.getTitleResourceId());

		this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		this.navigationView = (NavigationView) this.findViewById(R.id.drawer_content);
		Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);

		if (this.drawerLayout != null) {
			this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, toolbar, R.string.abc_action_bar_home_description, R.string.abc_action_bar_home_description);

			this.drawerLayout.setDrawerListener(this.drawerToggle);
			this.drawerLayout.post(new Runnable() {
				@Override
				public void run() {
					drawerToggle.syncState();
				}
			});
		}

		if (this.navigationView != null) {
			this.navigationView.setNavigationItemSelectedListener(this);
			this.navigationView.getMenu().findItem(this.getSelectedMenuId()).setChecked(true);
		}

		if (toolbar != null) {
			this.setSupportActionBar(toolbar);
		}

		if (savedInstanceState == null) {
			Fragment fragment = this.getFragment();

			if (fragment != null) {
				this.getSupportFragmentManager().beginTransaction()
						.replace(R.id.content, fragment)
						.commit();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!(this instanceof SettingsActivity)) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

			if (TextUtils.isEmpty(preferences.getString("server_address", ""))) {
				Intent intent = new Intent(this, SettingsActivity.class);

				this.startActivity(intent);
			}
		} else {
			this.displayHomeAsUp(this.displayHomeAsUp());
		}
	}

	private void displayHomeAsUp(boolean displayHomeAsUp) {
		ActionBar actionBar = this.getSupportActionBar();

		if (displayHomeAsUp) {
			if (this.drawerToggle != null) {
				this.drawerToggle.setDrawerIndicatorEnabled(false);
			}

			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		} else {
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(false);
			}

			if (this.drawerToggle != null) {
				this.drawerToggle.setDrawerIndicatorEnabled(true);
			}
		}
	}
}
