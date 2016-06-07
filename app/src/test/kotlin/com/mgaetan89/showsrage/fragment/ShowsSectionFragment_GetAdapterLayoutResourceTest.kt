package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsSectionFragment_GetAdapterLayoutResourceTest(val preferredShowLayout: String?, val layoutId: Int) {
    @Test
    fun getAdapterLayoutResource() {
        assertThat(ShowsSectionFragment.getAdapterLayoutResource(this.preferredShowLayout)).isEqualTo(this.layoutId)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, R.layout.adapter_shows_list_content_poster),
                    arrayOf<Any?>("", R.layout.adapter_shows_list_content_poster),
                    arrayOf<Any?>("banner", R.layout.adapter_shows_list_content_banner),
                    arrayOf<Any?>("fan_art", R.layout.adapter_shows_list_content_poster),
                    arrayOf<Any?>("poster", R.layout.adapter_shows_list_content_poster)
            )
        }
    }
}
