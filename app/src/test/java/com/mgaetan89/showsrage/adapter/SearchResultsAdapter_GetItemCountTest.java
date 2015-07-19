package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.model.SearchResultItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SearchResultsAdapter_GetItemCountTest {
	@Parameterized.Parameter(1)
	public int itemCount;

	@Parameterized.Parameter(0)
	public List<SearchResultItem> searchResultItems;

	private SearchResultsAdapter adapter;

	@Before
	public void before() {
		this.adapter = new SearchResultsAdapter(this.searchResultItems);
	}

	@Test
	public void getItemCount() {
		assertThat(this.adapter.getItemCount()).isEqualTo(this.itemCount);
	}

	@After
	public void after() {
		this.adapter = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, 0},
				{Collections.emptyList(), 0},
				{Collections.singletonList(new SearchResultItem()), 1},
				{Arrays.asList(new SearchResultItem(), new SearchResultItem(), new SearchResultItem()), 3},
		});
	}
}
