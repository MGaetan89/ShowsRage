package com.mgaetan89.showsrage.fragment;

import android.widget.Spinner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class AddShowOptionsFragment_GetLocationTest {
	@Parameterized.Parameter(1)
	public String location;

	@Parameterized.Parameter(0)
	public Spinner spinner;

	@Test
	public void getLocation() {
		assertThat(AddShowOptionsFragment.Companion.getLocation(this.spinner)).isEqualTo(this.location);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, null},
				{getMockedSpinner(null), null},
				{getMockedSpinner(""), ""},
				{getMockedSpinner("/home/videos/Shows"), "/home/videos/Shows"},
		});
	}

	private static Spinner getMockedSpinner(String selectedItem) {
		Spinner spinner = mock(Spinner.class);
		when(spinner.getSelectedItem()).thenReturn(selectedItem);

		return spinner;
	}
}
