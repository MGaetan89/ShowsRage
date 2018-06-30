package com.mgaetan89.showsrage

import com.mgaetan89.showsrage.model.PlayingVideoData
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ShowsRageApplicationTest {
    @Test
    fun hasPlayingVideo() {
        val application = ShowsRageApplication()
        assertThat(application.hasPlayingVideo()).isFalse()

        application.playingVideo = PlayingVideoData()
        assertThat(application.hasPlayingVideo()).isTrue()

        application.playingVideo = null
        assertThat(application.hasPlayingVideo()).isFalse()
    }
}
