package com.mgaetan89.showsrage.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.BaseActivity;
import com.mgaetan89.showsrage.adapter.RootDirectoriesAdapter;
import com.mgaetan89.showsrage.helper.GenericCallback;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.Set;

import retrofit.Callback;
import retrofit.client.Response;

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
	private Spinner rootDirectory = null;

	@Nullable
	private Spinner status = null;

	@Nullable
	private SwitchCompat subtitles = null;

	public AddShowOptionsFragment() {
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		int indexerId = this.getArguments().getInt(Constants.Bundle.INDEXER_ID, 0);

		if (indexerId <= 0) {
			return;
		}

		String allowedQuality = this.getAllowedQuality(this.allowedQuality);
		int anime = (this.anime != null && this.anime.isChecked()) ? 1 : 0;
		String language = this.getLanguage(this.language);
		String location = getLocation(this.rootDirectory);
		String preferredQuality = this.getPreferredQuality(this.preferredQuality);
		String status = this.getStatus(this.status);
		int subtitles = (this.subtitles != null && this.subtitles.isChecked()) ? 1 : 0;

		Callback<GenericResponse> callback = new AddShowCallback(this.getActivity());

		SickRageApi.Companion.getInstance().getServices()
				.addNewShow(indexerId, preferredQuality, allowedQuality, status, language, anime, subtitles, location, callback);
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

			LinearLayout rootDirectoryLayout = (LinearLayout) view.findViewById(R.id.root_directory_layout);

			if (rootDirectoryLayout != null) {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
				Set<String> rootDirectories = preferences.getStringSet(Constants.Preferences.Fields.ROOT_DIRS, null);

				if (rootDirectories == null || rootDirectories.size() < 2) {
					rootDirectoryLayout.setVisibility(View.GONE);
				} else {
					this.rootDirectory = (Spinner) view.findViewById(R.id.root_directory);

					if (this.rootDirectory != null) {
						this.rootDirectory.setAdapter(new RootDirectoriesAdapter(this.getContext(), rootDirectories));
					}

					rootDirectoryLayout.setVisibility(View.VISIBLE);
				}
			}
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
		this.rootDirectory = null;
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
	/* package */ static String getLocation(@Nullable Spinner rootDirectorySpinner) {
		if (rootDirectorySpinner != null) {
			Object rootDirectory = rootDirectorySpinner.getSelectedItem();

			if (rootDirectory != null) {
				return rootDirectory.toString();
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

	private static class AddShowCallback extends GenericCallback {
		private AddShowCallback(FragmentActivity activity) {
			super(activity);
		}

		@Override
		public void success(GenericResponse genericResponse, Response response) {
			super.success(genericResponse, response);

			FragmentActivity activity = this.getActivity();

			if (activity != null) {
				Intent intent = new Intent(activity, BaseActivity.class);

				activity.startActivity(intent);
			}
		}
	}
}
