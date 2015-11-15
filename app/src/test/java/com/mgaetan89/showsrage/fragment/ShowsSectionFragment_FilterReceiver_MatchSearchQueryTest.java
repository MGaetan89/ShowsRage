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
public class ShowsSectionFragment_FilterReceiver_MatchSearchQueryTest {
	@Parameterized.Parameter(1)
	public String searchQuery;

	@Parameterized.Parameter(2)
	public boolean match;

	@Parameterized.Parameter(0)
	public Show show;

	@Test
	public void matchSearchQuery() {
		assertThat(ShowsSectionFragment.FilterReceiver.matchSearchQuery(this.show, this.searchQuery)).isEqualTo(this.match);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{gson.fromJson("{show_name: \"\"}", Show.class), null, true},
				{gson.fromJson("{show_name: \"\"}", Show.class), "", true},
				{gson.fromJson("{show_name: \"\"}", Show.class), " ", true},
				{gson.fromJson("{show_name: \"\"}", Show.class), "  ", true},
				{gson.fromJson("{show_name: \"Show Name 1\"}", Show.class), null, true},
				{gson.fromJson("{show_name: \"Show Name 2\"}", Show.class), "", true},
				{gson.fromJson("{show_name: \"Show Name 3\"}", Show.class), " ", true},
				{gson.fromJson("{show_name: \"Show Name 4\"}", Show.class), "  ", true},
				{gson.fromJson("{show_name: \"\"}", Show.class), "abc", false},
				{gson.fromJson("{show_name: \"\"}", Show.class), "Abc", false},
				{gson.fromJson("{show_name: \"Show Name 5\"}", Show.class), "Show", true},
				{gson.fromJson("{show_name: \"Show Name 6\"}", Show.class), "show", true},
				{gson.fromJson("{show_name: \"Show Name 7\"}", Show.class), "Name", true},
				{gson.fromJson("{show_name: \"Show Name 8\"}", Show.class), "name", true},
				{gson.fromJson("{show_name: \"Show Name 9\"}", Show.class), "Show name", true},
				{gson.fromJson("{show_name: \"Show Name 10\"}", Show.class), "show Name", true},
				{gson.fromJson("{show_name: \"Show Name 11\"}", Show.class), "Abc", false},
				{gson.fromJson("{show_name: \"Show Name 12\"}", Show.class), "abc", false},
				{gson.fromJson("{show_name: \"Show Name 13\"}", Show.class), "Name Show", false},
				{gson.fromJson("{show_name: \"Show Name 14\"}", Show.class), "name show", false},
		});
	}
}
