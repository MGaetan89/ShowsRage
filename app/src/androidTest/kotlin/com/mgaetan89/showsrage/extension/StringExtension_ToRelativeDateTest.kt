package com.mgaetan89.showsrage.extension

import android.support.test.runner.AndroidJUnit4
import android.text.format.DateUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class StringExtension_ToRelativeDateTest {
    @Test
    fun toRelativeDate_en() {
        Locale.setDefault(Locale.ENGLISH)

        assertThat(null.toRelativeDate("", 0L)).isEqualTo("N/A")
        assertThat(null.toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("N/A")
        assertThat(null.toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("N/A")
        assertThat(null.toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("N/A")
        assertThat("".toRelativeDate("", 0L)).isEqualTo("N/A")
        assertThat("".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("N/A")
        assertThat("".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("N/A")
        assertThat("".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("N/A")
        assertThat("02:34:56".toRelativeDate("", 0L)).isEqualTo("02:34:56")
        assertThat("02:34:56".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("02:34:56")
        assertThat("02:34:56".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("02:34:56")
        assertThat("02:34:56".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("02:34:56")
        assertThat("15.06.2018".toRelativeDate("", 0L)).isEqualTo("15.06.2018")
        assertThat("15.06.2018".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("15.06.2018")
        assertThat("15.06.2018".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("15.06.2018")
        assertThat("15.06.2018".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("15.06.2018")
        assertThat("2018-06-15".toRelativeDate("", 0L)).isEqualTo("2018-06-15")
        assertThat("2018-06-15".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("Jun 15, 2018")
        assertThat("2018-06-15".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("2018-06-15")
        assertThat("2018-06-15".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("Jun 15, 2018")
        assertThat("2018-06-15 02:34:56".toRelativeDate("", 0L)).isEqualTo("2018-06-15 02:34:56")
        assertThat("2018-06-15 02:34:56".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("Jun 15, 2018")
        assertThat("2018-06-15 02:34:56".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("2018-06-15 02:34:56")
        assertThat("2018-06-15 02:34:56".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("Jun 15, 2018")
    }

    @Test
    fun toRelativeDate_fr() {
        Locale.setDefault(Locale.FRENCH)

        assertThat(null.toRelativeDate("", 0L)).isEqualTo("N/A")
        assertThat(null.toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("N/A")
        assertThat(null.toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("N/A")
        assertThat(null.toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("N/A")
        assertThat("".toRelativeDate("", 0L)).isEqualTo("N/A")
        assertThat("".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("N/A")
        assertThat("".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("N/A")
        assertThat("".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("N/A")
        assertThat("02:34:56".toRelativeDate("", 0L)).isEqualTo("02:34:56")
        assertThat("02:34:56".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("02:34:56")
        assertThat("02:34:56".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("02:34:56")
        assertThat("02:34:56".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("02:34:56")
        assertThat("15.06.2018".toRelativeDate("", 0L)).isEqualTo("15.06.2018")
        assertThat("15.06.2018".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("15.06.2018")
        assertThat("15.06.2018".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("15.06.2018")
        assertThat("15.06.2018".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("15.06.2018")
        assertThat("2018-06-15".toRelativeDate("", 0L)).isEqualTo("2018-06-15")
        assertThat("2018-06-15".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("15 juin 2018")
        assertThat("2018-06-15".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("2018-06-15")
        assertThat("2018-06-15".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("15 juin 2018")
        assertThat("2018-06-15 02:34:56".toRelativeDate("", 0L)).isEqualTo("2018-06-15 02:34:56")
        assertThat("2018-06-15 02:34:56".toRelativeDate("yyyy-MM-dd", 0L)).isEqualTo("15 juin 2018")
        assertThat("2018-06-15 02:34:56".toRelativeDate("", DateUtils.DAY_IN_MILLIS)).isEqualTo("2018-06-15 02:34:56")
        assertThat("2018-06-15 02:34:56".toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)).isEqualTo("15 juin 2018")
    }
}
