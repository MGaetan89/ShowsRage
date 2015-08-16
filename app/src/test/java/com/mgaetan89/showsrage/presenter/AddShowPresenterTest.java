package com.mgaetan89.showsrage.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.SearchResultsAdapter;
import com.mgaetan89.showsrage.model.SearchResultItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddShowPresenterTest {
	private AddShowPresenter presenter = null;
	private final List<SearchResultItem> searchResults = new ArrayList<>();

	@Before
	public void before() {
		this.presenter = new AddShowPresenter(this.searchResults);
	}

	@Test
	public void getAdapter() {
		assertThat(this.presenter.getAdapter()).isInstanceOf(SearchResultsAdapter.class);
	}

	@Test
	public void getEmptyViewVisibility() {
		this.searchResults.clear();

		assertThat(this.presenter.getEmptyViewVisibility()).isEqualTo(View.VISIBLE);

		this.searchResults.add(new SearchResultItem());

		assertThat(this.presenter.getEmptyViewVisibility()).isEqualTo(View.GONE);
	}

	@Test
	public void getLayoutManager_Default() {
		RecyclerView.LayoutManager layoutManager = this.presenter.getLayoutManager();

		assertThat(layoutManager).isInstanceOf(StaggeredGridLayoutManager.class);
		assertThat(((StaggeredGridLayoutManager) layoutManager).getSpanCount()).isEqualTo(1);
		assertThat(((StaggeredGridLayoutManager) layoutManager).getOrientation()).isEqualTo(StaggeredGridLayoutManager.VERTICAL);
	}

	@Test
	public void getLayoutManager_ContextNotNull() {
		Resources resources = mock(Resources.class);
		when(resources.getInteger(R.integer.shows_column_count)).thenReturn(5);

		Context context = mock(Context.class);
		when(context.getResources()).thenReturn(resources);

		this.presenter.setContext(context);

		RecyclerView.LayoutManager layoutManager = this.presenter.getLayoutManager();

		assertThat(layoutManager).isInstanceOf(GridLayoutManager.class);
		assertThat(((GridLayoutManager) layoutManager).getSpanCount()).isEqualTo(5);
	}

	@Test
	public void getLayoutManager_ContextNull() {
		this.presenter.setContext(null);

		RecyclerView.LayoutManager layoutManager = this.presenter.getLayoutManager();

		assertThat(layoutManager).isInstanceOf(StaggeredGridLayoutManager.class);
		assertThat(((StaggeredGridLayoutManager) layoutManager).getSpanCount()).isEqualTo(1);
		assertThat(((StaggeredGridLayoutManager) layoutManager).getOrientation()).isEqualTo(StaggeredGridLayoutManager.VERTICAL);
	}

	@Test
	public void getRecyclerViewVisibility() {
		this.searchResults.clear();

		assertThat(this.presenter.getRecyclerViewVisibility()).isEqualTo(View.GONE);

		this.searchResults.add(new SearchResultItem());

		assertThat(this.presenter.getRecyclerViewVisibility()).isEqualTo(View.VISIBLE);
	}

	@After
	public void after() {
		this.presenter = null;

		this.searchResults.clear();
	}
}
