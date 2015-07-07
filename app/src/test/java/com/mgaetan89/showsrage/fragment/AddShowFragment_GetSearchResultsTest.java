package com.mgaetan89.showsrage.fragment;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.SearchResults;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AddShowFragment_GetSearchResultsTest {
	@Parameterized.Parameter(0)
	public SearchResults searchResults;

	@Parameterized.Parameter(1)
	public int size;

	@Test
	public void getSearchResults() {
		assertThat(AddShowFragment.getSearchResults(this.searchResults).size()).isEqualTo(this.size);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{null, 0},
				{new SearchResults(), 0},
				{gson.fromJson("{data: null}", SearchResults.class), 0},
				{gson.fromJson("{data: {}}", SearchResults.class), 0},
				{gson.fromJson("{data: {results: null}}", SearchResults.class), 0},
				{gson.fromJson("{data: {results: []}}", SearchResults.class), 0},
				{gson.fromJson("{data: {results: [{}, {}]}}", SearchResults.class), 2},
		});
	}
}
