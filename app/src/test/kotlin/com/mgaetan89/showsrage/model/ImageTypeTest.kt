package com.mgaetan89.showsrage.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ImageTypeTest {
    @Test
    fun command() {
        assertThat(ImageType.BANNER.command).isEqualTo("show.getbanner")
        assertThat(ImageType.FAN_ART.command).isEqualTo("show.getfanart")
        assertThat(ImageType.POSTER.command).isEqualTo("show.getposter")
    }
}
