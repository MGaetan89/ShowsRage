package com.mgaetan89.showsrage.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.EmptyFragmentHostCallback;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ShowsSectionFragment;
import com.mgaetan89.showsrage.model.Show;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShowsPagerAdapterTest {
	private ShowsPagerAdapter adapter;

	@NonNull
	private final Map<Integer, List<Show>> shows = new HashMap<>();

	{
		{
			this.shows.put(0, Collections.<Show>emptyList());
			this.shows.put(1, Collections.<Show>emptyList());
		}
	}

	@Before
	public void before() {
		Intent intent = mock(Intent.class);
		when(intent.getExtras()).thenReturn(mock(Bundle.class));

		FragmentActivity activity = mock(FragmentActivity.class);
		when(activity.getIntent()).thenReturn(intent);
		when(activity.getResources()).thenReturn(mock(Resources.class));

		Fragment fragment = mock(Fragment.class);

		try {
			Field fragmentHostField = Fragment.class.getDeclaredField("mHost");
			fragmentHostField.setAccessible(true);
			fragmentHostField.set(fragment, new EmptyFragmentHostCallback(activity));
		} catch (IllegalAccessException ignored) {
		} catch (NoSuchFieldException ignored) {
		}

		when(fragment.getString(R.string.animes)).thenReturn("Animes");
		when(fragment.getString(R.string.shows)).thenReturn("Shows");

		this.adapter = new ShowsPagerAdapter(mock(FragmentManager.class), fragment, this.shows);
	}

	@Test
	public void getCount() {
		assertThat(this.adapter.getCount()).isEqualTo(this.shows.size());
	}

	@Test
	public void getItem() {
		for (int i = 0; i < this.shows.size(); i++) {
			Fragment fragment = this.adapter.getItem(i);
			assertThat(fragment).isInstanceOf(ShowsSectionFragment.class);
			assertThat(fragment.getArguments()).isNotNull();
			assertThat(fragment.getArguments().containsKey(Constants.Bundle.INSTANCE.getANIME()));
		}
	}

	@Test
	public void getPageTitle() {
		assertThat(this.adapter.getPageTitle(0)).isEqualTo("Shows");
		assertThat(this.adapter.getPageTitle(1)).isEqualTo("Animes");
	}

	@After
	public void after() {
		this.adapter = null;
	}
}
