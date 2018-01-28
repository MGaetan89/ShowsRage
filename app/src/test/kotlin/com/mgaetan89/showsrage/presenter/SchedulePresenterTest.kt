package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.spy

@RunWith(Parameterized::class)
class SchedulePresenterTest(
		val schedule: Schedule?, val airDate: CharSequence?, val airDateTime: String?, val airTime: String?, val airTimeOnly: String?, val episode: Int,
		val network: String, val posterUrl: String, val quality: String, val season: Int, val showName: String
) {
	private lateinit var presenter: SchedulePresenter

	@Before
	fun before() {
		this.presenter = spy(SchedulePresenter(this.schedule, null))
		doReturn(this.schedule).`when`(this.presenter)._getSchedule()
	}

	@Test
	fun getAirDate() {
		assertThat(this.presenter.getAirDate()).isEqualTo(this.airDate)
	}

	@Test
	fun getAirTimeOnly() {
		assertThat(this.presenter.getAirTimeOnly()).isEqualTo(this.airTimeOnly)
	}

	@Test
	fun getEpisode() {
		assertThat(this.presenter.getEpisode()).isEqualTo(this.episode)
	}

	@Test
	fun getNetwork() {
		assertThat(this.presenter.getNetwork()).isEqualTo(this.network)
	}

	@Test
	fun getPosterUrl() {
		assertThat(this.presenter.getPosterUrl()).isEqualTo(this.posterUrl)
	}

	@Test
	fun getQuality() {
		assertThat(this.presenter.getQuality()).isEqualTo(this.quality)
	}

	@Test
	fun getSeason() {
		assertThat(this.presenter.getSeason()).isEqualTo(this.season)
	}

	@Test
	fun getShowName() {
		assertThat(this.presenter.getShowName()).isEqualTo(this.showName)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any?>> {
			val gson = SickRageApi.gson

			return listOf(
					arrayOf(null, null, null, null, null, 0, "", "", "", 0, ""),
					arrayOf(gson.fromJson("{airdate: \"\", airs: \"\", episode: 2, network: \"\", quality: \"\", season: 11, show_name: \"\", tvdbid: 456}", Schedule::class.java), null, null, null, null, 2, "", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=456", "", 11, ""),
					arrayOf(gson.fromJson("{airdate: \"2015-01-01\", airs: \"Monday 0:00 PM\", episode: 3, network: \"ABC\", quality: \"HD1080p\", season: 12, show_name: \"Show 1\", tvdbid: 789}", Schedule::class.java), null, null, null, "0:00 PM", 3, "ABC", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "HD1080p", 12, "Show 1"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-02\", airs: \"monday 1:00 PM\", episode: 4, network: \"CBS\", quality: \"HD\", season: 13, show_name: \"Show 2\", tvdbid: 789}", Schedule::class.java), null, null, null, "1:00 PM", 4, "CBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "HD", 13, "Show 2"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-03\", airs: \"Tuesday 2:00 PM\", episode: 5, network: \"TBS\", quality: \"Any\", season: 14, show_name: \"Show 3\", tvdbid: 789}", Schedule::class.java), null, null, null, "2:00 PM", 5, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 14, "Show 3"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-04\", airs: \"tuesday 3:00 PM\", episode: 6, network: \"TBS\", quality: \"Any\", season: 15, show_name: \"Show 4\", tvdbid: 789}", Schedule::class.java), null, null, null, "3:00 PM", 6, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 15, "Show 4"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-05\", airs: \"Wednesday 4:00 PM\", episode: 7, network: \"TBS\", quality: \"Any\", season: 16, show_name: \"Show 5\", tvdbid: 789}", Schedule::class.java), null, null, null, "4:00 PM", 7, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 16, "Show 5"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-06\", airs: \"wednesday 5:00 PM\", episode: 8, network: \"TBS\", quality: \"Any\", season: 17, show_name: \"Show 6\", tvdbid: 789}", Schedule::class.java), null, null, null, "5:00 PM", 8, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 17, "Show 6"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-07\", airs: \"Thursday 6:00 PM\", episode: 9, network: \"TBS\", quality: \"Any\", season: 18, show_name: \"Show 7\", tvdbid: 789}", Schedule::class.java), null, null, null, "6:00 PM", 9, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 18, "Show 7"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-08\", airs: \"thursday 7:00 PM\", episode: 10, network: \"TBS\", quality: \"Any\", season: 19, show_name: \"Show 8\", tvdbid: 789}", Schedule::class.java), null, null, null, "7:00 PM", 10, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 19, "Show 8"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-09\", airs: \"Friday 8:00 PM\", episode: 11, network: \"TBS\", quality: \"Any\", season: 20, show_name: \"Show 9\", tvdbid: 789}", Schedule::class.java), null, null, null, "8:00 PM", 11, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 20, "Show 9"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-10\", airs: \"friday 9:00 PM\", episode: 12, network: \"TBS\", quality: \"Any\", season: 21, show_name: \"Show 10\", tvdbid: 789}", Schedule::class.java), null, null, null, "9:00 PM", 12, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 21, "Show 10"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-11\", airs: \"Saturday 10:00 PM\", episode: 13, network: \"TBS\", quality: \"Any\", season: 22, show_name: \"Show 11\", tvdbid: 789}", Schedule::class.java), null, null, null, "10:00 PM", 13, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 22, "Show 11"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-12\", airs: \"saturday 11:00 PM\", episode: 14, network: \"TBS\", quality: \"Any\", season: 23, show_name: \"Show 12\", tvdbid: 789}", Schedule::class.java), null, null, null, "11:00 PM", 14, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 23, "Show 12"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-13\", airs: \"Sunday 0:00 PM\", episode: 15, network: \"TBS\", quality: \"Any\", season: 24, show_name: \"Show 13\", tvdbid: 789}", Schedule::class.java), null, null, null, "0:00 PM", 15, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 24, "Show 13"),
					arrayOf(gson.fromJson("{airdate: \"2015-01-14\", airs: \"sunday 1:00 PM\", episode: 16, network: \"TBS\", quality: \"Any\", season: 25, show_name: \"Show 14\", tvdbid: 789}", Schedule::class.java), null, null, null, "1:00 PM", 16, "TBS", "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=789", "Any", 25, "Show 14")
			)
		}
	}
}
