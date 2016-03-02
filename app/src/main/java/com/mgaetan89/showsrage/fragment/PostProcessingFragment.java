package com.mgaetan89.showsrage.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.Spinner;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.GenericCallback;
import com.mgaetan89.showsrage.network.SickRageApi;

public class PostProcessingFragment extends DialogFragment implements DialogInterface.OnClickListener {
	@Nullable
	private SwitchCompat forceProcessing = null;

	@Nullable
	private Spinner processingMethod = null;

	@Nullable
	private SwitchCompat replaceFiles = null;

	public PostProcessingFragment() {
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		int force = checkableToInteger(this.forceProcessing);
		String method = this.getProcessingMethod(this.processingMethod);
		int replace = checkableToInteger(this.replaceFiles);

		SickRageApi.Companion.getInstance().getServices().postProcess(replace, force, method, new GenericCallback(this.getActivity()));
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(this.getActivity());
		View view = inflater.inflate(R.layout.fragment_post_processing, null);

		if (view != null) {
			this.forceProcessing = (SwitchCompat) view.findViewById(R.id.force_processing);
			this.processingMethod = (Spinner) view.findViewById(R.id.processing_method);
			this.replaceFiles = (SwitchCompat) view.findViewById(R.id.replace_files);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(), this.getTheme());
		builder.setTitle(R.string.post_processing);
		builder.setView(view);
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.setPositiveButton(R.string.process, this);

		return builder.create();
	}

	@Override
	public void onDestroyView() {
		this.forceProcessing = null;
		this.processingMethod = null;
		this.replaceFiles = null;

		super.onDestroyView();
	}

	@Nullable
	/* package */ String getProcessingMethod(Spinner spinner) {
		int processingMethodIndex = 0;

		if (spinner != null) {
			processingMethodIndex = spinner.getSelectedItemPosition();
		}

		if (processingMethodIndex > 0) {
			String processingMethods[] = this.getResources().getStringArray(R.array.processing_methods_values);

			if (processingMethodIndex < processingMethods.length) {
				return processingMethods[processingMethodIndex];
			}
		}

		return null;
	}

	/* package */
	static int checkableToInteger(@Nullable Checkable checkable) {
		if (checkable == null) {
			return 0;
		}

		return checkable.isChecked() ? 1 : 0;
	}
}
