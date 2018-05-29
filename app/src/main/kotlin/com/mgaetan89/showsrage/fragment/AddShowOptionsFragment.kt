package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.RootDirectoriesAdapter
import com.mgaetan89.showsrage.extension.getRootDirs
import com.mgaetan89.showsrage.extension.inflate
import com.mgaetan89.showsrage.extension.toInt
import com.mgaetan89.showsrage.helper.GenericCallback
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.model.RootDir
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import retrofit.client.Response

class AddShowOptionsFragment : DialogFragment(), DialogInterface.OnClickListener {
	private var allowedQuality: Spinner? = null
	private var anime: SwitchCompat? = null
	private var futureStatus: Spinner? = null
	private var language: Spinner? = null
	private var preferredQuality: Spinner? = null
	private var rootDirectory: Spinner? = null
	private var seasonFolders: SwitchCompat? = null
	private var status: Spinner? = null
	private var subtitles: SwitchCompat? = null

	override fun onClick(dialog: DialogInterface?, which: Int) {
		val activity = this.activity ?: return
		val indexerId = this.arguments?.getInt(Constants.Bundle.INDEXER_ID) ?: return

		if (indexerId <= 0) {
			return
		}

		val allowedQuality = this.getAllowedQuality(this.allowedQuality)
		val anime = this.anime?.isChecked.toInt()
		val callback = AddShowCallback(activity)
		val futureStatus = this.getStatus(this.futureStatus)
		val language = this.getLanguage(this.language)
		val location = getLocation(this.rootDirectory)
		val preferredQuality = this.getPreferredQuality(this.preferredQuality)
		val seasonFolders = this.seasonFolders?.isChecked.toInt()
		val status = this.getStatus(this.status)
		val subtitles = this.subtitles?.isChecked.toInt()

		SickRageApi.instance.services?.addNewShow(indexerId, preferredQuality, allowedQuality, status, futureStatus, language, anime, seasonFolders, subtitles, location, callback)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val context = this.context ?: return super.onCreateDialog(savedInstanceState)
		val view = context.inflate(R.layout.fragment_add_show_options)

		this.allowedQuality = view.findViewById(R.id.allowed_quality) as Spinner?
		this.anime = view.findViewById(R.id.anime) as SwitchCompat?
		this.futureStatus = view.findViewById(R.id.future_status) as Spinner?
		this.language = view.findViewById(R.id.language) as Spinner?
		this.preferredQuality = view.findViewById(R.id.preferred_quality) as Spinner?
		this.seasonFolders = view.findViewById(R.id.season_folders) as SwitchCompat?
		this.status = view.findViewById(R.id.status) as Spinner?
		this.subtitles = view.findViewById(R.id.subtitles) as SwitchCompat?

		val rootDirectoryLayout = view.findViewById(R.id.root_directory_layout) as LinearLayout?

		if (rootDirectoryLayout != null) {
			val realm = Realm.getDefaultInstance()
			val rootDirectories = realm.getRootDirs()

			if (rootDirectories.size < 2) {
				rootDirectoryLayout.visibility = View.GONE
			} else {
				this.rootDirectory = view.findViewById(R.id.root_directory) as Spinner?

				if (this.rootDirectory != null) {
					this.rootDirectory!!.adapter = RootDirectoriesAdapter(rootDirectories)
				}

				rootDirectoryLayout.visibility = View.VISIBLE
			}

			realm.close()
		}

		val builder = AlertDialog.Builder(context)
		builder.setTitle(R.string.add_show)
		builder.setView(view)
		builder.setPositiveButton(R.string.add, this)
		builder.setNegativeButton(R.string.cancel, null)

		return builder.show()
	}

	override fun onDestroyView() {
		this.allowedQuality = null
		this.anime = null
		this.futureStatus = null
		this.language = null
		this.preferredQuality = null
		this.rootDirectory = null
		this.seasonFolders = null
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
			val selectedItem = rootDirectorySpinner?.selectedItem

			if (selectedItem is RootDir) {
				return selectedItem.location
			}

			return null
		}

		fun newInstance(indexerId: Int) = AddShowOptionsFragment().apply {
			this.arguments = Bundle().apply {
				this.putInt(Constants.Bundle.INDEXER_ID, indexerId)
			}
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
