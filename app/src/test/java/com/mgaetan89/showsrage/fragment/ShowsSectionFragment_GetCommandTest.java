package com.mgaetan89.showsrage.fragment;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.Show;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsSectionFragment_GetCommandTest {
	@Parameterized.Parameter(1)
	public String command;

	@Parameterized.Parameter(0)
	public List<Show> shows;

	@Test
	public void getCommand() {
		assertThat(ShowsSectionFragment.getCommand(this.shows)).isEqualTo(this.command);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{null, ""},
				{Collections.emptyList(), ""},
				{Collections.singletonList(gson.fromJson("{indexerid: 123}", Show.class)), "show.stats_123"},
				{Arrays.asList(
						gson.fromJson("{indexerid: 123}", Show.class),
						null,
						gson.fromJson("{indexerid: 456}", Show.class)
				), "show.stats_123|show.stats_456"},
				{Arrays.asList(
						gson.fromJson("{indexerid: 123}", Show.class),
						gson.fromJson("{indexerid: 456}", Show.class),
						gson.fromJson("{}", Show.class)
				), "show.stats_123|show.stats_456"},
				{Arrays.asList(
						gson.fromJson("{indexerid: 123}", Show.class),
						null,
						gson.fromJson("{}", Show.class)
				), "show.stats_123"},
		});
	}
}
