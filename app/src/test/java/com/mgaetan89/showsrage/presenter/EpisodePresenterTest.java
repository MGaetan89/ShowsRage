package com.mgaetan89.showsrage.presenter;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.network.SickRageApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class EpisodePresenterTest {
	@Parameterized.Parameter(1)
	public String airDate;

	@Parameterized.Parameter(0)
	public Episode episode;

	@Parameterized.Parameter(2)
	public String quality;

	@Parameterized.Parameter(3)
	public int statusColor;

	private EpisodePresenter presenter;

	@Before
	public void before() {
		this.presenter = new EpisodePresenter(this.episode);
	}

	@Test
	public void getAirDate() {
		assertThat(this.presenter.getAirDate()).isEqualTo(this.airDate);
	}

	@Test
	public void getQuality() {
		assertThat(this.presenter.getQuality()).isEqualTo(this.quality);
	}

	@Test
	public void getStatusColor() {
		assertThat(this.presenter.getStatusColor()).isEqualTo(this.statusColor);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = SickRageApi.Companion.getGson();

		return Arrays.asList(new Object[][]{
				{null, null, "", android.R.color.transparent},
				{gson.fromJson("{airdate: null, quality: \"N/A\", status: \"archived\"}", Episode.class), null, "", R.color.green},
				{gson.fromJson("{airdate: \"\", quality: \"HD1080p\", status: \"downloaded\"}", Episode.class), null, "HD1080p", R.color.green},
				{gson.fromJson("{airdate: \"2015-01-01\", quality: \"HD\", status: \"ignored\"}", Episode.class), null, "HD", R.color.blue},
				{gson.fromJson("{airdate: \"2015-01-01\", quality: \"Any\", status: \"skipped\"}", Episode.class), null, "Any", R.color.blue},
				{gson.fromJson("{airdate: \"2015-01-01\", quality: \"Custom\", status: \"snatched\"}", Episode.class), null, "Custom", R.color.purple},
				{gson.fromJson("{airdate: \"2015-01-01\", quality: \"Any\", status: \"snatched (proper)\"}", Episode.class), null, "Any", R.color.purple},
				{gson.fromJson("{airdate: \"2015-01-01\", quality: \"SD\", status: \"unaired\"}", Episode.class), null, "SD", R.color.yellow},
				{gson.fromJson("{airdate: \"2015-01-01\", quality: \"SD\", status: \"wanted\"}", Episode.class), null, "SD", R.color.red},
				{gson.fromJson("{airdate: \"2015-01-01\", quality: \"SD\", status: \"status\"}", Episode.class), null, "SD", android.R.color.transparent},
		});
	}
}
