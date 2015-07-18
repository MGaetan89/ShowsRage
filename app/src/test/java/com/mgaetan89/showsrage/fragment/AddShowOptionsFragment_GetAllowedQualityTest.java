package com.mgaetan89.showsrage.fragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Spinner;

import com.mgaetan89.showsrage.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class AddShowOptionsFragment_GetAllowedQualityTest {
	@Parameterized.Parameter(1)
	public String allowedQuality;

	@Parameterized.Parameter(0)
	public Spinner spinner;

	private AddShowOptionsFragment fragment;

	@Before
	public void before() {
		Resources resources = mock(Resources.class);
		when(resources.getStringArray(R.array.allowed_qualities_keys)).thenReturn(new String[]{
				"sdtv",
				"sddvd",
				"hdtv",
				"rawhdtv",
				"fullhdtv",
				"hdwebdl",
				"fullhdwebdl",
				"hdbluray",
				"fullhdbluray",
				"unknown",
		});

		FragmentActivity activity = mock(FragmentActivity.class);
		when(activity.getResources()).thenReturn(resources);

		this.fragment = spy(new AddShowOptionsFragment());

		try {
			Field fragmentActivityField = Fragment.class.getDeclaredField("mActivity");
			fragmentActivityField.setAccessible(true);
			fragmentActivityField.set(this.fragment, activity);
		} catch (IllegalAccessException ignored) {
		} catch (NoSuchFieldException ignored) {
		}

		this.fragment.onAttach(activity);

		when(this.fragment.getResources()).thenReturn(resources);
	}

	@Test
	public void getAllowedQuality() {
		assertThat(this.fragment.getAllowedQuality(this.spinner)).isEqualTo(this.allowedQuality);
	}

	@After
	public void after() {
		this.fragment = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, "sdtv"},
				{getMockedSpinner(-1), null},
				{getMockedSpinner(0), null},
				{getMockedSpinner(1), "sdtv"},
				{getMockedSpinner(2), "sddvd"},
				{getMockedSpinner(3), "hdtv"},
				{getMockedSpinner(4), "rawhdtv"},
				{getMockedSpinner(5), "fullhdtv"},
				{getMockedSpinner(6), "hdwebdl"},
				{getMockedSpinner(7), "fullhdwebdl"},
				{getMockedSpinner(8), "hdbluray"},
				{getMockedSpinner(9), "fullhdbluray"},
				{getMockedSpinner(10), "unknown"},
				{getMockedSpinner(11), null},
		});
	}

	private static Spinner getMockedSpinner(int selectedItemPosition) {
		Spinner spinner = mock(Spinner.class);
		when(spinner.getSelectedItemPosition()).thenReturn(selectedItemPosition);

		return spinner;
	}
}
