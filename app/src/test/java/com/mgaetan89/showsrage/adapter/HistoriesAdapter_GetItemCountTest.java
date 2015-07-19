package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.model.History;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HistoriesAdapter_GetItemCountTest {
	@Parameterized.Parameter(0)
	public List<History> histories;

	@Parameterized.Parameter(1)
	public int itemCount;

	private HistoriesAdapter adapter;

	@Before
	public void before() {
		this.adapter = new HistoriesAdapter(this.histories);
	}

	@Test
	public void getItemCount() {
		assertThat(this.adapter.getItemCount()).isEqualTo(this.itemCount);
	}

	@After
	public void after() {
		this.adapter = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, 0},
				{Collections.emptyList(), 0},
				{Collections.singletonList(new History()), 1},
				{Arrays.asList(new History(), new History(), new History()), 3},
		});
	}
}
