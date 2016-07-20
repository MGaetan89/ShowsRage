package com.mgaetan89.showsrage.adapter

import com.mgaetan89.showsrage.model.Show
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsAdapter_GetSectionTitleTest(val show: Show, val ignoreArticles: Boolean, val sectionTitle: String) {
    private lateinit var adapter: ShowsAdapter

    @Before
    fun before() {
        this.adapter = ShowsAdapter(listOf(this.show), 0, this.ignoreArticles)
    }

    @Test
    fun getSectionTitle() {
        assertThat(this.adapter.getSectionTitle(0)).isEqualTo(this.sectionTitle)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(getShow(null), false, ""),
                    arrayOf(getShow(""), false, ""),
                    arrayOf(getShow("arrow"), false, "A"),
                    arrayOf(getShow("Arrow"), false, "A"),
                    arrayOf(getShow("the walking dead"), false, "T"),
                    arrayOf(getShow("The Walking Dead"), false, "T"),
                    arrayOf(getShow("arrow"), true, "A"),
                    arrayOf(getShow("Arrow"), true, "A"),
                    arrayOf(getShow("the walking dead"), true, "W"),
                    arrayOf(getShow("The Walking Dead"), true, "W")
            )
        }

        private fun getShow(name: String?): Show {
            return Show().apply {
                this.showName = name
            }
        }
    }
}
