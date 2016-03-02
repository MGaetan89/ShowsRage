package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.mgaetan89.showsrage.R

class ShowsFiltersFragment : DialogFragment(), DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface?, which: Int) {
        // TODO Save filter preferences
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(this.context, this.theme)
                .setTitle(R.string.filter)
                .setView(R.layout.fragment_shows_filter)
                .setPositiveButton(R.string.filter, this)
                .setNegativeButton(R.string.cancel, null)
                .show()
    }
}
