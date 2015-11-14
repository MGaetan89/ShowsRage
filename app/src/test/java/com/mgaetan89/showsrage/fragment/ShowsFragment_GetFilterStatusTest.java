package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsFragment_GetFilterStatusTest {
	@Parameterized.Parameter(0)
	public int filterId;

	@Parameterized.Parameter(1)
	public int filterStatus;

	@Test
	public void getFilterStatus() {
		assertThat(ShowsFragment.getFilterStatus(this.filterId)).isEqualTo(this.filterStatus);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{0, ShowsFragment.FILTER_STATUS_ALL},
				{R.id.filter_status_all, ShowsFragment.FILTER_STATUS_ALL},
				{R.id.filter_status_continuing, ShowsFragment.FILTER_STATUS_CONTINUING},
				{R.id.filter_status_ended, ShowsFragment.FILTER_STATUS_ENDED},
				{R.id.filter_status_unknown, ShowsFragment.FILTER_STATUS_UNKNOWN},
		});
	}
}
