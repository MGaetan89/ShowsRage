package com.mgaetan89.showsrage.activity;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ShowFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShowActivityTest {
	private ShowActivity activity;

	@Before
	public void before() {
		this.activity = new ShowActivity();
	}

	@Test
	public void displayHomeAsUp() {
		assertThat(this.activity.displayHomeAsUp()).isTrue();
	}

	@Test
	public void getFragment() {
		assertThat(this.activity.getFragment().getClass()).isEqualTo(ShowFragment.class);
	}

	@Test
	public void getSelectedMenuId() {
		assertThat(this.activity.getSelectedMenuId()).isEqualTo(R.id.menu_shows);
	}

	@Test
	public void getTitleResourceId() {
		assertThat(this.activity.getTitleResourceId()).isEqualTo(R.string.show);
	}

	@After
	public void after() {
		this.activity = null;
	}
}