package com.mgaetan89.showsrage.presenter;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.SearchResultItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SearchResultPresenterTest {
	@Parameterized.Parameter(1)
	public CharSequence firstAired;

	@Parameterized.Parameter(2)
	public int indexerNameRes;

	@Parameterized.Parameter(3)
	public String name;

	@Parameterized.Parameter(0)
	public SearchResultItem searchResult;

	private SearchResultPresenter presenter;

	@Before
	public void before() {
		this.presenter = new SearchResultPresenter(this.searchResult);
	}

	@Test
	public void getFirstAired() {
		assertThat(this.presenter.getFirstAired()).isEqualTo(this.firstAired);
	}

	@Test
	public void getIndexerNameRes() {
		assertThat(this.presenter.getIndexerNameRes()).isEqualTo(this.indexerNameRes);
	}

	@Test
	public void getName() {
		assertThat(this.presenter.getName()).isEqualTo(this.name);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{null, "", 0, ""},
				{gson.fromJson("{first_aired: 2015-01-01, indexer: 0, name: \"Show 0\"}", SearchResultItem.class), null, 0, "Show 0"},
				{gson.fromJson("{first_aired: 2015-01-01, indexer: 1, name: \"Show 1\"}", SearchResultItem.class), null, R.string.the_tvdb, "Show 1"},
				{gson.fromJson("{first_aired: 2015-01-01, indexer: 2, name: \"Show 2\"}", SearchResultItem.class), null, R.string.tvrage, "Show 2"},
		});
	}
}
