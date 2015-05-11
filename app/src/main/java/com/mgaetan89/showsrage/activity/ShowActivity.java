package com.mgaetan89.showsrage.activity;

import android.os.Bundle;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ShowFragment;

/**
 * Requires a {@link com.mgaetan89.showsrage.Constants.Bundle#SHOW_MODEL Constants.Bundle#SHOW_MODEL} associated with a non-{@code null} {@link com.mgaetan89.showsrage.model.Show Show} in its {@link android.content.Intent Intent}.
 */
public class ShowActivity extends BaseActivity {
	@Override
	protected int getSelectedMenuItemIndex() {
		return -1;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.show;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new ShowFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(true);
	}
}
