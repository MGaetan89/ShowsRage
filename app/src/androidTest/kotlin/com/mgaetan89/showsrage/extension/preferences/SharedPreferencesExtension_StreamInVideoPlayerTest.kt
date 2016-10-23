package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.streamInVideoPlayer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_StreamInVideoPlayerTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
    }

    @Test
    fun streamInVideoPlayer_False() {
        this.preference.edit().putBoolean(Fields.STREAM_IN_VIDEO_PLAYER.field, false).apply()

        val streamInVideoPlayer = this.preference.streamInVideoPlayer()

        assertThat(streamInVideoPlayer).isFalse()
    }

    @Test
    fun streamInVideoPlayer_Missing() {
        this.preference.edit().putBoolean(Fields.STREAM_IN_VIDEO_PLAYER.field, false).apply()

        val streamInVideoPlayer = this.preference.streamInVideoPlayer()

        assertThat(streamInVideoPlayer).isFalse()
    }

    @Test
    fun streamInVideoPlayer_True() {
        this.preference.edit().putBoolean(Fields.STREAM_IN_VIDEO_PLAYER.field, true).apply()

        val streamInVideoPlayer = this.preference.streamInVideoPlayer()

        assertThat(streamInVideoPlayer).isTrue()
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
