package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AddShowFragment_OnQueryTextChangeTest {
    @Parameterized.Parameter(0)
    var query: String? = null

    @Parameterized.Parameter(1)
    var valid: Boolean = false

    private lateinit var fragment: AddShowFragment

    @Before
    fun before() {
        this.fragment = AddShowFragment()
    }

    @Test
    fun onQueryTextChange() {
        assertThat(this.fragment.onQueryTextChange(this.query)).isFalse()
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = AddShowFragment_IsQueryValidTest.data()
    }
}
