package com.mgaetan89.showsrage.model;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class Episode_GetStatusForMenuIdTest {
	@Parameterized.Parameter(1)
	public String episodeStatus;

	@Parameterized.Parameter(0)
	public int menuId;

	@Test
	public void getStatusForMenuId() {
		assertThat(Episode.getStatusForMenuId(this.menuId)).isEqualTo(this.episodeStatus);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{R.id.menu_episode_set_status_failed, "failed"},
				{R.id.menu_episode_set_status_ignored, "ignored"},
				{R.id.menu_episode_set_status_skipped, "skipped"},
				{R.id.menu_episode_set_status_wanted, "wanted"},
				{R.id.menu_change_quality, null},
		});
	}
}
