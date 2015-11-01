package com.mgaetan89.showsrage.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Spinner;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;

public class ChangeQualityFragment extends DialogFragment implements DialogInterface.OnClickListener {
	public ChangeQualityFragment() {
	}

	@Override
	public void onClick(DialogInterface dialogInterface, int which) {
		Dialog dialog = this.getDialog();
		int indexerId = this.getArguments().getInt(Constants.Bundle.INDEXER_ID, 0);
		Fragment parentFragment = this.getParentFragment();

		if (dialog == null || indexerId <= 0 || !(parentFragment instanceof ShowOverviewFragment)) {
			return;
		}

		Spinner allowedQualitySpinner = (Spinner) dialog.findViewById(R.id.allowed_quality);
		String allowedQuality = this.getAllowedQuality(allowedQualitySpinner);

		Spinner preferredQualitySpinner = (Spinner) dialog.findViewById(R.id.preferred_quality);
		String preferredQuality = this.getPreferredQuality(preferredQualitySpinner);

		Callback<GenericResponse> callback = ((ShowOverviewFragment) parentFragment).getSetShowQualityCallback();

		SickRageApi.getInstance().getServices().setShowQuality(indexerId, allowedQuality, preferredQuality, callback);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setTitle(R.string.change_quality);
		builder.setView(R.layout.fragment_change_quality);
		builder.setNegativeButton(R.string.cancel, null);
		builder.setPositiveButton(R.string.change, this);

		return builder.show();
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
}
