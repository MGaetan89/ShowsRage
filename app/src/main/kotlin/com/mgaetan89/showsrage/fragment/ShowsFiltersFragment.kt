package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioGroup
import com.mgaetan89.showsrage.R

class ShowsFiltersFragment : DialogFragment(), CompoundButton.OnCheckedChangeListener, DialogInterface.OnClickListener {
    private val STATUS_CONTINUING = 1
    private val STATUS_ENDED = 2
    private val STATUS_UNKOWN = 4
    private val STATUS_ALL = STATUS_CONTINUING or STATUS_ENDED or STATUS_UNKOWN

    private var activePaused: RadioGroup? = null
    private var statusAll: CheckBox? = null
    private var statusContinuing: CheckBox? = null
    private var statusEnded: CheckBox? = null
    private var statusUnknown: CheckBox? = null

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView?.id == R.id.filter_status_all) {
            this.setCheckBoxStateQuietly(this.statusContinuing, isChecked)
            this.setCheckBoxStateQuietly(this.statusEnded, isChecked)
            this.setCheckBoxStateQuietly(this.statusUnknown, isChecked)
        } else {
            if (!isChecked) {
                this.setCheckBoxStateQuietly(this.statusAll, isChecked)
            } else {
                if (this.statusContinuing?.isChecked ?: false && this.statusEnded?.isChecked ?: false && this.statusUnknown?.isChecked ?: false) {
                    this.setCheckBoxStateQuietly(this.statusAll, isChecked)
                }
            }
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val statusContinuing = if (this.statusContinuing?.isChecked ?: false) STATUS_CONTINUING else 0
        val statusEnded = if (this.statusEnded?.isChecked ?: false) STATUS_ENDED else 0
        val statusUnknown = if (this.statusUnknown?.isChecked ?: false) STATUS_UNKOWN else 0

        val showActivePaused = getActivePausedState(this.activePaused?.checkedRadioButtonId)
        val showStatus = if (this.statusAll?.isChecked ?: false) {
            STATUS_ALL
        } else {
            statusContinuing or statusEnded or statusUnknown
        }

        // TODO Save filters values in the Preferences
        // TODO Send Broadcast to notify of filters change

        this.dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_shows_filter, null)

        if (view != null) {
            this.activePaused = view.findViewById(R.id.filter_active_paused) as RadioGroup?
            this.statusAll = view.findViewById(R.id.filter_status_all) as CheckBox?
            this.statusContinuing = view.findViewById(R.id.filter_status_continuing) as CheckBox?
            this.statusEnded = view.findViewById(R.id.filter_status_ended) as CheckBox?
            this.statusUnknown = view.findViewById(R.id.filter_status_unknown) as CheckBox?

            this.statusAll?.setOnCheckedChangeListener(this)
            this.statusContinuing?.setOnCheckedChangeListener(this)
            this.statusEnded?.setOnCheckedChangeListener(this)
            this.statusUnknown?.setOnCheckedChangeListener(this)
        }

        return AlertDialog.Builder(this.context)
                .setTitle(R.string.filter)
                .setView(view)
                .setPositiveButton(R.string.filter, this)
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    override fun onDestroyView() {
        this.activePaused = null
        this.statusAll = null
        this.statusContinuing = null
        this.statusEnded = null
        this.statusUnknown = null

        super.onDestroyView()
    }

    private fun setCheckBoxStateQuietly(checkBox: CheckBox?, checked: Boolean) {
        checkBox?.setOnCheckedChangeListener(null)
        checkBox?.isChecked = checked
        checkBox?.setOnCheckedChangeListener(this)
    }

    companion object {
        private fun getActivePausedState(id: Int?): ActivePaused {
            return when (id) {
                R.id.filter_active -> ActivePaused.ACTIVE
                R.id.filter_paused -> ActivePaused.PAUSED
                R.id.filter_all -> ActivePaused.ALL
                else -> ActivePaused.ALL
            }
        }
    }

    private enum class ActivePaused {
        ACTIVE,
        ALL,
        PAUSED
    }
}
