package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.DialogFragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.ShowsFilters

class ShowsFiltersFragment : DialogFragment(), CompoundButton.OnCheckedChangeListener, DialogInterface.OnClickListener {
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
        val showState = ShowsFilters.State.getStateForViewId(this.activePaused?.checkedRadioButtonId)
        val showStatus = ShowsFilters.Status.getStatusForStates(this.statusAll?.isChecked, this.statusContinuing?.isChecked, this.statusEnded?.isChecked, this.statusUnknown?.isChecked)

        with(PreferenceManager.getDefaultSharedPreferences(this.context).edit()) {
            putString(Constants.Preferences.Fields.SHOW_FILTER_STATE, showState.name)
            putInt(Constants.Preferences.Fields.SHOW_FILTER_STATUS, showStatus)
            apply()
        }

        val intent = Intent(Constants.Intents.ACTION_FILTER_SHOWS)
        intent.putExtra(Constants.Bundle.SEARCH_QUERY, this.arguments?.getString(Constants.Bundle.SEARCH_QUERY))

        LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent)

        this.dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        val state = preferences.getString(Constants.Preferences.Fields.SHOW_FILTER_STATE, Constants.Preferences.Defaults.SHOW_FILTER_STATE)
        val status = preferences.getInt(Constants.Preferences.Fields.SHOW_FILTER_STATUS, Constants.Preferences.Defaults.SHOW_FILTER_STATUS)
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_shows_filter, null)

        if (view != null) {
            this.activePaused = view.findViewById(R.id.filter_active_paused) as RadioGroup?
            this.statusAll = view.findViewById(R.id.filter_status_all) as CheckBox?
            this.statusContinuing = view.findViewById(R.id.filter_status_continuing) as CheckBox?
            this.statusEnded = view.findViewById(R.id.filter_status_ended) as CheckBox?
            this.statusUnknown = view.findViewById(R.id.filter_status_unknown) as CheckBox?

            (view.findViewById(ShowsFilters.State.getViewIdForState(ShowsFilters.State.valueOf(state))) as RadioButton?)?.isChecked = true

            this.statusAll?.isChecked = ShowsFilters.Status.isAll(status)
            this.statusContinuing?.isChecked = ShowsFilters.Status.isContinuing(status)
            this.statusEnded?.isChecked = ShowsFilters.Status.isEnded(status)
            this.statusUnknown?.isChecked = ShowsFilters.Status.isUnknown(status)

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
}
