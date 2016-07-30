package com.mgaetan89.showsrage.fragment

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.changeLocale
import com.mgaetan89.showsrage.extension.getLocale
import com.mgaetan89.showsrage.extension.useDarkTheme
import com.mgaetan89.showsrage.widget.HistoryWidgetProvider

class SettingsDisplayFragment : SettingsFragment() {
    override fun getTitleResourceId() = R.string.display

    override fun getXmlResourceFile() = R.xml.settings_display

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        when (key) {
            Fields.DISPLAY_LANGUAGE.field -> this.changeLanguage(sharedPreferences)
            Fields.THEME.field -> this.changeTheme(sharedPreferences)
        }
    }

    private fun changeLanguage(sharedPreferences: SharedPreferences?) {
        val newLocale = sharedPreferences.getLocale()

        this.resources.changeLocale(newLocale)

        this.activity.recreate()

        this.updateWidgets()
    }

    private fun changeTheme(sharedPreferences: SharedPreferences?) {
        if (sharedPreferences.useDarkTheme()) {
            (this.activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            (this.activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        this.updateWidgets()
    }

    private fun updateWidgets() {
        val context = this.context ?: return
        val widgetIds = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(ComponentName(context, HistoryWidgetProvider::class.java))
        val intent = Intent(context, HistoryWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)

        context.sendBroadcast(intent)
    }
}
