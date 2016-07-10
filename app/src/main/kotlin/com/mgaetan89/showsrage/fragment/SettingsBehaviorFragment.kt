package com.mgaetan89.showsrage.fragment

import android.support.v7.app.AlertDialog
import android.support.v7.preference.Preference
import com.mgaetan89.showsrage.R
import io.realm.Realm

class SettingsBehaviorFragment : SettingsFragment() {
    override fun getTitleResourceId() = R.string.behavior

    override fun getXmlResourceFile() = R.xml.settings_behavior

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if ("behavior_clear_local".equals(preference?.key)) {
            this.confirmClearLocalData()

            return true
        }

        return super.onPreferenceTreeClick(preference)
    }

    private fun confirmClearLocalData() {
        AlertDialog.Builder(this.activity)
                .setMessage(R.string.clear_local_data_confirm)
                .setPositiveButton(R.string.clear, { dialog, which ->
                    with(Realm.getDefaultInstance()) {
                        executeTransaction {
                            it.deleteAll()
                        }
                        close()
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }
}
