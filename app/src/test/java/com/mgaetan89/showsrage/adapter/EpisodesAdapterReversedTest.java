package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.model.Episode;

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
public class EpisodesAdapterReversedTest {
	@Parameterized.Parameter(3)
	public int episodeNumber;

	@Parameterized.Parameter(0)
	public List<Episode> episodes;

	@Parameterized.Parameter(1)
	public int itemCount;

	@Parameterized.Parameter(2)
	public int position;

	private EpisodesAdapter adapter;

	@Before
	public void before() {
		this.adapter = new EpisodesAdapter(this.episodes, 1, null, true);
	}

	@Test
	public void getEpisodeNumber() {
		assertThat(this.adapter.getEpisodeNumber(this.position)).isEqualTo(this.episodeNumber);
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
				{null, 0, 0, 0},
				{null, 0, 1, -1},
				{Collections.emptyList(), 0, 0, 0},
				{Collections.emptyList(), 0, 1, -1},
				{Collections.singletonList(new Episode()), 1, 0, 1},
				{Collections.singletonList(new Episode()), 1, 1, 0},
				{Arrays.asList(new Episode(), new Episode(), new Episode()), 3, 0, 3},
				{Arrays.asList(new Episode(), new Episode(), new Episode()), 3, 1, 2},
				{Arrays.asList(new Episode(), new Episode(), new Episode()), 3, 2, 1},
				{Arrays.asList(new Episode(), new Episode(), new Episode()), 3, 3, 0},
		});
	}
}
