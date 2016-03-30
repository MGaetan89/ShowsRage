package com.mgaetan89.showsrage.fragment;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.ShowsFilters;

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
		int all = ShowsFilters.Status.ALL.getStatus();
		int continuing = ShowsFilters.Status.CONTINUING.getStatus();
		int ended = ShowsFilters.Status.ENDED.getStatus();
		int unknown = ShowsFilters.Status.UNKNOWN.getStatus();
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{gson.fromJson("{status: \"continuing\"}", Show.class), all, true},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), all, true},
				{gson.fromJson("{status: \"ended\"}", Show.class), all, true},
				{gson.fromJson("{status: \"Ended\"}", Show.class), all, true},
				{gson.fromJson("{status: \"unknown\"}", Show.class), all, true},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), all, true},
				{gson.fromJson("{status: \"other status\"}", Show.class), all, true},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), all, true},
				{gson.fromJson("{status: \"continuing\"}", Show.class), continuing, true},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), continuing, true},
				{gson.fromJson("{status: \"ended\"}", Show.class), continuing, false},
				{gson.fromJson("{status: \"Ended\"}", Show.class), continuing, false},
				{gson.fromJson("{status: \"unknown\"}", Show.class), continuing, false},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), continuing, false},
				{gson.fromJson("{status: \"other status\"}", Show.class), continuing, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), continuing, false},
				{gson.fromJson("{status: \"continuing\"}", Show.class), continuing | ended, true},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), continuing | ended, true},
				{gson.fromJson("{status: \"ended\"}", Show.class), continuing | ended, true},
				{gson.fromJson("{status: \"Ended\"}", Show.class), continuing | ended, true},
				{gson.fromJson("{status: \"unknown\"}", Show.class), continuing | ended, false},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), continuing | ended, false},
				{gson.fromJson("{status: \"other status\"}", Show.class), continuing | ended, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), continuing | ended, false},
				{gson.fromJson("{status: \"continuing\"}", Show.class), continuing | ended | unknown, true},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), continuing | ended | unknown, true},
				{gson.fromJson("{status: \"ended\"}", Show.class), continuing | ended | unknown, true},
				{gson.fromJson("{status: \"Ended\"}", Show.class), continuing | ended | unknown, true},
				{gson.fromJson("{status: \"unknown\"}", Show.class), continuing | ended | unknown, true},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), continuing | ended | unknown, true},
				{gson.fromJson("{status: \"other status\"}", Show.class), continuing | ended | unknown, true},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), continuing | ended | unknown, true},
				{gson.fromJson("{status: \"continuing\"}", Show.class), continuing | unknown, true},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), continuing | unknown, true},
				{gson.fromJson("{status: \"ended\"}", Show.class), continuing | unknown, false},
				{gson.fromJson("{status: \"Ended\"}", Show.class), continuing | unknown, false},
				{gson.fromJson("{status: \"unknown\"}", Show.class), continuing | unknown, true},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), continuing | unknown, true},
				{gson.fromJson("{status: \"other status\"}", Show.class), continuing | unknown, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), continuing | unknown, false},
				{gson.fromJson("{status: \"continuing\"}", Show.class), ended, false},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), ended, false},
				{gson.fromJson("{status: \"ended\"}", Show.class), ended, true},
				{gson.fromJson("{status: \"Ended\"}", Show.class), ended, true},
				{gson.fromJson("{status: \"unknown\"}", Show.class), ended, false},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), ended, false},
				{gson.fromJson("{status: \"other status\"}", Show.class), ended, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), ended, false},
				{gson.fromJson("{status: \"continuing\"}", Show.class), ended | unknown, false},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), ended | unknown, false},
				{gson.fromJson("{status: \"ended\"}", Show.class), ended | unknown, true},
				{gson.fromJson("{status: \"Ended\"}", Show.class), ended | unknown, true},
				{gson.fromJson("{status: \"unknown\"}", Show.class), ended | unknown, true},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), ended | unknown, true},
				{gson.fromJson("{status: \"other status\"}", Show.class), ended | unknown, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), ended | unknown, false},
				{gson.fromJson("{status: \"continuing\"}", Show.class), unknown, false},
				{gson.fromJson("{status: \"Continuing\"}", Show.class), unknown, false},
				{gson.fromJson("{status: \"ended\"}", Show.class), unknown, false},
				{gson.fromJson("{status: \"Ended\"}", Show.class), unknown, false},
				{gson.fromJson("{status: \"unknown\"}", Show.class), unknown, true},
				{gson.fromJson("{status: \"Unknown\"}", Show.class), unknown, true},
				{gson.fromJson("{status: \"other status\"}", Show.class), unknown, false},
				{gson.fromJson("{status: \"Other Status\"}", Show.class), unknown, false},
		});
	}
}
