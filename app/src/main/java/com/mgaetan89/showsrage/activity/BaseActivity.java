package com.mgaetan89.showsrage.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public abstract class BaseActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener, Drawer.OnDrawerNavigationListener {
	@Nullable
	private Drawer drawer = null;

	@Override
	public void onBackPressed() {
		if (this.drawer != null && this.drawer.isDrawerOpen()) {
			this.drawer.closeDrawer();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onItemClick(AdapterView<?> adapterView, View view, int position, long id, IDrawerItem drawerItem) {
		if (drawerItem instanceof Nameable) {
			switch (((Nameable) drawerItem).getNameRes()) {
				case R.string.coming_episodes: {
					Intent intent = new Intent(this, ComingEpisodesActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

					this.startActivity(intent);

					return true;
				}

				case R.string.history:
					Toast.makeText(this, "Display history", Toast.LENGTH_SHORT).show();

					return true;

				case R.string.logs: {
					Intent intent = new Intent(this, LogsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

					this.startActivity(intent);

					return true;
				}

				case R.string.restart_sickrage: {
					new AlertDialog.Builder(this)
							.setMessage(R.string.restart_confirm)
							.setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									SickRageApi.getInstance().getServices().restart();
								}
							})
							.setNegativeButton(android.R.string.cancel, null)
							.show();

					return true;
				}

				case R.string.settings: {
					Intent intent = new Intent(this, SettingsActivity.class);

					this.startActivity(intent);

					return true;
				}

				case R.string.shows: {
					Intent intent = new Intent(this, ShowsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

					this.startActivity(intent);

					return true;
				}

				case R.string.shutdown_sickrage: {
					new AlertDialog.Builder(this)
							.setMessage(R.string.shutdown_confirm)
							.setPositiveButton(R.string.shutdown, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									SickRageApi.getInstance().getServices().restart();
								}
							})
							.setNegativeButton(android.R.string.cancel, null)
							.show();

					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean onNavigationClickListener(View view) {
		if (this.drawer != null && !this.drawer.getActionBarDrawerToggle().isDrawerIndicatorEnabled()) {
			this.onBackPressed();

			return true;
		}

		return false;
	}

	protected void displayHomeAsUp(boolean displayHomeAsUp) {
		ActionBar actionBar = this.getSupportActionBar();

		if (displayHomeAsUp) {
			if (this.drawer != null) {
				this.drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
			}

			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		} else {
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(false);
			}

			if (this.drawer != null) {
				this.drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
			}
		}
	}

	protected abstract int getSelectedMenuItemIndex();

	@StringRes
	protected abstract int getTitleResourceId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);

		SickRageApi.getInstance().init(this);

		this.setTitle(this.getTitleResourceId());

		Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);

		if (toolbar != null) {
			this.setSupportActionBar(toolbar);
		}

		this.drawer = new DrawerBuilder(this)
				.withActionBarDrawerToggle(true)
				.withActionBarDrawerToggleAnimated(true)
				.withActivity(this)
				.withDisplayBelowToolbar(true)
				.withOnDrawerItemClickListener(this)
				.withOnDrawerNavigationListener(this)
				.withSelectedItem(this.getSelectedMenuItemIndex())
				.withToolbar(toolbar)
				.withTranslucentStatusBar(false)
				.addDrawerItems(
						new PrimaryDrawerItem().withName(R.string.shows).withIcon(R.drawable.ic_tv_white_24dp).withIconTintingEnabled(true),
						new PrimaryDrawerItem().withName(R.string.coming_episodes).withIcon(R.drawable.ic_event_white_24dp).withIconTintingEnabled(true),
						new PrimaryDrawerItem().withName(R.string.history).withIcon(R.drawable.ic_history_white_24dp).withIconTintingEnabled(true),
						new PrimaryDrawerItem().withName(R.string.logs).withIcon(R.drawable.ic_list_white_24dp).withIconTintingEnabled(true),
						new DividerDrawerItem(),
						new SecondaryDrawerItem().withName(R.string.restart_sickrage).withIcon(R.drawable.ic_replay_white_24dp).withIconTintingEnabled(true).withCheckable(false),
						new SecondaryDrawerItem().withName(R.string.shutdown_sickrage).withIcon(R.drawable.ic_stop_white_24dp).withIconTintingEnabled(true).withCheckable(false)
				)
				.addStickyDrawerItems(
						new PrimaryDrawerItem().withName(R.string.settings).withIcon(R.drawable.ic_settings_white_24dp).withIconTintingEnabled(true)
				)
				.build();
		this.drawer.getDrawerLayout().setScrimColor(Color.TRANSPARENT);
	}

	public enum MenuItems {
		SHOWS,
		COMING_EPISODES,
		HISTORY,
		LOGS,
		RESTART,
		SHUTDOWN,
		SETTINGS
	}
}
