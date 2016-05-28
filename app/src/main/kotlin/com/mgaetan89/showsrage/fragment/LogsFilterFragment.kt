package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.RealmManager

class LogsFilterFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = RealmManager.getLogsGroup().toTypedArray()
        val checked = arrayOfNulls<Boolean>(items.size).map { true }.toBooleanArray()
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(R.string.filter)
        builder.setMultiChoiceItems(items, checked, null)
        builder.setNegativeButton(R.string.cancel, null)
        builder.setPositiveButton(R.string.filter, null)

        return builder.show()
    }
}
