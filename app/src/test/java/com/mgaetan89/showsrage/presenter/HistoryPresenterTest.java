package com.mgaetan89.showsrage.presenter;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.History;
import com.mgaetan89.showsrage.network.SickRageApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HistoryPresenterTest {
	@Parameterized.Parameter(1)
	public int episode;

	@Parameterized.Parameter(0)
	public History history;

	@Parameterized.Parameter(2)
	public String posterUrl;

	@Parameterized.Parameter(3)
	public String provider;

	@Parameterized.Parameter(4)
	public String providerQuality;

	@Parameterized.Parameter(5)
	public String quality;

	@Parameterized.Parameter(6)
	public int season;

	@Parameterized.Parameter(7)
	public String showName;

	private HistoryPresenter presenter;

	@Before
	public void before() {
		this.presenter = new HistoryPresenter(this.history);
	}

	@Test
	public void getEpisode() {
		assertThat(this.presenter.getEpisode()).isEqualTo(this.episode);
	}

	@Test
	public void getPosterUrl() {
		assertThat(this.presenter.getPosterUrl()).isEqualTo(this.posterUrl);
	}

	@Test
	public void getProvider() {
		assertThat(this.presenter.getProvider()).isEqualTo(this.provider);
	}

	@Test
	public void getProviderQuality() {
		assertThat(this.presenter.getProviderQuality()).isEqualTo(this.providerQuality);
	}

	@Test
	public void getQuality() {
		assertThat(this.presenter.getQuality()).isEqualTo(this.quality);
	}

	@Test
	public void getSeason() {
		assertThat(this.presenter.getSeason()).isEqualTo(this.season);
	}

	@Test
	public void getShowName() {
		assertThat(this.presenter.getShowName()).isEqualTo(this.showName);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = SickRageApi.Companion.getGson();

		return Arrays.asList(new Object[][]{
				{null, 0, "", "", null, "", 0, ""},
				{gson.fromJson("{episode: 1, provider: \"CtrlHD\", quality: \"HD1080p\", season: 2, show_name: \"Show 1\", tvdbid: 123}", History.class), 1, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=123", "CtrlHD", null, "HD1080p", 2, "Show 1"},
				{gson.fromJson("{episode: 2, provider: \"-1\", quality: \"HD\", season: 3, show_name: \"Show 2\", tvdbid: 456}", History.class), 2, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=456", "-1", "HD", "HD", 3, "Show 2"},
		});
	}
}
