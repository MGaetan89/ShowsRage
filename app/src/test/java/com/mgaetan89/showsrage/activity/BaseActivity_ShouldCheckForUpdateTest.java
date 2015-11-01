package com.mgaetan89.showsrage.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class BaseActivity_ShouldCheckForUpdateTest {
	@Parameterized.Parameter(0)
	public long checkInterval;

	@Parameterized.Parameter(2)
	public long lastCheckTime;

	@Parameterized.Parameter(1)
	public boolean manualCheck;

	@Parameterized.Parameter(3)
	public boolean result;

	@Test
	public void shouldCheckForUpdate() {
		assertThat(BaseActivity.shouldCheckForUpdate(this.checkInterval, this.manualCheck, this.lastCheckTime)).isEqualTo(this.result);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		final long checkInterval = 21_600_000L;
		final long offset = 5000L;

		return Arrays.asList(new Object[][]{
				// Automatic check
				{0L, false, 0, false},
				{0L, false, System.currentTimeMillis() - offset, false},
				{0L, false, System.currentTimeMillis(), false},
				{0L, false, System.currentTimeMillis() + offset, false},
				{checkInterval, false, 0, true},
				{checkInterval, false, System.currentTimeMillis() - checkInterval - offset, true},
				{checkInterval, false, System.currentTimeMillis() - checkInterval, true},
				{checkInterval, false, System.currentTimeMillis() - checkInterval + offset, false},

				// Manual check
				{0L, true, 0, true},
				{0L, true, System.currentTimeMillis() - offset, true},
				{0L, true, System.currentTimeMillis(), true},
				{0L, true, System.currentTimeMillis() + offset, true},
				{checkInterval, true, 0, true},
				{checkInterval, true, System.currentTimeMillis() - checkInterval - offset, true},
				{checkInterval, true, System.currentTimeMillis() - checkInterval, true},
				{checkInterval, true, System.currentTimeMillis() - checkInterval + offset, true},
		});
	}
}
