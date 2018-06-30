package com.mgaetan89.showsrage.adapter

import android.support.v4.app.FragmentManager
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.fragment.ScheduleSectionFragment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class SchedulePagerAdapterTest {
	private lateinit var adapter: SchedulePagerAdapter

	private val ids = listOf("missed", "today", "soon", "later")
	private val labels = listOf("Missed", "Today", "Soon", "Later")

	@Before
	fun before() {
		this.adapter = SchedulePagerAdapter(mock(FragmentManager::class.java), this.ids, this.labels)
	}

	@Test
	fun getCount() {
		assertThat(this.adapter.count).isEqualTo(this.ids.size)
	}

	@Test
	fun getItem() {
		for (i in this.ids.indices) {
			val fragment = this.adapter.getItem(i)
			assertThat(fragment).isInstanceOf(ScheduleSectionFragment::class.java)
			assertThat(fragment.arguments).isNotNull()
			assertThat(fragment.arguments!!.containsKey(Constants.Bundle.SCHEDULE_SECTION))
		}
	}

	@Test
	fun getPageTitle() {
		for (i in this.ids.indices) {
			assertThat(this.adapter.getPageTitle(i)).isEqualTo(this.labels[i])
		}
	}
}
