package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsFragment_GetFilterPausedActiveModeTest {
	@Parameterized.Parameter(0)
	public int filterId;

	@Parameterized.Parameter(1)
	public int filterMode;

	@Test
	public void getFilterPausedActiveMode() {
		assertThat(ShowsFragment.getFilterPausedActiveMode(this.filterId)).isEqualTo(this.filterMode);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{0, ShowsFragment.FILTER_PAUSED_ACTIVE_BOTH},
				{R.id.filter_active, ShowsFragment.FILTER_PAUSED_ACTIVE_ACTIVE},
				{R.id.filter_all, ShowsFragment.FILTER_PAUSED_ACTIVE_BOTH},
				{R.id.filter_paused, ShowsFragment.FILTER_PAUSED_ACTIVE_PAUSED},
		});
	}
}
