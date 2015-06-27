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
public class ComingEpisodesAdapter_GetItemAtPositionTest {
	@Parameterized.Parameter(0)
	public Map<String, List<ComingEpisode>> comingEpisodes;

	@Parameterized.Parameter(2)
	public Class<?> itemType;

	@Parameterized.Parameter(1)
	public int position;

	private ComingEpisodesAdapter adapter;

	@Before
	public void before() {
		this.adapter = new ComingEpisodesAdapter(this.comingEpisodes);
	}

	@Test
	public void getItemAtPosition() {
		Object item = this.adapter.getItemAtPosition(this.position);

		if (item == null) {
			assertThat(this.itemType).isNull();
		} else {
			assertThat(item.getClass()).isEqualTo(this.itemType);
		}
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
				{data, -1, null},
				{data, 0, String.class}, // Section 1
				{data, 1, ComingEpisode.class}, // Section 1, Episode 1
				{data, 2, ComingEpisode.class}, // Section 1, Episode 2
				{data, 3, String.class}, // Section 3
				{data, 4, ComingEpisode.class}, // Section 3, Episode 1
				{data, 5, ComingEpisode.class}, // Section 3, Episode 2
				{data, 6, ComingEpisode.class}, // Section 3, Episode 3
				{data, 7, null},
				{data, 8, null},
		});
	}
}
