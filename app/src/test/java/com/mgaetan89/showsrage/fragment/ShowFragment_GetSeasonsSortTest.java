package com.mgaetan89.showsrage.fragment;

import android.content.SharedPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ShowFragment_GetSeasonsSortTest {
	@Parameterized.Parameter(1)
	public String sort;

	@Parameterized.Parameter(0)
	public boolean status;

	private SharedPreferences preferences = null;

	@Before
	public void before() {
		this.preferences = mock(SharedPreferences.class);
		when(this.preferences.getBoolean("display_seasons_sort", false)).thenReturn(this.status);
	}

	@Test
	public void getSeasonsSort() {
		String sort = ShowFragment.getSeasonsSort(this.preferences);

		assertThat(sort).isEqualTo(this.sort);
	}

	@Test
	public void getSeasonsSortNull() {
		String sort = ShowFragment.getSeasonsSort(null);

		assertThat(sort).isEqualTo("desc");
	}

	@After
	public void after() {
		this.preferences = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{false, "desc"},
				{true, "asc"}
		});
	}
}
