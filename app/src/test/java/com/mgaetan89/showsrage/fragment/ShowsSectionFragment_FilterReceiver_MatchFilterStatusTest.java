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
public class ShowsSectionFragment_FilterReceiver_MatchFilterStatusTest {
	@Parameterized.Parameter(1)
	public int filterStatus;

	@Parameterized.Parameter(2)
	public boolean match;

	@Parameterized.Parameter(0)
	public Show show;

	@Test
	public void matchFilterStatus() {
		assertThat(ShowsSectionFragment.FilterReceiver.matchFilterStatus(this.show, this.filterStatus)).isEqualTo(this.match);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{gson.fromJson("{status: \"continuing\"}", Show.class), ShowsFragment.FILTER_STATUS_ALL, true},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), ShowsFragment.FILTER_STATUS_ALL, true},
				{gson.fromJson("{status: \"ended\"}", Show.class), ShowsFragment.FILTER_STATUS_ALL, true},
				{gson.fromJson("{status: \"Ended\"}", Show.class), ShowsFragment.FILTER_STATUS_ALL, true},
				{gson.fromJson("{status: \"unknown\"}", Show.class), ShowsFragment.FILTER_STATUS_ALL, true},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), ShowsFragment.FILTER_STATUS_ALL, true},
				{gson.fromJson("{status: \"other status\"}", Show.class), ShowsFragment.FILTER_STATUS_ALL, true},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), ShowsFragment.FILTER_STATUS_ALL, true},
				{gson.fromJson("{status: \"continuing\"}", Show.class), ShowsFragment.FILTER_STATUS_CONTINUING, true},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), ShowsFragment.FILTER_STATUS_CONTINUING, true},
				{gson.fromJson("{status: \"ended\"}", Show.class), ShowsFragment.FILTER_STATUS_CONTINUING, false},
				{gson.fromJson("{status: \"Ended\"}", Show.class), ShowsFragment.FILTER_STATUS_CONTINUING, false},
				{gson.fromJson("{status: \"unknown\"}", Show.class), ShowsFragment.FILTER_STATUS_CONTINUING, false},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), ShowsFragment.FILTER_STATUS_CONTINUING, false},
				{gson.fromJson("{status: \"other status\"}", Show.class), ShowsFragment.FILTER_STATUS_CONTINUING, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), ShowsFragment.FILTER_STATUS_CONTINUING, false},
				{gson.fromJson("{status: \"continuing\"}", Show.class), ShowsFragment.FILTER_STATUS_ENDED, false},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), ShowsFragment.FILTER_STATUS_ENDED, false},
				{gson.fromJson("{status: \"ended\"}", Show.class), ShowsFragment.FILTER_STATUS_ENDED, true},
				{gson.fromJson("{status: \"Ended\"}", Show.class), ShowsFragment.FILTER_STATUS_ENDED, true},
				{gson.fromJson("{status: \"unknown\"}", Show.class), ShowsFragment.FILTER_STATUS_ENDED, false},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), ShowsFragment.FILTER_STATUS_ENDED, false},
				{gson.fromJson("{status: \"other status\"}", Show.class), ShowsFragment.FILTER_STATUS_ENDED, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), ShowsFragment.FILTER_STATUS_ENDED, false},
				{gson.fromJson("{status: \"continuing\"}", Show.class), ShowsFragment.FILTER_STATUS_UNKNOWN, false},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), ShowsFragment.FILTER_STATUS_UNKNOWN, false},
				{gson.fromJson("{status: \"ended\"}", Show.class), ShowsFragment.FILTER_STATUS_UNKNOWN, false},
				{gson.fromJson("{status: \"Ended\"}", Show.class), ShowsFragment.FILTER_STATUS_UNKNOWN, false},
				{gson.fromJson("{status: \"unknown\"}", Show.class), ShowsFragment.FILTER_STATUS_UNKNOWN, true},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), ShowsFragment.FILTER_STATUS_UNKNOWN, true},
				{gson.fromJson("{status: \"other status\"}", Show.class), ShowsFragment.FILTER_STATUS_UNKNOWN, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), ShowsFragment.FILTER_STATUS_UNKNOWN, false},
				{gson.fromJson("{status: \"continuing\"}", Show.class), 4, false},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), 4, false},
				{gson.fromJson("{status: \"ended\"}", Show.class), 4, false},
				{gson.fromJson("{status: \"Ended\"}", Show.class), 4, false},
				{gson.fromJson("{status: \"unknown\"}", Show.class), 4, false},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), 4, false},
				{gson.fromJson("{status: \"other status\"}", Show.class), 4, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), 4, false},
		});
	}
}
