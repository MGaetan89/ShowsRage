package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class EpisodePresenterTest(val episode: Episode?, val airDate: String?, val quality: String, val statusColor: Int) {
    private lateinit var presenter: EpisodePresenter

    @Before
    fun before() {
        this.presenter = EpisodePresenter(this.episode)
    }

    @Test
    fun getAirDate() {
        assertThat(this.presenter.getAirDate()).isEqualTo(this.airDate)
    }

    @Test
    fun getQuality() {
        assertThat(this.presenter.getQuality()).isEqualTo(this.quality)
    }

    @Test
    fun getStatusColor() {
        assertThat(this.presenter.getStatusColor()).isEqualTo(this.statusColor)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(null, null, "", android.R.color.transparent),
                    arrayOf(gson.fromJson("{airdate: null, quality: \"N/A\", status: \"archived\"}", Episode::class.java), null, "", R.color.green),
                    arrayOf(gson.fromJson("{airdate: \"\", quality: \"HD1080p\", status: \"downloaded\"}", Episode::class.java), null, "HD1080p", R.color.green),
                    arrayOf(gson.fromJson("{airdate: \"2015-01-01\", quality: \"HD\", status: \"ignored\"}", Episode::class.java), null, "HD", R.color.blue),
                    arrayOf(gson.fromJson("{airdate: \"2015-01-01\", quality: \"Any\", status: \"skipped\"}", Episode::class.java), null, "Any", R.color.blue),
                    arrayOf(gson.fromJson("{airdate: \"2015-01-01\", quality: \"Custom\", status: \"snatched\"}", Episode::class.java), null, "Custom", R.color.purple),
                    arrayOf(gson.fromJson("{airdate: \"2015-01-01\", quality: \"Any\", status: \"snatched (proper)\"}", Episode::class.java), null, "Any", R.color.purple),
                    arrayOf(gson.fromJson("{airdate: \"2015-01-01\", quality: \"SD\", status: \"unaired\"}", Episode::class.java), null, "SD", R.color.yellow),
                    arrayOf(gson.fromJson("{airdate: \"2015-01-01\", quality: \"SD\", status: \"wanted\"}", Episode::class.java), null, "SD", R.color.red),
                    arrayOf(gson.fromJson("{airdate: \"2015-01-01\", quality: \"SD\", status: \"status\"}", Episode::class.java), null, "SD", android.R.color.transparent)
            )
        }
    }
}
