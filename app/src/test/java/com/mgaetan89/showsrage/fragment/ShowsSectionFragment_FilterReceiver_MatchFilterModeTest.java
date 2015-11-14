package com.mgaetan89.showsrage.fragment;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.Show;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsSectionFragment_FilterReceiver_MatchFilterModeTest {
	@Parameterized.Parameter(1)
	public int filterMode;

	@Parameterized.Parameter(2)
	public boolean match;

	@Parameterized.Parameter(0)
	public Show show;

	@Test
	public void matchFilterMode() {
		assertThat(ShowsSectionFragment.FilterReceiver.matchFilterMode(this.show, this.filterMode)).isEqualTo(this.match);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{gson.fromJson("{paused: 0}", Show.class), ShowsFragment.FILTER_PAUSED_ACTIVE_ACTIVE, true},
				{gson.fromJson("{paused: 1}", Show.class), ShowsFragment.FILTER_PAUSED_ACTIVE_ACTIVE, false},
				{gson.fromJson("{paused: 0}", Show.class), ShowsFragment.FILTER_PAUSED_ACTIVE_BOTH, true},
				{gson.fromJson("{paused: 1}", Show.class), ShowsFragment.FILTER_PAUSED_ACTIVE_BOTH, true},
				{gson.fromJson("{paused: 0}", Show.class), ShowsFragment.FILTER_PAUSED_ACTIVE_PAUSED, false},
				{gson.fromJson("{paused: 1}", Show.class), ShowsFragment.FILTER_PAUSED_ACTIVE_PAUSED, true},
				{gson.fromJson("{paused: 0}", Show.class), 3, false},
				{gson.fromJson("{paused: 1}", Show.class), 3, false},
		});
	}
}
