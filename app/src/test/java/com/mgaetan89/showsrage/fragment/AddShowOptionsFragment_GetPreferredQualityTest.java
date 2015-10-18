package com.mgaetan89.showsrage.fragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Spinner;

import com.mgaetan89.showsrage.EmptyFragmentHostCallback;
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
public class AddShowOptionsFragment_GetPreferredQualityTest {
	@Parameterized.Parameter(1)
	public String preferredQuality;

	@Parameterized.Parameter(0)
	public Spinner spinner;

	private AddShowOptionsFragment fragment;

	@Before
	public void before() {
		Resources resources = mock(Resources.class);
		when(resources.getStringArray(R.array.preferred_qualities_keys)).thenReturn(new String[]{
				"sddvd",
				"hdtv",
				"rawhdtv",
				"fullhdtv",
				"hdwebdl",
				"fullhdwebdl",
				"hdbluray",
				"fullhdbluray",
		});

		FragmentActivity activity = mock(FragmentActivity.class);
		when(activity.getResources()).thenReturn(resources);

		this.fragment = spy(new AddShowOptionsFragment());

		try {
			Field fragmentHostField = Fragment.class.getDeclaredField("mHost");
			fragmentHostField.setAccessible(true);
			fragmentHostField.set(this.fragment, new EmptyFragmentHostCallback(activity));
		} catch (IllegalAccessException ignored) {
		} catch (NoSuchFieldException ignored) {
		}

		when(this.fragment.getResources()).thenReturn(resources);
	}

	@Test
	public void getPreferredQuality() {
		assertThat(this.fragment.getPreferredQuality(this.spinner)).isEqualTo(this.preferredQuality);
	}

	@After
	public void after() {
		this.fragment = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, "sddvd"},
				{getMockedSpinner(-1), null},
				{getMockedSpinner(0), null},
				{getMockedSpinner(1), "sddvd"},
				{getMockedSpinner(2), "hdtv"},
				{getMockedSpinner(3), "rawhdtv"},
				{getMockedSpinner(4), "fullhdtv"},
				{getMockedSpinner(5), "hdwebdl"},
				{getMockedSpinner(6), "fullhdwebdl"},
				{getMockedSpinner(7), "hdbluray"},
				{getMockedSpinner(8), "fullhdbluray"},
				{getMockedSpinner(9), null},
		});
	}

	private static Spinner getMockedSpinner(int selectedItemPosition) {
		Spinner spinner = mock(Spinner.class);
		when(spinner.getSelectedItemPosition()).thenReturn(selectedItemPosition);

		return spinner;
	}
}
