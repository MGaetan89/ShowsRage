package com.mgaetan89.showsrage.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.fragment.RemoteControlFragment;
import com.mgaetan89.showsrage.fragment.StatisticsFragment;
import com.mgaetan89.showsrage.helper.Utils;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.model.UpdateResponse;
import com.mgaetan89.showsrage.model.UpdateResponseWrapper;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mgaetan89.showsrage.view.ColoredToolbar;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class BaseActivity extends AppCompatActivity implements Callback<GenericResponse>, NavigationView.OnNavigationItemSelectedListener {
	private static final float COLOR_DARK_FACTOR = 0.8f;

	@Nullable
	private AppBarLayout appBarLayout = null;

	@Nullable
	private LinearLayout drawerHeader = null;

	@Nullable
	private DrawerLayout drawerLayout = null;

	@Nullable
	private ActionBarDrawerToggle drawerToggle = null;

	@Nullable
	private NavigationView navigationView = null;

	@Nullable
	private TabLayout tabLayout = null;

	@Nullable
	private ColoredToolbar toolbar = null;

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
			case R.id.menu_check_update: {
				this.checkForUpdate(true);

				menuItem.setChecked(false);

				return true;
			}

			case R.id.menu_coming_episodes: {
				Intent intent = new Intent(this, ComingEpisodesActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

				this.startActivity(intent);

				return true;
			}

			case R.id.menu_history: {
				Intent intent = new Intent(this, HistoryActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

				this.startActivity(intent);

				return true;
			}

			case R.id.menu_logs: {
				Intent intent = new Intent(this, LogsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

				this.startActivity(intent);

				return true;
			}

			case R.id.menu_remote_control: {
				new RemoteControlFragment().show(this.getSupportFragmentManager(), "remote_control");

				menuItem.setChecked(false);

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

				menuItem.setChecked(false);

				return true;
			}

			case R.id.menu_settings: {
				Intent intent = new Intent(this, SettingsActivity.class);

				this.startActivity(intent);

				return true;
			}

			case R.id.menu_shows: {
				Intent intent = new Intent(this, ShowsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

				this.startActivity(intent);

				return true;
			}

			case R.id.menu_statistics: {
				StatisticsFragment fragment = new StatisticsFragment();
				fragment.show(this.getSupportFragmentManager(), "statistics");

				menuItem.setChecked(false);

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

	public void setPalette(Palette palette) {
		Palette.Swatch accent = palette.getDarkMutedSwatch();
		int accentColor;
		Palette.Swatch primary = palette.getVibrantSwatch();
		int primaryColor;

		if (accent == null) {
			accent = palette.getMutedSwatch();

			if (accent == null) {
				accent = palette.getLightMutedSwatch();
			}
		}

		if (accent == null) {
			accentColor = ContextCompat.getColor(this, R.color.accent);
		} else {
			accentColor = accent.getRgb();
		}

		if (primary == null) {
			primary = palette.getLightVibrantSwatch();

			if (primary == null) {
				primary = palette.getDarkVibrantSwatch();

				if (primary == null) {
					primary = palette.getLightMutedSwatch();

					if (primary == null) {
						primary = palette.getMutedSwatch();

						if (primary == null) {
							primary = palette.getDarkMutedSwatch();
						}
					}
				}
			}
		}

		if (primary == null) {
			primaryColor = ContextCompat.getColor(this, R.color.primary);
		} else {
			primaryColor = primary.getRgb();
		}

		this.setThemeColors(primaryColor, accentColor);
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		Toast.makeText(this, genericResponse.getMessage(), Toast.LENGTH_SHORT).show();
	}

	public void updateRemoteControlVisibility() {
		if (this.navigationView != null) {
			boolean hasRemotePlaybackClient = ((ShowsRageApplication) this.getApplication()).hasPlayingVideo();

			this.navigationView.getMenu().findItem(R.id.menu_remote_control).setVisible(hasRemotePlaybackClient);
		}
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

		this.appBarLayout = (AppBarLayout) this.findViewById(R.id.app_bar);
		this.drawerHeader = (LinearLayout) this.findViewById(R.id.drawer_header);
		this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		this.navigationView = (NavigationView) this.findViewById(R.id.drawer_content);
		this.tabLayout = (TabLayout) this.findViewById(R.id.tabs);
		this.toolbar = (ColoredToolbar) this.findViewById(R.id.toolbar);

		if (this.drawerLayout != null) {
			this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar, R.string.abc_action_bar_home_description, R.string.abc_action_bar_home_description);

			this.drawerLayout.setDrawerListener(this.drawerToggle);
			this.drawerLayout.post(new Runnable() {
				@Override
				public void run() {
					if (BaseActivity.this.drawerToggle != null) {
						BaseActivity.this.drawerToggle.syncState();
					}
				}
			});
		}

		if (this.navigationView != null) {
			this.navigationView.setNavigationItemSelectedListener(this);
			this.navigationView.getMenu().findItem(this.getSelectedMenuId()).setChecked(true);
		}

		if (this.toolbar != null) {
			this.setSupportActionBar(this.toolbar);
		}

		if (savedInstanceState == null) {
			Fragment fragment = this.getFragment();

			if (fragment != null) {
				this.getSupportFragmentManager().beginTransaction()
						.replace(R.id.content, fragment)
						.commit();
			}
		}

		// Set the colors of the Activity
		Intent intent = this.getIntent();

		if (intent != null) {
			int colorAccent = intent.getIntExtra(Constants.Bundle.COLOR_ACCENT, 0);
			int colorPrimary = intent.getIntExtra(Constants.Bundle.COLOR_PRIMARY, 0);

			if (colorPrimary != 0) {
				this.setThemeColors(colorPrimary, colorAccent);
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

				return;
			}
		}

		this.updateRemoteControlVisibility();
		this.displayHomeAsUp(this.displayHomeAsUp());
		this.checkForUpdate(false);
	}

	private void checkForUpdate(final boolean manualCheck) {
		long lastVersionCheckTime = PreferenceManager.getDefaultSharedPreferences(this).getLong(Constants.Preferences.Fields.LAST_VERSION_CHECK_TIME, 0L);

		if (!shouldCheckForUpdate(manualCheck, lastVersionCheckTime)) {
			return;
		}

		SickRageApi.getInstance().getServices().checkForUpdate(new CheckForUpdateCallback(this, manualCheck));
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

	private void setThemeColors(int colorPrimary, int colorAccent) {
		int textColor = Utils.getContrastColor(colorPrimary);

		if (this.appBarLayout != null) {
			this.appBarLayout.setBackgroundColor(colorPrimary);
		}

		if (this.drawerHeader != null) {
			this.drawerHeader.setBackgroundColor(colorPrimary);
		}

		if (this.navigationView != null) {
			int colors[] = {
					colorPrimary,
					Color.WHITE
			};
			int states[][] = {
					{android.R.attr.state_checked},
					{}
			};
			ColorStateList colorStateList = new ColorStateList(states, colors);

			this.navigationView.setItemIconTintList(colorStateList);
			this.navigationView.setItemTextColor(colorStateList);
		}

		if (this.tabLayout != null) {
			int selectedTextColor = ColorUtils.setAlphaComponent(textColor, (int) (0.7f * 255));

			this.tabLayout.setSelectedTabIndicatorColor(colorAccent);
			this.tabLayout.setTabTextColors(selectedTextColor, textColor);
		}

		if (this.toolbar != null) {
			this.toolbar.setItemColor(textColor);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			float colorPrimaryDark[] = new float[3];
			ColorUtils.colorToHSL(colorPrimary, colorPrimaryDark);
			colorPrimaryDark[2] *= COLOR_DARK_FACTOR;

			this.getWindow().setStatusBarColor(ColorUtils.HSLToColor(colorPrimaryDark));
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (textColor == Color.BLACK) {
				this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			}
		}

		this.getIntent().putExtra(Constants.Bundle.COLOR_ACCENT, colorAccent);
		this.getIntent().putExtra(Constants.Bundle.COLOR_PRIMARY, colorPrimary);
	}

	/* package */
	static boolean shouldCheckForUpdate(boolean manualCheck, long lastCheckTime) {
		return manualCheck || System.currentTimeMillis() - lastCheckTime >= Constants.VERSION_CHECK_INTERVAL;
	}

	private static final class CheckForUpdateCallback implements Callback<UpdateResponseWrapper> {
		private boolean manualCheck = false;
		private WeakReference<AppCompatActivity> activityReference = null;

		private CheckForUpdateCallback(AppCompatActivity activity, boolean manualCheck) {
			this.activityReference = new WeakReference<>(activity);
			this.manualCheck = manualCheck;
		}

		@Override
		public void failure(RetrofitError error) {
			// SickRage may not support this request
			// SickRage version 4.0.30 is required
			if (this.manualCheck) {
				AppCompatActivity activity = this.activityReference.get();

				if (activity != null) {
					Toast.makeText(activity, R.string.sickrage_4030_required, Toast.LENGTH_SHORT).show();
				}
			}

			error.printStackTrace();
		}

		@Override
		public void success(UpdateResponseWrapper updateResponseWrapper, Response response) {
			if (updateResponseWrapper != null) {
				this.handleCheckForUpdateResponse(updateResponseWrapper.getData(), this.manualCheck);
			}
		}

		private void handleCheckForUpdateResponse(@Nullable UpdateResponse update, boolean manualCheck) {
			if (update == null) {
				return;
			}

			AppCompatActivity activity = this.activityReference.get();

			if (activity == null) {
				return;
			}

			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
			editor.putLong(Constants.Preferences.Fields.LAST_VERSION_CHECK_TIME, System.currentTimeMillis());
			editor.apply();

			if (!update.needsUpdate()) {
				if (manualCheck) {
					Toast.makeText(activity, R.string.no_update, Toast.LENGTH_SHORT).show();
				}

				return;
			}

			if (manualCheck) {
				Toast.makeText(activity, R.string.new_update, Toast.LENGTH_SHORT).show();
			}

			Intent intent = new Intent(activity, UpdateActivity.class);
			intent.putExtra(Constants.Bundle.UPDATE_MODEL, update);

			PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			Notification notification = new NotificationCompat.Builder(activity)
					.setAutoCancel(true)
					.setColor(ContextCompat.getColor(activity, R.color.primary))
					.setContentIntent(pendingIntent)
					.setContentTitle(activity.getString(R.string.app_name))
					.setContentText(activity.getString(R.string.update_available))
					.setLocalOnly(true)
					.setSmallIcon(R.drawable.ic_notification)
					.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
					.setStyle(
							new NotificationCompat.BigTextStyle()
									.bigText(activity.getString(
											R.string.update_available_detailed,
											update.getCurrentVersion().getVersion(),
											update.getLatestVersion().getVersion(),
											update.getCommitsOffset()
									))
					)
					.build();

			NotificationManager notificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
			notificationManager.notify(0, notification);
		}
	}
}
