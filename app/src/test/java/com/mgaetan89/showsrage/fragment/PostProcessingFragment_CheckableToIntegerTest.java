package com.mgaetan89.showsrage.fragment;

import android.widget.Checkable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class PostProcessingFragment_CheckableToIntegerTest {
	@Parameterized.Parameter(0)
	public Checkable checkable;

	@Parameterized.Parameter(1)
	public boolean checked;

	@Parameterized.Parameter(2)
	public int result;

	@Before
	public void before() {
		if (this.checkable != null) {
			this.checkable.setChecked(this.checked);

			when(this.checkable.isChecked()).thenReturn(this.checked);
		}
	}

	@Test
	public void checkableToInteger() {
		assertThat(PostProcessingFragment.Companion.checkableToInteger(this.checkable)).isEqualTo(this.result);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, false, 0},
				{null, true, 0},
				{mock(Checkable.class), false, 0},
				{mock(Checkable.class), true, 1},
		});
	}
}
