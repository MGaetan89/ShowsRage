package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.EmptyFragmentHostCallback
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.EpisodeDetailFragment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class EpisodePagerAdapterTest {
    private lateinit var adapter: EpisodePagerAdapter

    private val episodes = (10 downTo 1).toList()

    @Before
    fun before() {
        val intent = mock(Intent::class.java)
        `when`(intent.extras).thenReturn(mock(Bundle::class.java))

        val activity = mock(FragmentActivity::class.java)
        `when`(activity.intent).thenReturn(intent)
        `when`(activity.resources).thenReturn(mock(Resources::class.java))

        val fragment = mock(Fragment::class.java)

        try {
            val fragmentHostField = Fragment::class.java.getDeclaredField("mHost")
            fragmentHostField.isAccessible = true
            fragmentHostField.set(fragment, EmptyFragmentHostCallback(activity))
        } catch (ignored: IllegalAccessException) {
        } catch (ignored: NoSuchFieldException) {
        }

        `when`(fragment.getString(R.string.episode_name_short, 1)).thenReturn("E1")
        `when`(fragment.getString(R.string.episode_name_short, 2)).thenReturn("E2")
        `when`(fragment.getString(R.string.episode_name_short, 3)).thenReturn("E3")
        `when`(fragment.getString(R.string.episode_name_short, 4)).thenReturn("E4")
        `when`(fragment.getString(R.string.episode_name_short, 5)).thenReturn("E5")
        `when`(fragment.getString(R.string.episode_name_short, 6)).thenReturn("E6")
        `when`(fragment.getString(R.string.episode_name_short, 7)).thenReturn("E7")
        `when`(fragment.getString(R.string.episode_name_short, 8)).thenReturn("E8")
        `when`(fragment.getString(R.string.episode_name_short, 9)).thenReturn("E9")
        `when`(fragment.getString(R.string.episode_name_short, 10)).thenReturn("E10")

        this.adapter = EpisodePagerAdapter(null, fragment, this.episodes)
    }

    @Test
    fun getCount() {
        assertThat(this.adapter.count).isEqualTo(this.episodes.size)
    }

    @Test
    fun getItem() {
        for (i in this.episodes.indices) {
            val fragment = this.adapter.getItem(i)
            assertThat(fragment).isInstanceOf(EpisodeDetailFragment::class.java)
            assertThat(fragment!!.arguments).isNotNull()
            assertThat(fragment.arguments.containsKey(Constants.Bundle.EPISODE_ID))
            assertThat(fragment.arguments.containsKey(Constants.Bundle.EPISODE_NUMBER))
            assertThat(fragment.arguments.containsKey(Constants.Bundle.SEASON_NUMBER))
            assertThat(fragment.arguments.containsKey(Constants.Bundle.INDEXER_ID))
        }
    }

    @Test
    fun getPageTitle() {
        assertThat(this.adapter.getPageTitle(0)).isEqualTo("E10")
        assertThat(this.adapter.getPageTitle(1)).isEqualTo("E9")
        assertThat(this.adapter.getPageTitle(2)).isEqualTo("E8")
        assertThat(this.adapter.getPageTitle(3)).isEqualTo("E7")
        assertThat(this.adapter.getPageTitle(4)).isEqualTo("E6")
        assertThat(this.adapter.getPageTitle(5)).isEqualTo("E5")
        assertThat(this.adapter.getPageTitle(6)).isEqualTo("E4")
        assertThat(this.adapter.getPageTitle(7)).isEqualTo("E3")
        assertThat(this.adapter.getPageTitle(8)).isEqualTo("E2")
        assertThat(this.adapter.getPageTitle(9)).isEqualTo("E1")
    }
}
