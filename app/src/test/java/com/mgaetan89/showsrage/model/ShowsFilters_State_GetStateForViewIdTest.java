package com.mgaetan89.showsrage.model;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsFilters_State_GetStateForViewIdTest {
	@Parameterized.Parameter(1)
	public ShowsFilters.State state;

	@Parameterized.Parameter(0)
	public int viewId;

	@Test
	public void getStateForViewId() {
		assertThat(ShowsFilters.State.Companion.getStateForViewId(this.viewId)).isEqualTo(this.state);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{-42, ShowsFilters.State.ALL},
				{0, ShowsFilters.State.ALL},
				{42, ShowsFilters.State.ALL},
				{R.id.filter_status_ended, ShowsFilters.State.ALL},
				{R.id.filter_active, ShowsFilters.State.ACTIVE},
				{R.id.filter_all, ShowsFilters.State.ALL},
				{R.id.filter_paused, ShowsFilters.State.PAUSED},
		});
	}
}
