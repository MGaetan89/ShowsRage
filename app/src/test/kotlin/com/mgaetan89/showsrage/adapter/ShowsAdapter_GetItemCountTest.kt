package com.mgaetan89.showsrage.adapter

import com.mgaetan89.showsrage.model.Show
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsAdapter_GetItemCountTest {
    @Parameterized.Parameter(1)
    var itemCount: Int = 0

    @Parameterized.Parameter(0)
    var shows: List<Show> = emptyList()

    private lateinit var adapter: ShowsAdapter

    @Before
    fun before() {
        this.adapter = ShowsAdapter(this.shows, 0)
    }

    @Test
    fun getItemCount() {
        assertThat(this.adapter.itemCount).isEqualTo(this.itemCount)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(emptyList<Any>(), 0),
                    arrayOf(listOf(Show()), 1),
                    arrayOf(listOf(Show(), Show(), Show()), 3)
            )
        }
    }
}
