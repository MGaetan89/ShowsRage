package com.mgaetan89.showsrage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mgaetan89.showsrage.model.Show;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ShowsPagerAdapter_EmptyTest {
	private ShowsPagerAdapter adapter;

	@Before
	public void before() {
		this.adapter = new ShowsPagerAdapter(mock(FragmentManager.class), mock(Fragment.class), Collections.<Integer, List<Show>>emptyMap());
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
