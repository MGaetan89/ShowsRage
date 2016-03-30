package com.mgaetan89.showsrage.model;

import com.mgaetan89.showsrage.model.ShowsFilters.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsFilters_Status_IsTest {
	@Parameterized.Parameter(1)
	public boolean all;

	@Parameterized.Parameter(2)
	public boolean continuing;

	@Parameterized.Parameter(3)
	public boolean ended;

	@Parameterized.Parameter(0)
	public int status;

	@Parameterized.Parameter(4)
	public boolean unknown;

	@Test
	public void isAll() {
		assertThat(ShowsFilters.Status.Companion.isAll(this.status)).isEqualTo(this.all);
	}

	@Test
	public void isContinuing() {
		assertThat(ShowsFilters.Status.Companion.isContinuing(this.status)).isEqualTo(this.continuing);
	}

	@Test
	public void isEnded() {
		assertThat(ShowsFilters.Status.Companion.isEnded(this.status)).isEqualTo(this.ended);
	}

	@Test
	public void isUnknown() {
		assertThat(ShowsFilters.Status.Companion.isUnknown(this.status)).isEqualTo(this.unknown);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		int all = Status.ALL.getStatus();
		int continuing = Status.CONTINUING.getStatus();
		int ended = Status.ENDED.getStatus();
		int unknown = Status.UNKNOWN.getStatus();

		return Arrays.asList(new Object[][]{
				{0, false, false, false, false},
				{all, true, true, true, true},
				{continuing, false, true, false, false},
				{continuing | ended, false, true, true, false},
				{continuing | ended | unknown, true, true, true, true},
				{continuing | unknown, false, true, false, true},
				{ended, false, false, true, false},
				{ended | unknown, false, false, true, true},
				{unknown, false, false, false, true},
		});
	}
}
