package com.mgaetan89.showsrage.model;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowStat_GetTotalDoneTest {
	@Parameterized.Parameter(1)
	public int totalDone;

	@Parameterized.Parameter(0)
	public ShowStat showStat;

	@Test
	public void getStatusBackgroundColor() {
		assertThat(this.showStat.getTotalDone()).isEqualTo(this.totalDone);
	}

	@Parameterized.Parameters(name = "{index}: {0} - {1}")
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{new ShowStat(), 0},
				{gson.fromJson("{}", ShowStat.class), 0},
				{gson.fromJson("{archived: 0}", ShowStat.class), 0},
				{gson.fromJson("{archived: 1}", ShowStat.class), 1},
				{gson.fromJson("{archived: 2, downloaded: null}", ShowStat.class), 2},
				{gson.fromJson("{archived: 3, downloaded: {}}", ShowStat.class), 3},
				{gson.fromJson("{archived: 4, downloaded: {1080p_bluray: 1}}", ShowStat.class), 4},
				{gson.fromJson("{archived: 5, downloaded: {total: 2}}", ShowStat.class), 7},
				{gson.fromJson("{archived: 6, downloaded: {1080p_bluray: 3, total: 4}}", ShowStat.class), 10},
		});
	}
}
