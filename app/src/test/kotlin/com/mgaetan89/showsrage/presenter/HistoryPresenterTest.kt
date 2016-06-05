package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class HistoryPresenterTest {
    @Parameterized.Parameter(1)
    var episode: Int = 0

    @Parameterized.Parameter(0)
    var history: History? = null

    @Parameterized.Parameter(2)
    var posterUrl: String? = null

    @Parameterized.Parameter(3)
    var provider: String? = null

    @Parameterized.Parameter(4)
    var providerQuality: String? = null

    @Parameterized.Parameter(5)
    var quality: String? = null

    @Parameterized.Parameter(6)
    var season: Int = 0

    @Parameterized.Parameter(7)
    var showName: String? = null

    private lateinit var presenter: HistoryPresenter

    @Before
    fun before() {
        this.presenter = HistoryPresenter(this.history)
    }

    @Test
    fun getEpisode() {
        assertThat(this.presenter.getEpisode()).isEqualTo(this.episode)
    }

    @Test
    fun getPosterUrl() {
        assertThat(this.presenter.getPosterUrl()).isEqualTo(this.posterUrl)
    }

    @Test
    fun getProvider() {
        assertThat(this.presenter.getProvider()).isEqualTo(this.provider)
    }

    @Test
    fun getProviderQuality() {
        assertThat(this.presenter.getProviderQuality()).isEqualTo(this.providerQuality)
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
                    arrayOf(null, 0, "", "", null, "", 0, ""),
                    arrayOf(gson.fromJson("{episode: 1, provider: \"CtrlHD\", quality: \"HD1080p\", season: 2, show_name: \"Show 1\", tvdbid: 123}", History::class.java), 1, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=123", "CtrlHD", null, "HD1080p", 2, "Show 1"),
                    arrayOf(gson.fromJson("{episode: 2, provider: \"-1\", quality: \"HD\", season: 3, show_name: \"Show 2\", tvdbid: 456}", History::class.java), 2, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=456", "-1", "HD", "HD", 3, "Show 2")
            )
        }
    }
}
