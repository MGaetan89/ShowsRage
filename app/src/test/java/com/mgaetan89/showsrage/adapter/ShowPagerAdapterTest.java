package com.mgaetan89.showsrage.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.SeasonFragment;
import com.mgaetan89.showsrage.fragment.ShowOverviewFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ShowPagerAdapterTest {
	private ShowPagerAdapter adapter;

	@NonNull
	private final List<Integer> seasons = Arrays.asList(3, 2, 1, 0);

	@Before
	public void before() {
		FragmentActivity activity = mock(FragmentActivity.class);
		when(activity.getResources()).thenReturn(mock(Resources.class));

		Fragment fragment = spy(new Fragment());

		try {
			Field fragmentActivityField = Fragment.class.getDeclaredField("mActivity");
			fragmentActivityField.setAccessible(true);
			fragmentActivityField.set(fragment, activity);
		} catch (IllegalAccessException ignored) {
		} catch (NoSuchFieldException ignored) {
		}

		when(fragment.getString(R.string.season_number, 1)).thenReturn("Season 1");
		when(fragment.getString(R.string.season_number, 2)).thenReturn("Season 2");
		when(fragment.getString(R.string.season_number, 3)).thenReturn("Season 3");
		when(fragment.getString(R.string.show)).thenReturn("Show");
		when(fragment.getString(R.string.specials)).thenReturn("Specials");

		fragment.onAttach(activity);

		this.adapter = new ShowPagerAdapter(null, fragment, this.seasons);
	}

	@Test
	public void getCount() {
		assertThat(this.adapter.getCount()).isEqualTo(this.seasons.size() + 1);
	}

	@Test
	public void getItem_Season() {
		for (int i = 1; i < this.seasons.size(); i++) {
			Fragment fragment = this.adapter.getItem(i);
			assertThat(fragment).isInstanceOf(SeasonFragment.class);
			assertThat(fragment.getArguments()).isNotNull();
			assertThat(fragment.getArguments().containsKey(Constants.Bundle.SEASON_NUMBER));
		}
	}

	@Test
	public void getItem_ShowOverview() {
		Fragment fragment = this.adapter.getItem(0);
		assertThat(fragment).isInstanceOf(ShowOverviewFragment.class);
		assertThat(fragment.getArguments()).isNull();
	}

	@Test
	public void getPageTitle_Season() {
		assertThat(this.adapter.getPageTitle(1)).isEqualTo("Season 3");
		assertThat(this.adapter.getPageTitle(2)).isEqualTo("Season 2");
		assertThat(this.adapter.getPageTitle(3)).isEqualTo("Season 1");
	}

	@Test
	public void getPageTitle_ShowOverview() {
		assertThat(this.adapter.getPageTitle(0)).isEqualTo("Show");
	}

	@Test
	public void getPageTitle_Specials() {
		assertThat(this.adapter.getPageTitle(4)).isEqualTo("Specials");
	}

	@After
	public void after() {
		this.adapter = null;
	}
}
