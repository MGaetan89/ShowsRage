package com.mgaetan89.showsrage.presenter;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.network.SickRageApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowPresenterTest {
	@Parameterized.Parameter(1)
	public String bannerUrl;

	@Parameterized.Parameter(2)
	public int downloaded;

	@Parameterized.Parameter(3)
	public int episodesCount;

	@Parameterized.Parameter(4)
	public String network;

	@Parameterized.Parameter(5)
	public boolean paused;

	@Parameterized.Parameter(6)
	public String posterUrl;

	@Parameterized.Parameter(7)
	public String quality;

	@Parameterized.Parameter(0)
	public Show show;

	@Parameterized.Parameter(8)
	public String showName;

	@Parameterized.Parameter(9)
	public int snatched;

	private ShowPresenter presenter;

	@Before
	public void before() {
		this.presenter = new ShowPresenter(this.show);
	}

	@Test
	public void getBannerUrl() {
		assertThat(this.presenter.getBannerUrl()).isEqualTo(this.bannerUrl);
	}

	@Test
	public void getDownloaded() {
		assertThat(this.presenter.getDownloaded()).isEqualTo(this.downloaded);
	}

	@Test
	public void getEpisodesCount() {
		assertThat(this.presenter.getEpisodesCount()).isEqualTo(this.episodesCount);
	}

	@Test
	public void getNetwork() {
		assertThat(this.presenter.getNetwork()).isEqualTo(this.network);
	}

	@Test
	public void getPosterUrl() {
		assertThat(this.presenter.getPosterUrl()).isEqualTo(this.posterUrl);
	}

	@Test
	public void getQuality() {
		assertThat(this.presenter.getQuality()).isEqualTo(this.quality);
	}

	@Test
	public void getShowName() {
		assertThat(this.presenter.getShowName()).isEqualTo(this.showName);
	}

	@Test
	public void getSnatched() {
		assertThat(this.presenter.getSnatched()).isEqualTo(this.snatched);
	}

	@Test
	public void isPaused() {
		assertThat(this.presenter.isPaused()).isEqualTo(this.paused);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = SickRageApi.Companion.getGson();

		return Arrays.asList(new Object[][]{
				{null, "", 0, 0, "", false, "", "", "", 0},
				{gson.fromJson("{downloaded: 10, episodesCount: 20, network: \"ABC\", paused: 0, quality: \"HD\", show_name: \"Show 1\", snatched: 5, tvdbid: 123}", Show.class), "https://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvdbid=123", 10, 20, "ABC", false, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=123", "HD", "Show 1", 5},
				{gson.fromJson("{downloaded: 20, episodesCount: 30, network: \"CBS\", paused: 1, quality: \"HD1080p\", show_name: \"Show 2\", snatched: 10, tvdbid: 456}", Show.class), "https://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvdbid=456", 20, 30, "CBS", true, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=456", "HD1080p", "Show 2", 10},
		});
	}
}
