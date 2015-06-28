package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.model.Indexer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SearchResultsAdapter_GetIndexerTypeTest {
	@Parameterized.Parameter(0)
	public int indexerId;

	@Parameterized.Parameter(1)
	public Indexer indexerType;

	@Test
	public void getIndexType() {
		assertThat(SearchResultsAdapter.getIndexType(this.indexerId)).isEqualTo(this.indexerType);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{-1, null},
				{0, null},
				{1, Indexer.TVDB},
				{2, Indexer.TVRAGE},
				{3, null},
		});
	}
}
