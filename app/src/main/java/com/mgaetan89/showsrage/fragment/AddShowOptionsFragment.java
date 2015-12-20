package com.mgaetan89.showsrage.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.AddShowActivity;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;

public class AddShowOptionsFragment extends DialogFragment implements DialogInterface.OnClickListener {
	@Nullable
	private Spinner allowedQuality = null;

	@Nullable
	private SwitchCompat anime = null;

	@Nullable
	private Spinner language = null;

	@Nullable
	private Spinner preferredQuality = null;

	@Nullable
	private Spinner status = null;

	@Nullable
	private SwitchCompat subtitles = null;

	public AddShowOptionsFragment() {
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		int indexerId = this.getArguments().getInt(Constants.Bundle.INDEXER_ID, 0);
		FragmentActivity activity = this.getActivity();

		if (indexerId <= 0 || !(activity instanceof AddShowActivity)) {
			return;
		}

		String allowedQuality = this.getAllowedQuality(this.allowedQuality);
		int anime = (this.anime != null && this.anime.isChecked()) ? 1 : 0;
		String language = this.getLanguage(this.language);
		String preferredQuality = this.getPreferredQuality(this.preferredQuality);
		String status = this.getStatus(this.status);
		int subtitles = (this.subtitles != null && this.subtitles.isChecked()) ? 1 : 0;

		Callback<GenericResponse> callback = ((AddShowActivity) activity).getAddShowCallback();

		SickRageApi.Companion.getInstance().getServices()
				.addNewShow(indexerId, preferredQuality, allowedQuality, status, language, anime, subtitles, callback);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_add_show_options, null);

		if (view != null) {
			this.allowedQuality = (Spinner) view.findViewById(R.id.allowed_quality);
			this.anime = (SwitchCompat) view.findViewById(R.id.anime);
			this.language = (Spinner) view.findViewById(R.id.language);
			this.preferredQuality = (Spinner) view.findViewById(R.id.preferred_quality);
			this.status = (Spinner) view.findViewById(R.id.status);
			this.subtitles = (SwitchCompat) view.findViewById(R.id.subtitles);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setTitle(R.string.add_show);
		builder.setView(view);
		builder.setPositiveButton(R.string.add, this);
		builder.setNegativeButton(R.string.cancel, null);

		return builder.show();
	}

	@Override
	public void onDestroyView() {
		this.allowedQuality = null;
		this.anime = null;
		this.language = null;
		this.preferredQuality = null;
		this.status = null;
		this.subtitles = null;

		super.onDestroyView();
	}

	@Nullable
	/* package */ String getAllowedQuality(@Nullable Spinner allowedQualitySpinner) {
		int allowedQualityIndex = 0;

		if (allowedQualitySpinner != null) {
			// Skip the "Ignore" first item
			allowedQualityIndex = allowedQualitySpinner.getSelectedItemPosition() - 1;
		}

		if (allowedQualityIndex >= 0) {
			String qualities[] = this.getResources().getStringArray(R.array.allowed_qualities_keys);

			if (allowedQualityIndex < qualities.length) {
				return qualities[allowedQualityIndex];
			}
		}

		return null;
	}

	@Nullable
	/* package */ String getLanguage(@Nullable Spinner languageSpinner) {
		int languageIndex = 0;

		if (languageSpinner != null) {
			languageIndex = languageSpinner.getSelectedItemPosition();
		}

		if (languageIndex >= 0) {
			String languages[] = this.getResources().getStringArray(R.array.languages_keys);

			if (languageIndex < languages.length) {
				return languages[languageIndex];
			}
		}

		return null;
	}

	@Nullable
	/* package */ String getPreferredQuality(@Nullable Spinner preferredQualitySpinner) {
		int preferredQualityIndex = 0;

		if (preferredQualitySpinner != null) {
			// Skip the "Ignore" first item
			preferredQualityIndex = preferredQualitySpinner.getSelectedItemPosition() - 1;
		}

		if (preferredQualityIndex >= 0) {
			String qualities[] = this.getResources().getStringArray(R.array.preferred_qualities_keys);

			if (preferredQualityIndex < qualities.length) {
				return qualities[preferredQualityIndex];
			}
		}

		return null;
	}

	@Nullable
	/* package */ String getStatus(@Nullable Spinner statusSpinner) {
		int statusIndex = 0;

		if (statusSpinner != null) {
			statusIndex = statusSpinner.getSelectedItemPosition();
		}

		if (statusIndex >= 0) {
			String statuses[] = this.getResources().getStringArray(R.array.status_keys);

			if (statusIndex < statuses.length) {
				return statuses[statusIndex];
			}
		}

		return null;
	}
}
