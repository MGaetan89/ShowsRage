package com.mgaetan89.showsrage.adapter;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.SearchResultItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SearchResultsAdapter_GetIndexerIdTest {
	@Parameterized.Parameter(1)
	public int indexerId;

	@Parameterized.Parameter(0)
	public SearchResultItem searchResultItem;

	@Test
	public void getIndexId() {
		assertThat(SearchResultsAdapter.getIndexId(this.searchResultItem)).isEqualTo(this.indexerId);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{null, 0},
				{gson.fromJson("{indexer: -1, tvdbid: 123, tvrageid: 456}", SearchResultItem.class), 0},
				{gson.fromJson("{indexer: 0, tvdbid: 123, tvrageid: 456}", SearchResultItem.class), 0},
				{gson.fromJson("{indexer: 1, tvdbid: 123, tvrageid: 456}", SearchResultItem.class), 123},
				{gson.fromJson("{indexer: 2, tvdbid: 123, tvrageid: 456}", SearchResultItem.class), 456},
				{gson.fromJson("{indexer: 3, tvdbid: 123, tvrageid: 456}", SearchResultItem.class), 0},
		});
	}
}
