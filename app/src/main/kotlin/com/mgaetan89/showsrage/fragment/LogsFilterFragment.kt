package com.mgaetan89.showsrage.fragment

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.helper.humanize

class LogsFilterFragment : DialogFragment(), DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
    private val items = RealmManager.getLogsGroup().toTypedArray()
    private val itemsFormatted = Array(this.items.size) { "" }
    private val selectedIndices = mutableListOf<Int>()

    init {
        this.items.forEachIndexed { i, item ->
            this.itemsFormatted[i] = item.humanize()

            // TODO Reselect previously selected items
            // By default, we select every items
            this.selectedIndices.add(i)
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val selectedItems = this.items.filterIndexed { i, item -> this.selectedIndices.contains(i) }.toTypedArray()
        val data = Intent()
        data.putExtra(Constants.Bundle.LOGS_GROUPS, selectedItems)

        this.targetFragment.onActivityResult(this.targetRequestCode, Activity.RESULT_OK, data)

        dialog?.dismiss()
    }

    override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
        if (isChecked) {
            this.selectedIndices.add(which)
        } else {
            this.selectedIndices.remove(which)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val checked = arrayOfNulls<Boolean>(this.itemsFormatted.size).map { true }.toBooleanArray()
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(R.string.filter)
        builder.setMultiChoiceItems(this.itemsFormatted, checked, this)
        builder.setNegativeButton(R.string.cancel, null)
        builder.setPositiveButton(R.string.filter, this)

        return builder.show()
    }
}
