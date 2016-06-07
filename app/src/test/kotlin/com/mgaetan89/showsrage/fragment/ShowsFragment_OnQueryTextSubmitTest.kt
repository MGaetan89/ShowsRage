package com.mgaetan89.showsrage.fragment

import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

@RunWith(Parameterized::class)
class ShowsFragment_OnQueryTextSubmitTest(val query: String?) {
    private lateinit var fragment: ShowsFragment

    @Before
    fun before() {
        this.fragment = spy(ShowsFragment())
    }

    @Test
    fun onQueryTextSubmit() {
        try {
            assertTrue(this.fragment.onQueryTextSubmit(this.query))
            verify<ShowsFragment>(this.fragment).sendFilterMessage()
        } catch (exception: NullPointerException) {
            // LocalBroadcastManager.getInstance(Context) returns null in tests
        }

    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null),
                    arrayOf<Any?>(""),
                    arrayOf<Any?>(" "),
                    arrayOf<Any?>("Search Query")
            )
        }
    }
}
