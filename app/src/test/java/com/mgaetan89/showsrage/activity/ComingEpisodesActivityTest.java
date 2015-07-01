package com.mgaetan89.showsrage.activity;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ComingEpisodesFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ComingEpisodesActivityTest {
	private ComingEpisodesActivity activity;

	@Before
	public void before() {
		this.activity = new ComingEpisodesActivity();
	}

	@Test
	public void displayHomeAsUp() {
		assertThat(this.activity.displayHomeAsUp()).isFalse();
	}

	@Test
	public void getFragment() {
		assertThat(this.activity.getFragment().getClass()).isEqualTo(ComingEpisodesFragment.class);
	}

	@Test
	public void getSelectedMenuId() {
		assertThat(this.activity.getSelectedMenuId()).isEqualTo(R.id.menu_coming_episodes);
	}

	@Test
	public void getTitleResourceId() {
		assertThat(this.activity.getTitleResourceId()).isEqualTo(R.string.coming_episodes);
	}

	@After
	public void after() {
		this.activity = null;
	}
}
