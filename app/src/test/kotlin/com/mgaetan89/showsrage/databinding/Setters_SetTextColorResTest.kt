package com.mgaetan89.showsrage.databinding

import android.content.Context
import android.content.res.Resources
import android.widget.TextView
import com.mgaetan89.showsrage.R
import org.junit.Test
import org.mockito.Matchers.anyInt
import org.mockito.Mockito.*

class Setters_SetTextColorResTest {
    @Test
    fun setTextColorRes() {
        val context = mock(Context::class.java)
        `when`(context.resources).thenReturn(mock(Resources::class.java))

        val textView = mock(TextView::class.java)
        `when`(textView.context).thenReturn(context)

        setTextColorRes(textView, R.color.primary)

        verify(textView).setTextColor(anyInt())
    }
}
