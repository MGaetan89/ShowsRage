package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ScheduleFragment_GetSectionNameTest(val sectionId: String?, val sectionName: Int) {
	@Test
	fun getSectionName() {
		assertThat(ScheduleFragment.getSectionName(this.sectionId)).isEqualTo(this.sectionName)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any?>> {
			return listOf(
					arrayOf<Any?>(null, 0),
					arrayOf<Any?>("", 0),
					arrayOf<Any?>("later", R.string.later),
					arrayOf<Any?>("Later", R.string.later),
					arrayOf<Any?>("missed", R.string.missed),
					arrayOf<Any?>("Missed", R.string.missed),
					arrayOf<Any?>("soon", R.string.soon),
					arrayOf<Any?>("Soon", R.string.soon),
					arrayOf<Any?>("today", R.string.today),
					arrayOf<Any?>("Today", R.string.today),
					arrayOf<Any?>("statusid", 0),
					arrayOf<Any?>("StatusId", 0)
			)
		}
	}
}
