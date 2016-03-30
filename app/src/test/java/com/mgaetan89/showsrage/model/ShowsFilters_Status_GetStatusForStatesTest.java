package com.mgaetan89.showsrage.model;

import com.mgaetan89.showsrage.model.ShowsFilters.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsFilters_Status_GetStatusForStatesTest {
	@Parameterized.Parameter(0)
	public boolean all;

	@Parameterized.Parameter(1)
	public boolean continuing;

	@Parameterized.Parameter(2)
	public boolean ended;

	@Parameterized.Parameter(4)
	public int status;

	@Parameterized.Parameter(3)
	public boolean unknown;

	@Test
	public void getStatusForStates() {
		assertThat(ShowsFilters.Status.Companion.getStatusForStates(this.all, this.continuing, this.ended, this.unknown)).isEqualTo(this.status);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		int all = Status.ALL.getStatus();
		int continuing = Status.CONTINUING.getStatus();
		int ended = Status.ENDED.getStatus();
		int unknown = Status.UNKNOWN.getStatus();

		return Arrays.asList(new Object[][]{
				{false, false, false, false, all},
				{false, false, false, true, unknown},
				{false, false, true, false, ended},
				{false, false, true, true, ended | unknown},
				{false, true, false, false, continuing},
				{false, true, false, true, continuing | unknown},
				{false, true, true, false, continuing | ended},
				{false, true, true, true, continuing | ended | unknown},
				{true, false, false, false, all},
				{true, false, false, true, all},
				{true, false, true, false, all},
				{true, false, true, true, all},
				{true, true, false, false, all},
				{true, true, false, true, all},
				{true, true, true, false, all},
				{true, true, true, true, all},
		});
	}
}
