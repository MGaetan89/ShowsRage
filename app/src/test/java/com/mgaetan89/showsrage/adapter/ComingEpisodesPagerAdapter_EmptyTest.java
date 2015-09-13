package com.mgaetan89.showsrage.adapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ComingEpisodesPagerAdapter_EmptyTest {
	private ComingEpisodesPagerAdapter adapter;

	public ComingEpisodesPagerAdapter_EmptyTest() {
	}

	@Before
	public void before() {
		this.adapter = new ComingEpisodesPagerAdapter(null, null, null);
	}

	@Test
	public void getCount() {
		assertThat(this.adapter.getCount()).isEqualTo(0);
	}

	@After
	public void after() {
		this.adapter = null;
	}
}
