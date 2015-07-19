package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.model.ComingEpisode;

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
public class ComingEpisodesAdapter_GetItemCountTest {
	@Parameterized.Parameter(0)
	public List<ComingEpisode> comingEpisodes;

	@Parameterized.Parameter(1)
	public int itemCount;

	private ComingEpisodesAdapter adapter;

	@Before
	public void before() {
		this.adapter = new ComingEpisodesAdapter(this.comingEpisodes);
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
				{Collections.singletonList(new ComingEpisode()), 1},
				{Arrays.asList(new ComingEpisode(), new ComingEpisode(), new ComingEpisode()), 3},
		});
	}
}
