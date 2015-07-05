package com.mgaetan89.showsrage.fragment;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.Show;

import org.assertj.core.data.MapEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@RunWith(Parameterized.class)
public class ShowsFragment_GetCommandParametersTest {
	@Parameterized.Parameter(1)
	public MapEntry commandParameters[];

	@Parameterized.Parameter(0)
	public List<Show> shows;

	@Test
	public void getCommandParameters() {
		assertThat(ShowsFragment.getCommandParameters(this.shows)).containsOnly(this.commandParameters);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{null, new MapEntry[0]},
				{Collections.emptyList(), new MapEntry[0]},
				{Collections.singletonList(gson.fromJson("{indexerid: 123}", Show.class)), new MapEntry[]{
						entry("show.stats_123.indexerid", 123)
				}},
				{Arrays.asList(
						gson.fromJson("{indexerid: 123}", Show.class),
						null,
						gson.fromJson("{indexerid: 456}", Show.class)
				), new MapEntry[]{
						entry("show.stats_123.indexerid", 123),
						entry("show.stats_456.indexerid", 456),
				}},
				{Arrays.asList(
						gson.fromJson("{indexerid: 123}", Show.class),
						gson.fromJson("{indexerid: 456}", Show.class),
						gson.fromJson("{}", Show.class)
				), new MapEntry[]{
						entry("show.stats_123.indexerid", 123),
						entry("show.stats_456.indexerid", 456),
				}},
				{Arrays.asList(
						gson.fromJson("{indexerid: 123}", Show.class),
						null,
						gson.fromJson("{}", Show.class)
				), new MapEntry[]{
						entry("show.stats_123.indexerid", 123),
				}},
		});
	}
}
