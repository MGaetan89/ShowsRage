package com.mgaetan89.showsrage.adapter

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.mgaetan89.showsrage.EmptyFragmentHostCallback
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.ShowOverviewFragment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ShowPagerAdapter_EmptyTest {
	private lateinit var adapter: ShowPagerAdapter

	@Before
	fun before() {
		val activity = mock(FragmentActivity::class.java)
		`when`(activity.resources).thenReturn(mock(Resources::class.java))

		val fragment = mock(Fragment::class.java)

		try {
			val fragmentHostField = Fragment::class.java.getDeclaredField("mHost")
			fragmentHostField.isAccessible = true
			fragmentHostField.set(fragment, EmptyFragmentHostCallback(activity))
		} catch (ignored: IllegalAccessException) {
		} catch (ignored: NoSuchFieldException) {
		}

		`when`(fragment.getString(R.string.show)).thenReturn("Show")

		this.adapter = ShowPagerAdapter(mock(FragmentManager::class.java), fragment, emptyList())
	}

	@Test
	fun getCount() {
		assertThat(this.adapter.count).isEqualTo(1)
	}

	@Test
	fun getItem() {
		val fragment = this.adapter.getItem(0)
		assertThat(fragment).isInstanceOf(ShowOverviewFragment::class.java)
		assertThat(fragment.arguments).isNull()
	}

	@Test
	fun getPageTitle() {
		assertThat(this.adapter.getPageTitle(0)).isEqualTo("Show")
	}
}
