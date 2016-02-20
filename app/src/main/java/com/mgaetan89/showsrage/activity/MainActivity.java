package com.mgaetan89.showsrage.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.fragment.HistoryFragment;
import com.mgaetan89.showsrage.fragment.LogsFragment;
import com.mgaetan89.showsrage.fragment.PostProcessingFragment;
import com.mgaetan89.showsrage.fragment.RemoteControlFragment;
import com.mgaetan89.showsrage.fragment.ScheduleFragment;
import com.mgaetan89.showsrage.fragment.SettingsAboutFragment;
import com.mgaetan89.showsrage.fragment.SettingsAboutLicensesFragment;
import com.mgaetan89.showsrage.fragment.SettingsAboutShowsRageFragment;
import com.mgaetan89.showsrage.fragment.SettingsBehaviorFragment;
import com.mgaetan89.showsrage.fragment.SettingsDisplayFragment;
import com.mgaetan89.showsrage.fragment.SettingsExperimentalFeaturesFragment;
import com.mgaetan89.showsrage.fragment.SettingsFragment;
import com.mgaetan89.showsrage.fragment.SettingsServerApiKeyFragment;
import com.mgaetan89.showsrage.fragment.SettingsServerFragment;
import com.mgaetan89.showsrage.fragment.ShowsFragment;
import com.mgaetan89.showsrage.fragment.StatisticsFragment;
import com.mgaetan89.showsrage.helper.ShowsRageReceiver;
import com.mgaetan89.showsrage.helper.Utils;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.model.RootDir;
import com.mgaetan89.showsrage.model.RootDirs;
import com.mgaetan89.showsrage.model.UpdateResponse;
import com.mgaetan89.showsrage.model.UpdateResponseWrapper;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mgaetan89.showsrage.view.ColoredToolbar;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements Callback<GenericResponse>, NavigationView.OnNavigationItemSelectedListener {
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

	@NonNull
	private final BroadcastReceiver receiver = new ShowsRageReceiver(this);

	@Nullable
	private TabLayout tabLayout = null;

	@Nullable
	private ColoredToolbar toolbar = null;

	public void displayHomeAsUp(boolean displayHomeAsUp) {
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
		boolean eventHandled = true;
		Fragment fragment = null;
		int id = menuItem.getItemId();

		switch (id) {
			case R.id.menu_check_update: {
				eventHandled = false;

				this.checkForUpdate(true);

				break;
			}

			case R.id.menu_history: {
				fragment = new HistoryFragment();

				break;
			}

			case R.id.menu_logs: {
				fragment = new LogsFragment();

				break;
			}

			case R.id.menu_post_processing: {
				eventHandled = false;

				new PostProcessingFragment().show(this.getSupportFragmentManager(), "post_processing");

				break;
			}

			case R.id.menu_remote_control: {
				eventHandled = false;

				new RemoteControlFragment().show(this.getSupportFragmentManager(), "remote_control");

				break;
			}

			case R.id.menu_restart: {
				eventHandled = false;

				new AlertDialog.Builder(this)
						.setMessage(R.string.restart_confirm)
						.setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								SickRageApi.Companion.getInstance().getServices().restart(MainActivity.this);
							}
						})
						.setNegativeButton(android.R.string.cancel, null)
						.show();

				break;
			}

			case R.id.menu_schedule: {
				fragment = new ScheduleFragment();

				break;
			}

			case R.id.menu_settings: {
				SettingsFragment settingsFragment = new SettingsFragment();

				this.removeCurrentSupportFragment();

				this.getFragmentManager().beginTransaction()
						.replace(R.id.content, settingsFragment)
						.commit();

				break;
			}

			case R.id.menu_shows: {
				fragment = new ShowsFragment();

				break;
			}

			case R.id.menu_statistics: {
				eventHandled = false;

				new StatisticsFragment().show(this.getSupportFragmentManager(), "statistics");

				break;
			}
		}

		if (this.drawerLayout != null && this.navigationView != null) {
			this.drawerLayout.closeDrawer(this.navigationView);
		}

		if (eventHandled) {
			menuItem.setChecked(true);

			if (this.tabLayout != null) {
				this.tabLayout.removeAllTabs();
				this.tabLayout.setVisibility(View.GONE);
			}
		}

		if (fragment != null) {
			this.removeCurrentFragment();
			this.resetThemeColors();

			if (this.toolbar != null) {
				this.toolbar.getMenu().clear();
			}

			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment)
					.commit();
		}

		return eventHandled;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);

		SickRageApi.Companion.getInstance().init(PreferenceManager.getDefaultSharedPreferences(this));
		SickRageApi.Companion.getInstance().getServices().getRootDirs(new RootDirsCallback(this));

		this.appBarLayout = (AppBarLayout) this.findViewById(R.id.app_bar);
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
					if (MainActivity.this.drawerToggle != null) {
						MainActivity.this.drawerToggle.syncState();
					}
				}
			});
		}

		if (this.navigationView != null) {
			this.drawerHeader = (LinearLayout) this.navigationView.inflateHeaderView(R.layout.drawer_header);

			this.navigationView.setNavigationItemSelectedListener(this);
		}

		if (this.toolbar != null) {
			this.setSupportActionBar(this.toolbar);
		}

		Intent intent = this.getIntent();

		if (intent != null) {
			// Set the colors of the Activity
			int colorAccent = intent.getIntExtra(Constants.Bundle.INSTANCE.getCOLOR_ACCENT(), 0);
			int colorPrimary = intent.getIntExtra(Constants.Bundle.INSTANCE.getCOLOR_PRIMARY(), 0);

			if (colorPrimary != 0) {
				this.setThemeColors(colorPrimary, colorAccent);
			}
		}

		this.displayStartFragment();
	}

	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver);

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.Intents.INSTANCE.getACTION_EPISODE_ACTION_SELECTED());
		intentFilter.addAction(Constants.Intents.INSTANCE.getACTION_EPISODE_SELECTED());
		intentFilter.addAction(Constants.Intents.INSTANCE.getACTION_SEARCH_RESULT_SELECTED());
		intentFilter.addAction(Constants.Intents.INSTANCE.getACTION_SHOW_SELECTED());

		LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver, intentFilter);

		this.updateRemoteControlVisibility();
		this.checkForUpdate(false);
	}

	private void checkForUpdate(final boolean manualCheck) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		long lastVersionCheckTime = preferences.getLong(Constants.Preferences.Fields.INSTANCE.getLAST_VERSION_CHECK_TIME(), 0L);
		long checkInterval = Long.valueOf(preferences.getString("behavior_version_check", "0"));

		if (!shouldCheckForUpdate(checkInterval, manualCheck, lastVersionCheckTime)) {
			return;
		}

		SickRageApi.Companion.getInstance().getServices().checkForUpdate(new CheckForUpdateCallback(this, manualCheck));
	}

	private void displayStartFragment() {
		Intent intent = this.getIntent();

		Uri data = intent.getData();

		if (data != null) {
			// Start the correct Setting Fragment, if necessary
			SettingsFragment settingFragment = getSettingFragmentForPath(data.getPath());

			if (settingFragment != null) {
				this.getFragmentManager().beginTransaction()
						.replace(R.id.content, settingFragment)
						.commit();

				return;
			}
		}

		if (this.navigationView != null) {
			this.navigationView.getMenu().performIdentifierAction(R.id.menu_shows, 0);
		}
	}

	@Nullable
	/* package */ static SettingsFragment getSettingFragmentForPath(@Nullable String path) {
		if (path != null) {
			switch (path) {
				case "/":
					return new SettingsFragment();

				case "/about":
					return new SettingsAboutFragment();

				case "/about/licenses":
					return new SettingsAboutLicensesFragment();

				case "/about/showsrage":
					return new SettingsAboutShowsRageFragment();

				case "/behavior":
					return new SettingsBehaviorFragment();

				case "/display":
					return new SettingsDisplayFragment();

				case "/experimental_features":
					return new SettingsExperimentalFeaturesFragment();

				case "/server":
					return new SettingsServerFragment();

				case "/server/api_key":
					return new SettingsServerApiKeyFragment();
			}
		}

		return null;
	}

	private void resetThemeColors() {
		int colorAccent = ContextCompat.getColor(this, R.color.accent);
		int colorPrimary = ContextCompat.getColor(this, R.color.primary);

		this.setThemeColors(colorPrimary, colorAccent);
	}

	private void setThemeColors(int colorPrimary, int colorAccent) {
		int textColor = Utils.INSTANCE.getContrastColor(colorPrimary);

		if (this.appBarLayout != null) {
			this.appBarLayout.setBackgroundColor(colorPrimary);
		}

		if (this.drawerHeader != null) {
			this.drawerHeader.setBackgroundColor(colorPrimary);

			ImageView logo = (ImageView) this.drawerHeader.findViewById(R.id.app_logo);
			TextView name = (TextView) this.drawerHeader.findViewById(R.id.app_name);

			if (logo != null) {
				Drawable drawable = DrawableCompat.wrap(logo.getDrawable());
				DrawableCompat.setTint(drawable, textColor);
			}

			if (name != null) {
				name.setTextColor(textColor);
			}
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

		this.getIntent().putExtra(Constants.Bundle.INSTANCE.getCOLOR_ACCENT(), colorAccent);
		this.getIntent().putExtra(Constants.Bundle.INSTANCE.getCOLOR_PRIMARY(), colorPrimary);
	}

	private void removeCurrentFragment() {
		android.app.FragmentManager fragmentManager = this.getFragmentManager();
		android.app.Fragment fragment = fragmentManager.findFragmentById(R.id.content);

		if (fragment != null) {
			fragmentManager.beginTransaction()
					.remove(fragment)
					.commit();
		}
	}

	private void removeCurrentSupportFragment() {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(R.id.content);

		if (fragment != null) {
			fragmentManager.beginTransaction()
					.remove(fragment)
					.commit();
		}
	}

	/* package */
	static boolean shouldCheckForUpdate(long checkInterval, boolean manualCheck, long lastCheckTime) {
		// Always check for new version if the user triggered the version check himself
		if (manualCheck) {
			return true;
		}

		// The automatic version check is disabled
		if (checkInterval == 0L) {
			return false;
		}

		// Check if we need to look for new update, depending on the user preferences
		return System.currentTimeMillis() - lastCheckTime >= checkInterval;
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
			editor.putLong(Constants.Preferences.Fields.INSTANCE.getLAST_VERSION_CHECK_TIME(), System.currentTimeMillis());
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
			intent.putExtra(Constants.Bundle.INSTANCE.getUPDATE_MODEL(), update);

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

	private static final class RootDirsCallback implements Callback<RootDirs> {
		@NonNull
		private WeakReference<Activity> activityReference = new WeakReference<>(null);

		private RootDirsCallback(Activity activity) {
			this.activityReference = new WeakReference<>(activity);
		}

		@Override
		public void failure(RetrofitError error) {
			error.printStackTrace();
		}

		@Override
		public void success(RootDirs rootDirs, Response response) {
			Activity activity = this.activityReference.get();

			if (activity == null || rootDirs == null) {
				return;
			}

			List<RootDir> data = rootDirs.getData();
			Set<String> rootPaths = new HashSet<>(data.size());

			for (RootDir rootDir : data) {
				rootPaths.add(rootDir.getLocation());
			}

			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
			editor.putStringSet(Constants.Preferences.Fields.INSTANCE.getROOT_DIRS(), rootPaths);
			editor.apply();
		}
	}
}
