package com.mgaetan89.showsrage.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mgaetan89.showsrage.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public abstract class BaseActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener, Drawer.OnDrawerNavigationListener {
	@Nullable
	private Drawer.Result drawerResult = null;

	@Override
	public void onBackPressed() {
		if (this.drawerResult != null && this.drawerResult.isDrawerOpen()) {
			this.drawerResult.closeDrawer();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id, IDrawerItem drawerItem) {
		if (drawerItem instanceof PrimaryDrawerItem) {
			switch (((PrimaryDrawerItem) drawerItem).getNameRes()) {
				case R.string.coming_episodes: {
					Intent intent = new Intent(this, ComingEpisodesActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

					this.startActivity(intent);

					break;
				}

				case R.string.history:
					Toast.makeText(this, "Display history", Toast.LENGTH_SHORT).show();

					break;

				case R.string.logs: {
					Intent intent = new Intent(this, LogsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

					this.startActivity(intent);

					break;
				}

				case R.string.settings: {
					Intent intent = new Intent(this, SettingsActivity.class);

					this.startActivity(intent);

					break;
				}

				case R.string.shows: {
					Intent intent = new Intent(this, ShowsActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

					this.startActivity(intent);

					break;
				}
			}
		}
	}

	@Override
	public boolean onNavigationClickListener(View view) {
		if (this.drawerResult != null && !this.drawerResult.getActionBarDrawerToggle().isDrawerIndicatorEnabled()) {
			this.onBackPressed();

			return true;
		}

		return false;
	}

	protected void displayHomeAsUp(boolean displayHomeAsUp) {
		ActionBar actionBar = this.getSupportActionBar();

		if (displayHomeAsUp) {
			if (this.drawerResult != null) {
				this.drawerResult.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
			}

			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		} else {
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(false);
			}

			if (this.drawerResult != null) {
				this.drawerResult.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
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

		this.setTitle(this.getTitleResourceId());

		Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);

		if (toolbar != null) {
			this.setSupportActionBar(toolbar);
		}

		this.drawerResult = new Drawer()
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
						new PrimaryDrawerItem().withName(R.string.shows),
						new PrimaryDrawerItem().withName(R.string.coming_episodes),
						new PrimaryDrawerItem().withName(R.string.history),
						new PrimaryDrawerItem().withName(R.string.logs)
				)
				.addStickyDrawerItems(
						new PrimaryDrawerItem().withName(R.string.settings)
				)
				.build();
		this.drawerResult.getDrawerLayout().setScrimColor(Color.TRANSPARENT);
	}
}
