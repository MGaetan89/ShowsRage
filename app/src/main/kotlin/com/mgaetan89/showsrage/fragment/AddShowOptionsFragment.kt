package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.RootDirectoriesAdapter
import com.mgaetan89.showsrage.helper.GenericCallback
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.model.RootDir
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import retrofit.client.Response

open class AddShowOptionsFragment : DialogFragment(), DialogInterface.OnClickListener {
    private var allowedQuality: Spinner? = null
    private var anime: SwitchCompat? = null
    private var language: Spinner? = null
    private var preferredQuality: Spinner? = null
    private var rootDirectory: Spinner? = null
    private var status: Spinner? = null
    private var subtitles: SwitchCompat? = null

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val indexerId = this.arguments.getInt(Constants.Bundle.INDEXER_ID, 0)

        if (indexerId <= 0) {
            return
        }

        val allowedQuality = this.getAllowedQuality(this.allowedQuality)
        val anime = if (this.anime?.isChecked ?: false) 1 else 0
        val callbsck = AddShowCallback(this.activity)
        val language = this.getLanguage(this.language)
        val location = getLocation(this.rootDirectory)
        var preferredQuality = this.getPreferredQuality(this.preferredQuality)
        val status = this.getStatus(this.status)
        val subtitles = if (this.subtitles?.isChecked ?: false) 1 else 0

        SickRageApi.instance.services?.addNewShow(indexerId, preferredQuality, allowedQuality, status, language, anime, subtitles, location, callbsck)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_add_show_options, null)

        if (view != null) {
            this.allowedQuality = view.findViewById(R.id.allowed_quality) as Spinner?
            this.anime = view.findViewById(R.id.anime) as SwitchCompat?
            this.language = view.findViewById(R.id.language) as Spinner?
            this.preferredQuality = view.findViewById(R.id.preferred_quality) as Spinner?
            this.status = view.findViewById(R.id.status) as Spinner?
            this.subtitles = view.findViewById(R.id.subtitles) as SwitchCompat?

            val rootDirectoryLayout = view.findViewById(R.id.root_directory_layout) as LinearLayout?

            if (rootDirectoryLayout != null) {
                val rootDirectories = Realm.getDefaultInstance().where(RootDir::class.java).findAll()

                if (rootDirectories.size < 2) {
                    rootDirectoryLayout.visibility = View.GONE
                } else {
                    this.rootDirectory = view.findViewById(R.id.root_directory) as Spinner?

                    if (this.rootDirectory != null) {
                        this.rootDirectory!!.adapter = RootDirectoriesAdapter(this.context, rootDirectories)
                    }

                    rootDirectoryLayout.visibility = View.VISIBLE
                }
            }
        }

        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(R.string.add_show)
        builder.setView(view)
        builder.setPositiveButton(R.string.add, this)
        builder.setNegativeButton(R.string.cancel, null)

        return builder.show()
    }

    override fun onDestroyView() {
        this.allowedQuality = null
        this.anime = null
        this.language = null
        this.preferredQuality = null
        this.rootDirectory = null
        this.status = null
        this.subtitles = null

        super.onDestroyView()
    }

    fun getAllowedQuality(allowedQualitySpinner: Spinner?): String? {
        val allowedQualityIndex = (allowedQualitySpinner?.selectedItemPosition ?: 1) - 1

        if (allowedQualityIndex >= 0) {
            val qualities = this.resources.getStringArray(R.array.allowed_qualities_keys)

            if (allowedQualityIndex < qualities.size) {
                return qualities[allowedQualityIndex]
            }
        }

        return null
    }

    fun getLanguage(languageSpinner: Spinner?): String? {
        val languageIndex = languageSpinner?.selectedItemPosition ?: 0

        if (languageIndex >= 0) {
            val languages = this.resources.getStringArray(R.array.languages_keys)

            if (languageIndex < languages.size) {
                return languages[languageIndex]
            }
        }

        return null
    }

    fun getPreferredQuality(preferredQualitySpinner: Spinner?): String? {
        val preferredQualityIndex = (preferredQualitySpinner?.selectedItemPosition ?: 1) - 1

        if (preferredQualityIndex >= 0) {
            val qualities = this.resources.getStringArray(R.array.preferred_qualities_keys)

            if (preferredQualityIndex < qualities.size) {
                return qualities[preferredQualityIndex]
            }
        }

        return null
    }

    fun getStatus(statusSpinner: Spinner?): String? {
        val statusIndex = statusSpinner?.selectedItemPosition ?: 0

        if (statusIndex >= 0) {
            val statuses = this.resources.getStringArray(R.array.status_keys)

            if (statusIndex < statuses.size) {
                return statuses[statusIndex]
            }
        }

        return null
    }

    companion object {
        fun getLocation(rootDirectorySpinner: Spinner?): String? {
            return rootDirectorySpinner?.selectedItem?.toString() ?: null
        }
    }

    private class AddShowCallback(activity: FragmentActivity) : GenericCallback(activity) {
        override fun success(genericResponse: GenericResponse?, response: Response?) {
            super.success(genericResponse, response)

            val activity = this.getActivity() ?: return

            val intent = Intent(activity, MainActivity::class.java)

            activity.startActivity(intent)
        }
    }
}
