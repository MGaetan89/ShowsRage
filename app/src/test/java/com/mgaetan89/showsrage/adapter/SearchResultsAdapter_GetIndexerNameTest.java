package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SearchResultsAdapter_GetIndexerNameTest {
	@Parameterized.Parameter(0)
	public int indexerId;

	@Parameterized.Parameter(1)
	public int indexerName;

	@Test
	public void getIndexerName() {
		assertThat(SearchResultsAdapter.getIndexerName(this.indexerId)).isEqualTo(this.indexerName);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{-1, 0},
				{0, 0},
				{1, R.string.the_tvdb},
				{2, R.string.tvrage},
				{3, 0},
		});
	}
}
