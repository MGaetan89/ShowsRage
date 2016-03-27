package com.mgaetan89.showsrage.fragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AddShowFragment_IsQueryValidTest {
	@Parameterized.Parameter(0)
	public String query;

	@Parameterized.Parameter(1)
	public boolean valid;

	@Test
	public void isQueryValid() {
		assertThat(AddShowFragment.Companion.isQueryValid(this.query)).isEqualTo(this.valid);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, false},
				{"", false},
				{" ", false},
				{"  ", false},
				{" some query ", true},
		});
	}
}
