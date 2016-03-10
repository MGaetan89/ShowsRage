package com.mgaetan89.showsrage.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.mgaetan89.showsrage.EmptyFragmentHostCallback;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ShowOverviewFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShowPagerAdapter_EmptyTest {
	private ShowPagerAdapter adapter;

	@Before
	public void before() {
		FragmentActivity activity = mock(FragmentActivity.class);
		when(activity.getResources()).thenReturn(mock(Resources.class));

		Fragment fragment = mock(Fragment.class);

		try {
			Field fragmentHostField = Fragment.class.getDeclaredField("mHost");
			fragmentHostField.setAccessible(true);
			fragmentHostField.set(fragment, new EmptyFragmentHostCallback(activity));
		} catch (IllegalAccessException ignored) {
		} catch (NoSuchFieldException ignored) {
		}

		when(fragment.getString(R.string.show)).thenReturn("Show");

		this.adapter = new ShowPagerAdapter(null, fragment, Collections.<Integer>emptyList());
	}

	@Test
	public void getCount() {
		assertThat(this.adapter.getCount()).isEqualTo(1);
	}

	@Test
	public void getItem() {
		Fragment fragment = this.adapter.getItem(0);
		assertThat(fragment).isInstanceOf(ShowOverviewFragment.class);
		assertThat(fragment.getArguments()).isNull();
	}

	@Test
	public void getPageTitle() {
		assertThat(this.adapter.getPageTitle(0)).isEqualTo("Show");
	}

	@After
	public void after() {
		this.adapter = null;
	}
}
