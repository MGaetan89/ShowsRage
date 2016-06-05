package com.mgaetan89.showsrage.adapter

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.EmptyFragmentHostCallback
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.SeasonFragment
import com.mgaetan89.showsrage.fragment.ShowOverviewFragment
import junit.framework.Assert.assertTrue
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ShowPagerAdapterTest {
    private lateinit var adapter: ShowPagerAdapter

    private val seasons = listOf(3, 2, 1, 0)

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

        `when`(fragment.getString(R.string.season_number, 1)).thenReturn("Season 1")
        `when`(fragment.getString(R.string.season_number, 2)).thenReturn("Season 2")
        `when`(fragment.getString(R.string.season_number, 3)).thenReturn("Season 3")
        `when`(fragment.getString(R.string.show)).thenReturn("Show")
        `when`(fragment.getString(R.string.specials)).thenReturn("Specials")

        this.adapter = ShowPagerAdapter(mock(FragmentManager::class.java), fragment, this.seasons)
    }

    @Test
    fun getCount() {
        assertThat(this.adapter.count).isEqualTo(this.seasons.size + 1)
    }

    @Test
    fun getItem_Season() {
        for (i in 1..this.seasons.size - 1) {
            val fragment = this.adapter.getItem(i)
            assertTrue(fragment != null)
            assertThat(fragment).isInstanceOf(SeasonFragment::class.java)
            assertThat(fragment!!.arguments).isNotNull()
            assertThat(fragment.arguments.containsKey(Constants.Bundle.SEASON_NUMBER))
        }
    }

    @Test
    fun getItem_ShowOverview() {
        val fragment = this.adapter.getItem(0)
        assertTrue(fragment != null)
        assertThat(fragment).isInstanceOf(ShowOverviewFragment::class.java)
        assertThat(fragment!!.arguments).isNull()
    }

    @Test
    fun getPageTitle_Season() {
        assertThat(this.adapter.getPageTitle(1)).isEqualTo("Season 3")
        assertThat(this.adapter.getPageTitle(2)).isEqualTo("Season 2")
        assertThat(this.adapter.getPageTitle(3)).isEqualTo("Season 1")
    }

    @Test
    fun getPageTitle_ShowOverview() {
        assertThat(this.adapter.getPageTitle(0)).isEqualTo("Show")
    }

    @Test
    fun getPageTitle_Specials() {
        assertThat(this.adapter.getPageTitle(4)).isEqualTo("Specials")
    }
}
