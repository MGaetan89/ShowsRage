package com.mgaetan89.showsrage.fragment;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.ShowsFilters;
import com.mgaetan89.showsrage.network.SickRageApi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsSectionFragment_FilterReceiver_MatchFilterStateTest {
	@Parameterized.Parameter(1)
	public ShowsFilters.State filterState;

	@Parameterized.Parameter(2)
	public boolean match;

	@Parameterized.Parameter(0)
	public Show show;

	@Test
	public void matchFilterState() {
		assertThat(ShowsSectionFragment.FilterReceiver.Companion.matchFilterState(this.show, this.filterState)).isEqualTo(this.match);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = SickRageApi.Companion.getGson();

		return Arrays.asList(new Object[][]{
				{gson.fromJson("{paused: 0}", Show.class), null, false},
				{gson.fromJson("{paused: 1}", Show.class), null, false},
				{gson.fromJson("{paused: 0}", Show.class), ShowsFilters.State.ACTIVE, true},
				{gson.fromJson("{paused: 1}", Show.class), ShowsFilters.State.ACTIVE, false},
				{gson.fromJson("{paused: 0}", Show.class), ShowsFilters.State.ALL, true},
				{gson.fromJson("{paused: 1}", Show.class), ShowsFilters.State.ALL, true},
				{gson.fromJson("{paused: 0}", Show.class), ShowsFilters.State.PAUSED, false},
				{gson.fromJson("{paused: 1}", Show.class), ShowsFilters.State.PAUSED, true},
		});
	}
}
