package com.mgaetan89.showsrage.fragment;

import android.app.SearchManager;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class AddShowFragment_GetQueryFromIntentTest {
	@Parameterized.Parameter(0)
	public Intent intent;

	@Parameterized.Parameter(1)
	public String query;

	@Test
	public void getQueryFromIntent() {
		if (this.intent != null) {
			when(this.intent.getStringExtra(SearchManager.QUERY)).thenReturn(this.query);
		}

		assertThat(AddShowFragment.Companion.getQueryFromIntent(this.intent)).isEqualTo(this.query);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, ""},
				{getMockedIntent(Intent.ACTION_DIAL), ""},
				{getMockedIntent(Intent.ACTION_SEARCH), ""},
				{getMockedIntent(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, (String) null), ""},
				{getMockedIntent(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, ""), ""},
				{getMockedIntent(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, "Some query"), "Some query"},
		});
	}

	private static Intent getMockedIntent(String action) {
		Intent intent = mock(Intent.class);
		when(intent.putExtra(anyString(), anyString())).thenReturn(intent);
		when(intent.getAction()).thenReturn(action);

		return intent;
	}
}
