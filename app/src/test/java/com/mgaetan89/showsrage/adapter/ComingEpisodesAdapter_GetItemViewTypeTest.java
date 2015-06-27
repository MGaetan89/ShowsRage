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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ComingEpisodesAdapter_GetItemViewTypeTest {
	@Parameterized.Parameter(0)
	public Map<String, List<ComingEpisode>> comingEpisodes;

	@Parameterized.Parameter(1)
	public int position;

	@Parameterized.Parameter(2)
	public int viewType;

	private ComingEpisodesAdapter adapter;

	@Before
	public void before() {
		this.adapter = new ComingEpisodesAdapter(this.comingEpisodes);
	}

	@Test
	public void getItemViewType() {
		assertThat(this.adapter.getItemViewType(this.position)).isEqualTo(this.viewType);
	}

	@After
	public void after() {
		this.adapter = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Map<String, List<ComingEpisode>> data = new LinkedHashMap<>();
		data.put("Section 0", null);
		data.put("Section 1", Arrays.asList(new ComingEpisode(), new ComingEpisode()));
		data.put("Section 2", Collections.<ComingEpisode>emptyList());
		data.put("Section 3", Arrays.asList(new ComingEpisode(), new ComingEpisode(), new ComingEpisode()));
		data.put("Section 4", Collections.<ComingEpisode>emptyList());

		return Arrays.asList(new Object[][]{
				{data, -1, ComingEpisodesAdapter.ITEM_TYPE_UNKNOWN},
				{data, 0, ComingEpisodesAdapter.ITEM_TYPE_SECTION}, // Section 1
				{data, 1, ComingEpisodesAdapter.ITEM_TYPE_EPISODE}, // Section 1, Episode 1
				{data, 2, ComingEpisodesAdapter.ITEM_TYPE_EPISODE}, // Section 1, Episode 2
				{data, 3, ComingEpisodesAdapter.ITEM_TYPE_SECTION}, // Section 3
				{data, 4, ComingEpisodesAdapter.ITEM_TYPE_EPISODE}, // Section 3, Episode 1
				{data, 5, ComingEpisodesAdapter.ITEM_TYPE_EPISODE}, // Section 3, Episode 2
				{data, 6, ComingEpisodesAdapter.ITEM_TYPE_EPISODE}, // Section 3, Episode 3
				{data, 7, ComingEpisodesAdapter.ITEM_TYPE_UNKNOWN},
				{data, 8, ComingEpisodesAdapter.ITEM_TYPE_UNKNOWN},
		});
	}
}
