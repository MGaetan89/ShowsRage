package com.mgaetan89.showsrage.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.fragment.ScheduleSectionFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchedulePagerAdapterTest {
	private SchedulePagerAdapter adapter;

	@NonNull
	private final List<String> ids = Arrays.asList("missed", "today", "soon", "later");

	@NonNull
	private final List<String> labels = Arrays.asList("Missed", "Today", "Soon", "Later");

	@Before
	public void before() {
		this.adapter = new SchedulePagerAdapter(null, this.ids, this.labels);
	}

	@Test
	public void getCount() {
		assertThat(this.adapter.getCount()).isEqualTo(this.ids.size());
	}

	@Test
	public void getItem() {
		for (int i = 0; i < this.ids.size(); i++) {
			Fragment fragment = this.adapter.getItem(i);
			assertThat(fragment).isInstanceOf(ScheduleSectionFragment.class);
			assertThat(fragment.getArguments()).isNotNull();
			assertThat(fragment.getArguments().containsKey(Constants.Bundle.INSTANCE.getSCHEDULE_SECTION()));
		}
	}

	@Test
	public void getPageTitle() {
		for (int i = 0; i < this.ids.size(); i++) {
			assertThat(this.adapter.getPageTitle(i)).isEqualTo(this.labels.get(i));
		}
	}

	@After
	public void after() {
		this.adapter = null;
	}
}
