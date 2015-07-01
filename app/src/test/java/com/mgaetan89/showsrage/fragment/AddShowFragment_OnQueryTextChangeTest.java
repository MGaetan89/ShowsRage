package com.mgaetan89.showsrage.fragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AddShowFragment_OnQueryTextChangeTest {
	@Parameterized.Parameter(0)
	public String query;

	@Parameterized.Parameter(1)
	public boolean valid;

	private AddShowFragment fragment;

	@Before
	public void before() {
		this.fragment = new AddShowFragment();
	}

	@Test
	public void onQueryTextChange() {
		assertThat(this.fragment.onQueryTextChange(this.query)).isFalse();
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
