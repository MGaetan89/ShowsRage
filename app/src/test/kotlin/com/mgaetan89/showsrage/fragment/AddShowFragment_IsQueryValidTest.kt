package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AddShowFragment_IsQueryValidTest(val query: String?, val valid: Boolean) {
	@Test
	fun isQueryValid() {
		assertThat(AddShowFragment.isQueryValid(this.query)).isEqualTo(this.valid)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any?>> {
			return listOf(
					arrayOf<Any?>(null, false),
					arrayOf<Any?>("", false),
					arrayOf<Any?>(" ", false),
					arrayOf<Any?>("  ", false),
					arrayOf<Any?>(" some query ", true)
			)
		}
	}
}
