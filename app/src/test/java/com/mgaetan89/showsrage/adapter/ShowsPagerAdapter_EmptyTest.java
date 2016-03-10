package com.mgaetan89.showsrage.adapter;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.mgaetan89.showsrage.model.Show;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ShowsPagerAdapter_EmptyTest {
	private ShowsPagerAdapter adapter;

	@Before
	public void before() {
		this.adapter = new ShowsPagerAdapter(null, mock(Fragment.class), new SparseArray<ArrayList<Show>>());
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
