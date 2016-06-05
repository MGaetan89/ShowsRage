package com.mgaetan89.showsrage.databinding

import android.widget.TextView
import com.mgaetan89.showsrage.R
import org.assertj.core.api.Fail.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(Parameterized::class)
class Setters_SetTextResTest {
    @Parameterized.Parameter(1)
    var text: Any = Unit

    @Parameterized.Parameter(0)
    var textRes: Int = 0

    @Test
    fun setTextRes() {
        val textView = mock(TextView::class.java)

        setTextRes(textView, this.textRes)

        if (this.text is String) {
            verify(textView).text = this.text as String
        } else if (this.text is Int) {
            verify(textView).setText(this.text as Int)
        } else {
            fail("text is not valid")
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf<Any>(R.string.app_name, R.string.app_name),
                    arrayOf(0, "")
            )
        }
    }
}
