package com.mgaetan89.showsrage.activity;

import com.mgaetan89.showsrage.Constants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class BaseActivity_ShouldCheckForUpdateTest {
	@Parameterized.Parameter(1)
	public long lastCheckTime;

	@Parameterized.Parameter(0)
	public boolean manualCheck;

	@Parameterized.Parameter(2)
	public boolean result;

	@Test
	public void shouldCheckForUpdate() {
		assertThat(BaseActivity.shouldCheckForUpdate(this.manualCheck, this.lastCheckTime)).isEqualTo(this.result);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		final long offset = 5000L;

		return Arrays.asList(new Object[][]{
				// Automatic check
				{false, System.currentTimeMillis() - Constants.Preferences.Defaults.VERSION_CHECK_INTERVAL - offset, true},
				{false, System.currentTimeMillis() - Constants.Preferences.Defaults.VERSION_CHECK_INTERVAL, true},
				{false, System.currentTimeMillis() - Constants.Preferences.Defaults.VERSION_CHECK_INTERVAL + offset, false},
				{false, System.currentTimeMillis() - offset, false},
				{false, System.currentTimeMillis(), false},
				{false, System.currentTimeMillis() + offset, false},
				{false, 0, true},

				// Manual check
				{true, System.currentTimeMillis() - Constants.Preferences.Defaults.VERSION_CHECK_INTERVAL - offset, true},
				{true, System.currentTimeMillis() - Constants.Preferences.Defaults.VERSION_CHECK_INTERVAL, true},
				{true, System.currentTimeMillis() - Constants.Preferences.Defaults.VERSION_CHECK_INTERVAL + offset, true},
				{true, System.currentTimeMillis() - offset, true},
				{true, System.currentTimeMillis(), true},
				{true, System.currentTimeMillis() + offset, true},
				{true, 0, true},
		});
	}
}
