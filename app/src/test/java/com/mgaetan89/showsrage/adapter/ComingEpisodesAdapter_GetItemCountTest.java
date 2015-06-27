package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.model.ComingEpisode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ComingEpisodesAdapter_GetItemCountTest {
	@Parameterized.Parameter(1)
	public int count;

	@Parameterized.Parameter(0)
	public Map<String, List<ComingEpisode>> comingEpisodes;

	private ComingEpisodesAdapter adapter;

	@Before
	public void before() {
		this.adapter = new ComingEpisodesAdapter(this.comingEpisodes);
	}

	@Test
	public void getItemCount() {
		assertThat(this.adapter.getItemCount()).isEqualTo(this.count);
	}

	@After
	public void after() {
		this.adapter = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Collection<Object[]> data = new ArrayList<>();

		// Entry 1
		Map<String, List<ComingEpisode>> entry = null;

		data.add(new Object[]{entry, 0});

		// Entry 2
		entry = Collections.emptyMap();

		data.add(new Object[]{entry, 0});

		// Entry 3
		entry = Collections.singletonMap("a", null);

		data.add(new Object[]{entry, 0});

		// Entry 4
		entry = Collections.singletonMap("b", Collections.<ComingEpisode>emptyList());

		data.add(new Object[]{entry, 0});

		// Entry 5
		entry = Collections.singletonMap("c", Collections.<ComingEpisode>singletonList(null));

		data.add(new Object[]{entry, 2});

		// Entry 6
		entry = new LinkedHashMap<>();
		entry.put("d", Collections.<ComingEpisode>singletonList(null));
		entry.put("e", Collections.<ComingEpisode>emptyList());
		entry.put("f", Arrays.<ComingEpisode>asList(null, null, null));

		data.add(new Object[]{entry, 6});

		return data;
	}
}
