package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsSectionFragment_IsShowValidTest(val show: Show?, val valid: Boolean) {
    @Test
    fun isShowValid() {
        assertThat(ShowsSectionFragment.isShowValid(this.show)).isEqualTo(this.valid)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf<Any?>(null, false),
                    arrayOf<Any?>(gson.fromJson("{}", Show::class.java), false),
                    arrayOf<Any?>(gson.fromJson("{indexerid: 0}", Show::class.java), false),
                    arrayOf<Any?>(gson.fromJson("{indexerid: 123}", Show::class.java), true)
            )
        }
    }
}
