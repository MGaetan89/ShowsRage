package com.mgaetan89.showsrage.model;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsFilters_State_GetViewIdForStateTest {
	@Parameterized.Parameter(0)
	public ShowsFilters.State state;

	@Parameterized.Parameter(1)
	public int viewId;

	@Test
	public void getViewIdForState() {
		assertThat(ShowsFilters.State.Companion.getViewIdForState(this.state)).isEqualTo(this.viewId);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{ShowsFilters.State.ACTIVE, R.id.filter_active},
				{ShowsFilters.State.ALL, R.id.filter_all},
				{ShowsFilters.State.PAUSED, R.id.filter_paused},
		});
	}
}
