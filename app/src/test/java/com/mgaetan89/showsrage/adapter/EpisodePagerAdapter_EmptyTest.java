package com.mgaetan89.showsrage.adapter;

import android.support.v4.app.Fragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class EpisodePagerAdapter_EmptyTest {
	private EpisodePagerAdapter adapter;

	@Before
	public void before() {
		this.adapter = new EpisodePagerAdapter(null, mock(Fragment.class), Collections.<Integer>emptyList());
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
