package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ScheduleFragment_GetSectionNameTest {
	@Parameterized.Parameter(0)
	public String sectionId;

	@Parameterized.Parameter(1)
	public int sectionName;

	@Test
	public void getSectionName() {
		assertThat(ScheduleFragment.getSectionName(this.sectionId)).isEqualTo(this.sectionName);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, 0},
				{"", 0},
				{"later", R.string.later},
				{"Later", R.string.later},
				{"missed", R.string.missed},
				{"Missed", R.string.missed},
				{"soon", R.string.soon},
				{"Soon", R.string.soon},
				{"today", R.string.today},
				{"Today", R.string.today},
				{"statusid", 0},
				{"StatusId", 0},
		});
	}
}
