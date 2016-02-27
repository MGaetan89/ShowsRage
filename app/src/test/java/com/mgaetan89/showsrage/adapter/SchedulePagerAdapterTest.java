package com.mgaetan89.showsrage.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.fragment.ScheduleSectionFragment;
import com.mgaetan89.showsrage.model.Schedule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchedulePagerAdapterTest {
	private SchedulePagerAdapter adapter;

	@NonNull
	private final List<ArrayList<Schedule>> comingEpisodes = new ArrayList<>();

	@NonNull
	private final List<String> sections = Arrays.asList("Missed", "Today", "Soon", "Later");

	public SchedulePagerAdapterTest() {
		for (String ignored : this.sections) {
			this.comingEpisodes.add(new ArrayList<Schedule>());
		}
	}

	@Before
	public void before() {
		this.adapter = new SchedulePagerAdapter(null, this.sections, this.comingEpisodes);
	}

	@Test
	public void getCount() {
		assertThat(this.adapter.getCount()).isEqualTo(this.sections.size());
	}

	@Test
	public void getItem() {
		for (int i = 0; i < this.sections.size(); i++) {
			Fragment fragment = this.adapter.getItem(i);
			assertThat(fragment).isInstanceOf(ScheduleSectionFragment.class);
			assertThat(fragment.getArguments()).isNotNull();
			assertThat(fragment.getArguments().containsKey(Constants.Bundle.INSTANCE.getSCHEDULES()));
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
