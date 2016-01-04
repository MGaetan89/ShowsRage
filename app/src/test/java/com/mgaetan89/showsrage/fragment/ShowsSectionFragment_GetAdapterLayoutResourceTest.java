package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsSectionFragment_GetAdapterLayoutResourceTest {
	@Parameterized.Parameter(1)
	public int layoutId;

	@Parameterized.Parameter(0)
	public String preferredShowLayout;

	@Test
	public void getAdapterLayoutResource() {
		assertThat(ShowsSectionFragment.getAdapterLayoutResource(this.preferredShowLayout)).isEqualTo(this.layoutId);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, R.layout.adapter_shows_list_content_poster},
				{"", R.layout.adapter_shows_list_content_poster},
				{"banner", R.layout.adapter_shows_list_content_banner},
				{"fan_art", R.layout.adapter_shows_list_content_poster},
				{"poster", R.layout.adapter_shows_list_content_poster},
		});
	}
}
