package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowPresenterTest(
        val show: Show?, val bannerUrl: String, val downloaded: Int, val episodesCount: Int, val network: String,
        val paused: Boolean, val posterUrl: String, val quality: String, val showName: String, val snatched: Int
) {
    private lateinit var presenter: ShowPresenter

    @Before
    fun before() {
        val realmShow = if (this.show != null) {
            RealmManager.getRealm()?.copyToRealm(this.show)
        } else {
            this.show
        }

        this.presenter = ShowPresenter(realmShow)
    }

    @Test
    fun getBannerUrl() {
        assertThat(this.presenter.getBannerUrl()).isEqualTo(this.bannerUrl)
    }

    @Test
    fun getDownloaded() {
        assertThat(this.presenter.getDownloaded()).isEqualTo(this.downloaded)
    }

    @Test
    fun getEpisodesCount() {
        assertThat(this.presenter.getEpisodesCount()).isEqualTo(this.episodesCount)
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

    @Ignore
    @Test
    fun getSnatched() {
        assertThat(this.presenter.getSnatched()).isEqualTo(this.snatched)
    }

    @Test
    fun isPaused() {
        assertThat(this.presenter.isPaused()).isEqualTo(this.paused)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(null, "", 0, 0, "", false, "", "", "", 0),
                    arrayOf(gson.fromJson("{downloaded: 10, episodesCount: 20, network: \"ABC\", paused: 0, quality: \"HD\", show_name: \"Show 1\", snatched: 5, tvdbid: 123}", Show::class.java), "https://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvdbid=123", 10, 20, "ABC", false, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=123", "HD", "Show 1", 5),
                    arrayOf(gson.fromJson("{downloaded: 20, episodesCount: 30, network: \"CBS\", paused: 1, quality: \"HD1080p\", show_name: \"Show 2\", snatched: 10, tvdbid: 456}", Show::class.java), "https://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvdbid=456", 20, 30, "CBS", true, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=456", "HD1080p", "Show 2", 10)
            )
        }
    }
}
