package com.mgaetan89.showsrage.model;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowStat_GetTotalPendingTest {
	@Parameterized.Parameter(1)
	public int totalPending;

	@Parameterized.Parameter(0)
	public ShowStat showStat;

	@Test
	public void getTotalPending() {
		assertThat(this.showStat.getTotalPending()).isEqualTo(this.totalPending);
	}

	@Parameterized.Parameters(name = "{index}: {0} - {1}")
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{new ShowStat(), 0},
				{gson.fromJson("{}", ShowStat.class), 0},
				{gson.fromJson("{snatched_best: 0}", ShowStat.class), 0},
				{gson.fromJson("{snatched_best: 1}", ShowStat.class), 1},
				{gson.fromJson("{snatched_best: 2, snatched: null}", ShowStat.class), 2},
				{gson.fromJson("{snatched_best: 3, snatched: {}}", ShowStat.class), 3},
				{gson.fromJson("{snatched_best: 4, snatched: {1080p_bluray: 1}}", ShowStat.class), 4},
				{gson.fromJson("{snatched_best: 5, snatched: {total: 2}}", ShowStat.class), 7},
				{gson.fromJson("{snatched_best: 6, snatched: {1080p_bluray: 3, total: 4}}", ShowStat.class), 10},
		});
	}
}
