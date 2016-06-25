package com.mgaetan89.showsrage.presenter

import com.google.gson.Gson
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.spy

@RunWith(Parameterized::class)
class ShowPresenterTest(
        val show: Show?, val showStat: RealmShowStat?, val bannerUrl: String, val downloaded: Int,
        val episodesCount: Int, val network: String, val paused: Boolean, val posterUrl: String, val quality: String,
        val showName: String, val snatched: Int
) {
    private lateinit var presenter: ShowPresenter

    @Before
    fun before() {
        this.presenter = spy(ShowPresenter(this.show))
        doReturn(this.show != null).`when`(this.presenter).isShowValid()
        doReturn(this.showStat).`when`(this.presenter).getShowStat()
    }

    @Test
    fun getBannerUrl() {
        assertThat(this.presenter.getBannerUrl()).isEqualTo(this.bannerUrl)
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
    fun getShowName() {
        assertThat(this.presenter.getShowName()).isEqualTo(this.showName)
    }

    @Test
    fun isPaused() {
        assertThat(this.presenter.isPaused()).isEqualTo(this.paused)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<out Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(null, null, "", 0, 0, "", false, "", "", "", 0),
                    arrayOf(getShow(gson, "ABC", 0, "HD", "Show 1", 123), getShowStat(gson, 10, 20, 5), "?cmd=show.getbanner&tvdbid=123", 10, 20, "ABC", false, "?cmd=show.getposter&tvdbid=123", "HD", "Show 1", 5),
                    arrayOf(getShow(gson, "CBS", 1, "HD1080p", "Show 2", 456), getShowStat(gson, 20, 30, 10), "?cmd=show.getbanner&tvdbid=456", 20, 30, "CBS", true, "?cmd=show.getposter&tvdbid=456", "HD1080p", "Show 2", 10)
            )
        }

        private fun getShow(gson: Gson, network: String, paused: Int, quality: String, showName: String, tvDbId: Int): Show {
            return gson.fromJson("{network: \"$network\", paused: $paused, quality: \"$quality\", show_name: \"$showName\", tvdbid: $tvDbId}", Show::class.java)
        }

        private fun getShowStat(gson: Gson, downloaded: Int, episodesCount: Int, snatched: Int): RealmShowStat {
            return gson.fromJson("{downloaded: $downloaded, episodesCount: $episodesCount, snatched: $snatched}", RealmShowStat::class.java)
        }
    }
}
