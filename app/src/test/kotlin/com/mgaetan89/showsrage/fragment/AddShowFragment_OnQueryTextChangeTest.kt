package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AddShowFragment_OnQueryTextChangeTest(val query: String?, val valid: Boolean) {
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
