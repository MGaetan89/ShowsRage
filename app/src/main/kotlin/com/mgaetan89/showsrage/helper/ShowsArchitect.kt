package com.mgaetan89.showsrage.helper

import android.widget.TextView
import com.mgaetan89.showsrage.model.Show
import io.kolumbus.Architect
import io.kolumbus.activity.TableActivity

class ShowsArchitect : Architect() {
    override fun displayInt(textView: TextView, value: Int) {
        val show = RealmManager.getShow(value)

        if (show != null) {
            textView.setOnClickListener {
                TableActivity.start(textView.context, Show::class.java, arrayOf(show))
            }

            super.displayString(textView, "[$value] ${show.showName}")
        } else {
            super.displayInt(textView, value)
        }
    }
}
