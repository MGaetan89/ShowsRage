package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.spy

@RunWith(Parameterized::class)
class HistoryPresenterTest(
        val history: History?, val episode: Int, val posterUrl: String, val provider: String,
        val providerQuality: String?, val quality: String, val season: Int, val showName: String
) {
    private lateinit var presenter: HistoryPresenter

    @Before
    fun before() {
        this.presenter = spy(HistoryPresenter(this.history))
        doReturn(this.history != null).`when`(this.presenter).isHistoryValid()
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
