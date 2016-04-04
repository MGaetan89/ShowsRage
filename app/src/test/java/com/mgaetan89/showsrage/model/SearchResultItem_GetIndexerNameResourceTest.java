package com.mgaetan89.showsrage.model;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.network.SickRageApi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SearchResultItem_GetIndexerNameResourceTest {
	@Parameterized.Parameter(0)
	public SearchResultItem searchResult;

	@Parameterized.Parameter(1)
	public int indexerNameResource;

	@Test
	public void getIndexerName() {
		assertThat(this.searchResult.getIndexerNameResource()).isEqualTo(this.indexerNameResource);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = SickRageApi.Companion.getGson();

		return Arrays.asList(new Object[][]{
				{gson.fromJson("{}", SearchResultItem.class), 0},
				{gson.fromJson("{indexer: -1}", SearchResultItem.class), 0},
				{gson.fromJson("{indexer: 0}", SearchResultItem.class), 0},
				{gson.fromJson("{indexer: 1}", SearchResultItem.class), R.string.the_tvdb},
				{gson.fromJson("{indexer: 2}", SearchResultItem.class), R.string.tvrage},
				{gson.fromJson("{indexer: 3}", SearchResultItem.class), 0},
		});
	}
}
