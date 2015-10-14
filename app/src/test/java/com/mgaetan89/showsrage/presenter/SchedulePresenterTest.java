package com.mgaetan89.showsrage.presenter;

import com.mgaetan89.showsrage.model.Schedule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SchedulePresenterTest {
	@Parameterized.Parameter(1)
	public CharSequence airDate;

	@Parameterized.Parameter(2)
	public int episode;

	@Parameterized.Parameter(3)
	public String network;

	@Parameterized.Parameter(4)
	public String posterUrl;

	@Parameterized.Parameter(5)
	public String quality;

	@Parameterized.Parameter(0)
	public Schedule schedule;

	@Parameterized.Parameter(6)
	public int season;

	@Parameterized.Parameter(7)
	public String showName;

	private SchedulePresenter presenter;

	@Before
	public void before() {
		this.presenter = new SchedulePresenter(this.schedule);
	}

	@Test
	public void getAirDate() {
		assertThat(this.presenter.getAirDate()).isEqualTo(this.airDate);
	}

	@Test
	public void getEpisode() {
		assertThat(this.presenter.getEpisode()).isEqualTo(this.episode);
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
	public void getSeason() {
		assertThat(this.presenter.getSeason()).isEqualTo(this.season);
	}

	@Test
	public void getShowName() {
		assertThat(this.presenter.getShowName()).isEqualTo(this.showName);
	}

	// TODO Add more test cases
	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, null, 0, "", "", "", 0, ""},
		});
	}
}
