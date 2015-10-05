package com.mgaetan89.showsrage.adapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SchedulePagerAdapter_EmptyTest {
	private SchedulePagerAdapter adapter;

	public SchedulePagerAdapter_EmptyTest() {
	}

	@Before
	public void before() {
		this.adapter = new SchedulePagerAdapter(null, null, null);
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
