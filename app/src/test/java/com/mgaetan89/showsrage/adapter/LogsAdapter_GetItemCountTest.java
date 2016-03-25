package com.mgaetan89.showsrage.adapter;

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
public class LogsAdapter_GetItemCountTest {
	@Parameterized.Parameter(1)
	public int itemCount;

	@Parameterized.Parameter(0)
	public List<String> logs;

	private LogsAdapter adapter;

	@Before
	public void before() {
		this.adapter = new LogsAdapter(this.logs);
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
				{Collections.emptyList(), 0},
				{Collections.singletonList(""), 1},
				{Arrays.asList("", "", ""), 3},
		});
	}
}
