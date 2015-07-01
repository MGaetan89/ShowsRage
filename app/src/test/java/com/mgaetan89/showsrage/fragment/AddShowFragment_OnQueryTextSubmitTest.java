package com.mgaetan89.showsrage.fragment;

import android.content.SharedPreferences;

import com.mgaetan89.showsrage.network.SickRageApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class AddShowFragment_OnQueryTextSubmitTest {
	@Parameterized.Parameter(0)
	public String query;

	@Parameterized.Parameter(1)
	public boolean valid;

	private AddShowFragment fragment;

	@Before
	public void before() {
		SharedPreferences preferences = mock(SharedPreferences.class);
		when(preferences.getString(anyString(), anyString())).thenReturn("");

		SickRageApi.getInstance().init(preferences);

		this.fragment = new AddShowFragment();
	}

	@Test
	public void onQueryTextSubmit() {
		assertThat(this.fragment.onQueryTextSubmit(this.query)).isEqualTo(this.valid);
	}

	@After
	public void after() {
		this.fragment = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return AddShowFragment_IsQueryValidTest.data();
	}
}
