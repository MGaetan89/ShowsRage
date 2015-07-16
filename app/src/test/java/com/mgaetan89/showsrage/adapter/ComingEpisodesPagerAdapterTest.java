package com.mgaetan89.showsrage.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.fragment.ComingEpisodesSectionFragment;
import com.mgaetan89.showsrage.model.ComingEpisode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ComingEpisodesPagerAdapterTest {
	private ComingEpisodesPagerAdapter adapter;

	@NonNull
	private final List<ArrayList<ComingEpisode>> comingEpisodes = new ArrayList<>();

	@NonNull
	private final List<String> sections = Arrays.asList("Missed", "Today", "Soon", "Later");

	public ComingEpisodesPagerAdapterTest() {
		for (String ignored : this.sections) {
			this.comingEpisodes.add(new ArrayList<ComingEpisode>());
		}
	}

	@Before
	public void before() {
		this.adapter = new ComingEpisodesPagerAdapter(null, this.sections, this.comingEpisodes);
	}

	@Test
	public void getCount() {
		assertThat(this.adapter.getCount()).isEqualTo(this.sections.size());
	}

	@Test
	public void getItem() {
		for (int i = 0; i < this.sections.size(); i++) {
			Fragment fragment = this.adapter.getItem(i);
			assertThat(fragment).isInstanceOf(ComingEpisodesSectionFragment.class);
			assertThat(fragment.getArguments()).isNotNull();
			assertThat(fragment.getArguments().containsKey(Constants.Bundle.COMING_EPISODES));
		}
	}

	@Test
	public void getPageTitle() {
		for (int i = 0; i < this.sections.size(); i++) {
			assertThat(this.adapter.getPageTitle(i)).isEqualTo(this.sections.get(i));
		}
	}

	@After
	public void after() {
		this.adapter = null;
	}
}
