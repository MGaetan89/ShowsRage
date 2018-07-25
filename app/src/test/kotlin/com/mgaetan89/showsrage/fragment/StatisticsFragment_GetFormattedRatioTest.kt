package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StatisticsFragment_GetFormattedRatioTest {
    @Test
    fun getFormattedRatio() {
        assertThat(StatisticsFragment.getFormattedRatio(-5, -5)).isEqualTo("100")
        assertThat(StatisticsFragment.getFormattedRatio(-2, -5)).isEqualTo("40")
        assertThat(StatisticsFragment.getFormattedRatio(0, -5)).isEqualTo("0")
        assertThat(StatisticsFragment.getFormattedRatio(2, -5)).isEqualTo("-40")
        assertThat(StatisticsFragment.getFormattedRatio(5, -5)).isEqualTo("-100")

        assertThat(StatisticsFragment.getFormattedRatio(-5, -2)).isEqualTo("250")
        assertThat(StatisticsFragment.getFormattedRatio(-2, -2)).isEqualTo("100")
        assertThat(StatisticsFragment.getFormattedRatio(0, -2)).isEqualTo("0")
        assertThat(StatisticsFragment.getFormattedRatio(2, -2)).isEqualTo("-100")
        assertThat(StatisticsFragment.getFormattedRatio(5, -2)).isEqualTo("-250")

        assertThat(StatisticsFragment.getFormattedRatio(-5, 0)).isEqualTo("0")
        assertThat(StatisticsFragment.getFormattedRatio(-2, 0)).isEqualTo("0")
        assertThat(StatisticsFragment.getFormattedRatio(0, 0)).isEqualTo("0")
        assertThat(StatisticsFragment.getFormattedRatio(2, 0)).isEqualTo("0")
        assertThat(StatisticsFragment.getFormattedRatio(5, 0)).isEqualTo("0")

        assertThat(StatisticsFragment.getFormattedRatio(-5, 2)).isEqualTo("-250")
        assertThat(StatisticsFragment.getFormattedRatio(-2, 2)).isEqualTo("-100")
        assertThat(StatisticsFragment.getFormattedRatio(0, 2)).isEqualTo("0")
        assertThat(StatisticsFragment.getFormattedRatio(2, 2)).isEqualTo("100")
        assertThat(StatisticsFragment.getFormattedRatio(5, 2)).isEqualTo("250")

        assertThat(StatisticsFragment.getFormattedRatio(-5, 5)).isEqualTo("-100")
        assertThat(StatisticsFragment.getFormattedRatio(-2, 5)).isEqualTo("-40")
        assertThat(StatisticsFragment.getFormattedRatio(0, 5)).isEqualTo("0")
        assertThat(StatisticsFragment.getFormattedRatio(2, 5)).isEqualTo("40")
        assertThat(StatisticsFragment.getFormattedRatio(5, 5)).isEqualTo("100")
    }
}
