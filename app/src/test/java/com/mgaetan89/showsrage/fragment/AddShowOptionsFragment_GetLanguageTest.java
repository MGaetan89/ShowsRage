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
public class AddShowOptionsFragment_GetLanguageTest {
	@Parameterized.Parameter(1)
	public String language;

	@Parameterized.Parameter(0)
	public Spinner spinner;

	private AddShowOptionsFragment fragment;

	@Before
	public void before() {
		Resources resources = mock(Resources.class);
		when(resources.getStringArray(R.array.languages_keys)).thenReturn(new String[]{
				"en",
				"fr",
				"cs",
				"da",
				"de",
				"el",
				"es",
				"fi",
				"he",
				"hr",
				"hu",
				"it",
				"ja",
				"ko",
				"nl",
				"no",
				"pl",
				"pt",
				"ru",
				"sl",
				"sv",
				"tr",
				"zh",
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
	public void getLanguage() {
		assertThat(this.fragment.getLanguage(this.spinner)).isEqualTo(this.language);
	}

	@After
	public void after() {
		this.fragment = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, "en"},
				{getMockedSpinner(-1), null},
				{getMockedSpinner(0), "en"},
				{getMockedSpinner(1), "fr"},
				{getMockedSpinner(2), "cs"},
				{getMockedSpinner(3), "da"},
				{getMockedSpinner(4), "de"},
				{getMockedSpinner(5), "el"},
				{getMockedSpinner(6), "es"},
				{getMockedSpinner(7), "fi"},
				{getMockedSpinner(8), "he"},
				{getMockedSpinner(9), "hr"},
				{getMockedSpinner(10), "hu"},
				{getMockedSpinner(11), "it"},
				{getMockedSpinner(12), "ja"},
				{getMockedSpinner(13), "ko"},
				{getMockedSpinner(14), "nl"},
				{getMockedSpinner(15), "no"},
				{getMockedSpinner(16), "pl"},
				{getMockedSpinner(17), "pt"},
				{getMockedSpinner(18), "ru"},
				{getMockedSpinner(19), "sl"},
				{getMockedSpinner(20), "sv"},
				{getMockedSpinner(21), "tr"},
				{getMockedSpinner(22), "zh"},
				{getMockedSpinner(23), null},
		});
	}

	private static Spinner getMockedSpinner(int selectedItemPosition) {
		Spinner spinner = mock(Spinner.class);
		when(spinner.getSelectedItemPosition()).thenReturn(selectedItemPosition);

		return spinner;
	}
}
