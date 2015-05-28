package com.mgaetan89.showsrage.activity;

import com.mgaetan89.showsrage.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsActivityTest {
	private SettingsActivity activity;

	@Before
	public void before() {
		this.activity = new SettingsActivity();
	}

	@Test
	public void getSelectedMenuItemIndex() {
		assertThat(this.activity.getSelectedMenuItemIndex()).isEqualTo(BaseActivity.MenuItems.SETTINGS.ordinal());
	}

	@Test
	public void getTitleResourceId() {
		assertThat(this.activity.getTitleResourceId()).isEqualTo(R.string.settings);
	}

	@After
	public void after() {
		this.activity = null;
	}
}