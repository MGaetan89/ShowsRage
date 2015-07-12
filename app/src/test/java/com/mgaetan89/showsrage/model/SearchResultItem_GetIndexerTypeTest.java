package com.mgaetan89.showsrage.model;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SearchResultItem_GetIndexerTypeTest {
	@Parameterized.Parameter(0)
	public SearchResultItem searchResult;

	@Parameterized.Parameter(1)
	public Indexer indexerType;

	@Test
	public void getIndexerType() {
		assertThat(this.searchResult.getIndexerType()).isEqualTo(this.indexerType);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{gson.fromJson("{}", SearchResultItem.class), null},
				{gson.fromJson("{indexer: -1}", SearchResultItem.class), null},
				{gson.fromJson("{indexer: 0}", SearchResultItem.class), null},
				{gson.fromJson("{indexer: 1}", SearchResultItem.class), Indexer.TVDB},
				{gson.fromJson("{indexer: 2}", SearchResultItem.class), Indexer.TVRAGE},
				{gson.fromJson("{indexer: 3}", SearchResultItem.class), null},
		});
	}
}
