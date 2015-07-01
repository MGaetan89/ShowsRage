package com.mgaetan89.showsrage.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.mgaetan89.showsrage.R;

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

public class EpisodePagerAdapterTest {
	private EpisodePagerAdapter adapter;

	@NonNull
	private final List<Integer> episodes = Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1);

	@Before
	public void before() {
		Fragment fragment = new Fragment();

		try {
			FragmentActivity activity = mock(FragmentActivity.class);
			when(activity.getResources()).thenReturn(mock(Resources.class));

			Class<?> fragmentClass = fragment.getClass();
			Field fragmentActivityField = fragmentClass.getDeclaredField("mActivity");
			fragmentActivityField.setAccessible(true);
			fragmentActivityField.set(fragment, activity);
		} catch (IllegalAccessException ignored) {
		} catch (NoSuchFieldException ignored) {
		}

		fragment = spy(fragment);

		when(fragment.getString(R.string.episode_name_short, 1)).thenReturn("E1");
		when(fragment.getString(R.string.episode_name_short, 2)).thenReturn("E2");
		when(fragment.getString(R.string.episode_name_short, 3)).thenReturn("E3");
		when(fragment.getString(R.string.episode_name_short, 4)).thenReturn("E4");
		when(fragment.getString(R.string.episode_name_short, 5)).thenReturn("E5");
		when(fragment.getString(R.string.episode_name_short, 6)).thenReturn("E6");
		when(fragment.getString(R.string.episode_name_short, 7)).thenReturn("E7");
		when(fragment.getString(R.string.episode_name_short, 8)).thenReturn("E8");
		when(fragment.getString(R.string.episode_name_short, 9)).thenReturn("E9");
		when(fragment.getString(R.string.episode_name_short, 10)).thenReturn("E10");

		fragment.onAttach(mock(Activity.class));

		this.adapter = new EpisodePagerAdapter(null, fragment, this.episodes);
	}

	@Test
	public void getCount() {
		assertThat(this.adapter.getCount()).isEqualTo(this.episodes.size());
	}

	@Test
	public void getPageTitle() {
		assertThat(this.adapter.getPageTitle(0)).isEqualTo("E10");
		assertThat(this.adapter.getPageTitle(1)).isEqualTo("E9");
		assertThat(this.adapter.getPageTitle(2)).isEqualTo("E8");
		assertThat(this.adapter.getPageTitle(3)).isEqualTo("E7");
		assertThat(this.adapter.getPageTitle(4)).isEqualTo("E6");
		assertThat(this.adapter.getPageTitle(5)).isEqualTo("E5");
		assertThat(this.adapter.getPageTitle(6)).isEqualTo("E4");
		assertThat(this.adapter.getPageTitle(7)).isEqualTo("E3");
		assertThat(this.adapter.getPageTitle(8)).isEqualTo("E2");
		assertThat(this.adapter.getPageTitle(9)).isEqualTo("E1");
	}

	@After
	public void after() {
		this.adapter = null;
	}
}
