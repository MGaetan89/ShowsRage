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
public class PostProcessingFragment_GetProcessingMethodsTest {
	@Parameterized.Parameter(1)
	public String processingMethod;

	@Parameterized.Parameter(0)
	public Spinner spinner;

	private PostProcessingFragment fragment;

	@Before
	public void before() {
		Resources resources = mock(Resources.class);
		when(resources.getStringArray(R.array.processing_methods_values)).thenReturn(new String[]{
				"",
				"copy",
				"move",
				"hardlink",
				"symlink",
		});

		FragmentActivity activity = mock(FragmentActivity.class);
		when(activity.getResources()).thenReturn(resources);

		this.fragment = spy(new PostProcessingFragment());

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
	public void getProcessingMethod() {
		assertThat(this.fragment.getProcessingMethod(this.spinner)).isEqualTo(this.processingMethod);
	}

	@After
	public void after() {
		this.fragment = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, null},
				{getMockedSpinner(-1), null},
				{getMockedSpinner(0), null},
				{getMockedSpinner(1), "copy"},
				{getMockedSpinner(2), "move"},
				{getMockedSpinner(3), "hardlink"},
				{getMockedSpinner(4), "symlink"},
				{getMockedSpinner(5), null},
		});
	}

	private static Spinner getMockedSpinner(int selectedItemPosition) {
		Spinner spinner = mock(Spinner.class);
		when(spinner.getSelectedItemPosition()).thenReturn(selectedItemPosition);

		return spinner;
	}
}
